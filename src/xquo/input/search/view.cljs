(ns xquo.input.search.view
  (:require [applied-science.js-interop :as j]
            [react-native.react.core :as react]
            [react-native.utils :as rn.utils]
            [xquo.components.tags.context-tag.view :as context-tag]
            [xquo.components.text.view :as text]
            [xquo.context :as context]
            [xquo.input.search.style :as style]
            [xquo.input.text.view :as input]
            [xquo.tag.standard.view :as tag]))

(defn- tag-view [{:keys [entry blur?]}]
  (let [{:keys [type] :or {type :standard}} entry
        {:keys [light-theme?]}              (context/use-theme-color)]
    (case type
      :standard
      (let [{:keys [label] :as props} (:tag entry)]
        [tag/tag (-> props
                     (dissoc :label)
                     (assoc :size  (:size props 32)
                            :blur? (:blur? props blur?)))
         label])

      :context
      (let [{:keys [label] :as props} (:context-tag entry)]
        [context-tag/context-tag (-> props
                                     (dissoc :label)
                                     (assoc :size  (:size props 32)
                                            :blur? (:blur? props (or light-theme? blur?))))
         label])

      nil)))

(defn search-input [{:keys [default-value]}]
  (let [last-value (atom (or default-value ""))]
    (fn [{:keys        [value tags blur? disabled? clearable? on-change-text on-focus on-blur on-selection-change]
          external-ref :input-ref
          root-style   :style
          :or          {clearable? true}
          :as          props}]
      (let [controlled?                  (contains? props :value)
            tag-count                    (count tags)
            tagged?                      (seq tags)
            multiple?                    (> tag-count 1)
            internal-ref                 (react/use-ref nil)
            input-ref                    (or external-ref internal-ref)
            [focused? set-focused!]      (react/use-state false)
            [selected? set-selected!]    (react/use-state false)
            focused?                     (and focused? (not disabled?))
            [_ set-value-metric!]        (react/use-state nil)
            [text-width set-text-width!] (react/use-state 0)
            current-value                (or (if controlled? value @last-value) "")
            filled?                      (seq current-value)
            resting?                     (and tagged? (not focused?) (not filled?))
            clear?                       (and clearable? (or tagged? filled?))
            cursor-width                 (cond
                                           (and focused? selected?) 4
                                           focused?                 1
                                           :else                    0)
            clear-width                  (cond
                                           (not clear?) 0
                                           resting?     20
                                           multiple?    52
                                           :else        28)
            width                        (+ (if (or resting? (not filled?)) 0 text-width) cursor-width clear-width)
            focus!                       (react/use-callback (fn []
                                                               (when-let [native (j/get input-ref :current)]
                                                                 (j/call native :focus)))
                                                             [input-ref])
            focus-in!                    (react/use-callback (fn [event]
                                                               (set-focused! true)
                                                               (when on-focus (on-focus event)))
                                                             [on-focus])
            focus-out!                   (react/use-callback (fn [event]
                                                               (set-focused! false)
                                                               (when on-blur (on-blur event)))
                                                             [on-blur])
            change!                      (react/use-callback (fn [next-value]
                                                               (reset! last-value next-value)
                                                               (when-not controlled?
                                                                 (set-value-metric! (cond
                                                                                      multiple?        next-value
                                                                                      (seq next-value) 1
                                                                                      :else            0)))
                                                               (when on-change-text (on-change-text next-value)))
                                                             [controlled? multiple? on-change-text])
            selection!                   (react/use-callback (fn [event]
                                                               (let [start (j/get-in event [:nativeEvent :selection :start])
                                                                     end   (j/get-in event [:nativeEvent :selection :end])]
                                                                 (set-selected! (< start end)))
                                                               (when on-selection-change (on-selection-change event)))
                                                             [on-selection-change])
            measure!                     (react/use-callback (fn [event]
                                                               (let [lines (j/get-in event [:nativeEvent :lines])
                                                                     line  (aget lines 0)]
                                                                 (when line
                                                                   (set-text-width! (j/get line :width)))))
                                                             [])]
        [:rn/pressable {:disabled (or disabled? (:read-only props) (= false (:editable props)))
                        :on-press focus!
                        :style    (rn.utils/add-styles (style/root multiple? resting?) root-style)}
         (when tagged?
           [:rn/view {:pointer-events (when disabled? :none)
                      :style          [style/tags (when (or multiple? resting?) style/growing)]}
            (into [:rn/view {:style (style/tag-content multiple?)}]
                  (map (fn [entry] [tag-view {:entry entry :blur? blur?}]))
                  tags)])
         [input/input (-> props
                          (dissoc :tags :style :content :label :button :multiline? :multiline :min-height :max-height)
                          (assoc :input-ref           input-ref
                                 :on-change-text      change!
                                 :on-focus            focus-in!
                                 :on-blur             focus-out!
                                 :on-selection-change selection!
                                 :placeholder         (when-not tagged? (:placeholder props "Search..."))
                                 :clearable?          clearable?
                                 :size                32
                                 :font                :font/regular-15
                                 :return-key-type     (:return-key-type props :search)
                                 :auto-capitalize     (:auto-capitalize props :none)
                                 :auto-correct        (:auto-correct props false)
                                 :style               (style/input multiple? resting? width)))
          {:tag-count tag-count :resting? resting?}]
         (when multiple?
           [text/text {:pointer-events  :none
                       :font            :font/regular-15
                       :number-of-lines 1
                       :on-text-layout  measure!
                       :style           style/measure}
            current-value])]))))
