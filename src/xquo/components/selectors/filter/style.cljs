(ns xquo.components.selectors.filter.style
  (:require [reagent-extended-compiler.utils.transforms :refer [defstyle]]
            [xquo.foundations.borders :as borders]
            [xquo.foundations.colors :as colors]))

(defstyle container-base
  {:border-width    1
   :justify-content :center
   :align-items     :center})

(defstyle container-size-32-icon
  {:width         32
   :height        32
   :padding       6
   :border-radius (:border/size-32 borders/border-radius-values)})

(defstyle container-size-24-icon
  {:width         24
   :height        24
   :border-radius (:border/size-24 borders/border-radius-values)})

(defstyle container-size-32-text
  {:height         32
   :padding-left   6
   :padding-right  10
   :padding-top    5
   :padding-bottom 5
   :gap            4
   :border-radius  (:border/size-32 borders/border-radius-values)
   :flex-direction :row})

(defstyle container-size-24-text
  {:height         24
   :padding-left   6
   :padding-right  8
   :padding-top    3
   :padding-bottom 3
   :gap            4
   :border-radius  (:border/size-24 borders/border-radius-values)
   :flex-direction :row})

(defn container-size-style [size text?]
  (cond
    (and text? (= size 24)) container-size-24-text
    text?                   container-size-32-text
    (= size 24)             container-size-24-icon
    :else                   container-size-32-icon))

(defn surface-color-style [theme background selected?]
  (cond
    (and (= theme :theme/dark)
         (= background :blur))
    {:background-color (if selected?
                         (colors/get-color :color/blue 60)
                         :transparent)
     :border-color     (colors/get-color :color/white-10)}

    (= theme :theme/dark)
    {:background-color (if selected?
                         (colors/get-color :color/blue 60)
                         :transparent)
     :border-color     (if selected?
                         (colors/get-color :color/white-10)
                         (colors/get-color :color/neutral-80))}

    (= background :blur)
    {:background-color (if selected?
                         (colors/get-color :color/blue 50)
                         :transparent)
     :border-color     (if selected?
                         (colors/get-color :color/neutral-80-20)
                         (colors/get-color :color/neutral-80-10))}

    selected?
    {:background-color (colors/get-color :color/blue 50)
     :border-color     (colors/get-color :color/neutral-80-10)}

    :else
    {:background-color :transparent
     :border-color     (colors/get-color :color/neutral-20)}))

(defn text-color [theme selected?]
  (if (or selected? (= theme :theme/dark))
    (colors/get-color :color/white-100)
    (colors/get-color :color/neutral-100)))

(defn icon-color [theme size selected?]
  (cond
    selected? (colors/get-color :color/white-100)
    (= theme :theme/dark) (colors/get-color :color/white-100)
    (= size 24) (colors/get-color :color/neutral-100)
    :else (colors/get-color :color/neutral-50)))

(defn label-font [size]
  (if (= size 24)
    :font/medium-13
    :font/medium-15))
