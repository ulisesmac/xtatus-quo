(ns xquo.input.profile.style
  (:require [react-native.utils :refer [defstyle style]]
            [xquo.foundations.colors :as colors]))

(defn container [disabled?]
  (style {:align-self         :stretch
          :min-width          0
          :flex-direction     :row
          :align-items        :center
          :padding-horizontal 12
          :padding-vertical   8
          :gap                20
          :opacity            (if disabled? 0.3 1)}))

(defstyle edit
  {:height         26
   :width          20
   :padding-top    5
   :padding-bottom 1})

(defstyle counter
  {:height         26
   :padding-top    6
   :padding-bottom 2})

(defn secondary [{:keys [theme]} blur?]
  (if blur?
    (colors/themed theme :color/neutral-80-40 :color/white-40)
    (colors/themed theme :color/neutral-40 :color/neutral-50)))

(defn placeholder [{:keys [theme dark-theme?] :as theme-color} blur? focused?]
  (cond
    (and focused? blur?)   (colors/themed theme :color/neutral-80-20 :color/white-20)
    focused?               (colors/themed theme :color/neutral-30 :color/neutral-60)
    (and dark-theme? blur?) (colors/get-color :color/white-30)
    :else                  (secondary theme-color blur?)))

(defn cursor [{:keys [theme color dark-theme?]} blur?]
  (if blur?
    (colors/themed theme :color/neutral-100 :color/white-100)
    (colors/get-color color (if dark-theme? 60 50))))

(defn selection [{:keys [color dark-theme?]}]
  (colors/get-color color (if dark-theme? 60 50) 20))

(defn counter-text [theme blur?]
  (style {:height               18
          :line-height          18
          :include-font-padding false
          :text-align           :right
          :color                (secondary theme blur?)}))
