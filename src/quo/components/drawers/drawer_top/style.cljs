(ns quo.components.drawers.drawer-top.style
  (:require
    [quo.foundations.colors :as colors]))

(def container
  {:padding-horizontal 20
   :padding-bottom     12
   :flex-direction     :row})

(def body-container
  {:flex 1})

(defn description
  [theme blur?]
  {:color      (if blur?
                 colors/white-opa-40
                 (colors/theme-colors colors/neutral-50 colors/neutral-40 theme))
   :margin-top 2})

(def left-container
  {:margin-right    8
   :justify-content :center})

(def row
  {:flex-direction :row
   :align-items    :center})

(def keycard-icon
  {:margin-left 4})

(def title-container
  {:flex-direction :row
   :align-items    :center})

(def title-icon
  {:margin-left 4})
