(ns xquo.calendar.year.style
  (:require [react-native.utils :refer [defstyle style]]
            [xquo.foundations.colors :as colors]))

(defstyle container
  {:width         48
   :height        32
   :border-radius 10})

(defstyle title
  {:width                48
   :height               32
   :text-align           :center
   :text-align-vertical  :center
   :include-font-padding false})

(defn container-style [dark-theme? selected? disabled?]
  (style {:background-color (when selected?
                              (colors/get-color (if dark-theme? :color/neutral-70 :color/neutral-10)))
          :opacity          (if disabled? 0.3 1)}))

(defn title-style [dark-theme? selected?]
  (let [color (cond
                (and selected? dark-theme?)
                :color/white-100

                selected?
                :color/neutral-100

                dark-theme?
                :color/neutral-40

                :else
                :color/neutral-50)]
    (style {:color (colors/get-color color)})))
