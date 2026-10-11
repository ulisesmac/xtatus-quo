(ns xquo.browser.input.style
  (:require [react-native.utils :refer [defstyle style]]
            [xquo.foundations.colors :as colors]))

(defstyle container
  {:height             32
   :padding-horizontal 20})

(defstyle content
  {:height         32
   :flex-direction :row
   :align-items    :center})

(defstyle display
  {:flex-direction :row
   :align-items    :center
   :gap            0
   :max-width      "100%"})

(defstyle address
  {:flex-direction :row
   :flex-shrink    1
   :align-items    :center
   :gap            4})

(defstyle domain
  {:flex-shrink          1
   :include-font-padding false
   :height               22
   :text-align-vertical  :center})

(defstyle lock
  {:margin-left 2})

(defstyle input
  {:flex                 1
   :height               32
   :padding              0
   :padding-vertical     5
   :include-font-padding false
   :text-align-vertical  :center})

(defstyle clear
  {:height          32
   :width           12
   :align-items     :center
   :justify-content :center})

(defn text-color [theme]
  (style {:color (colors/themed theme :color/neutral-100 :color/white-100)}))

(defn placeholder-color [theme blur?]
  (if blur?
    (colors/themed theme :color/neutral-80-20 :color/white-20)
    (colors/themed theme :color/neutral-30 :color/neutral-70)))

(defn secondary-color [theme blur?]
  (if blur?
    (colors/themed theme :color/neutral-80-50 :color/white-40)
    (colors/themed theme :color/neutral-50 :color/neutral-40)))

(defn clear-color [theme blur?]
  (if blur?
    (colors/themed theme :color/neutral-80-30 :color/white-10)
    (colors/themed theme :color/neutral-40 :color/neutral-70)))
