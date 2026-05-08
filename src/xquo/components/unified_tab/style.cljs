(ns xquo.components.unified-tab.style
  (:require [reagent-extended-compiler.utils.transforms :refer [defstyle style]]
            [xquo.foundations.borders :as borders]
            [xquo.foundations.colors :as colors]
            [xquo.react-native-reanimated :as rnr]))

(def ^:private tab-transition-easing (rnr/cubic-bezier 0.25 0.1 0.25 1))
(def ^:private tab-transition-duration "300ms")
(def ^:private text-color-transition-duration "140ms")

(defstyle root
  {:width              "100%"
   :padding-horizontal 20
   :padding-vertical   8})

(defstyle root-with-content
  {:flex  1
   :width "100%"})

(defn- tab-container-background-color [type dark-theme? blur?]
  (cond
    (and (= type :grey) dark-theme? blur?) (colors/get-color :color/white-5)
    (and (= type :grey) blur?)             (colors/get-color :color/neutral-80-5)
    (and (= type :grey) dark-theme?)       (colors/get-color :color/neutral-80)
    (= type :grey)                         (colors/get-color :color/neutral-10)
    (and (= type :dark-grey) dark-theme?)  (colors/get-color :color/neutral-90)
    (= type :dark-grey)                    (colors/get-color :color/neutral-20)))

(defn tab-container [size type dark-theme? blur?]
  (style {:height             size
          :border-radius      (:border/size-32 borders/border-radius-values)
          :flex-direction     :row
          :padding            2
          :gap                2
          :background-color   (tab-container-background-color type dark-theme? blur?)}))

(defn- selected-indicator-background-color [type dark-theme? blur?]
  (cond
    (and (= type :grey) dark-theme? blur?) (colors/get-color :color/white-20)
    (and (= type :grey) blur?)             (colors/get-color :color/neutral-80-60)
    (and (= type :grey) dark-theme?)       (colors/get-color :color/neutral-60)
    (= type :grey)                         (colors/get-color :color/neutral-50)
    (and (= type :dark-grey) dark-theme?)  (colors/get-color :color/neutral-60)
    (= type :dark-grey)                    (colors/get-color :color/neutral-50)))

(defstyle selected-indicator-frame
  {:position       :absolute
   :top            2
   :right          2
   :bottom         2
   :left           2
   :flex-direction :row
   :gap            2})

(defstyle selected-indicator-slot
  {:flex      1
   :min-width 0})

(defn selected-indicator [selected-index translate-x gap-translate-x type dark-theme? blur?]
  (if translate-x
    (style {:width            "100%"
            :height           "100%"
            :border-radius    (:border/size-24 borders/border-radius-values)
            :background-color (selected-indicator-background-color type dark-theme? blur?)
            :transform        [{:translate-x translate-x}
                               {:translate-x gap-translate-x}]})
    (style {:width                      "100%"
            :height                     "100%"
            :border-radius              (:border/size-24 borders/border-radius-values)
            :background-color           (selected-indicator-background-color type dark-theme? blur?)
            :transform                  [{:translate-x (str (* 100 selected-index) "%")}
                                         {:translate-x (* 2 selected-index)}]
            :transition-property        "transform"
            :transition-duration        tab-transition-duration
            :transition-timing-function tab-transition-easing})))

(defstyle tab-item
  {:flex            1
   :min-width       0
   :align-items     :center
   :justify-content :center})

(defstyle tab-content
  {:min-width       0
   :flex-direction  :row
   :align-items     :center
   :justify-content :center
   :gap             4})

(defstyle icon-frame
  {:position :relative})

(defstyle selected-icon-layer
  {:position :absolute
   :top      0
   :left     0})

(defn icon-layer [visible?]
  (style {:opacity                    (if visible? 1 0)
          :transition-property        "opacity"
          :transition-duration        text-color-transition-duration
          :transition-timing-function tab-transition-easing}))

(defstyle content-text
  {:min-width   0
   :flex-shrink 1})

(defstyle content-root
  {:flex     1
   :width    "100%"
   :overflow :hidden})

(defstyle content-track
  {:flex           1
   :flex-direction :row})

(defn content-track-layout [content-count]
  (style {:width (str (* 100 content-count) "%")}))

(defstyle content-page
  {:flex        1
   :width       "100%"
   :flex-shrink 0})

(defn content-track-state [active-index content-count]
  (style {:transform                  [{:translate-x (str (* (/ -100 content-count) active-index) "%")}]
          :transition-property        "transform"
          :transition-duration        tab-transition-duration
          :transition-timing-function tab-transition-easing}))

(defn content-track-animated-state [translate-x]
  (style {:transform [{:translate-x translate-x}]}))

(def selected-content-color (colors/get-color :color/white-100))

(defn text-color [dark-theme? selected?]
  {:color                      (cond
                                 selected?    selected-content-color
                                 dark-theme?  (colors/get-color :color/white-100)
                                 :else        (colors/get-color :color/neutral-100))
   :transition-property        "color"
   :transition-duration        text-color-transition-duration
   :transition-timing-function tab-transition-easing})
