(ns xquo.profile.showcase-card.view
  (:require [react-native.react.core :as react]
            [react-native.utils :as rn.utils]
            [xquo.components.button.style :as button-style]
            [xquo.components.text.view :as text]
            [xquo.context :as context]
            [xquo.profile.showcase-card.style :as style]))

(defn showcase-card [{:keys [title disabled? on-press on-long-press on-press-in on-press-out]
                     :as   props}
                    & children]
  (let [theme                   (context/use-theme)
        [pressed? set-pressed!] (react/use-state false)
        pressable?              (or on-press on-long-press)
        pressed?                (and pressable? pressed? (not disabled?))
        component               (if pressable? :rn/pressable :rn/view)
        press-style             (if pressed?
                                  button-style/pressable-pressed-state-style
                                  button-style/pressable-default-state-style)
        press-in!               (react/use-callback (fn [event]
                                                     (set-pressed! true)
                                                     (when on-press-in (on-press-in event)))
                                                   [on-press-in])
        press-out!              (react/use-callback (fn [event]
                                                     (set-pressed! false)
                                                     (when on-press-out (on-press-out event)))
                                                   [on-press-out])]
    [component (cond-> props
                 :always (dissoc :title :disabled? :on-press-in :on-press-out)
                 :always (assoc :style (rn.utils/add-styles style/container (:style props)))
                 pressable? (assoc :disabled     disabled?
                                   :on-press-in  press-in!
                                   :on-press-out press-out!))
     [:animated/view {:style [(style/surface theme disabled?)
                              (when pressable? press-style)]}
      [text/text {:font            :font/semibold-15
                  :number-of-lines 1
                  :ellipsize-mode  :tail
                  :style           (style/title theme)}
       title]
      (into [:rn/view {:style style/content}] children)]]))
