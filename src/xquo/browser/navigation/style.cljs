(ns xquo.browser.navigation.style
  (:require [react-native.utils :refer [defstyle]]))

(defstyle container
  {:align-self :stretch})

(defstyle actions
  {:height             56
   :padding-horizontal 20
   :padding-vertical   12
   :flex-direction     :row
   :align-items        :center
   :justify-content    :space-between})

(defstyle trailing
  {:flex-direction :row
   :align-items    :center
   :gap            12})

(defstyle history
  {:flex-direction :row
   :gap            8})
