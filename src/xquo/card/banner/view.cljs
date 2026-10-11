(ns xquo.card.banner.view
  (:require [react-native.react.core :as react]
            [react-native.utils :refer [add-styles]]
            [xquo.card.banner.style :as style]
            [xquo.components.button.style :as button-style]
            [xquo.components.text.view :as text]
            [xquo.context :as context]))

(defn banner [{:keys [title description image on-press] :as props}]
  (let [theme                   (context/use-theme)
        [pressed? set-pressed!] (react/use-state false)
        on-press-in!            (react/use-callback #(set-pressed! true) [])
        on-press-out!           (react/use-callback #(set-pressed! false) [])]
    [:animated/pressable {:on-press     on-press
                          :on-press-in  on-press-in!
                          :on-press-out on-press-out!
                          :style        (add-styles (style/container theme pressed?)
                                                    (if pressed?
                                                      button-style/pressable-pressed-state-style
                                                      button-style/pressable-default-state-style)
                                                    (:style props))}
     [text/text {:font :font/semibold-15 :number-of-lines 1 :style style/title} title]
     [text/text {:font :font/regular-13 :number-of-lines 1 :style style/description} description]
     [:rn/image {:source      (or (:source image) {:uri (:url image)})
                 :resize-mode :contain
                 :style       (add-styles style/image (:style image))}]]))
