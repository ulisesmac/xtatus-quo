(ns xquo.dapp.group.style
  (:require [react-native.utils :refer [defstyle style]]
            [xquo.foundations.colors :as colors]))

(defstyle container
  {:border-radius  16
   :padding-bottom 8})

(defstyle header
  {:height             60
   :padding-top        8
   :padding-bottom     12
   :padding-horizontal 12
   :flex-direction     :row
   :align-items        :center
   :gap                8})

(defstyle text
  {:flex      1
   :min-width 0})

(defstyle title-row
  {:flex-direction :row
   :align-items    :center
   :gap            8})

(defstyle title
  {:height               22
   :flex-shrink          1
   :include-font-padding false
   :text-align-vertical  :center})

(defstyle items
  {:padding-horizontal 8
   :gap                8})

(defstyle list-card
  {:padding-right 10})

(defn border [theme blur?]
  (style {:position      :absolute
          :top           0
          :right         0
          :bottom        0
          :left          0
          :border-radius 16
          :border-width  1
          :border-color  (if blur?
                           (colors/themed theme :color/neutral-80-5 :color/white-5)
                           (colors/themed theme :color/neutral-20 :color/neutral-80))}))
