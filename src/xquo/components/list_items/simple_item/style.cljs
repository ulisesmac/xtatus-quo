(ns xquo.components.list-items.simple-item.style
  (:require [react-native.utils :refer [defstyle style]]
            [xquo.foundations.borders :as borders]
            [xquo.foundations.colors :as colors]))

(defstyle container-base
  {:align-self    :stretch
   :position      :relative
   :overflow      :hidden
   :border-radius (borders/radius 40)})

(defstyle row
  {:flex-direction     :row
   :align-items        :center
   :column-gap         12
   :padding-horizontal 12
   :padding-vertical   8
   :z-index            1})

(defstyle leading
  {:width           32
   :height          32
   :flex-shrink     0
   :align-items     :center
   :justify-content :center
   :border-radius   (borders/radius :max)})

(defstyle emoji
  {:width                15
   :height               15
   :font-size            15
   :line-height          15
   :text-align           :center
   :text-align-vertical  :center
   :include-font-padding false})

(defstyle content
  {:flex      1
   :min-width 0})

(defstyle title
  {:min-width 0})

(defstyle right
  {:width           20
   :height          20
   :flex-shrink     0
   :align-items     :center
   :justify-content :center})

(def leading-icon-color
  (colors/get-color :color/neutral-80-70))

(def icon-background-color
  (colors/get-color :color/neutral-80-5))

(def emoji-background-color
  (colors/get-color :color/primary-50-10))

(defn leading-background [background-color]
  (style {:background-color background-color}))
