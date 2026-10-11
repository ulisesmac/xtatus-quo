(ns xquo.components.info.list-info-box.style
  (:require [react-native.utils :refer [defstyle style]]
            [xquo.foundations.colors :as colors]))

(defn container [theme]
  (style {:border-radius      16
          :border-width       1
          :padding-horizontal 15
          :padding-vertical   11
          :gap                8
          :background-color   (colors/themed theme :color/neutral-2.5 :color/neutral-90)
          :border-color       (colors/themed theme :color/neutral-10 :color/neutral-80)}))

(defn title [theme]
  (style {:include-font-padding false
          :line-height          18
          :color                (colors/themed theme :color/neutral-50 :color/neutral-40)}))

(defstyle items
  {:gap 2})

(defstyle item
  {:padding-horizontal 0
   :padding-vertical   0})

(defstyle description
  {:include-font-padding false
   :line-height          18
   :min-height           20
   :text-align-vertical  :center})
