(ns xquo.components.input.style
  (:require [reagent-extended-compiler.utils.transforms :refer [defstyle style]]
            [xquo.foundations.borders :as borders]
            [xquo.foundations.colors :as colors]
            [xquo.foundations.typography :as typography]))

(def container-layout-styles
  {40 {nil                (style {:padding-horizontal 16
                                  :padding-right      12
                                  :padding-vertical   9
                                  :border-radius      (:border/sizes-40-56 borders/border-radius-values)
                                  :min-height         40})
       :icon              (style {:padding-left     12
                                  :padding-right    16
                                  :padding-vertical 9
                                  :border-radius    (:border/sizes-40-56 borders/border-radius-values)
                                  :min-height       40})
       :clear             (style {:padding-left     16
                                  :padding-right    12
                                  :padding-vertical 9
                                  :border-radius    (:border/sizes-40-56 borders/border-radius-values)
                                  :min-height       40})
       :icon-clear        (style {:padding-horizontal 12
                                  :padding-vertical   9
                                  :border-radius      (:border/sizes-40-56 borders/border-radius-values)
                                  :min-height         40})
       :button            (style {:padding-left   16
                                  :padding-right  8
                                  :padding-top    8
                                  :padding-bottom 8
                                  :border-radius  (:border/sizes-40-56 borders/border-radius-values)
                                  :min-height     40})
       :button-icon       (style {:padding-left   12
                                  :padding-right  8
                                  :padding-top    8
                                  :padding-bottom 8
                                  :border-radius  (:border/sizes-40-56 borders/border-radius-values)
                                  :min-height     40})
       :button-clear      (style {:padding-left   16
                                  :padding-right  8
                                  :padding-top    8
                                  :padding-bottom 8
                                  :border-radius  (:border/sizes-40-56 borders/border-radius-values)
                                  :min-height     40})
       :button-icon-clear (style {:padding-left   12
                                  :padding-right  8
                                  :padding-top    8
                                  :padding-bottom 8
                                  :border-radius  (:border/sizes-40-56 borders/border-radius-values)
                                  :min-height     40})}
   32 {nil                (style {:padding-horizontal 12
                                  :padding-vertical   5
                                  :border-radius      (:border/size-32 borders/border-radius-values)
                                  :min-height         32})
       :icon              (style {:padding-left     8
                                  :padding-right    12
                                  :padding-vertical 5
                                  :border-radius    (:border/size-32 borders/border-radius-values)
                                  :min-height       32})
       :clear             (style {:padding-left     12
                                  :padding-right    8
                                  :padding-vertical 5
                                  :border-radius    (:border/size-32 borders/border-radius-values)
                                  :min-height       32})
       :icon-clear        (style {:padding-horizontal 8
                                  :padding-vertical   5
                                  :border-radius      (:border/size-32 borders/border-radius-values)
                                  :min-height         32})
       :button            (style {:padding-left   12
                                  :padding-right  4
                                  :padding-top    4
                                  :padding-bottom 4
                                  :border-radius  (:border/size-32 borders/border-radius-values)
                                  :min-height     32})
       :button-icon       (style {:padding-left   8
                                  :padding-right  4
                                  :padding-top    4
                                  :padding-bottom 4
                                  :border-radius  (:border/size-32 borders/border-radius-values)
                                  :min-height     32})
       :button-clear      (style {:padding-left   12
                                  :padding-right  4
                                  :padding-top    4
                                  :padding-bottom 4
                                  :border-radius  (:border/size-32 borders/border-radius-values)
                                  :min-height     32})
       :button-icon-clear (style {:padding-left   8
                                  :padding-right  4
                                  :padding-top    4
                                  :padding-bottom 4
                                  :border-radius  (:border/size-32 borders/border-radius-values)
                                  :min-height     32})}})

(defn container-layout-style [size layout]
  (get-in container-layout-styles [size layout]))

(def container-slot-gap-styles
  {40 (style {:gap 8})
   32 (style {:gap 4})})

(defstyle root-base
  {:flex 1})

(defstyle root-gap-8
  {:gap 8})

(defstyle root-disabled
  {:opacity 0.3})

(defstyle top-row-base
  {:flex-direction :row
   :align-items    :center})

(defstyle label-slot
  {:flex      1
   :min-width 1})

(defstyle counter-slot
  {:flex       1
   :min-width  1
   :text-align :right})

(defstyle container-base
  {:flex-direction :row
   :align-self     :stretch
   :border-width   1})

(defstyle container-single-line
  {:align-items :center})

(defstyle container-multiline
  {:align-items :flex-start})

(defstyle content-base
  {:flex           1
   :min-width      1
   :flex-direction :row
   ;:background-color :yellow
   })

(defstyle content-single-line
  {:min-height  22
   :align-items :center})

(defstyle content-multiline
  {:align-items :flex-start})

(defstyle text-input-base
  {:flex                 1
   :min-width            1
   :font-family          (:regular typography/font-families)
   :font-size            (:font-size typography/paragraph-1-15)
   :letter-spacing       (:letter-spacing typography/paragraph-1-15)
   :include-font-padding false
   :padding              0
   :padding-horizontal   0
   :padding-vertical     0
   :padding-top          0
   :padding-right        0
   :padding-bottom       0
   :padding-left         0
   :margin               0})

(defstyle text-input-single-line-ios
  {:line-height         18
   :padding-top         0
   :padding-bottom      0})

(defstyle text-input-single-line-android
  {:height              22
   :line-height         22
   :text-align-vertical :center})

(defstyle text-input-multiline-ios
  {:line-height         22
   :padding-top         0
   :padding-bottom      0})

(defstyle text-input-multiline-android
  {:min-height          22
   :line-height         22
   :text-align-vertical :top})

(defstyle icon-slot-base
  {:width            20
   :height           20
   :justify-content  :center
   :align-items      :center})

(defstyle clear-icon-slot
  {:width            20
   :height           22
   :padding-top      1
   :justify-content  :flex-start
   :align-items      :center})

(defstyle trailing-button-slot
  {:height          24
   :justify-content :center
   :align-items     :center})

(defn labels-color [dark-theme? blur?]
  {:color (cond
            (and dark-theme? blur?) (colors/get-color :color/white-40)
            blur?                   (colors/get-color :color/neutral-80-40)
            dark-theme?             (colors/get-color :color/neutral-40)
            :else                   (colors/get-color :color/neutral-50))})

(defn leading-icon-color [dark-theme? blur?]
  {:color (cond
            (and dark-theme? blur?) (colors/get-color :color/white-70)
            blur?                   (colors/get-color :color/neutral-80-70)
            dark-theme?             (colors/get-color :color/neutral-40)
            :else                   (colors/get-color :color/neutral-50))})

(defn clear-icon-color [dark-theme? blur?]
  {:color (cond
            (and dark-theme? blur?) (colors/get-color :color/white-10)
            blur?                   (colors/get-color :color/neutral-80-30)
            dark-theme?             (colors/get-color :color/neutral-60)
            :else                   (colors/get-color :color/neutral-40))})

(defn placeholder-color [dark-theme? blur? focused?]
  (cond
    (and dark-theme? blur? focused?)
    (colors/get-color :color/white-20)

    (and dark-theme? blur?)
    (colors/get-color :color/white-40)

    (and dark-theme? focused?)
    (colors/get-color :color/neutral-60)

    dark-theme?
    (colors/get-color :color/neutral-50)

    (and blur? focused?)
    (colors/get-color :color/neutral-80-20)

    blur?
    (colors/get-color :color/neutral-80-40)

    focused?
    (colors/get-color :color/neutral-30)

    :else
    (colors/get-color :color/neutral-40)))

(defn selection-color [color]
  (colors/get-color color 50))

(defn container-color-style [dark-theme? blur? error? focused?]
  (style {:background-color (cond
                              (and dark-theme? (not blur?))
                              (colors/get-color :color/neutral-95)

                              (not blur?)
                              (colors/get-color :color/white-100)

                              :else
                              :transparent)
          :border-color     (cond
                              error?
                              (colors/get-color :color/danger-50-40)

                              (and dark-theme? blur? focused?)
                              (colors/get-color :color/white-40)

                              (and dark-theme? blur?)
                              (colors/get-color :color/white-10)

                              (and blur? focused?)
                              (colors/get-color :color/neutral-80-20)

                              blur?
                              (colors/get-color :color/neutral-80-10)

                              (and dark-theme? focused?)
                              (colors/get-color :color/neutral-60)

                              dark-theme?
                              (colors/get-color :color/neutral-70)

                              focused?
                              (colors/get-color :color/neutral-40)

                              :else
                              (colors/get-color :color/neutral-20))}))
