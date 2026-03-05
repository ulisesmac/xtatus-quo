(ns xquo.components.settings.sort-item.style
  (:require [reagent-extended-compiler.utils.transforms :refer [defstyle]]
            [xquo.foundations.borders :as borders]
            [xquo.foundations.colors :as colors]))

(defstyle container-base
  {:flex-direction :row
   :align-items    :center
   :padding-left   16
   :padding-right  12
   :padding-top    12
   :padding-bottom 12
   :border-radius  (:border/sizes-40-56 borders/border-radius-values)})

(defstyle label-wrapper
  {:flex 1})

(defstyle actions-row
  {:flex-direction :row
   :align-items    :center})

(defstyle action-gap
  {:margin-left 8})

(defn label-color [theme]
  (if (= theme :theme/dark)
    (colors/get-color :color/white-100)
    (colors/get-color :color/neutral-100)))
