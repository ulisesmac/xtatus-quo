(ns xquo.components.settings.section-label.style
  (:require [reagent-extended-compiler.utils.transforms :refer [defstyle]]
            [xquo.foundations.colors :as colors]))

(defstyle row-base
  {:flex-direction :row
   :align-items    :center})

(defstyle row-gap-12
  {:gap 12})

(defstyle label-slot
  {:flex      1
   :min-width 1})

(defstyle counter-value
  {:text-align :right})

(defstyle description-base
  {:flex-direction :column
   :align-items    :flex-start
   :gap            2})

(defstyle description-line
  {:align-self :stretch})

(defn label-color [theme blur?]
  {:color (cond
            (and (= theme :theme/dark) blur?) (colors/get-color :color/white-40)
            (= theme :theme/dark)             (colors/get-color :color/neutral-40)
            :else                             (colors/get-color :color/neutral-50))})

(defn counter-color [theme blur?]
  {:color (cond
            (and (= theme :theme/dark) blur?) (colors/get-color :color/white-40)
            (= theme :theme/dark)             (colors/get-color :color/neutral-30)
            :else                             (colors/get-color :color/neutral-40))})
