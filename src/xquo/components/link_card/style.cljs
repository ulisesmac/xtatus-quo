(ns xquo.components.link-card.style
  (:require [react-native.utils :refer [defstyle style]]
            [xquo.foundations.colors :as colors]))

(defstyle container
  {:border-radius 16
   :border-width  1
   :padding       12
   :row-gap       4})

(defn gradient [color]
  (style {:background-image (str "radial-gradient(85.49% 243.46% at 85.49% 25.52%, "
                                (colors/get-color (colors/get-color color) 50 6)
                                " 0%, "
                                (colors/get-color :color/white-2)
                                " 100%)")}))

(defstyle border-light
  {:border-color (colors/get-color :color/neutral-80-5)})

(defstyle border-dark
  {:border-color (colors/get-color :color/white-5)})

(defstyle border-light-pressed
  {:border-color (colors/get-color :color/neutral-80-20)})

(defstyle border-dark-pressed
  {:border-color (colors/get-color :color/white-20)})

(defstyle top-row
  {:flex-direction :row
   :height         20})

(defstyle image
  {:height 20
   :width  20})

(defstyle handle-light
  {:color (colors/get-color :color/neutral-50)})

(defstyle handle-dark
  {:color (colors/get-color :color/neutral-40)})
