(ns xquo.divider.style
  (:require [react-native.utils :refer [defstyle style]]
            [xquo.foundations.colors :as colors]))

(defstyle title
  {:height               18
   :include-font-padding false
   :text-align-vertical  :center})

(defn gradient [color level]
  (style {:background-image (str "linear-gradient(180deg, "
                                 (colors/get-color color level 5)
                                 " 0%, "
                                 (colors/get-color color level 0)
                                 " 100%)")}))

(defn title-color [color]
  (style {:color color}))
