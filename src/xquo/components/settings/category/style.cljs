(ns xquo.components.settings.category.style
  (:require [reagent-extended-compiler.utils.transforms :refer [defstyle]]
            [xquo.foundations.borders :as borders]
            [xquo.foundations.colors :as colors]))

(defstyle container-base
  {:padding-left   20
   :padding-right  20
   :padding-top    12
   :padding-bottom 8
   :gap            12})

(defstyle surface-base
  {:align-self    :stretch
   :overflow      :hidden
   :border-radius (:border/card-section borders/border-radius-values)})

(defstyle divider-base
  {:height     1
   :align-self :stretch})

(defn surface-color-style [theme blur?]
  (cond
    (and (= theme :theme/dark)
         blur?)
    {:background-color (colors/get-color :color/white-5)}

    (= theme :theme/dark)
    {:background-color (colors/get-color :color/neutral-95)
     :border-width     1
     :border-color     (colors/get-color :color/neutral-80)}

    :else
    {:background-color (colors/get-color :color/white-100)
     :border-width     1
     :border-color     (colors/get-color :color/neutral-10)}))

(defn divider-color-style [theme blur?]
  {:background-color (cond
                       (and (= theme :theme/dark)
                            blur?)
                       (colors/get-color :color/white-5)

                       (= theme :theme/dark)
                       (colors/get-color :color/neutral-80)

                       :else
                       (colors/get-color :color/neutral-10))})
