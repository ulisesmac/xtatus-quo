(ns xquo.components.drawer.bottom-actions.style
  (:require [reagent-extended-compiler.utils.transforms :refer [defstyle]]
            [xquo.foundations.colors :as colors]))

(defstyle container-base
  {:width "100%"})

(defstyle top-description-row
  {:flex-direction :row
   :align-items    :center
   :justify-content :center
   :gap            5
   :padding-top    12
   :padding-bottom 4})

(defstyle top-error-row
  {:padding-left   20
   :padding-right  20
   :padding-top    15
   :padding-bottom 7
   :align-items    :center
   :justify-content :center})

(defstyle top-error-content
  {:flex-direction :row
   :align-items    :flex-start
   :justify-content :center
   :gap            4})

(defstyle top-error-icon
  {:padding-top    1
   :padding-bottom 1})

(defstyle context-tag-placeholder
  {:width            24
   :height           24
   :background-color (colors/get-color :color/danger 50)})

(defstyle actions-row
  {:flex-direction     :row
   :align-items        :center
   :gap                12
   :padding-horizontal 20
   :padding-vertical   12})

(defstyle action-slot
  {:flex 1})

(defstyle bottom-description-row
  {:padding-left   40
   :padding-right  40
   :padding-bottom 12})

(defstyle bottom-description-text
  {:text-align :center})

(defn description-text-style [theme background scroll? position status]
  {:color (cond
            (= status :error)
            (colors/get-color :color/danger
                              (if (= theme :theme/dark) 60 50))

            (= theme :theme/light)
            (if (and scroll? (= position :bottom))
              (colors/get-color :color/neutral 80 70)
              (colors/get-color :color/neutral 50))

            (and scroll? (= position :top) (= background :blur))
            (colors/get-color :color/white 70)

            (and scroll? (= position :bottom))
            (colors/get-color :color/white 70)

            (= background :blur)
            (colors/get-color :color/white 40)

            :else
            (colors/get-color :color/neutral 40))})
