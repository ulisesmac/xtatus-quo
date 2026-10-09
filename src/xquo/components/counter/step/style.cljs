(ns xquo.components.counter.step.style
  (:require [react-native.utils :refer [defstyle style]]
            [xquo.foundations.colors :as colors]))

(def ^:private widths
  {1 20
   2 20
   3 28})

(defstyle container
  {:height        20
   :border-radius 6
   :overflow      :hidden})

(defstyle surface
  {:position      :absolute
   :top           1
   :bottom        1
   :border-radius 6})

(defn container-width-style [characters]
  (style {:width     (widths characters)
          :min-width (if (< characters 3) 20 28)}))

(defn surface-style [color type dark-theme? blur? characters]
  (let [horizontal-inset (if (= characters 1) 1 0)]
    (style {:left             horizontal-inset
            :right            horizontal-inset
            :background-color (case type
                                :complete (colors/get-color color (if dark-theme? 60 50))
                                :active (colors/get-color color 50 10)
                                nil)
            :border-width     (when (= type :neutral) 1)
            :border-color     (when (= type :neutral)
                                (cond
                                  (and dark-theme? blur?) (colors/get-color :color/white-10)
                                  dark-theme?            (colors/get-color :color/neutral-80)
                                  blur?                  (colors/get-color :color/neutral-80-5)
                                  :else                  (colors/get-color :color/neutral-20)))})))

(defn value-style [type dark-theme? characters]
  (style {:margin-top           (if (= characters 1) 2 1)
          :margin-left          (case characters 1 2 2 0 3)
          :margin-right         (if (= characters 2) 0 2)
          :height               (if (= characters 1) 16 18)
          :text-align           :center
          :text-align-vertical  :center
          :include-font-padding false
          :color                (colors/get-color (if (or (= type :complete) dark-theme?)
                                                    :color/white-100
                                                    :color/neutral-100))}))
