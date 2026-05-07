(ns xquo.components.page-nav.style
  (:require [reagent-extended-compiler.utils.transforms :refer [defstyle style]]
            [xquo.foundations.borders :as borders]
            [xquo.foundations.colors :as colors]))

(defn- dark-background? [background]
  (contains? #{:neutral-90 :neutral-95 :neutral-100} background))

(defn nav-surface-color [theme background]
  (case background
    :white       (colors/get-color :color/white)
    :neutral-5   (colors/get-color :color/neutral-5)
    :neutral-90  (colors/get-color :color/neutral-90)
    :neutral-95  (colors/get-color :color/neutral-95)
    :neutral-100 (colors/get-color :color/neutral-100)
    :photo       :transparent
    :blur        :transparent
    (if (= theme :theme/dark)
      (colors/get-color :color/neutral-95)
      (colors/get-color :color/white))))

(defn title-color [theme background]
  (if (or (= theme :theme/dark) (dark-background? background))
    (colors/get-color :color/white)
    (colors/get-color :color/neutral-100)))

(defn description-color [theme background]
  (if (or (= theme :theme/dark) (dark-background? background))
    (colors/get-color :color/neutral-40)
    (colors/get-color :color/neutral-50)))

(defn icon-color [theme background]
  (if (or (= theme :theme/dark) (dark-background? background))
    (colors/get-color :color/white)
    (colors/get-color :color/neutral-100)))

(defn dropdown-surface-style [theme background]
  (case background
    :photo (style {:background-color (colors/get-color :color/white-40)})
    :blur  (style {:background-color (if (= theme :theme/dark)
                                       (colors/get-color :color/white-20)
                                       (colors/get-color :color/white-40))})
    (style {:background-color (if (or (= theme :theme/dark) (dark-background? background))
                                (colors/get-color :color/white-10)
                                (colors/get-color :color/neutral-10))})))

(defn action-button-type [theme background]
  (if (#{:photo :blur} background)
    :grey
    (if (or (= theme :theme/dark) (dark-background? background))
      :dark-grey
      :grey)))

(defn nav-surface-style [theme background]
  (style {:background-color (nav-surface-color theme background)}))

(defstyle container-base
  {:height             56
   :width              "100%"
   :padding-horizontal 20
   :flex-direction     :row
   :align-items        :center})

(defstyle side-slot
  {:width           40
   :height          40
   :justify-content :center})

(defstyle center-slot
  {:flex            1
   :justify-content :center
   :padding-horizontal 10})

(defstyle center-slot-centered
  {:align-items :center})

(defstyle center-slot-left
  {:align-items :flex-start})

(defstyle right-actions-row
  {:flex-direction  :row
   :justify-content :flex-end
   :align-items     :center})

(defstyle action-gap
  {:margin-left 8})

(defstyle right-slot-balanced
  {:width 40})

(defstyle right-slot-unbalanced
  {:min-width 40})

(defstyle placeholder-avatar-gap
  {:margin-right 8})

(defstyle title-row
  {:flex-direction :row
   :align-items    :center})

(defstyle dropdown-trigger
  {:padding-left    12
   :padding-right   8
   :padding-top     5
   :padding-bottom  5
   :border-radius   (:border/size-32 borders/border-radius-values)
   :justify-content :center
   :align-items     :center
   :flex-direction  :row})

(defstyle dropdown-chevron
  {:margin-left 4})

(defstyle title-description-column
  {:justify-content :center
   :align-items     :flex-start})

(defstyle leading-media
  {:width         24
   :height        24
   :border-radius (:border/size-24 borders/border-radius-values)
   :margin-right  8})

(defstyle token-logo
  {:width         20
   :height        20
   :border-radius (:border/max borders/border-radius-values)
   :margin-right  6})

(defstyle network-logo
  {:width         18
   :height        18
   :border-radius (:border/max borders/border-radius-values)
   :margin-right  4})

(defstyle wallet-networks-container
  {:height          30
   :padding-left    8
   :padding-right   6
   :border-radius   (:border/size-32 borders/border-radius-values)
   :flex-direction  :row
   :align-items     :center
   :justify-content :center})

(defn wallet-networks-surface-style [theme background]
  (style {:background-color (if (or (= theme :theme/dark) (dark-background? background))
                              (colors/get-color :color/white-10)
                              (colors/get-color :color/neutral-10))}))

(defstyle placeholder-account-switcher
  {:width            32
   :height           32
   :border-radius    (:border/size-32 borders/border-radius-values)
   :background-color (colors/get-color :color/danger-50)})

(defstyle placeholder-channel-avatar
  {:width            20
   :height           20
   :background-color (colors/get-color :color/danger-50)})

(defstyle placeholder-dots
  {:width            18
   :height           12
   :background-color (colors/get-color :color/danger-50)})

(defn center-opacity-style [opacity]
  (when opacity
    (style {:opacity opacity})))
