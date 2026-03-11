(ns xquo.components.selectors.options.style
  (:require [reagent-extended-compiler.utils.transforms :refer [defstyle style]]
            [xquo.foundations.borders :as borders]
            [xquo.foundations.colors :as colors]
            [xquo.foundations.spacing :as spacing]))

(defstyle option-base
  {:border-width   1
   :border-radius  (:border/card-section borders/border-radius-values)
   :overflow       :hidden
   :flex-direction :column})

(defn container-style [horizontal?]
  (style (cond-> {:align-items :stretch}
           horizontal? (assoc :flex-direction :row
                              :column-gap     (spacing/spacing-values 4))
           (not horizontal?) (assoc :row-gap (spacing/spacing-values 4)))))

(defn option-border-style [theme selected? color]
  {:border-color (cond
                   (and selected? (= theme :theme/dark)) (colors/get-color color 60)
                   selected?                            (colors/get-color color 50)
                   (= theme :theme/dark)               (colors/get-color :color/neutral-80)
                   :else                               (colors/get-color :color/neutral-20))})
