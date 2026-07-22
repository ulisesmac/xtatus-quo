(ns xquo.components.page-top.style
  (:require [react-native.utils :refer [defstyle]]))

(defstyle container
  {:width              "100%"
   :padding-horizontal 20
   :padding-vertical   12})

(defstyle title-row
  {:flex-direction :row
   :align-items    :center
   :gap            8})

(defstyle leading-image
  {:width       32
   :height      32
   :flex-shrink 0})

(defstyle title
  {:flex      1
   :min-width 1})

(defstyle description
  {:margin-top 8})
