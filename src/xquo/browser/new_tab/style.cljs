(ns xquo.browser.new-tab.style
  (:require [react-native.utils :refer [defstyle style]]
            [xquo.foundations.colors :as colors]))

(defstyle container
  {:width           160
   :height          172
   :border-radius   16
   :align-items     :center
   :justify-content :center
   :gap             8})

(defstyle border
  {:position :absolute
   :top      -0.5
   :left     -0.5
   :overflow :visible})

(defstyle title
  {:height               18
   :include-font-padding false
   :text-align-vertical  :center})

(defn background [color]
  (style {:background-color (colors/get-color color 50 10)}))
