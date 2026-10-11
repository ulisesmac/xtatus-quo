(ns xquo.demo.notification.style
  (:require [react-native.utils :refer [defstyle style]]
            [xquo.foundations.colors :as colors]))

(defstyle background
  {:position :absolute
   :top      0
   :left     0
   :right    0
   :height   "55%"})

(defstyle fill
  {:position :absolute
   :top      0
   :left     0
   :bottom   0
   :right    0})

(defstyle preview
  {:height          "40%"
   :align-items     :center
   :justify-content :center
   :gap             12})

(defstyle blur-preview
  {:flex  1
   :width "100%"})

(defstyle scene
  {:padding 16
   :gap     12})

(defstyle photos
  {:flex-direction :row
   :gap            12})

(defstyle photo
  {:flex          1
   :height        160
   :border-radius 12})

(defstyle landscape
  {:width         "100%"
   :height        160
   :border-radius 12})

(defstyle swatches
  {:flex-direction :row
   :gap            8})

(defn swatch [color]
  (style {:flex             1
          :padding-vertical 16
          :border-radius    8
          :text-align       :center
          :color            (colors/get-color :color/white)
          :background-color (colors/get-color color 50)}))

(defstyle preview-notification
  {:position :absolute
   :top      48
   :left     0
   :right    0})

(defstyle shell
  {:position         :absolute
   :top              0
   :left             0
   :bottom           0
   :right            0
   :background-color (colors/get-color :color/neutral-80-80)})

(defstyle controls
  {:padding 20
   :gap     16})
