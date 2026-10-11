(ns xquo.components.favicon.style
  (:require [react-native.utils :refer [defstyle style]]
            [xquo.foundations.colors :as colors]))

(defstyle container
  {:width           16
   :height          16
   :flex-shrink     0
   :border-radius   8
   :overflow        :hidden
   :align-items     :center
   :justify-content :center})

(defstyle image
  {:width  16
   :height 16})

(defstyle initials
  {:include-font-padding false
   :text-align           :center
   :text-align-vertical  :center})

(defn background [theme blur?]
  (style {:background-color (if blur?
                              (colors/themed theme :color/neutral-80-5 :color/white-5)
                              (colors/themed theme :color/neutral-10 :color/neutral-80))}))

(defn foreground [theme blur?]
  (style {:color (if blur?
                   (colors/themed theme :color/neutral-80-50 :color/white-70)
                   (colors/themed theme :color/neutral-50 :color/neutral-40))}))

(defn border [theme image?]
  (style {:position      :absolute
          :top           0
          :right         0
          :bottom        0
          :left          0
          :border-radius 8
          :border-width  1
          :border-color  (if image?
                           (colors/get-color :color/neutral-80-5)
                           (colors/themed theme :color/neutral-80-5 :color/white-5))}))
