(ns xquo.banner.standard.view
  (:require [react-native.react.core :as react]
            [react-native.utils :as rn.utils]
            [xquo.banner.standard.style :as style]
            [xquo.components.counter.view :as counter]
            [xquo.components.icon.view :as icon]
            [xquo.components.text.view :as text]
            [xquo.context :as context]
            [xquo.foundations.colors :as colors]))

(defn banner-standard [{:keys [title icon counter blur? on-press-in on-press-out] :as props}]
  (let [{:keys [color dark-theme?]} (context/use-theme-color)
        foreground                  (colors/get-color (if dark-theme? :color/white-100 :color/neutral-100))
        [pressed? set-pressed!]     (react/use-state false)
        on-press-in!                (react/use-callback (fn [event]
                                                          (set-pressed! true)
                                                          (when on-press-in
                                                            (on-press-in event)))
                                                        [on-press-in])
        on-press-out!               (react/use-callback (fn [event]
                                                          (set-pressed! false)
                                                          (when on-press-out
                                                            (on-press-out event)))
                                                        [on-press-out])]
    [:animated/pressable (-> props
                             (dissoc :title :icon :counter :blur? :style :on-press-in :on-press-out)
                             (assoc :style        (rn.utils/add-styles style/container
                                                                       (style/state-style color pressed?)
                                                                       (:style props))
                                    :on-press-in  on-press-in!
                                    :on-press-out on-press-out!))
     (when icon
       [icon/view (assoc icon
                         :size  (:size icon 20)
                         :color (:color icon foreground))])
     [text/text {:font            :font/regular-13
                 :number-of-lines 1
                 :ellipsize-mode  :tail
                 :style           style/title}
      title]
     (when counter
       [counter/counter {:type :secondary :blur? blur?} counter])]))
