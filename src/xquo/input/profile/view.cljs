(ns xquo.input.profile.view
  (:require [applied-science.js-interop :as j]
            [react-native.react.core :as react]
            [react-native.utils :as rn.utils]
            [xquo.components.icon.view :as icon]
            [xquo.components.text.view :as text]
            [xquo.context :as context]
            [xquo.foundations.typography :as typography]
            [xquo.input.profile.style :as style]
            [xquo.input.text.style :as input-style]))

(defn profile-input [{:keys        [value default-value max-length disabled? blur? on-change-text on-focus on-blur]
                      external-ref :input-ref
                      root-style   :style
                      :as          props}]
  (let [theme                   (context/use-theme-color)
        controlled?             (contains? props :value)
        [length set-length!]    (react/use-state (count (or default-value "")))
        [focused? set-focused!] (react/use-state false)
        internal-ref            (react/use-ref nil)
        input-ref               (or external-ref internal-ref)
        editable?               (and (not disabled?) (:editable props true) (not (:read-only props)))
        focused?                (and focused? editable?)
        current-length          (if controlled? (count (or value "")) length)
        focus!                  (react/use-callback (fn []
                                                      (when-let [native (j/get input-ref :current)]
                                                        (j/call native :focus)))
                                                    [input-ref])
        change!                 (react/use-callback (fn [next-value]
                                                      (when-not controlled?
                                                        (set-length! (count next-value)))
                                                      (when on-change-text (on-change-text next-value)))
                                                    [controlled? on-change-text])
        focus-in!               (react/use-callback (fn [event]
                                                      (set-focused! true)
                                                      (when on-focus (on-focus event)))
                                                    [on-focus])
        focus-out!              (react/use-callback (fn [event]
                                                      (set-focused! false)
                                                      (when on-blur (on-blur event)))
                                                    [on-blur])
        text-style              (react/use-memo (fn []
                                                  [input-style/text-input-base
                                                   (typography/get-style :font/semibold-19)
                                                   (input-style/text-input-layout theme false 26 26)])
                                                [(:theme theme)])]
    [:rn/pressable {:disabled (not editable?)
                    :on-press focus!
                    :style    (rn.utils/add-styles (style/container disabled?) root-style)}
     [:rn/text-input (-> props
                         (dissoc :blur? :disabled? :input-ref :auto-focus?)
                         (assoc :ref                     input-ref
                                :style                   text-style
                                :multiline               false
                                :editable                editable?
                                :auto-focus              (and editable? (:auto-focus? props (:auto-focus props)))
                                :on-change-text          change!
                                :on-focus                focus-in!
                                :on-blur                 focus-out!
                                :placeholder             (:placeholder props "Type something")
                                :placeholder-text-color  (:placeholder-text-color props (style/placeholder theme blur? focused?))
                                :cursor-color            (:cursor-color props (style/cursor theme blur?))
                                :selection-color         (:selection-color props (style/selection theme))
                                :underline-color-android (:underline-color-android props "transparent")))]
     (if focused?
       (when max-length
         [:rn/view {:style style/counter}
          [text/text {:font  :font/regular-13
                      :style (style/counter-text theme blur?)}
           (str current-length "/" max-length)]])
       [:rn/view {:style style/edit}
        [icon/view {:name  :icon/edit
                    :size  20
                    :color (style/secondary theme blur?)}]])]))
