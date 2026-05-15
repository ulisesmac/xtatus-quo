(ns xquo.components.emoji-picker.style
  (:require [react-native.core :as rn]
            [reagent-extended-compiler.utils.transforms :refer [defstyle style]]
            [xquo.foundations.colors :as colors]))

(defstyle root
  {:flex 1})

(def ^:private footer-height 56)

(defstyle search-input-container
  {:padding-horizontal 20
   :padding-top        20
   :padding-bottom     12
   :height             (+ 32 20 12)})


(defn sheet-region-background [theme]
  (style {:background-color (if rn/ios?
                              (colors/themed theme :color/white-70 :color/neutral-80-70)
                              (colors/themed theme :color/white-95 :color/neutral-95-95))}))

(defstyle emoji-row
  {:flex-direction     :row
   :column-gap         18.5
   :justify-content    :flex-start
   :padding-vertical   8
   :padding-horizontal 20})

(defstyle emoji-pressable-base
  {:justify-content :center
   :align-items     :center})

(defn emoji-pressable-size [size]
  (style {:width  size
          :height size}))

(defstyle emoji-text
  {:font-size            50
   :include-font-padding false})

(defstyle list-root
  {:flex 1})

(defn list-content [bottom-safe-area searching?]
  (style {:padding-bottom (+ bottom-safe-area
                             (if searching? 0 footer-height))}))

(defn category-footer-container [bottom-safe-area]
  (style {:flex-direction     :row
          :align-items        :center
          :justify-content    :space-between
          :padding-horizontal 20
          :padding-top        12
          :padding-bottom     (+ 12 bottom-safe-area)}))
