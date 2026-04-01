(ns xquo.components.empty-state.style
  (:require [reagent-extended-compiler.utils.transforms :refer [defstyle]]
            [xquo.foundations.colors :as colors]))

(defstyle root-base
  {:width           "100%"
   :align-items     :center
   :justify-content :center})

(defstyle content-base
  {:width       "100%"
   :padding-top 12
   :align-items :center
              :gap 8})

(defstyle top-base
  {:width       "100%"
   :align-items :center})

(defstyle illustration
  {:width  80
   :height 80})

(defn illustration-tint-style [image-tint]
  (when image-tint
    {:tint-color (colors/get-color image-tint)}))

(defstyle illustration-placeholder
  {:width            80
   :height           80
   :background-color (colors/get-color :color/danger-50)})

(defstyle text-combination
  {:width              "100%"
   :align-items        :center
   :padding-horizontal 20
   :padding-vertical   12
   :gap                2})

(defstyle title-slot
  {:width "100%"})

(defstyle title-text
  {:text-align :center})

(defstyle description-text
  {:width      "100%"
   :text-align :center})

(defstyle actions-base
  {:width              "100%"
   :align-items        :center
   :justify-content    :center
   :padding-horizontal 20
   :padding-bottom     12})

(defstyle secondary-action-spacing
  {:margin-top 12})
