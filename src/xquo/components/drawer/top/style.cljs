(ns xquo.components.drawer.top.style
  (:require [reagent-extended-compiler.utils.transforms :refer [defstyle]]
            [xquo.foundations.borders :as borders]
            [xquo.foundations.colors :as colors]))

(defstyle container-base
  {:align-self :stretch})

(defstyle content-base
  {:padding-horizontal 20
   :padding-bottom     12})

(defstyle content-column
  {:flex-direction :column
   :align-items    :flex-start})

(defstyle content-gap-2
  {:gap 2})

(defstyle content-gap-4
  {:gap 4})

(defstyle title-row
  {:flex-direction :row})

(defstyle title-row-center
  {:align-items :center})

(defstyle title-row-baseline
  {:align-items :baseline})

(defstyle title-row-gap-12
  {:gap 12})

(defstyle title-row-gap-20
  {:gap 20})

(defstyle title-slot
  {:flex      1
   :min-width 1})

(defstyle title-inline-row
  {:flex-direction :row
   :align-items    :center
   :gap            4})

(defstyle leading-row
  {:flex-direction :row
   :align-items    :center
   :gap            8})

(defstyle leading-column
  {:flex            1
   :min-width       1
   :flex-direction  :column
   :align-items     :flex-start
   :justify-content :center})

(defstyle leading-placeholder-base
  {:width  32
   :height 32})

(defstyle context-row
  {:flex-direction :row
   :align-items    :center
   :gap            4})

(defstyle context-placeholder-base
  {:height 24})

(defstyle description-row
  {:flex-direction :row
   :align-items    :center
   :gap            4})

(defstyle description-icon-scale
  {:transform [{:scale 0.8}]})

(defstyle counter-text
  {:text-align :right})

(defn container-color-style [theme background]
  (cond
    (= background :blur) nil
    (= theme :theme/dark) {:background-color (colors/get-color :color/neutral 95 70)}
    :else                 {:background-color (colors/get-color :color/white 70)}))

(defn title-text-style [theme]
  {:color (if (= theme :theme/dark)
            (colors/get-color :color/white 100)
            (colors/get-color :color/neutral 100))})

(defn secondary-text-style [theme background]
  {:color (cond
            (and (= theme :theme/dark) (= background :blur))
            (colors/get-color :color/white 40)

            (= theme :theme/dark)
            (colors/get-color :color/neutral 40)

            :else
            (colors/get-color :color/neutral 50))})

(defn counter-text-style [theme background]
  {:color (cond
            (and (= theme :theme/dark) (= background :blur))
            (colors/get-color :color/white 40)

            (= theme :theme/dark)
            (colors/get-color :color/neutral 40)

            :else
            (colors/get-color :color/neutral 40))})

(defn primary-button-style [theme color]
  (when (= theme :theme/dark)
    {:background-color (colors/get-color color 60)}))

(defn icon-color [theme background]
  (cond
    (and (= theme :theme/dark) (= background :blur)) (colors/get-color :color/white 40)
    (= theme :theme/dark)                            (colors/get-color :color/neutral 40)
    :else                                            (colors/get-color :color/neutral 50)))

(defn leading-placeholder-style []
  {:background-color (colors/get-color :color/danger 50)
   :border-radius    (:border/bounding-area borders/border-radius-values)})

(defn context-placeholder-style [width]
  {:width            width
   :background-color (colors/get-color :color/danger 50)
   :border-radius    (:border/bounding-area borders/border-radius-values)})

(defn description-segment-style [color]
  (when color
    {:color (colors/get-color color)}))
