(ns xquo.components.selectors.options.style
  (:require [reagent-extended-compiler.utils.transforms :refer [defstyle]]
            [xquo.foundations.borders :as borders]
            [xquo.foundations.colors :as colors]
            [xquo.foundations.spacing :as spacing]))

(defstyle option-base
  {:position       :relative
   :border-width   1
   :border-radius  (:border/card-section borders/border-radius-values)
   :overflow       :hidden
   :flex-direction :column})

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
     :column-gap     (spacing/spacing-values 4)}
    {:align-items :stretch
     :row-gap     (spacing/spacing-values 4)}))

(defn option-border-style [dark-theme? selected? color]
  {:border-color (cond
                   (and selected? dark-theme?) (colors/get-color color 60)
                   selected?                   (colors/get-color color 50)
                   dark-theme?                 (colors/get-color :color/neutral-80)
                   :else                       (colors/get-color :color/neutral-20))})
