(ns xquo.banner.alert.style
  (:require [react-native.utils :refer [defstyle style]]
            [xquo.foundations.colors :as colors]))

(defstyle container
  {:width              "100%"
   :min-height         48
   :flex-direction     :row
   :align-items        :flex-start
   :padding-horizontal 20
   :gap                20})

(defstyle label
  {:flex            1
   :min-width       0
   :justify-content :center})

(defstyle label-content
  {:flex-direction :row
   :align-items    :flex-start
   :gap            4})

(defstyle icon
  {:margin-top 1})

(defn container-style [dark-theme? button]
  (style {:padding-vertical (if button 12 15)
          :background-image (str "linear-gradient(180deg, "
                                 (colors/get-color :color/danger 50 (if dark-theme? 10 5))
                                 " 0%, "
                                 (colors/get-color :color/danger 50 0)
                                 " 100%)")}))

(defn label-style [button]
  (style {:min-height (if button 24 18)}))

(defn title-style [foreground]
  (style {:flex                 1
          :min-width            0
          :include-font-padding false
          :color                foreground}))
