(ns xquo.community.main-card.style
  (:require [react-native.utils :refer [defstyle style]]
            [xquo.foundations.colors :as colors]
            [xquo.foundations.shadows :as shadows]))

(defn container [theme]
  (style {:width         335
          :max-width     "100%"
          :border-radius 16
          :box-shadow    (shadows/get-box-shadow theme 2)}))

(defstyle clip
  {:border-radius 16
   :overflow      :hidden})

(defn cover [theme]
  (style {:height           64
          :background-color (colors/themed theme :color/white-100 :color/neutral-90)}))

(defstyle cover-image
  {:width  "100%"
   :height 64})

(defn cover-fill [theme color loading?]
  (style {:height           64
          :background-color (if loading?
                              (colors/themed theme :color/neutral-10 :color/neutral-80)
                              (colors/get-color color 50 20))}))

(defn body [theme loading?]
  (style {:margin-top              -24
          :padding-top             (if loading? 36 32)
          :padding-horizontal      12
          :padding-bottom          12
          :border-top-left-radius  16
          :border-top-right-radius 16
          :background-color        (colors/themed theme :color/white-100 :color/neutral-90)
          :box-shadow              (shadows/get-box-shadow :theme/light :inverted 2)}))

(defstyle avatar
  {:position :absolute
   :left     12
   :top      16})

(defstyle permissions
  {:position :absolute
   :right    8
   :top      48})

(defstyle info
  {:gap 2})

(defstyle title
  {:height               26
   :include-font-padding false})

(defstyle description
  {:height               22
   :include-font-padding false})

(defstyle stats
  {:margin-top     12
   :height         22
   :flex-direction :row
   :gap            12})

(defstyle tags
  {:margin-top     16
   :height         24
   :flex-direction :row
   :gap            8})

(defn skeleton [theme width height radius secondary?]
  (style {:width            width
          :height           height
          :border-radius    radius
          :background-color (if secondary?
                              (colors/themed theme :color/neutral-10 :color/neutral-80)
                              (colors/themed theme :color/neutral-20 :color/neutral-70))}))

(defstyle skeleton-info
  {:gap 8})

(defstyle skeleton-stats
  {:margin-top     28
   :height         14
   :flex-direction :row
   :gap            12})

(defstyle skeleton-stat
  {:flex-direction :row
   :align-items    :center
   :gap            4})

(defstyle skeleton-tags
  {:margin-top     20
   :flex-direction :row
   :gap            8})
