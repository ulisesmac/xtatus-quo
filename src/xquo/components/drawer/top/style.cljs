(ns xquo.components.drawer.top.style
  (:require [react-native.utils :refer [defstyle]]
            [xquo.foundations.borders :as borders]
            [xquo.foundations.colors :as colors]
            [xquo.foundations.spacing :refer [spacing]]))

(defstyle container-base
  {:align-self :stretch})

(defstyle handle-container
  {:align-self      :stretch
   :height          20
   :align-items     :center
   :justify-content :flex-end
   :padding-bottom  8})

(defstyle content-base
  {:padding-horizontal 20})

(defstyle content-bottom-12
  {:padding-bottom 12})

(defstyle content-bottom-8
  {:padding-bottom 8})

(defstyle content-column
  {:flex-direction :column
   :align-items    :flex-start})

(defstyle subcontent-slot
  {:align-self :stretch
   :height     (spacing 9)
   :margin-top (spacing 2)
   :overflow   :visible})

(defstyle context-row-slot
  {:margin-top (spacing 2)})

(defstyle description-row-slot
  {:margin-top (spacing 1)})

(defstyle description-row-slot-with-context
  {:margin-top (spacing 2)})

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

(defn leading-image [size border-radius]
  (cond-> {:width  size
           :height size}
    border-radius (assoc :border-radius border-radius)))

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

(defn handle-bar-style [theme]
  {:width            32
   :height           4
   :border-radius    100
   :background-color (if (= theme :theme/light)
                       (colors/get-color :color/neutral-100-5)
                       (colors/get-color :color/white-10))})

(defn title-text-style [theme]
  {:color (if (= theme :theme/dark)
            (colors/get-color :color/white-100)
            (colors/get-color :color/neutral-100))})

(defn secondary-text-style [theme blur?]
  {:color (cond
            (and (= theme :theme/dark) blur?)
            (colors/get-color :color/white-40)

            (= theme :theme/dark)
            (colors/get-color :color/neutral-40)

            :else (colors/get-color :color/neutral-50))})

(defn counter-text-style [theme blur?]
  {:color (cond
            (and (= theme :theme/dark) blur?)
            (colors/get-color :color/white-40)

            (= theme :theme/dark)
            (colors/get-color :color/neutral-50)

            :else (colors/get-color :color/neutral-40))})

(defn primary-button-style [theme color]
  (when (= theme :theme/dark)
    {:background-color (colors/get-color color 60)}))

(defn icon-color [theme blur?]
  (cond
    (and (= theme :theme/dark) blur?) (colors/get-color :color/white-40)
    (= theme :theme/dark)             (colors/get-color :color/neutral-40)
    :else                             (colors/get-color :color/neutral-50)))

(defn context-placeholder-style [width]
  {:width            width
   :background-color (colors/get-color :color/danger-50)
   :border-radius    (borders/radius :bounding-area)})

(defn description-segment-style [color]
  (when color
    {:color (colors/get-color color)}))
