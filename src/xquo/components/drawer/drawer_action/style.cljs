(ns xquo.components.drawer.drawer-action.style
  (:require [reagent-extended-compiler.utils.transforms :refer [defstyle]]
            [xquo.foundations.animations :as animations]
            [xquo.foundations.borders :as borders]
            [xquo.foundations.colors :as colors]))

(defstyle container-base
  {:border-radius (:border/sizes-40-56 borders/border-radius-values)})

(defstyle row-base
  {:flex-direction :row
   :align-items    :center})

(defstyle gap-12
  {:gap 12})

(defstyle padding-default
  {:padding-horizontal 12
   :padding-vertical   13})

(defstyle padding-description
  {:padding-horizontal 12
   :padding-top        8
   :padding-bottom     8})

(defstyle content-base
  {:flex           1
   :min-width      1
   :flex-direction :column
   :align-items    :flex-start})

(defstyle content-gap-2
  {:gap 2})

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

(defstyle title-row
  {:align-self :stretch})

(defstyle arrow-slot-default-state-style
  {:transform                  [{:translate-x 0}]
   :transition-property        (:transition-property animations/press-feedback)
   :transition-duration        (:default-duration animations/press-feedback)
   :transition-timing-function (:default-timing-function animations/press-feedback)})

(defstyle arrow-slot-pressed-state-style
  {:transform                  [{:translate-x 6}]
   :transition-property        (:transition-property animations/press-feedback)
   :transition-duration        (:pressed-duration animations/press-feedback)
   :transition-timing-function (:pressed-timing-function animations/press-feedback)})

(defn container-color-style [background pressed? selected? danger? color]
  (cond
    (and (or selected? pressed?) danger?)
    {:background-color (colors/get-color :color/danger 50 5)}

    (and (or selected? pressed?) color)
    {:background-color (colors/get-color color 50 5)}

    (and (or selected? pressed?) (= background :blur))
    {:background-color (colors/get-color :color/white 5)}

    (or selected? pressed?)
    {:background-color (colors/get-color :color/primary 50 5)}))

(defn title-text-style [theme background danger? color]
  (let [action-color (if danger? :color/danger color)]
    {:color
     (cond
       (and action-color (= theme :theme/dark) (not= background :blur))
       (colors/get-color action-color 60)

       action-color
       (colors/get-color action-color 50)

       (= theme :theme/light)
       (colors/get-color :color/neutral 100)

       :else
       (colors/get-color :color/white 100))}))

(defn description-text-style [theme background]
  {:color
   (cond
     (= theme :theme/light) (colors/get-color :color/neutral 50)
     (= background :blur)   (colors/get-color :color/white 40)
     :else                  (colors/get-color :color/neutral 40))})

(defn icon-color [theme background danger? color]
  (let [action-color (if danger? :color/danger color)]
    (cond
      action-color
      (:color (title-text-style theme background danger? color))

      (= theme :theme/light)
      (colors/get-color :color/neutral 50)

      (= background :blur)
      (colors/get-color :color/white 70)

      :else
      (colors/get-color :color/neutral 40))))

(defn trailing-icon-color [theme background danger?]
  (cond
    (and danger? (= theme :theme/dark) (not= background :blur))
    (colors/get-color :color/danger 60)

    danger?
    (colors/get-color :color/danger 50)

    (= theme :theme/light)
    (colors/get-color :color/neutral 50)

    (= background :blur)
    (colors/get-color :color/white 70)

    :else
    (colors/get-color :color/neutral 40)))
