(ns xquo.components.input.view
  (:require [applied-science.js-interop :as j]
            [react-native.react.core :as react]
            [react-native.utils :as rn.utils]
            [xquo.components.button.style :as button-style]
            [xquo.components.button.view :as button]
            [xquo.components.icon.view :as icon]
            [xquo.components.input.style :as style]
            [xquo.components.text.view :as text]
            [xquo.context :as context]
            [xquo.foundations.typography :as typography]))

(def ^:private button-hit-slop
  {32 {:top 6 :left 6 :right 6 :bottom 6}
   40 {:top 8 :left 8 :right 8 :bottom 8}})

(defn- labels-view [{:keys [theme blur? label length max-length]}]
  (let [color-style (style/label-text theme blur?)]
    [:rn/view {:style style/labels}
     (if label
       [text/text {:font :font/medium-13 :number-of-lines 1 :style [style/label-slot color-style]} label]
       [:rn/view {:style style/label-slot}])
     (when max-length
       [text/text {:font :font/regular-13 :number-of-lines 1 :style [style/counter-slot color-style]}
        (str length "/" max-length)])]))

(defn- leading-view [{:keys [theme blur? content]}]
  [:rn/view {:style style/leading-slot}
   (case (:type content)
     :icon
     (let [props (:icon content)]
       [icon/view (assoc props :size (:size props 20) :color (:color props (style/leading-color theme blur?)))])

     :image
     (let [image (:image content)]
       [:rn/image (-> image
                      (dissoc :url)
                      (assoc :source      (or (:source image) {:uri (:url image)})
                             :resize-mode (:resize-mode image :contain)
                             :style       (rn.utils/add-styles style/leading-image (:style image))))])

     nil)])

(defn- clear-button-view [{:keys [theme blur? hidden? disabled? on-press]}]
  (let [[pressed? set-pressed!] (react/use-state false)
        press-in!               (react/use-callback #(set-pressed! true) [])
        press-out!              (react/use-callback #(set-pressed! false) [])]
    [:animated/pressable {:disabled     (or hidden? disabled?)
                          :on-press     on-press
                          :on-press-in  press-in!
                          :on-press-out press-out!
                          :hit-slop     6
                          :style        [style/clear-slot
                                         (if pressed? button-style/pressable-pressed-state-style button-style/pressable-default-state-style)
                                         (style/clear-visibility hidden?)]}
     [icon/view {:name :icon/cross-circle :size 20 :color (style/clear-colors theme blur?)}]]))

(defn- text-input-view [{:keys [native-props theme font multiline? min-height max-height vertical-padding filled? value-cache]}]
  (let [[content-height set-content-height!] (react/use-state nil)
        font-style                           (typography/get-style font)
        line-height                          (js/Math.round (j/get font-style :lineHeight))
        minimum                              (max line-height (- (or min-height 0) vertical-padding))
        maximum                              (when max-height (max minimum (- max-height vertical-padding)))
        height                               (cond
                                               (not multiline?) line-height
                                               (not filled?)    minimum
                                               maximum          (min maximum (max minimum (or content-height line-height)))
                                               :else            (max minimum (or content-height line-height)))
        overflow?                            (and filled? maximum content-height (> content-height maximum))
        caller-size-change                   (:on-content-size-change native-props)
        size-change!                         (react/use-callback (fn [event]
                                                                   (when multiline?
                                                                     (let [measured    (j/get-in event [:nativeEvent :contentSize :height])
                                                                           next-height (if (seq @value-cache)
                                                                                         (* line-height (max 1 (js/Math.round (/ measured line-height))))
                                                                                         line-height)]
                                                                       (set-content-height! next-height)))
                                                                   (when caller-size-change (caller-size-change event)))
                                                                 [multiline? line-height caller-size-change value-cache])
        text-style                           (react/use-memo (fn []
                                                               [style/text-input-base font-style
                                                                (style/text-input-layout theme multiline? height line-height)])
                                                             [font (:theme theme) multiline? height line-height])]
    [:rn/text-input (cond-> (assoc native-props :style text-style)
                      (or multiline? caller-size-change) (assoc :on-content-size-change size-change!)
                      multiline? (assoc :multiline true :scroll-enabled (:scroll-enabled native-props overflow?)))]))

(defn input [initial-props]
  (let [last-value (atom (or (:default-value initial-props) ""))]
    (fn [{external-ref :input-ref
          :keys        [value content size label max-length clearable? button blur? error? disabled?
                        min-height max-height font on-change-text on-clear on-focus on-blur field-style]
          :or          {size 40 font :font/regular-15}
          :as          props}]
      (let [theme                   (context/use-theme-color)
            controlled?             (contains? props :value)
            internal-ref            (react/use-ref nil)
            input-ref               (or external-ref internal-ref)
            [focused? set-focused!] (react/use-state false)
            [_ set-value-metric!]   (react/use-state 0)
            current-value           (or (if controlled? value @last-value) "")
            filled?                 (not (empty? current-value))
            focused?                (and focused? (not disabled?))
            multiline?              (:multiline? props (:multiline props))
            leading?                (:type content)
            button?                 (not (nil? button))
            editable?               (and (not disabled?) (:editable props true) (not (:read-only props)))
            focus!                  (react/use-callback (fn []
                                                          (when editable?
                                                            (when-let [native (j/get input-ref :current)]
                                                              (j/call native :focus))))
                                                        [editable? input-ref])
            change!                 (react/use-callback (fn [next-value]
                                                          (reset! last-value next-value)
                                                          (when-not controlled?
                                                            (set-value-metric! (cond
                                                                                 max-length       (count next-value)
                                                                                 (seq next-value) 1
                                                                                 :else            0)))
                                                          (when on-change-text (on-change-text next-value)))
                                                        [controlled? max-length on-change-text])
            clear!                  (react/use-callback (fn []
                                                          (when-not disabled?
                                                            (when-not controlled?
                                                              (when-let [native (j/get input-ref :current)]
                                                                (j/call native :clear)))
                                                            (change! "")
                                                            (when on-clear (on-clear))))
                                                        [disabled? controlled? input-ref change! on-clear])
            focus-in!               (react/use-callback (fn [event]
                                                          (set-focused! true)
                                                          (when on-focus (on-focus event)))
                                                        [on-focus])
            focus-out!              (react/use-callback (fn [event]
                                                          (set-focused! false)
                                                          (when on-blur (on-blur event)))
                                                        [on-blur])
            cursor                  (style/cursor-color theme blur?)
            layout-style            (react/use-memo (fn []
                                                      (style/field-layout theme
                                                                          {:size       size
                                                                           :leading?   leading?
                                                                           :clearable? clearable?
                                                                           :button?    button?
                                                                           :blur?      blur?
                                                                           :error?     error?
                                                                           :focused?   focused?
                                                                           :filled?    filled?
                                                                           :disabled?  disabled?
                                                                           :multiline? multiline?}))
                                                    [(:theme theme) size leading? clearable? button? blur? error? focused? filled? disabled? multiline?])
            native-props            (-> props
                                        (dissoc :content
                                                :size
                                                :label
                                                :clearable?
                                                :button
                                                :blur?
                                                :error?
                                                :disabled?
                                                :multiline?
                                                :min-height
                                                :max-height
                                                :font
                                                :auto-focus?
                                                :on-clear
                                                :field-style
                                                :style
                                                :input-ref)
                                        (assoc :ref                     input-ref
                                               :editable                editable?
                                               :auto-focus              (and editable? (:auto-focus? props (:auto-focus props)))
                                               :on-change-text          change!
                                               :on-focus                focus-in!
                                               :on-blur                 focus-out!
                                               :placeholder-text-color  (:placeholder-text-color props (style/placeholder-color theme blur? focused?))
                                               :cursor-color            (:cursor-color props cursor)
                                               :selection-color         (:selection-color props cursor)
                                               :underline-color-android (:underline-color-android props "transparent")))]
        (when controlled?
          (reset! last-value current-value))
        [:rn/view {:style (rn.utils/add-styles style/root (:style props))}
         (when (or label max-length)
           [labels-view {:theme      theme
                         :blur?      blur?
                         :label      label
                         :length     (when max-length (count current-value))
                         :max-length max-length}])
         [:rn/pressable {:disabled disabled?
                         :on-press focus!
                         :style    [layout-style
                                    (style/field-color theme blur? error? focused? disabled?)
                                    (when multiline? (style/bounds (or min-height size) max-height))
                                    field-style]}
          (when content
            [leading-view {:theme theme :blur? blur? :content content}])
          [text-input-view {:native-props     native-props
                            :value-cache      last-value
                            :filled?          (when multiline? filled?)
                            :theme            theme
                            :font             font
                            :multiline?       multiline?
                            :min-height       min-height
                            :max-height       max-height
                            :vertical-padding (style/vertical-padding size button?)}]
          (when clearable?
            [clear-button-view {:theme theme :blur? blur? :hidden? (not filled?) :disabled? disabled? :on-press clear!}])
          (when button
            [button/button (-> button
                               (dissoc :label)
                               (assoc :size      (:size button 24)
                                      :type      (:type button :outline)
                                      :blur?     (:blur? button (and blur? (not (:photo? button))))
                                      :disabled? (or disabled? (:disabled? button))
                                      :hit-slop  (:hit-slop button (button-hit-slop size))
                                      :style     (rn.utils/add-styles (when disabled? style/inherited-disabled-button) (:style button))))
             (:label button)])]]))))
