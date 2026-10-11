(ns xquo.dapp.activity.style
  (:require [react-native.utils :refer [defstyle style]]
            [xquo.foundations.colors :as colors]))

(defstyle container
  {:padding-top        8
   :padding-bottom     12
   :padding-horizontal 12
   :border-radius      16
   :flex-direction     :row
   :align-items        :flex-start
   :gap                8})

(defstyle avatar
  {:margin-top 8})

(defstyle content
  {:flex      1
   :min-width 0
   :gap       2})

(defstyle header
  {:height         22
   :flex-direction :row
   :align-items    :center
   :gap            8})

(defstyle title-group
  {:flex-direction :row
   :align-items    :center
   :flex-shrink    1
   :gap            4})

(defstyle title
  {:height               22
   :flex-shrink          1
   :include-font-padding false
   :text-align-vertical  :center})

(defstyle flow
  {:flex-direction :row
   :flex-wrap      :wrap
   :align-items    :center
   :column-gap     4
   :row-gap        2})

(defstyle text
  {:height               18
   :include-font-padding false
   :text-align-vertical  :center})

(defstyle unread-slot
  {:width           32
   :height          22
   :align-items     :center
   :justify-content :center})

(defn unread [theme color]
  (style {:width            8
          :height           8
          :border-radius    4
          :background-color (colors/themed theme color)}))

(defn timestamp [theme blur?]
  (style {:flex                 1
          :height               16
          :margin-top           2
          :include-font-padding false
          :text-align-vertical  :center
          :color                (if blur?
                                  (colors/themed theme :color/neutral-80-40 :color/white-40)
                                  (colors/themed theme :color/neutral-40 :color/neutral-50))}))
