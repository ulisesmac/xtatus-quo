(ns xquo.components.info.info-message.style
  (:require [react-native.utils :refer [defstyle]]
            [xquo.foundations.colors :as colors]))

(defstyle container-base
  {:flex-direction :row
   :gap            4})

(defstyle default-size-base
  {:align-items :flex-start})

(defstyle tiny-size-base
  {:align-items :center})

(defstyle text-slot
  {:flex      1
   :min-width 1})

(defstyle icon-default
  {:padding-top    1
   :padding-bottom 1})

(defstyle icon-tiny
  {:padding-top    2
   :padding-bottom 2})

(defn message-color [theme background status]
  {:color (cond
            (= status :success)
            (colors/get-color :color/success
                              (if (= theme :theme/dark) 60 50))

            (= status :error)
            (colors/get-color :color/danger
                              (if (= theme :theme/dark) 60 50))

            (= status :warning)
            (colors/get-color :color/orange
                              (if (= theme :theme/dark) 60 50))

            (and (= theme :theme/dark) (= background :blur))
            (colors/get-color :color/white 40)

            (= theme :theme/dark)
            (colors/get-color :color/neutral 40)

            :else
            (colors/get-color :color/neutral 50))})
