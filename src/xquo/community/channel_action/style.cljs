(ns xquo.community.channel-action.style
  (:require [react-native.utils :refer [defstyle style]]
            [xquo.foundations.colors :as colors]))

(defstyle container
  {:width          104
   :height         102
   :border-radius  16
   :padding-top    12
   :padding-left   12
   :padding-right  12
   :padding-bottom 10
   :gap            16})

(defn state-style [color pressed? disabled?]
  (style {:background-color (colors/get-color color 50 (if pressed? 20 10))
          :opacity          (if disabled? 0.3 1)}))

(defstyle top
  {:height          20
   :flex-direction  :row
   :align-items     :flex-start
   :justify-content :space-between})

(defstyle title
  {:height               44
   :include-font-padding false})
