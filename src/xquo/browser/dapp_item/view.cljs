(ns xquo.browser.dapp-item.view
  (:require [react-native.react.core :as react]
            [react-native.utils :as rn.utils]
            [xquo.browser.dapp-item.style :as style]
            [xquo.components.avatar.style :as avatar-style]
            [xquo.components.text.view :as text]))

(defn dapp-item [{:keys [image title on-press-in on-press-out] :as props}]
  (let [[pressed? set-pressed!] (react/use-state false)
        press-in!               (react/use-callback (fn [event]
                                                      (set-pressed! true)
                                                      (when on-press-in (on-press-in event)))
                                                    [on-press-in])
        press-out!              (react/use-callback (fn [event]
                                                      (set-pressed! false)
                                                      (when on-press-out (on-press-out event)))
                                                    [on-press-out])]
    [:animated/pressable (-> props
                             (dissoc :image :title)
                             (assoc :on-press-in  press-in!
                                    :on-press-out press-out!
                                    :style        (rn.utils/add-styles style/container
                                                                       (avatar-style/press-state pressed?)
                                                                       (:style props))))
     [:rn/image {:source      (or (:source image) {:uri (:url image)})
                 :resize-mode :contain
                 :style       (rn.utils/add-styles style/image (:style image))}]
     [text/text {:font            :font/medium-13
                 :number-of-lines 1
                 :style           style/title}
      title]]))
