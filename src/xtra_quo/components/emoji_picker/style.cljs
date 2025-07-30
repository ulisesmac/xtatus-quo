(ns xtra-quo.components.emoji-picker.style
  (:require
   [react-native.safe-area :as safe-area]))

(def emoji-selection
  {:padding-top 12})

(def emoji-row
  {:flex-direction     :row
   :column-gap         18.5
   :justify-content    :flex-start
   :padding-vertical   8
   :padding-horizontal 20})

(def emoji-pressable
  {:justify-content :center
   :align-items     :center})

(def emoji-text
  {:font-size            50
   :include-font-padding false})

(def bottom-nav
  {:position           :absolute
   :bottom             0
   :left               0
   :right              0
   :flex-direction     :row
   :justify-content    :space-between
   :padding-horizontal 20
   :padding-top        12
   :padding-bottom     (+ 12 safe-area/bottom)})

(def input-patch
  {:position         :absolute
   :top              32
   :left             0
   :right            0
   :height           11})
