(ns xquo.components.list.style
  (:require [react-native.utils :refer [defstyle]]
            [xquo.foundations.animations :as animations]
            [xquo.foundations.colors :as colors]
            [xquo.foundations.spacing :refer [spacing]]))

(defstyle container-base
  {:width      "100%"
   :align-self :stretch})

(defstyle element-shell
  {:align-self    :stretch
   :position      :relative
   :overflow      :visible
   :border-radius 12})

(defstyle section-shell
  {:align-self :stretch
   :overflow   :hidden})

(defn section-content-transition-duration-ms [visible?]
  (if visible? 300 200))

(defn section-content-transition-duration [visible?]
  (str (section-content-transition-duration-ms visible?) "ms"))

(def section-content-transition-timing-function "ease-in-out")

(defstyle section-content-container
  {:align-self :stretch
   :overflow   :hidden})

(defstyle section-content-absolute
  {:left     0
   :position :absolute
   :right    0
   :top      0})

(defstyle section-content
  {:padding-bottom 8})

(defstyle element-padding
  {:padding-horizontal (spacing 8)
   :padding-vertical   (spacing 3)})

(defstyle pressable-element-padding
  {:padding-horizontal 12
   :padding-vertical   8})

(defstyle pressable-element-spacing
  {:margin-horizontal 8})

(defstyle element-container
  {:flex-direction     :row
   :align-items        :flex-start
   :gap                (spacing 4)
   :z-index            1})

(defstyle row-default-state-style
  {:transform                  [{:scale       (:default-scale animations/press-feedback)}
                                {:translate-y (:default-translate-y animations/press-feedback)}]
   :transition-property        (:transition-property animations/press-feedback)
   :transition-duration        (:default-duration animations/press-feedback)
   :transition-timing-function (:default-timing-function animations/press-feedback)})

(defstyle row-pressed-state-style
  {:transform                  [{:scale       (:pressed-scale animations/press-feedback)}
                                {:translate-y (:pressed-translate-y animations/press-feedback)}]
   :transition-property        (:transition-property animations/press-feedback)
   :transition-duration        (:pressed-duration animations/press-feedback)
   :transition-timing-function (:pressed-timing-function animations/press-feedback)})

(defstyle overlay-base
  {:position      :absolute
   :top           0
   :right         0
   :bottom        0
   :left          0
   :border-radius 12})

(defn pressed-color-style [color]
  (when color
    {:background-color (colors/get-color color 50 5)}))

(defn pressed-color-state-style [pressed?]
  {:opacity                    (if pressed? 1 0)
   :transition-property        "opacity"
   :transition-duration        120
   :transition-timing-function "ease-out"})

(defstyle content-container
  {:flex      1
   :min-width 0
   :gap       (spacing 1)})

(defstyle button-container
  {:align-self  :center
   :flex-shrink 0})

(defn bullet-color [theme]
  (if (= theme :theme/dark)
    (colors/get-color :color/neutral-50)
    (colors/get-color :color/neutral-40)))
