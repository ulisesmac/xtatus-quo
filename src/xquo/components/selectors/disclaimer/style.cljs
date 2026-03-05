(ns xquo.components.selectors.disclaimer.style
  (:require [reagent-extended-compiler.utils.transforms :refer [defstyle]]
            [xquo.foundations.borders :as borders]
            [xquo.foundations.colors :as colors]))

(defstyle container-base
  {:padding         12
   :border-width    1
   :border-radius   (:border/sizes-40-56 borders/border-radius-values)
   :flex-direction  :row
   :align-items     :flex-start})

(defstyle selector-style
  {:margin-right 8})

(defstyle text-wrapper
  {:flex 1})

(defstyle icon-style
  {:margin-left 8})

(defn container-color-style [theme background]
  (cond
    (and (= theme :theme/dark)
         (= background :blur))
    {:background-color (colors/get-color :color/white-5)
     :border-color     (colors/get-color :color/white-10)}

    (= theme :theme/dark)
    {:background-color (colors/get-color :color/neutral-80-40)
     :border-color     (colors/get-color :color/neutral-80)}

    :else
    {:background-color (colors/get-color :color/neutral-5)
     :border-color     (colors/get-color :color/neutral-20)}))

(defn text-color [theme]
  (if (= theme :theme/dark)
    (colors/get-color :color/white-100)
    (colors/get-color :color/neutral-100)))

(defn icon-color [theme background]
  (cond
    (and (= theme :theme/dark)
         (= background :blur))
    (colors/get-color :color/white-70)

    (= theme :theme/dark)
    (colors/get-color :color/neutral-40)

    :else
    (colors/get-color :color/neutral-50)))
