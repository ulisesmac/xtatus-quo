(ns xquo.components.divider.divider-line.style
  (:require [reagent-extended-compiler.utils.transforms :refer [defstyle]]
            [xquo.foundations.colors :as colors]))

(defstyle container-base
  {:padding-top        12
   :padding-bottom     8
   :padding-horizontal 20})

(defstyle line-base
  {:height 1})

(defn line-color-style [theme]
  {:background-color (if (= theme :theme/light)
                       (colors/get-color :color/neutral-10)
                       (colors/get-color :color/neutral-90))})
