(ns xquo.components.drawer.drawer-label.style
  (:require [react-native.utils :refer [defstyle]]
            [xquo.foundations.colors :as colors]))

(defstyle container-base
  {:align-self         :stretch
   :padding-horizontal 20
   :padding-bottom     12})

(defn text-style [theme blur?]
  {:color (cond
            blur?                 (colors/get-color :color/white-40)
            (= theme :theme/light) (colors/get-color :color/neutral-50)
            :else                 (colors/get-color :color/neutral-40))})
