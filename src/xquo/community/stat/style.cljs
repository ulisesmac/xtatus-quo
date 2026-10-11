(ns xquo.community.stat.style
  (:require [react-native.utils :refer [defstyle]]))

(defstyle container
  {:height         22
   :flex-direction :row
   :align-items    :center
   :gap            2})

(defstyle value
  {:include-font-padding false})
