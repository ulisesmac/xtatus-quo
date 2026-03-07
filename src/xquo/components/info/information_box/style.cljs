(ns xquo.components.info.information-box.style
  (:require [reagent-extended-compiler.utils.transforms :refer [defstyle]]
            [xquo.foundations.borders :as borders]
            [xquo.foundations.colors :as colors]))

(defstyle container-base
  {:border-width  1
   :border-radius (:border/sizes-40-56 borders/border-radius-values)})

(defstyle rich-layout-base
  {:padding-left   12
   :padding-right  12
   :padding-top    10
   :padding-bottom 12})

(defstyle rich-row-base
  {:flex-direction :row
   :align-items    :flex-start
   :align-self     :stretch
   :gap            8})

(defstyle rich-content-base
  {:flex-direction :column
   :align-items    :flex-start
   :flex           1
   :min-width      1
   :gap            8})

(defstyle compact-body-base
  {:flex      1
   :min-width 1})

(defstyle text-column-base
  {:align-self :stretch})

(defn compact-layout-style [theme close-icon?]
  {:flex-direction     :row
   :align-items        (if (and (= theme :theme/dark) close-icon?)
                         :center
                         :flex-start)
   :gap                8
   :padding-horizontal 12
   :padding-vertical   11})

(defn container-color-style [theme background status color]
  (cond
    (= status :informative)
    {:background-color (colors/get-color color 50 5)
     :border-color     (colors/get-color color 50 10)}

    (= status :error)
    {:background-color (colors/get-color :color/danger 50 5)
     :border-color     (colors/get-color :color/danger 50 10)}

    (and (= theme :theme/dark) (= background :blur))
    {:background-color (colors/get-color :color/white 5)
     :border-color     (colors/get-color :color/white 10)}

    (= theme :theme/dark)
    {:background-color (colors/get-color :color/neutral 80 40)
     :border-color     (colors/get-color :color/neutral 80)}

    :else
    {:background-color (colors/get-color :color/neutral 5)
     :border-color     (colors/get-color :color/neutral 20)}))

(defn leading-icon-style [close-icon? title button-label]
  (cond
    (and close-icon? title)
    {:padding-top 5}

    close-icon?
    (if button-label
      {:padding-top 3}
      {:padding-top    3
       :padding-bottom 3})

    title
    {:padding-top 3}

    :else
    {:padding-top    1
     :padding-bottom 1}))

(defn close-icon-style [title button-label]
  (if (or title button-label)
    {:padding-top    4
     :padding-bottom 3}
    {:padding-top    3
     :padding-bottom 3}))

(defn leading-icon-color [theme background status color]
  (cond
    (= status :informative)
    (colors/get-color color
                      (if (= theme :theme/dark) 60 50))

    (= status :error)
    (colors/get-color :color/danger
                      (if (= theme :theme/dark) 60 50))

    (and (= theme :theme/dark) (= background :blur))
    (colors/get-color :color/white 70)

    (= theme :theme/dark)
    (colors/get-color :color/neutral 40)

    :else
    (colors/get-color :color/neutral 50)))

(defn close-icon-color [theme status]
  (cond
    (= status :error)
    (colors/get-color :color/danger
                      (if (= theme :theme/dark) 60 50))

    (= theme :theme/dark)
    (colors/get-color :color/white 100)

    :else
    (colors/get-color :color/neutral 100)))

(defn title-text-style [theme status]
  {:color (cond
            (= status :error)
            (colors/get-color :color/danger
                              (if (= theme :theme/dark) 60 50))

            (= theme :theme/dark)
            (colors/get-color :color/white 100)

            :else
            (colors/get-color :color/neutral 100))})

(defn body-text-style [theme background status title]
  {:color (cond
            (= status :error)
            (colors/get-color :color/danger
                              (if (= theme :theme/dark) 60 50))

            title
            (cond
              (and (= theme :theme/dark) (= background :blur))
              (colors/get-color :color/white 40)

              (= theme :theme/dark)
              (colors/get-color :color/neutral 40)

              :else
              (colors/get-color :color/neutral 50))

            (= theme :theme/dark)
            (colors/get-color :color/white 100)

            :else
            (colors/get-color :color/neutral 100))})
