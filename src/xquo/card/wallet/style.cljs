(ns xquo.card.wallet.style
  (:require [react-native.utils :refer [defstyle style]]
            [xquo.foundations.colors :as colors]
            [xquo.foundations.shadows :as shadows]))

(defn container [theme]
  (style {:width            161
          :height           100
          :border-radius    16
          :background-color (colors/themed theme :color/white-100 :color/neutral-90)
          :box-shadow       (shadows/get-box-shadow theme 2)}))

(defstyle content
  {:flex               1
   :padding-horizontal 12
   :padding-vertical   10
   :gap                8})

(defstyle image
  {:width  32
   :height 32})

(defstyle title
  {:height               22
   :include-font-padding false})

(defn description [theme]
  (style {:height               18
          :color                (colors/themed theme :color/neutral-50 :color/neutral-40)
          :include-font-padding false}))

(defstyle dismiss
  {:position :absolute
   :top      8
   :right    8})
