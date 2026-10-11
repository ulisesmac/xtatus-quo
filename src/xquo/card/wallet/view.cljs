(ns xquo.card.wallet.view
  (:require [react-native.react.core :as react]
            [react-native.utils :refer [add-styles]]
            [xquo.card.wallet.style :as style]
            [xquo.components.button.style :as button-style]
            [xquo.components.button.view :as button]
            [xquo.components.text.view :as text]
            [xquo.context :as context]))

(defn wallet [{:keys [title description image on-press on-dismiss] :as props}]
  (let [theme                   (context/use-theme)
        [pressed? set-pressed!] (react/use-state false)
        on-press-in!            (react/use-callback #(set-pressed! true) [])
        on-press-out!           (react/use-callback #(set-pressed! false) [])]
    [:animated/view {:style (add-styles (style/container theme)
                                        (if pressed?
                                          button-style/pressable-pressed-state-style
                                          button-style/pressable-default-state-style)
                                        (:style props))}
     [:rn/pressable {:on-press     on-press
                     :on-press-in  on-press-in!
                     :on-press-out on-press-out!
                     :style        style/content}
      [:rn/image {:source      (or (:source image) {:uri (:url image)})
                  :resize-mode :contain
                  :style       (add-styles style/image (:style image))}]
      [:rn/view
       [text/text {:font :font/semibold-15 :number-of-lines 1 :style style/title} title]
       [text/text {:font :font/regular-13 :number-of-lines 1 :style (style/description theme)} description]]]
     (when on-dismiss
       [button/button {:type     :ghost
                       :size     24
                       :icon     {:name :icon/cross}
                       :on-press on-dismiss
                       :style    style/dismiss}])]))
