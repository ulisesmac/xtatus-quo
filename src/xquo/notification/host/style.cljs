(ns xquo.notification.host.style
  (:require [react-native.utils :refer [defstyle]]))

(defstyle root
  {:flex 1})

(defstyle stack
  {:position :absolute
   :top      0
   :left     0
   :right    0
   :bottom   0})

(defstyle item
  {:position :absolute
   :top      0
   :left     0
   :right    0})
