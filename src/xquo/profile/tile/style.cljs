(ns xquo.profile.tile.style
  (:require [react-native.utils :refer [defstyle style]]
            [xquo.foundations.colors :as colors]
            [xquo.foundations.shadows :as shadows]))

(defstyle container
  {:width 176 :height 176 :padding 8})

(defn frame [theme cover disabled?]
  (style {:flex             1
          :border-radius    16
          :opacity          (if disabled? 0.3 1)
          :background-color (colors/themed theme :color/white (if cover :color/neutral-95 :color/neutral-90))
          :box-shadow       (shadows/get-box-shadow :theme/light 2)}))

(defstyle clipped
  {:flex 1 :border-radius 16 :overflow :hidden})

(defstyle cover
  {:position :absolute :top 0 :left 0 :right 0 :height 64})

(defn cover-color [color]
  (style {:background-color (colors/get-color color 50 20)}))

(defn surface [theme]
  (style {:position         :absolute
          :top              40
          :right            0
          :bottom           0
          :left             0
          :border-radius    16
          :background-color (colors/themed theme :color/white :color/neutral-90)}))

(defstyle content
  {:flex 1 :padding 12})

(defn avatar-frame [size]
  (style {:width size :height size}))

(defn avatar-clearance [theme radius]
  (style {:position         :absolute
          :top              -2
          :left             -2
          :right            -2
          :bottom           -2
          :border-radius    (+ radius 2)
          :background-color (colors/themed theme :color/white :color/neutral-90)}))

(defstyle info
  {:height 40 :margin-top 8})

(defstyle footer
  {:flex 1 :justify-content :flex-end :align-items :flex-start})

(defn text [theme secondary?]
  (let [height (if secondary? 18 22)]
    (style {:height               height
            :line-height          height
            :include-font-padding false
            :text-align-vertical  :center
            :color                (if secondary?
                                    (colors/themed theme :color/neutral-50 :color/neutral-40)
                                    (colors/themed theme :color/neutral-100 :color/white))})))

(defn skeleton [theme width height radius]
  (style {:width            width
          :height           height
          :border-radius    radius
          :background-color (colors/themed theme :color/neutral-5 :color/neutral-80)}))

(defstyle skeleton-description
  {:margin-top 8})

(defn footer-outline [theme]
  (style {:width              56
          :height             28
          :border-radius      10
          :border-width       1
          :border-color       (colors/themed theme :color/neutral-5 :color/neutral-80)
          :padding-horizontal 7
          :align-items        :center
          :flex-direction     :row}))

(defn skeleton-token [theme index]
  (style {:width            16
          :height           16
          :border-radius    8
          :margin-left      (when (pos? index) -4)
          :border-width     1
          :border-color     (colors/themed theme :color/white :color/neutral-90)
          :background-color (colors/themed theme :color/neutral-5 :color/neutral-80)}))
