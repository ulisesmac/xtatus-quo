(ns xquo.components.page-top.style
  (:require [reagent-extended-compiler.utils.transforms :refer [defstyle]]))

(defstyle container
  {:width              "100%"
   :padding-horizontal 20
   :padding-vertical   12})

(defstyle description
  {:margin-top 8})
