(ns xquo.demo.style
  (:require [react-native.utils :refer [defstyle style]]
            [xquo.foundations.colors :as colors]))

(defn root [theme top bottom]
  (style {:flex             1
          :padding-top      top
          :padding-bottom   bottom
          :background-color (colors/themed theme :color/white :color/neutral-95)}))

(defstyle body
  {:flex 1 :min-height 0})

(defn preview [height]
  (style {:height             height
          :flex-shrink        0
          :min-height         0
          :overflow           :hidden
          :justify-content    :center
          :padding-horizontal 10
          :padding-vertical   20}))

(defn scroll-content [top]
  (style {:flex-grow 1 :padding-top (+ top 56)}))

(defstyle background
  {:position :absolute :top 0 :right 0 :bottom 0 :left 0})

(defstyle shell
  {:flex          1
   :border-radius 16
   :overflow      :hidden})

(defn blur-overlay [theme]
  (style {:flex             1
          :background-color (colors/themed theme :color/white-70 :color/neutral-80-80)}))

(defstyle preview-content
  {:flex-grow 1 :justify-content :center :padding 20})

(defn bottom-safe-area [theme bottom]
  (style {:position         :absolute
          :bottom           0
          :left             0
          :right            0
          :height           bottom
          :background-color (colors/themed theme :color/white :color/neutral-95)}))

(defn panel [theme]
  (style {:border-top-width 1
          :border-top-color (colors/themed theme :color/neutral-20 :color/white-10)
          :background-color (colors/themed theme :color/white :color/neutral-95)}))

(defstyle content
  {:padding 20 :gap 16})

(defstyle field
  {:gap 8})

(defstyle choices
  {:flex-direction :row :flex-wrap :wrap :gap 8})
