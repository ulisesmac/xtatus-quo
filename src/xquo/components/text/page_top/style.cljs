(ns xquo.components.text.page-top.style
  (:require [react-native.utils :refer [defstyle style]]
            [xquo.foundations.colors :as colors]))

(defstyle root
  {:width "100%"})

(defn content [spacing]
  (style {:padding-horizontal 20 :padding-vertical 12 :gap spacing}))

(defstyle title-row
  {:flex-direction :row :align-items :flex-start :gap 8})

(defstyle title
  {:flex 1 :min-width 0})

(defstyle image
  {:width 32 :height 32 :flex-shrink 0})

(defstyle description
  {:line-height 22 :include-font-padding false})

(defstyle footer
  {:padding-top 8 :padding-horizontal 20 :padding-bottom 12})

(defn border [theme blur?]
  (style {:position         :absolute
          :bottom           0
          :left             0
          :right            0
          :height           1
          :background-color (colors/themed theme :color/neutral-10 (if blur? :color/white-5 :color/neutral-80))}))
