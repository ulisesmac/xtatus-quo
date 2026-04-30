(ns xquo.components.list.style
  (:require [reagent-extended-compiler.utils.transforms :refer [defstyle]]
            [xquo.foundations.colors :as colors]
            [xquo.foundations.spacing :as spacing]))

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
(def section-content-transition-duration "300ms")
(def section-content-transition-timing-function "linear")

(defn section-content-container [visible? height]
  (cond-> {:align-self                 "stretch"
           :overflow                   "hidden"
           :transition-property        "height"
           :transition-duration        section-content-transition-duration
           :transition-timing-function section-content-transition-timing-function}
    (or height (not visible?))
    (assoc :height (if visible? height 0))))

(defstyle section-content
  {:padding-bottom section-content-bottom-padding})

(defstyle section-content-measuring
  {:position :absolute
   :top      0
   :right    0
   :left     0})

(defstyle section-content-opacity-opening
  {:opacity                    1
   :animation-name             {"0%"   {:opacity 0}
                                "30%"  {:opacity 0}
                                "100%" {:opacity 1}}
   :animation-duration         section-content-transition-duration
   :animation-timing-function  section-content-transition-timing-function
   :animation-iteration-count  1})

(defstyle section-content-opacity-closing
  {:opacity                    0
   :animation-name             {"0%"   {:opacity 1}
                                "70%"  {:opacity 0}
                                "100%" {:opacity 0}}
   :animation-duration         section-content-transition-duration
   :animation-timing-function  section-content-transition-timing-function
   :animation-iteration-count  1})

(defn section-content-opacity [visible?]
  (if visible?
    section-content-opacity-opening
    section-content-opacity-closing))

(defstyle element-padding
  {:padding-horizontal (spacing/spacing-values 8)
   :padding-vertical   (spacing/spacing-values 3)})

(defstyle pressable-element-padding
  {:padding-horizontal 12
   :padding-vertical   8})

(defstyle pressable-element-spacing
  {:margin-horizontal 8})

(defstyle element-container
  {:flex-direction     :row
   :align-items        :flex-start
   :gap                (spacing/spacing-values 4)
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
   :gap       (spacing/spacing-values 1)})

(defstyle button-container
  {:align-self  :center
   :flex-shrink 0})

(defn bullet-color [theme]
  (if (= theme :theme/dark)
    (colors/get-color :color/neutral-50)
    (colors/get-color :color/neutral-40)))
