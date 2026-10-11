(ns xquo.camera.shutter.view
  (:require [react-native.react.core :as react]
            [react-native.utils :as rn.utils]
            [xquo.camera.shutter.style :as style]
            [xquo.components.icon.view :as icon]
            [xquo.context :as context]
            [xquo.foundations.colors :as colors]))

(defn shutter [{:keys [action disabled? on-press-in on-press-out]
                :or   {action :shutter}
                :as   props}]
  (let [color                   (context/use-color)
        [pressed? set-pressed!] (react/use-state false)
        press-in!               (react/use-callback (fn [event]
                                                      (set-pressed! true)
                                                      (when on-press-in (on-press-in event)))
                                                    [on-press-in])
        press-out!              (react/use-callback (fn [event]
                                                      (set-pressed! false)
                                                      (when on-press-out (on-press-out event)))
                                                    [on-press-out])]
    [:rn/pressable (-> props
                       (dissoc :action :disabled?)
                       (assoc :disabled     disabled?
                              :on-press-in  press-in!
                              :on-press-out press-out!
                              :style        (rn.utils/add-styles style/container
                                                                 (style/disabled disabled?)
                                                                 (:style props))))
     (when (= action :shutter)
       [:rn/view {:pointer-events :none :style style/ring}])
     [:animated/view {:pointer-events :none :style (style/circle color action pressed?)}
      (when (= action :confirm)
        [icon/view {:name :icon/check :size 20 :color (colors/get-color :color/white-100)}])]]))
