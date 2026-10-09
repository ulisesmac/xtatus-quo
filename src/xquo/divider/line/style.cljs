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
    (and (= theme :theme/dark) blur?) (colors/get-color (if dashed? :color/white-10 :color/white-5))
    blur?                            (colors/get-color (if dashed? :color/neutral-80-10 :color/neutral-80-5))
    (= theme :theme/dark)            (colors/get-color (if dashed? :color/neutral-80 :color/neutral-90))
    :else                            (colors/get-color (if dashed? :color/neutral-20 :color/neutral-10))))
