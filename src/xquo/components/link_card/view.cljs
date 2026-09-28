(ns xquo.components.link-card.view
  (:require [react-native.react.core :as react]
            [xquo.components.button.style :as button-style]
            [xquo.components.icon.view :as icon]
            [xquo.components.link-card.style :as style]
            [xquo.components.text.view :as text]
            [xquo.context :as context]
            [xquo.foundations.colors :as colors]))

(defn link-card [{:keys [gradient-color on-press social-network social-handle]
                  {:keys [icon url source]} :image}]
  (let [{:keys [theme color dark-theme?]} (context/use-theme-color)
        [pressed? set-pressed!]           (react/use-state false)
        on-press-in!                     (react/use-callback #(set-pressed! true) [])
        on-press-out!                    (react/use-callback #(set-pressed! false) [])]
    [:animated/pressable {:on-press           on-press
                          :on-press-in        on-press-in!
                          :on-press-out       on-press-out!
                          :style              [style/container
                                               (style/gradient (or gradient-color color))
                                               (cond
                                                 (and pressed? dark-theme?) style/border-dark-pressed
                                                 pressed?                   style/border-light-pressed
                                                 dark-theme?                style/border-dark
                                                 :else                      style/border-light)
                                               (if pressed?
                                                 button-style/pressable-pressed-state-style
                                                 button-style/pressable-default-state-style)]}
     [:rn/view {:style style/top-row}
      (cond
        icon
        [icon/view (assoc icon
                         :size 20
                         :color (colors/themed theme :color/neutral-50 :color/neutral-40))]

        (or source url)
        [:rn/image {:resize-mode :contain
                    :source      (or source {:uri url})
                    :style       style/image}])]
     [:rn/view
      [text/text {:font :font/semibold-15}
       social-network]
      [text/text {:ellipsize-mode  :tail
                  :font            :font/regular-13
                  :number-of-lines 1
                  :style           (if (= theme :theme/dark) style/handle-dark style/handle-light)}
       social-handle]]]))
