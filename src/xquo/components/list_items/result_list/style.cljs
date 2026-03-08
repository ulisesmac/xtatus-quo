(ns xquo.components.list-items.result-list.style
  (:require [reagent-extended-compiler.utils.transforms :refer [defstyle style]]
            [xquo.react-native :as rn]
            [xquo.foundations.animations :as animations]
            [xquo.foundations.borders :as borders]
            [xquo.foundations.colors :as colors]))

(defstyle container-base
  {:padding-horizontal 12
   :padding-vertical   8
   :height             56
   :border-radius      12
   :overflow           :hidden
   :flex-direction     :row
   :align-items        :center
   :column-gap         8})

(defstyle content-column
  {:flex            1
   :min-width       1
   :align-self      :stretch
   :justify-content :center})

(defstyle title
  {:height 22})

(defstyle content
  {:height 18
   ;:background-color :blue
   })

(defstyle content-row
  {:flex           1
   :min-width      0
   :flex-direction :row
   :align-items    :center
   :column-gap     8})

(def overlay-base
  rn/style-sheet-absolute-fill)

(defstyle image
  {:width         32
   :height        32
   :align-self    :center
   :border-radius (:border/size-32 borders/border-radius-values)})

(defstyle image-slot
  {:width            32
   :align-self       :stretch
   :justify-content  :center
   :align-items      :center})

(defn- dark-theme? [theme]
  (= theme :theme/dark))

(defn- blur-background? [background]
  (= background :blur))

(defn title-color [theme]
  (if (dark-theme? theme)
    (colors/get-color :color/white-100)
    (colors/get-color :color/neutral-100)))

(defn container-color-style [theme background]
  (style {:background-color (if (blur-background? background)
                              :transparent
                              (if (dark-theme? theme)
                                (colors/get-color :color/neutral-95)
                                (colors/get-color :color/white-100)))}))

(defn pressed-color-style [theme background color]
  (when color
    (style {:background-color (if (and (dark-theme? theme)
                                       (blur-background? background))
                                (colors/get-color :color/white-5)
                                (colors/get-color color 50 5))})))

(defn active-color-style [theme background color]
  (when color
    (style {:background-color (if (and (dark-theme? theme)
                                       (blur-background? background))
                                (colors/get-color :color/white-10)
                                (colors/get-color color 50 10))})))

(defn active-overlay-state-style [active?]
  (style {:opacity                    (if active? 1 0)
          :transition-property        "opacity"
          :transition-duration        (if active?
                                       (:toggle-duration animations/state-change)
                                       (:radio-collapse-duration animations/state-change))
          :transition-timing-function (:transition-timing-function animations/state-change)}))
