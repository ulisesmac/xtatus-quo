(ns xquo.components.list-items.user-item.style
  (:require [react-native.utils :refer [defstyle]]
            [xquo.foundations.colors :as colors]))

(defstyle row
  {:height 40})

(defstyle name-only
  {:align-self :center})

(defstyle image-container
  {:align-items     :center
   :align-self      :center
   :border-radius   16
   :height          32
   :justify-content :center
   :overflow        :hidden
   :width           32})

(defstyle image-placeholder-light
  {:background-color (colors/get-color :color/neutral-80-5)})

(defstyle image-placeholder-dark
  {:background-color (colors/get-color :color/white-5)})

(defstyle description-light
  {:color (colors/get-color :color/neutral-50)})

(defstyle description-dark
  {:color (colors/get-color :color/neutral-40)})
