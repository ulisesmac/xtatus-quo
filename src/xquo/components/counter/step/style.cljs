(ns xquo.components.counter.step.style
  (:require [react-native.utils :refer [defstyle style]]
            [xquo.foundations.borders :as borders]
            [xquo.foundations.colors :as colors]))

(defstyle container-base
  {:height          20
   :border-radius   (borders/radius 16)
   :overflow        :hidden
   :justify-content :center
   :align-items     :center})

(defstyle surface-base
  {:position      :absolute
   :border-radius (borders/radius 16)})

(defstyle value-slot-base
  {:position        :absolute
   :justify-content :center
   :align-items     :center})

(defstyle container-width-20
  {:width 20})

(defstyle container-width-28
  {:width 28})

(defstyle surface-inset-1-char
  {:top    2
   :right  2
   :bottom 2
   :left   2})

(defstyle surface-inset-multi-char
  {:top    1
   :right  0
   :bottom 1
   :left   0})

(defstyle value-slot-inset-1-char
  {:top    2
   :right  2
   :bottom 2
   :left   2})

(defstyle value-slot-inset-2-char
  {:top    1
   :right  0
   :bottom 1
   :left   0})

(defstyle value-slot-inset-3-char
  {:top    1
   :right  2
   :bottom 1
   :left   3})

(defn- dark-theme? [theme]
  (= theme :theme/dark))

(defn- color-token
  ([color level]
   (keyword (namespace color)
            (str (name color) "-" level)))
  ([color level opacity]
   (keyword (namespace color)
            (str (name color) "-" level "-" opacity))))

(defn container-width-style [characters]
  (if (= characters 3)
    container-width-28
    container-width-20))

(defn surface-inset-style [characters]
  (if (= characters 1)
    surface-inset-1-char
    surface-inset-multi-char))

(defn- neutral-border-color [theme background]
  (cond
    (and (= background :blur) (dark-theme? theme)) (colors/get-color :color/white-10)
    (= background :blur)                           (colors/get-color :color/neutral-80-5)
    (dark-theme? theme)                            (colors/get-color :color/neutral-80)
    :else                                          (colors/get-color :color/neutral-20)))

(defn surface-color-style [theme type background color]
  (cond
    (= type :neutral)
    (style {:border-width 1
            :border-color (neutral-border-color theme background)})

    (= type :complete)
    (style {:background-color (if (dark-theme? theme)
                                (colors/get-color (color-token color 60))
                                (colors/get-color (color-token color 50)))})

    (= type :active)
    (style {:background-color (colors/get-color (color-token color 50 10))})

    :else
    (style {:border-width 1
            :border-color (neutral-border-color theme background)})))

(defn value-slot-inset-style [characters]
  (case characters
    1 value-slot-inset-1-char
    2 value-slot-inset-2-char
    3 value-slot-inset-3-char
    value-slot-inset-1-char))

(defn value-color [theme type]
  (cond
    (= type :complete) (colors/get-color :color/white-100)
    (dark-theme? theme) (colors/get-color :color/white-100)
    :else (colors/get-color :color/neutral-100)))
