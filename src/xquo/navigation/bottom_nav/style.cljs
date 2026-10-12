(ns xquo.navigation.bottom-nav.style
  (:require [react-native.utils :refer [defstyle style]]
            [xquo.foundations.colors :as colors]))

(defn container [item-count]
  (style {:width          "100%"
          :max-width      (+ 16 (* 68 item-count))
          :align-self     :center
          :flex-direction :row
          :padding        8
          :border-radius  18
          :overflow       :hidden}))

(defstyle tab
  {:width      :auto
   :flex-grow  1
   :flex-basis 0
   :min-width  0})

(defn backdrop [background-color]
  (style {:position         :absolute
          :top              0
          :right            0
          :bottom           0
          :left             0
          :border-radius    18
          :background-color (or background-color (colors/get-color :color/neutral-100-70))}))
