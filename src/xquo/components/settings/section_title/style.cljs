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

(defstyle counter-placeholder
  {:width         20
   :height        20
   :background-color (colors/get-color :color/danger 50)})

(defn label-color [theme]
  (if (= theme :theme/dark)
    (colors/get-color :color/white-100)
    (colors/get-color :color/black)))

(defn right-icon-color [theme]
  (if (= theme :theme/dark)
    (colors/get-color :color/neutral-40)
    (colors/get-color :color/neutral-50)))
