(ns xquo.card.banner.style
  (:require [react-native.utils :refer [defstyle style]]
            [xquo.foundations.colors :as colors]
            [xquo.foundations.shadows :as shadows]))

(defn container [theme pressed?]
  (style {:width            335
          :max-width        "100%"
          :height           56
          :padding-vertical 8
          :padding-left     12
          :padding-right    95
          :border-radius    12
          :background-color (colors/themed theme :color/white-100 :color/neutral-90)
          :box-shadow       (when-not pressed? (shadows/get-box-shadow :theme/light 2))}))

(defstyle title
  {:height               22
   :include-font-padding false})

(defstyle description
  {:height               18
   :include-font-padding false})

(defstyle image
  {:position :absolute
   :right    0
   :top      -4
   :width    95
   :height   64})
