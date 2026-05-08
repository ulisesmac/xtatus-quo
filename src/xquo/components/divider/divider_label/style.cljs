(ns xquo.components.divider.divider-label.style
  (:require [reagent-extended-compiler.utils.transforms :refer [defstyle style]]
            [xquo.components.button.style :as button-style]
            [xquo.components.settings.item.style :as settings-item-style]
            [xquo.foundations.animations :as animations]
            [xquo.foundations.colors :as colors]
            [react-native.core :as rn]))

(defstyle container-base
  {:width            "100%"
   :flex-direction   :row
   :align-items      :center
   :overflow         :hidden
   :position         :relative
   :padding-top      16
   :padding-bottom   8
   :padding-left     20
   :padding-right    20})

(defstyle compact-container
  {:padding-top 8})

(defn divider-line-style [compact?]
  {:border-top-width 1
   :padding-top      (if compact? 7 15)})

(defstyle left-chevron-container
  {:padding-left 16})

(defstyle title-slot
  {:flex       1
   :min-width  1
   :min-height 1})

(defstyle left-chevron-slot
  {:width           20
   :height          20
   :align-items     :center
   :justify-content :center
   :margin-right    2})

(defstyle right-chevron-slot
  {:width           20
   :height          20
   :align-items     :center
   :justify-content :center
   :margin-left     2})

(defn chevron-state [open? toggle-duration toggle-timing-function]
  (style {:transform                  [{:rotate (if open? "0deg" "-90deg")}]
          :transition-property        "transform"
          :transition-duration        toggle-duration
          :transition-timing-function toggle-timing-function}))

(defstyle counter-slot
  {:width           20
   :height          20
   :align-items     :center
   :justify-content :center
   :margin-left     2})

(defstyle right-slot
  {:min-width       20
   :height          20
   :align-items     :flex-end
   :justify-content :center
   :margin-left     2})

(def overlay-base rn/style-sheet-absolute-fill)

(defn overlay-color [theme blur?]
  (if blur?
    (settings-item-style/pressed-overlay-color-style theme blur?)
    (button-style/pressable-type-style theme :ghost :none :color/primary false true)))

(defn overlay-state [pressed?]
  (style {:opacity                    (if pressed? 1 0)
          :transition-property        "opacity"
          :transition-duration        (if pressed?
                                       (:pressed-duration animations/press-feedback)
                                       (:default-duration animations/press-feedback))
          :transition-timing-function (if pressed?
                                       (:pressed-timing-function animations/press-feedback)
                                       (:default-timing-function animations/press-feedback))}))

(defn title-color [theme blur?]
  {:color (cond
            (and (= theme :theme/dark) blur?)  (colors/get-color :color/white-70)
            (and (= theme :theme/light) blur?) (colors/get-color :color/neutral-80-70)
            (= theme :theme/dark)              (colors/get-color :color/neutral-40)
            :else                              (colors/get-color :color/neutral-50))})

(defn border-color [theme blur?]
  {:border-top-color (cond
                       (and (= theme :theme/dark) blur?)  (colors/get-color :color/white-5)
                       (and (= theme :theme/light) blur?) (colors/get-color :color/neutral-80-5)
                       (= theme :theme/dark)              (colors/get-color :color/neutral-90)
                       :else                              (colors/get-color :color/neutral-10))})

(defn icon-color [theme blur?]
  (:color (title-color theme blur?)))
