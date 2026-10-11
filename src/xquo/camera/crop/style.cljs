(ns xquo.camera.crop.style
  (:require [react-native.utils :refer [defstyle]]))

(defstyle container
  {:height          80
   :align-items     :center
   :justify-content :center})

(defstyle rotate-slot
  {:position :absolute
   :top      24
   :right    64})
