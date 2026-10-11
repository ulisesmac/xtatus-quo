(ns xquo.community.channel-action.view
  (:require [react-native.react.core :as react]
            [react-native.utils :refer [add-styles]]
            [xquo.community.channel-action.style :as style]
            [xquo.components.button.style :as button-style]
            [xquo.components.counter.view :as counter]
            [xquo.components.icon.view :as icon]
            [xquo.components.text.view :as text]
            [xquo.context :as context]
            [xquo.foundations.colors :as colors]))

(defn channel-action [{:keys [title icon counter disabled? on-press] :as props}]
  (let [theme                   (context/use-theme)
        color                   (context/use-color)
        [pressed? set-pressed!] (react/use-state false)
        on-press-in!            (react/use-callback #(set-pressed! true) [])
        on-press-out!           (react/use-callback #(set-pressed! false) [])]
    [:animated/pressable {:on-press     on-press
                          :disabled     disabled?
                          :on-press-in  on-press-in!
                          :on-press-out on-press-out!
                          :style        (add-styles style/container
                                                    (style/state-style color (and pressed? (not disabled?)) disabled?)
                                                    (if pressed?
                                                      button-style/pressable-pressed-state-style
                                                      button-style/pressable-default-state-style)
                                                    (:style props))}
     [:rn/view {:style style/top}
      [icon/view (assoc icon
                        :size  (:size icon 20)
                        :color (:color icon (colors/themed theme :color/neutral-100 :color/white-100)))]
      (when counter
        [counter/counter {:type :secondary} counter])]
     [text/text {:font :font/medium-15 :number-of-lines 2 :style style/title} title]]))
