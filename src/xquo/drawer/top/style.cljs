(ns xquo.drawer.top.style
  (:require [react-native.utils :refer [defstyle style]]
            [xquo.foundations.colors :as colors]))

(defn container [compact?]
  (style {:padding-horizontal 20
          :padding-bottom     (if compact? 8 12)}))

(defstyle leading-row
  {:flex-direction :row
   :align-items    :center
   :gap            8})

(defstyle column
  {:flex      1
   :min-width 0})

(defstyle title-row
  {:flex-direction :row
   :align-items    :center
   :min-height     26
   :gap            12})

(defstyle title
  {:flex           1
   :min-width      0
   :flex-direction :row
   :align-items    :center
   :gap            4})

(defstyle description-row
  {:flex-direction :row
   :align-items    :center
   :gap            4})

(defstyle tags
  {:flex-direction :row
   :flex-wrap      :wrap
   :align-items    :center
   :gap            4
   :margin-top     4})

(defn description [leading? tags?]
  (style {:margin-top (cond
                        tags?    4
                        leading? 0
                        :else    2)}))

(defn counter [theme blur?]
  (style {:color (colors/themed theme
                                :color/neutral-40
                                (if blur? :color/white-40 :color/neutral-50))}))

(defstyle image
  {:width  32
   :height 32})
