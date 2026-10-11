(ns xquo.camera.style
  (:require [react-native.utils :refer [defstyle style]]))

(defstyle actions
  {:height          80
   :flex-direction  :row
   :align-items     :center
   :justify-content :center
   :gap             64})

(defn orientation [orientation]
  (style {:transform [{:rotate (if (= orientation :horizontal) "90deg" "0deg")}]}))
