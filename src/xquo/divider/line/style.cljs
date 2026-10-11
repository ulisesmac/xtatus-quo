(ns xquo.divider.line.style
  (:require [react-native.utils :refer [defstyle]]
            [xquo.foundations.colors :as colors]))

(defstyle container-base
  {:width          "100%"
   :align-self     :stretch
   :padding-top    12
   :padding-bottom 8})

(defstyle line-base
  {:margin-top    -0.5
   :margin-bottom -0.5})

(defn line-color [theme blur? dashed?]
  (cond
    (and (= theme :theme/dark) blur? dashed?) (colors/get-color :color/white-10)
    (and (= theme :theme/dark) blur?)         (colors/get-color :color/white-5)
    (and blur? dashed?)                      (colors/get-color :color/neutral-80-10)
    blur?                                   (colors/get-color :color/neutral-80-5)
    (and (= theme :theme/dark) dashed?)       (colors/get-color :color/neutral-80)
    (= theme :theme/dark)                    (colors/get-color :color/neutral-90)
    dashed?                                 (colors/get-color :color/neutral-20)
    :else                                   (colors/get-color :color/neutral-10)))
