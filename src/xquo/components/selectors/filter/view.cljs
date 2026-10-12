(ns xquo.components.selectors.filter.view
  (:refer-clojure :exclude [filter])
  (:require [react-native.react.core :as react]
            [react-native.utils :as rn.utils]
            [xquo.components.button.style :as button-style]
            [xquo.components.icon.view :as icon]
            [xquo.components.selectors.filter.style :as style]
            [xquo.components.text.view :as text]
            [xquo.context :as context]))

(defn filter
  ([props] (filter props nil))
  ([{:keys [selected? disabled? size blur? icon on-select on-press on-press-in on-press-out]
     :or   {size 32}
     :as   props} label]
   (let [theme-color             (context/use-theme-color)
         [pressed? set-pressed!] (react/use-state false)
         pressed?                (and pressed? (not disabled?))
         press-in!               (react/use-callback (fn [event]
                                                       (set-pressed! true)
                                                       (when on-press-in (on-press-in event)))
                                                     [on-press-in])
         press-out!              (react/use-callback (fn [event]
                                                       (set-pressed! false)
                                                       (when on-press-out (on-press-out event)))
                                                     [on-press-out])
         press!                  (react/use-callback (fn [event]
                                                       (when on-select (on-select (not selected?)))
                                                       (when on-press (on-press event)))
                                                     [selected? on-select on-press])]
     [:animated/pressable (-> props
                              (dissoc :selected? :disabled? :size :blur? :icon :on-select)
                              (assoc :disabled     disabled?
                                     :hit-slop     (:hit-slop props 6)
                                     :on-press     press!
                                     :on-press-in  press-in!
                                     :on-press-out press-out!
                                     :style        (rn.utils/add-styles (style/container size label disabled?)
                                                                        (style/surface theme-color blur? selected?)
                                                                        (if pressed?
                                                                          button-style/pressable-pressed-state-style
                                                                          button-style/pressable-default-state-style)
                                                                        (:style props))))
      [icon/view (assoc icon
                        :name  (:name icon :icon/unread)
                        :size  (:size icon (style/icon-size size))
                        :color (:color icon (style/foreground theme-color selected?)))]
      (when label
        [text/text {:font (style/label-font size) :style (style/label size theme-color selected?)}
         label])])))
