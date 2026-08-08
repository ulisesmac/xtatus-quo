(ns xquo.components.page-top.style
  (:require [react-native.utils :refer [defstyle]]))

(defstyle container
  {:width              "100%"
   :padding-horizontal 20
   :padding-vertical   12})

(defstyle title-row
  {:flex-direction :row
   :align-items    :center})

(defstyle leading-image
  {:flex-shrink  0
   :height       32
   :margin-right 8
   :width        32})

(defstyle title
  {:flex      1
   :min-width 1})

(defstyle button
  {:flex-shrink 0
   :margin-left 20})

(defstyle description
  {:margin-top 8})
