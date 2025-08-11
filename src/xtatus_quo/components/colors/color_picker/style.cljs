(ns xtatus-quo.components.colors.color-picker.style
  (:require [quo.foundations.colors :as colors]))

(def item-outer-size 48)
(def item-inner-size 40)
(def item-radius 20)
(def content-gap 8)
(def content-padding-h 16)
(def content-padding-v 8)

(def color-selected-border
  {:flex-direction :row
   :flex           1
   :height         item-outer-size})

(def item-container {:width          item-outer-size
                     :height         item-outer-size
                     :flex-direction :row})

(def content-container {:column-gap         content-gap
                        :padding-vertical   content-padding-v
                        :padding-horizontal content-padding-h})

(defn selected-left [theme color]
  {:height                    item-outer-size
   :flex                      1
   :background-color          (colors/resolve-color color theme 20)
   :border-top-left-radius    24
   :border-bottom-left-radius 24})

(defn selected-right [theme color]
  {:height                     item-outer-size
   :flex                       1
   :background-color           (colors/resolve-color color theme 40)
   :border-top-right-radius    24
   :border-bottom-right-radius 24})

(defn color-circle [theme color]
  {:position         :absolute
   :top              0
   :left             0
   :width            item-inner-size
   :height           item-inner-size
   :background-color (colors/resolve-color color theme)
   :border-radius    item-radius
   :transform        [{:translate-x 4} {:translate-y 4}]
   :justify-content  :center
   :align-items      :center})
