(ns xquo.components.list.style
  (:require [react-native.utils :refer [defstyle]]
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
  {:align-self :stretch})

(def section-content-bottom-padding 8)
(def section-content-transition-duration-ms 200)
(def section-content-transition-duration (str section-content-transition-duration-ms "ms"))
(def section-content-transition-timing-function "linear")

(defn section-content-container [visible? height]
  (cond-> {:align-self "stretch"
           :overflow   "hidden"}
    height
    (assoc :transition-property        "height"
           :transition-duration        section-content-transition-duration
           :transition-timing-function section-content-transition-timing-function)

    (or height (not visible?))
    (assoc :height (if visible? height 0))))

(defstyle section-content
  {:padding-bottom section-content-bottom-padding})

(defstyle section-content-measuring
  {:position :absolute
   :top      0
   :right    0
   :left     0
   :opacity  0})

(defn section-content-opacity [visible?]
  {:opacity                    (if visible? 1 0)
   :transition-property        "opacity"
   :transition-duration        section-content-transition-duration
   :transition-timing-function section-content-transition-timing-function})

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
