(ns xquo.banner.global.style
  (:require [react-native.utils :refer [defstyle style]]
            [xquo.foundations.colors :as colors]))

(def type-color
  {:error :color/danger
   :alert :color/warning})

(defstyle container
  {:width "100%"})

(def row-height 40)

(defstyle row
  {:height          row-height
   :justify-content :center
   :align-items     :center})

(defstyle background
  {:position :absolute
   :left     0
   :top      0})

(defn stack [height]
  (style {:height   height
          :overflow :hidden}))

(defn title [type]
  (style {:height               18
          :include-font-padding false
          :text-align           :center
          :text-align-vertical  :center
          :color                (colors/get-color (type-color type) 60)}))
