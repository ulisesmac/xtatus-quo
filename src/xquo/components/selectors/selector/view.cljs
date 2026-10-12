(ns xquo.components.selectors.selector.view
  (:require [react-native.react.core :as react]
            [react-native.utils :as rn.utils]
            [xquo.components.button.style :as button-style]
            [xquo.components.icon.view :as icon]
            [xquo.components.selectors.selector.style :as style]
            [xquo.context :as context]
            [xquo.foundations.colors :as colors]))

(defn selector [{:keys [type selected? disabled? blur? on-select on-press on-press-in on-press-out]
                 :or   {type :toggle}
                 :as   props}]
  (let [{:keys [light-theme?] :as theme-color} (context/use-theme-color)
        controlled?                            (contains? props :selected?)
        [internal? set-internal!]              (react/use-state false)
        [pressed? set-pressed!]                (react/use-state false)
        selected?                              (if controlled? selected? internal?)
        pressed?                               (and pressed? (not disabled?))
        press-in!                              (react/use-callback (fn [event]
                                                                     (set-pressed! true)
                                                                     (when on-press-in (on-press-in event)))
                                                                   [on-press-in])
        press-out!                             (react/use-callback (fn [event]
                                                                     (set-pressed! false)
                                                                     (when on-press-out (on-press-out event)))
                                                                   [on-press-out])
        press!                                 (react/use-callback (fn [event]
                                                                     (let [next-selected? (not selected?)]
                                                                       (when-not controlled? (set-internal! next-selected?))
                                                                       (when on-select (on-select next-selected?))
                                                                       (when on-press (on-press event))))
                                                                   [selected? controlled? on-select on-press])]
    [:animated/pressable (-> props
                             (dissoc :type :selected? :disabled? :blur? :on-select)
                             (assoc :disabled     disabled?
                                    :hit-slop     (:hit-slop props 6)
                                    :on-press     press!
                                    :on-press-in  press-in!
                                    :on-press-out press-out!
                                    :style        (rn.utils/add-styles (style/containers type)
                                                                       (style/state-style theme-color type blur? selected? disabled?)
                                                                       (if pressed?
                                                                         button-style/pressable-pressed-state-style
                                                                         button-style/pressable-default-state-style)
                                                                       (:style props))))
     (case type
       :toggle
       [:animated/view {:style [style/toggle-handle (style/handle-position selected?)]}]

       :radio
       [:animated/view {:style (style/radio-dot theme-color blur? selected?)}]

       (when selected?
         [icon/view {:name  :icon/check-thick
                     :size  12
                     :color (colors/get-color (if (and (= type :filled-checkbox) light-theme?)
                                                :color/neutral-100
                                                :color/white))
                     :style style/checkmark}]))]))
