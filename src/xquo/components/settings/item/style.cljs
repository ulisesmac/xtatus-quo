(ns xquo.components.settings.item.style
  (:require [react-native.utils :refer [defstyle style]]
            [xquo.foundations.animations :as animations]
            [xquo.foundations.borders :as borders]
            [xquo.foundations.colors :as colors]))

(defstyle container-base
  {:overflow      :hidden
   :border-radius (borders/radius 40)})

(defstyle gap-12
  {:gap 12})

(defstyle gap-0
  {:gap 0})

(defstyle leading-icon-with-description
  {:align-self  :flex-start
   :padding-top 1})

(defstyle padding-short-default
  {:padding-horizontal 12
   :padding-vertical   13})

(defstyle padding-short-avatar
  {:padding-horizontal 12
   :padding-vertical   8})

(defstyle padding-short-none-image
  {:padding-left   16
   :padding-right  12
   :padding-top    13
   :padding-bottom 13})

(defstyle padding-description-default
  {:padding 12})

(defstyle padding-description-none-image
  {:padding-left   16
   :padding-right  12
   :padding-top    12
   :padding-bottom 12})

(defstyle padding-tag-default
  {:padding-left   12
   :padding-right  12
   :padding-top    12
   :padding-bottom 16})

(defstyle padding-tag-none-image
  {:padding-left   16
   :padding-right  12
   :padding-top    12
   :padding-bottom 16})

(defstyle title-slot
  {:flex       1
   :min-width  1
   :min-height 1})

(defstyle content-column-base
  {:flex           1
   :min-width      1
   :min-height     1
   :flex-direction :column
   :align-items    :flex-start})

(defstyle content-row-base
  {:flex           1
   :min-width      0
   :flex-direction :row
   :align-items    :center})

(defstyle overlay-base
  {:position :absolute
   :top      0
   :right    0
   :bottom   0
   :left     0})

(defstyle row-body-base
  {:flex           1
   :min-width      0
   :flex-direction :row
   :align-items    :center})

(defstyle content-column-gap-8
  {:gap 8})

(defstyle info-column
  {:align-self :stretch})

(defstyle tags-row
  {:column-gap     6
   :flex-direction :row
   :margin-top     8})

(defstyle description-row
  {:flex-direction :row
   :align-items    :center
   :gap            4
   :align-self     :stretch})

(defstyle status-row
  {:flex-direction :row
   :align-items    :center
   :gap            6
   :align-self     :stretch})

(defstyle status-dot
  {:width         8
   :height        8
   :border-radius (borders/radius :max)})

(defstyle right-content-base
  {:flex-direction :row
   :align-items    :center
   :flex-shrink    0})

(defstyle right-gap-4
  {:gap 4})

(defstyle right-gap-2
  {:gap 2})

(defstyle right-gap-6
  {:gap 6})

(defstyle right-margin-12
  {:margin-left 12})

(defstyle button-right-slot
  {:margin-left 12})

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

(defstyle row-body-default-state-style
  {:transform                  [{:translate-y (:default-translate-y animations/press-feedback)}]
   :transition-property        (:transition-property animations/press-feedback)
   :transition-duration        (:default-duration animations/press-feedback)
   :transition-timing-function (:default-timing-function animations/press-feedback)})

(defstyle row-body-pressed-state-style
  {:transform                  [{:translate-y (:pressed-translate-y animations/press-feedback)}]
   :transition-property        (:transition-property animations/press-feedback)
   :transition-duration        (:pressed-duration animations/press-feedback)
   :transition-timing-function (:pressed-timing-function animations/press-feedback)})

(defstyle arrow-default-state-style
  {:transform                  [{:translate-x 0}]
   :transition-property        (:transition-property animations/press-feedback)
   :transition-duration        (:default-duration animations/press-feedback)
   :transition-timing-function (:default-timing-function animations/press-feedback)})

(defstyle arrow-pressed-state-style
  {:transform                  [{:translate-x 6}]
   :transition-property        (:transition-property animations/press-feedback)
   :transition-duration        (:pressed-duration animations/press-feedback)
   :transition-timing-function (:pressed-timing-function animations/press-feedback)})

(defstyle label-text
  {:text-align :right})

(defstyle image-placeholder
  {:width            20
   :height           20
   :background-color (colors/get-color :color/danger 50)})

(defstyle avatar-placeholder
  {:width            32
   :height           32
   :background-color (colors/get-color :color/danger 50)})

(defstyle color-placeholder
  {:width            20
   :height           20
   :background-color (colors/get-color :color/danger 50)})

(defstyle tag-placeholder
  {:width            88
   :height           24
   :background-color (colors/get-color :color/danger 50)})

(defn- dark-theme? [theme]
  (= theme :theme/dark))

(defn container-padding-style [image-type description-visible? tag-visible?]
  (cond
    tag-visible?
    (if (= image-type :none)
      padding-tag-none-image
      padding-tag-default)

    description-visible?
    (if (= image-type :none)
      padding-description-none-image
      padding-description-default)

    (= image-type :avatar)
    padding-short-avatar

    (= image-type :none)
    padding-short-none-image

    :else
    padding-short-default))

(defn title-color [theme]
  (if (dark-theme? theme)
    (colors/get-color :color/white-100)
    (colors/get-color :color/neutral-100)))

(defn pressed-overlay-color-style [theme blur?]
  (style {:background-color (cond
                              (and (dark-theme? theme) blur?)
                              (colors/get-color :color/white-10)

                              blur?
                              (colors/get-color :color/neutral-80-10)

                              (dark-theme? theme)
                              (colors/get-color :color/white-5)

                              :else
                              (colors/get-color :color/neutral-80-5))}))

(defn pressed-overlay-state-style [pressed?]
  (style {:opacity                    (if pressed? 1 0)
          :transition-property        "opacity"
          :transition-duration        (if pressed?
                                       (:pressed-duration animations/press-feedback)
                                       (:default-duration animations/press-feedback))
          :transition-timing-function (if pressed?
                                       (:pressed-timing-function animations/press-feedback)
                                       (:default-timing-function animations/press-feedback))}))

(defn secondary-text-color [theme blur? pressed?]
  (cond
    (and (dark-theme? theme) blur? pressed?) (colors/get-color :color/white-70)
    (and (dark-theme? theme) blur?)          (colors/get-color :color/white-40)
    (and (dark-theme? theme) pressed?)       (colors/get-color :color/neutral-20)
    (dark-theme? theme)                      (colors/get-color :color/neutral-40)
    pressed?                                 (colors/get-color :color/neutral-70)
    :else                                    (colors/get-color :color/neutral-50)))

(defn leading-icon-color [theme blur? pressed?]
  (cond
    (and (dark-theme? theme) blur? pressed?) (colors/get-color :color/white-100)
    (and (dark-theme? theme) blur?)          (colors/get-color :color/white-70)
    (and (dark-theme? theme) pressed?)       (colors/get-color :color/white-70)
    (dark-theme? theme)                      (colors/get-color :color/neutral-40)
    pressed?                                 (colors/get-color :color/neutral-100)
    :else                                    (colors/get-color :color/neutral-50)))

(defn trailing-icon-color [theme blur? pressed?]
  (secondary-text-color theme blur? pressed?))

(defn status-dot-color [theme status-color]
  (if (dark-theme? theme)
    (colors/get-color status-color 60)
    (colors/get-color status-color 50)))
