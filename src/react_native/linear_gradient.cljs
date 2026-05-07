(ns react-native.linear-gradient
  (:require [react-native.corex :as rn]))

(defn linear-gradient
  [props & children]
  (into [rn/view props] children))
