(ns xquo.link-preview.style
  (:require [react-native.utils :refer [defstyle style]]
            [xquo.foundations.colors :as colors]))

(defn container [theme url?]
  (style {:width              "100%"
          :border-radius      16
          :padding-horizontal 12
          :padding-top        (if url? 12 10)
          :padding-bottom     12
          :gap                12
          :background-color   (colors/themed theme :color/white-100 :color/neutral-80-40)}))

(defn border [theme]
  (style {:position      :absolute
          :top           0
          :right         0
          :bottom        0
          :left          0
          :border-width  1
          :border-radius 16
          :border-color  (colors/themed theme :color/neutral-20 :color/neutral-80)}))

(defstyle content
  {:gap 8})

(defstyle main
  {:gap 2})

(defstyle title-row
  {:flex-direction :row
   :align-items    :center
   :gap            6})

(defstyle title
  {:flex                 1
   :height               22
   :include-font-padding false
   :text-align-vertical  :center})

(defstyle description
  {:include-font-padding false})

(defn url [theme]
  (style {:height               18
          :include-font-padding false
          :text-align-vertical  :center
          :color                (colors/themed theme :color/neutral-50 :color/neutral-40)}))

(defn bare-url [color dark-theme?]
  (style {:include-font-padding false
          :color                (colors/get-color color (if dark-theme? 60 50))}))

(defn media [detail?]
  (style {:width         "100%"
          :height        (when-not detail? 139)
          :aspect-ratio  (when detail? 1)
          :border-radius 12
          :overflow      :hidden}))

(defstyle image
  {:width "100%" :height "100%"})

(defstyle image-border
  {:position      :absolute
   :top           0
   :right         0
   :bottom        0
   :left          0
   :border-width  1
   :border-radius 12
   :border-color  (colors/get-color :color/neutral-80-5)})
