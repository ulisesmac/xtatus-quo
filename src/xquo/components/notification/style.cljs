(ns xquo.components.notification.style
  (:require [react-native.core :as rn]
            [reagent-extended-compiler.utils.transforms :refer [defstyle style]]
            [xquo.foundations.colors :as colors]))

(defstyle container-base
  {:padding-horizontal 12
   :align-self         :stretch})

(defstyle root-base
  {:align-self       :stretch
   :min-width        0
   :border-radius    12
   :padding          10
   :flex-direction   :row
   :align-items      :flex-start
   :column-gap       4})

(defstyle text-slot
  {:flex      1
   :min-width 1})

(defn root-color-style [theme]
  (style {:background-color (colors/themed theme
                                           (if rn/ios? :color/neutral-80-70 :color/neutral-80-95)
                                           (if rn/ios? :color/white-70 :color/white-95))}))

(defn text-color-style [theme]
  (style {:color (colors/themed theme :color/white :color/neutral-100)}))

(defn icon-color [theme type]
  (case type
    :notification/positive (colors/themed theme :color/success-60 :color/success-50)
    :notification/negative (colors/themed theme :color/danger-60 :color/danger-50)
    (colors/themed theme :color/white-60 :color/neutral-50)))
