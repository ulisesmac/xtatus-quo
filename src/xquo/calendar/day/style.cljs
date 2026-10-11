(ns xquo.calendar.day.style
  (:require [react-native.utils :refer [defstyle style]]
            [xquo.foundations.colors :as colors]))

(defstyle container
  {:width         32
   :height        32
   :border-radius 10})

(defstyle title
  {:width                32
   :height               32
   :text-align           :center
   :text-align-vertical  :center
   :include-font-padding false})

(defstyle today
  {:position      :absolute
   :left          14
   :top           27
   :width         4
   :height        2
   :border-radius 8})

(defn container-style [color dark-theme? selected? disabled?]
  (style {:background-color (when selected?
                              (colors/get-color color (if dark-theme? 60 50)))
          :opacity          (if disabled? 0.3 1)}))

(defn title-style [dark-theme? selected?]
  (style {:color (colors/get-color (if (or selected? dark-theme?)
                                     :color/white-100
                                     :color/neutral-100))}))

(defn today-style [color dark-theme? selected?]
  (style {:background-color (if selected?
                              (colors/get-color :color/white-100)
                              (colors/get-color color (if dark-theme? 60 50)))}))
