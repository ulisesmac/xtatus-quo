(ns xquo.components.selectors.options.style
  (:require [react-native.utils :refer [defstyle]]
            [xquo.foundations.borders :as borders]
            [xquo.foundations.colors :as colors]
            [xquo.foundations.spacing :refer [spacing]]))

(defstyle option-base
  {:position       :relative
   :border-width   1
   :border-radius  (borders/radius :card-section)
   :overflow       :hidden
   :flex-direction :column})

(defstyle option-disabled-overlay
  {:position         :absolute
   :top              0
   :right            0
   :bottom           0
   :left             0
   :z-index          2
   :background-color (colors/get-color :color/white-70)})

(defstyle root-base
  {:align-self :stretch})

(defstyle selector-slot
  {:position   :absolute
   :top        12
   :right      12
   :z-index    1})

(defn container-style [horizontal?]
  (if horizontal?
    {:align-items    :stretch
     :flex-direction :row
     :column-gap     (spacing 4)}
    {:align-items :stretch
     :row-gap     (spacing 4)}))

(defn option-border-style [dark-theme? selected? color]
  {:border-color (cond
                   (and selected? dark-theme?) (colors/get-color color 60)
                   selected?                   (colors/get-color color 50)
                   dark-theme?                 (colors/get-color :color/neutral-80)
                   :else                       (colors/get-color :color/neutral-20))})
