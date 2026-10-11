(ns xquo.components.empty-state.style
  (:require [react-native.utils :refer [defstyle style]]
            [xquo.foundations.colors :as colors]))

(defstyle root-base
  {:width           "100%"
   :align-items     :center
   :justify-content :center})

(defstyle content-base
  {:width       "100%"
   :padding-top 12
   :align-items :center
   :gap         8})

(defstyle top-base
  {:width       "100%"
   :align-items :center})

(defstyle illustration
  {:width  80
   :height 80})

(defn illustration-tint-style [image-tint]
  (when image-tint
    (style {:tint-color (colors/get-color image-tint)})))

(defstyle text-combination
  {:width              "100%"
   :align-items        :center
   :padding-horizontal 20
   :padding-bottom     12
   :gap                2})

(defn text-combination-top-spacing [image-spacing]
  (style {:padding-top image-spacing}))

(defstyle title-text
  {:width                "100%"
   :text-align           :center
   :include-font-padding false})

(defstyle description-text
  {:width                "100%"
   :text-align           :center
   :include-font-padding false})

(defstyle actions-base
  {:width              "100%"
   :align-items        :center
   :justify-content    :center
   :padding-horizontal 20
   :padding-bottom     12})

(defstyle actions-stack
  {:max-width "100%"
   :gap       12})
