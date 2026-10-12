(ns xquo.profile.showcase-card.style
  (:require [react-native.utils :refer [defstyle style]]
            [xquo.foundations.colors :as colors]
            [xquo.foundations.shadows :as shadows]))

(defstyle container
  {:width 176 :height 104 :padding 8})

(defn surface [theme disabled?]
  (style {:flex               1
          :border-radius      16
          :padding-top        10
          :padding-horizontal 12
          :padding-bottom     12
          :gap                12
          :opacity            (if disabled? 0.3 1)
          :background-color   (colors/themed theme :color/white :color/neutral-90)
          :box-shadow         (shadows/get-box-shadow :theme/light 2)}))

(defn title [theme]
  (style {:height               22
          :line-height          22
          :include-font-padding false
          :text-align-vertical  :center
          :color                (colors/themed theme :color/neutral-100 :color/white)}))

(defstyle content
  {:height 32 :flex-direction :row :align-items :center})
