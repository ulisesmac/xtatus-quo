(ns xquo.components.color-picker.color.style
  (:require [reagent-extended-compiler.utils.transforms :refer [defstyle]]
            [xquo.foundations.borders :as borders]
            [xquo.foundations.colors :as colors]))

(defstyle container-base
  {:width           48
   :height          48
   :position        :relative
   :overflow        :visible
   :align-items     :center
   :justify-content :center})

(defstyle selection-ring-base
  {:position      :absolute
   :top           0
   :left          0
   :width         48
   :height        48
   :overflow      :hidden
   :border-radius (:border/max borders/border-radius-values)})

(defstyle selection-ring-left
  {:position :absolute
   :top      0
   :left     0
   :width    24
   :height   48})

(defstyle selection-ring-right
  {:position :absolute
   :top      0
   :right    0
   :width    24
   :height   48})

(defstyle swatch-base
  {:width           40
   :height          40
   :align-items     :center
   :justify-content :center
   :border-radius   (:border/max borders/border-radius-values)})

(defn selection-ring-left-style [color]
  {:background-color (colors/get-color color 50 20)})

(defn selection-ring-right-style [color]
  {:background-color (colors/get-color color 50 40)})

(defn swatch-color-style [theme blur? color]
  {:background-color (colors/get-color color
                                       (if (and (= theme :theme/dark)
                                                (not blur?))
                                         60
                                         50))})
