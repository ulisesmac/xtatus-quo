(ns quo.components.banners.banner.style
  (:require
    [quo.foundations.colors :as colors]))

(defn container
  [theme]
  {:height           40
   :background-color (colors/resolve-color :blue theme 20)
   :flex-direction   :row
   :align-items      :center
   :padding-right    22
   :padding-left     20
   :padding-vertical 10})

(def counter
  {:flex            1
   :justify-content :center
   :align-items     :center})

(def icon
  {:flex         1
   :margin-right 10})

(defn text
  [hide-pin?]
  {:flex         (if hide-pin? 16 15)
   :margin-right 10})
