(ns xquo.drawer.action.style
  (:require [react-native.utils :refer [defstyle style]]
            [xquo.foundations.colors :as colors]))

(defn container [{:keys [dark-theme?]} color blur? selected? pressed? disabled? description? input?]
  (style {:border-radius      12
          :margin-horizontal  8
          :padding-horizontal 12
          :padding-top        (if description? 8 13)
          :padding-bottom     (cond
                                input?       12
                                description? 8
                                :else        13)
          :opacity            (if disabled? 0.3 1)
          :background-color   (when (or selected? pressed?)
                                (if (and blur? dark-theme?)
                                  (colors/get-color :color/white-5)
                                  (colors/get-color color 50 5)))}))

(defstyle row
  {:flex-direction :row
   :align-items    :flex-start
   :gap            12})

(defstyle text
  {:flex      1
   :min-width 0})

(defstyle icon
  {:margin-top 1})

(defstyle input
  {:margin-top 8})

(defstyle image
  {:width  32
   :height 32})

(defn foreground [{:keys [theme dark-theme?]} color blur? danger?]
  (if danger?
    (colors/get-color color (if (and dark-theme? (not blur?)) 60 50))
    (colors/themed theme :color/neutral-100 :color/white-100)))

(defn icon-color [{:keys [theme dark-theme?]} color blur?]
  (if color
    (colors/get-color color (if (and dark-theme? (not blur?)) 60 50))
    (colors/themed theme :color/neutral-50 (if blur? :color/white-70 :color/neutral-40))))
