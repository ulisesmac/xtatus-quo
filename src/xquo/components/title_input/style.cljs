(ns xquo.components.title-input.style
  (:require [react-native.utils :refer [defstyle]]
            [xquo.foundations.colors :as colors]
            [xquo.foundations.typography :as typography]))

(defstyle container
  {:width              "100%"
   :padding-horizontal 20
   :padding-vertical   12
   :flex-direction     :row
   :align-items        :flex-end
   :justify-content    :center})

(defstyle container-disabled
  {:opacity 0.3})

(defstyle text-input
  {:font-family          (:semibold typography/font-families)
   :font-size            (:font-size typography/heading-1-27)
   :line-height          (:line-height typography/heading-1-27)
   :letter-spacing       (:letter-spacing typography/heading-1-27)
   :flex                 1
   :min-width            1
   :height               32
   :padding              0
   :padding-horizontal   0
   :padding-vertical     0
   :padding-top          0
   :padding-right        0
   :padding-bottom       0
   :padding-left         0
   :margin               0
   :include-font-padding false})

(defstyle edit-icon-container
  {:padding-top    9
   :padding-bottom 3
   :align-items    :flex-end})

(defstyle counter-container
  {:padding-top    9
   :padding-bottom 2
   :align-items    :flex-end})

(defn text-input-color-style [dark-theme?]
  {:color (colors/get-color (if dark-theme? :color/white :color/neutral-100))})

(defn placeholder-color [dark-theme? blur?]
  (cond
    (and dark-theme? blur?) (colors/get-color :color/white-30)
    dark-theme?             (colors/get-color :color/neutral-50)
    blur?                   (colors/get-color :color/neutral-80-40)
    :else                   (colors/get-color :color/neutral-40)))

(defn edit-icon-color [dark-theme? blur?]
  (cond
    (and dark-theme? blur?) (colors/get-color :color/white-40)
    :else                   (placeholder-color dark-theme? blur?)))

(defn counter-text-style [dark-theme? blur?]
  {:color (edit-icon-color dark-theme? blur?)})
