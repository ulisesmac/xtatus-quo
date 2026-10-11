(ns xquo.drawer.divider.view
  (:require [react-native.utils :as rn.utils :refer [defstyle]]
            [xquo.divider.line.view :as line]))

(defstyle ^:private container
  {:padding-top 8})

(defn divider [props]
  [line/divider-line (assoc props :style (rn.utils/add-styles container (:style props)))])
