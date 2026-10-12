(ns xquo.profile.card.style
  (:require [react-native.utils :refer [defstyle style]]
            [xquo.foundations.colors :as colors]))

(defn container [color]
  (style {:width            "100%"
          :padding-top      12
          :border-radius    16
          :background-color (colors/get-color color 50 40)}))

(defstyle header
  {:padding-horizontal 12
   :flex-direction     :row
   :align-items        :flex-start
   :justify-content    :space-between
   :gap                8})

(defstyle actions
  {:flex-direction :row :align-items :center :gap 8})

(defstyle text
  {:padding-top        8
   :padding-horizontal 12
   :padding-bottom     10
   :gap                2})

(defstyle name-row
  {:flex-direction :row :align-items :center :gap 4})

(defstyle name-text
  {:flex-shrink          1
   :height               26
   :line-height          26
   :include-font-padding false
   :text-align-vertical  :center
   :color                (colors/get-color :color/white-100)})

(defstyle name-icon
  {:height 26 :padding-top 4})

(defstyle description
  {:height               22
   :line-height          22
   :include-font-padding false
   :text-align-vertical  :center
   :color                (colors/get-color :color/white-60)})
