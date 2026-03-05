(ns xquo.components.settings.section-title.style
  (:require [reagent-extended-compiler.utils.transforms :refer [defstyle]]
            [xquo.foundations.colors :as colors]))

(defstyle container-base
  {:flex-direction :row
   :align-items    :center
   :padding-left   20
   :padding-right  20
   :padding-top    8})

(defstyle container-gap-4
  {:gap 4})

(defstyle content-base
  {:flex-direction :row
   :align-items    :center
   :flex           1
   :min-width      1})

(defstyle content-gap-4
  {:gap 4})

(defstyle counter-base
  {:width         20
   :height        20
   :overflow      :hidden
   :border-radius 6})

(defstyle counter-surface
  {:position      :absolute
   :top           2
   :right         2
   :bottom        2
   :left          2
   :border-radius 6})

(defstyle counter-label-slot
  {:position        :absolute
   :top             2
   :right           2
   :bottom          2
   :left            2
   :justify-content :center
   :align-items     :center})

(defstyle counter-label
  {:text-align :center})

(defn label-color [theme]
  (if (= theme :theme/dark)
    (colors/get-color :color/white-100)
    (colors/get-color :color/neutral-100)))

(defn right-icon-color [theme]
  (if (= theme :theme/dark)
    (colors/get-color :color/neutral-40)
    (colors/get-color :color/neutral-50)))

(defn counter-surface-color [theme background]
  (cond
    (and (= theme :theme/dark) (= background :blur)) (colors/get-color :color/white 5)
    (= theme :theme/dark)                            (colors/get-color :color/neutral-80)
    :else                                            (colors/get-color :color/neutral-10)))

(defn counter-value-color [theme]
  (if (= theme :theme/dark)
    (colors/get-color :color/white-100)
    (colors/get-color :color/neutral-100)))
