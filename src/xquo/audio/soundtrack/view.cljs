(ns xquo.audio.soundtrack.view
  (:require [applied-science.js-interop :as j]
            [react-native.gesture-handler.core :as gh]
            [react-native.react.core :as react]
            [react-native.utils :refer [add-styles]]
            [xquo.audio.soundtrack.style :as style]
            [xquo.components.text.view :as text]
            [xquo.context :as context]
            [xquo.foundations.colors :as colors]))

(defn track [{:keys [progress state warning? on-seek track-color]
              :or   {progress 0 state :default}}]
  (let [theme              (context/use-theme)
        context-color      (context/use-color)
        listening?         (= state :listening)
        color              (colors/themed theme (if (or (= state :deleting)
                                                        (and (= state :default) warning?))
                                                  :color/danger
                                                  context-color))
        [width set-width!] (react/use-state 0)
        on-layout!         (react/use-callback #(set-width! (j/get-in % [:nativeEvent :layout :width])) [])
        seek!              (react/use-callback (fn [event]
                                                 (when (and on-seek (pos? width))
                                                   (on-seek (max 0 (min 1 (/ (j/get event :x) width))))))
                                               [on-seek width])
        gesture            (react/use-memo (fn []
                                             (doto (gh/gesture-pan)
                                               (j/call :enabled (boolean on-seek))
                                               (j/call :runOnJS true)
                                               (j/call :minDistance 0)
                                               (j/call :onBegin seek!)
                                               (j/call :onUpdate seek!)))
                                           [on-seek seek!])]
    [:gh/gesture-detector {:gesture gesture}
     [:rn/view {:on-layout on-layout! :style style/track-container}
      [:rn/view {:pointer-events :none :style (style/track theme track-color)}]
      (when (not= state :loading)
        [:rn/view {:pointer-events :none :style (style/progress progress color listening?)}])
      (when listening?
        [:rn/view {:pointer-events :none :style (style/thumb progress color)}])]]))

(defn soundtrack [{:keys [time state]
                   :or   {state :default}
                   :as   props}]
  (let [theme (context/use-theme)]
    [:rn/view {:style (add-styles (style/container time state) (:style props))}
     (when time
       [:rn/view {:style style/timer}
        (when (not= state :listening)
          [:rn/view {:style style/indicator-slot}
           [:rn/view {:style (style/indicator theme)}]])
        [text/text {:font :font/semibold-11 :style (style/time-label theme state)} time]])
     [track props]]))
