(ns xquo.components.drawer.drawer.style
  (:require [react-native.utils :refer [defstyle]]
            [xquo.foundations.colors :as colors]))

(defstyle container-base
  {:align-self               :stretch
   :overflow                 :hidden})

(defstyle divider-base
  {:height 1})

(defstyle content-section
  {:padding-bottom 8})

(defstyle action-section
  {:padding-left  8
   :padding-right 8})

(defstyle divider-section
  {:padding-top    8
   :padding-bottom 8})

(defstyle label-section
  {:padding-left   20
   :padding-right  20
   :padding-bottom 8})

(defstyle body-section
  {:padding-left  20
   :padding-right 20
   :padding-bottom 12})

(defstyle body-section-with-button
  {:padding-left  20
   :padding-right 20
   :padding-bottom 8})

(defstyle body-text
  {:width "100%"})

(defstyle button-section
  {:padding-left   20
   :padding-right  20
   :padding-top    8
   :padding-bottom 12})

(defstyle cta-button-dark-solid-style
  {:border-color (colors/get-color :color/neutral 70)})

(defn container-color-style [theme background]
  {:background-color
   (cond
     (= theme :theme/light) (colors/get-color :color/white 100)
     (= background :blur)   (colors/get-color :color/white 5)
     :else                  (colors/get-color :color/neutral 95))})

(defn divider-color-style [theme background]
  {:background-color
   (cond
     (= theme :theme/light) (colors/get-color :color/neutral 10)
     (= background :blur)   (colors/get-color :color/white 5)
     :else                  (colors/get-color :color/neutral 90))})

(defn body-text-style [theme]
  {:color (if (= theme :theme/light)
            (colors/get-color :color/neutral 100)
            (colors/get-color :color/white 100))})
