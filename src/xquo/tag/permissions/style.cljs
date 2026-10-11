(ns xquo.tag.permissions.style
  (:require [react-native.utils :refer [defstyle style]]
            [xquo.foundations.colors :as colors]))

(def token-size
  {24 20
   32 28})

(def icon-size
  {24 16
   32 20})

(def token-step
  {24 16
   32 20})

(def label-font
  {24 :font/medium-11
   32 :font/medium-13})

(def ^:private corner-radius
  {24 8
   32 10})

(def ^:private label-height
  {24 16
   32 18})

(defstyle container
  {:align-self     :flex-start
   :flex-direction :row
   :align-items    :center
   :padding-left   4
   :padding-right  2
   :padding-top    2
   :padding-bottom 2
   :gap            4})

(defstyle tokens
  {:flex-direction :row})

(defn background-color [dark-theme? blur? background-color]
  (colors/get-color (cond
                      background-color        background-color
                      (and dark-theme? blur?) :color/white-10
                      dark-theme?             :color/neutral-90
                      blur?                   :color/neutral-80-10
                      :else                   :color/neutral-10)))

(defn foreground [dark-theme? blur?]
  (colors/get-color (cond
                      (and dark-theme? blur?) :color/white-70
                      dark-theme?             :color/neutral-40
                      blur?                   :color/neutral-80-70
                      :else                   :color/neutral-50)))

(defn count-color [size dark-theme? blur?]
  (if (and (= size 24) dark-theme? blur?)
    (colors/get-color :color/white-80)
    (foreground dark-theme? blur?)))

(defn count-background [dark-theme? blur? background-color]
  (colors/get-color (cond
                      (and dark-theme? blur?)
                      :color/white-10

                      (and dark-theme? (= background-color :color/neutral-80))
                      :color/neutral-70

                      dark-theme?
                      :color/neutral-80

                      blur?
                      :color/neutral-80-10

                      :else
                      :color/neutral-20)))

(defn container-style [size background]
  (style {:height           size
          :border-radius    (corner-radius size)
          :background-color background}))

(defn slot [size last?]
  (style {:width  (if last? (token-size size) (token-step size))
          :height (token-size size)}))

(defn count-slot [size background]
  (let [diameter (token-size size)]
    (style {:width            diameter
            :height           diameter
            :border-radius    (/ diameter 2)
            :align-items      :center
            :justify-content  :center
            :background-color background})))

(defn count-text [size color]
  (style {:width                (- (token-size size) 2)
          :height               20
          :align-self           :flex-start
          :include-font-padding false
          :text-align           :center
          :text-align-vertical  :center
          :color                color}))

(defn separator [size color]
  (style {:height               (label-height size)
          :include-font-padding false
          :text-align-vertical  :center
          :color                color}))
