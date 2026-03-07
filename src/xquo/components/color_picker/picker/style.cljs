(ns xquo.components.color-picker.picker.style
  (:require [reagent-extended-compiler.utils.transforms :refer [defstyle]]))

(defstyle container-base
  {:width "100%"})

(defstyle content-container-base
  {:flex-grow 1})

(defstyle row-base
  {:flex-direction :row
   :align-items    :center
   :gap            8})

(defstyle row-centered
  {:align-self :center})
