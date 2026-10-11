(ns xquo.notification.style
  (:require [react-native.utils :refer [style]]
            [xquo.foundations.colors :as colors]))

(defn frame [radius inset]
  (style {:margin-horizontal inset
          :border-radius     radius
          :box-shadow        [{:offset-x    0
                               :offset-y    4
                               :blur-radius 20
                               :color       (colors/get-color :color/neutral-100-4)}]}))

(defn backdrop [{:keys [dark-theme?]} radius]
  (style {:position         :absolute
          :top              0
          :right            0
          :bottom           0
          :left             0
          :border-radius    radius
          :background-color (colors/get-color (if dark-theme? :color/white-70 :color/neutral-80-70))}))

(defn foreground [{:keys [dark-theme?]}]
  (colors/get-color (if dark-theme? :color/neutral-100 :color/white)))
