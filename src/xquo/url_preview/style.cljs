(ns xquo.url-preview.style
  (:require [react-native.utils :refer [defstyle style]]
            [xquo.foundations.colors :as colors]))

(defn container [theme loading? dark-theme?]
  (style {:width              "100%"
          :height             56
          :border-radius      12
          :padding-horizontal 12
          :padding-vertical   10
          :flex-direction     :row
          :align-items        (if loading? "center" "flex-start")
          :justify-content    (when loading? "center")
          :gap                (if (and loading? (not dark-theme?)) 8 6)
          :background-color   (when-not loading?
                                (colors/themed theme :color/neutral-5 :color/neutral-90))}))

(defstyle favicon
  {:margin-top 1})

(defstyle content
  {:flex 1 :min-width 0})

(defstyle channel
  {:height         18
   :flex-direction :row
   :align-items    :center})

(defstyle channel-text
  {:flex-shrink 1})

(defstyle text
  {:height               18
   :include-font-padding false
   :text-align-vertical  :center})

(defn secondary [theme]
  (style {:color (colors/themed theme :color/neutral-50 :color/neutral-40)}))

(defstyle image
  {:width 36 :height 36 :border-radius 8})

(defstyle clear
  {:width 20 :height 36})

(defstyle border
  {:position :absolute :top 0 :right 0 :bottom 0 :left 0})

(defn border-color [theme]
  (colors/themed theme :color/neutral-30 :color/neutral-80))
