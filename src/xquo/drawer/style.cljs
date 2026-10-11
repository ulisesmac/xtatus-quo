(ns xquo.drawer.style
  (:require [react-native.utils :refer [defstyle style]]
            [xquo.foundations.colors :as colors]))

(defstyle content
  {:align-self  :stretch
   :padding-top 20})

(defn backdrop [bottom]
  (style {:position                :absolute
          :top                     0
          :right                   0
          :bottom                  (- bottom)
          :left                    0
          :border-top-left-radius  20
          :border-top-right-radius 20
          :background-color        (colors/get-color :color/white-5)}))

(defn secondary [theme blur?]
  (colors/themed theme
                 :color/neutral-50
                 (if blur? :color/white-40 :color/neutral-40)))
