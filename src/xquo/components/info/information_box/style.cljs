(ns xquo.components.info.information-box.style
  (:require [react-native.utils :refer [defstyle]]
            [xquo.foundations.borders :as borders]
            [xquo.foundations.colors :as colors]))

(defstyle container-base
  {:border-width  1
   :border-radius (borders/radius 40)})

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

(defstyle body-slot-base
  {:align-self :stretch})

(defn compact-layout-style [theme close-button]
  {:flex-direction     :row
   :align-items        (if (and (= theme :theme/dark) close-button)
                         :center
                         :flex-start)
   :gap                8
   :padding-horizontal 12
   :padding-vertical   11})

(defn container-color-style [theme blur? status color]
  (cond
    (= status :info)
    {:background-color (colors/get-color color 50 5)
     :border-color     (colors/get-color color 50 10)}

    (= status :success)
    {:background-color (colors/get-color :color/success 50 5)
     :border-color     (colors/get-color :color/success 50 10)}

    (= status :warning)
    {:background-color (colors/get-color :color/warning 50 5)
     :border-color     (colors/get-color :color/warning 50 10)}

    (= status :error)
    {:background-color (colors/get-color :color/danger 50 5)
     :border-color     (colors/get-color :color/danger 50 10)}

    (and (= theme :theme/dark) blur?)
    {:background-color (colors/get-color :color/white 5)
     :border-color     (colors/get-color :color/white 10)}

    (= theme :theme/dark)
    {:background-color (colors/get-color :color/neutral 80 40)
     :border-color     (colors/get-color :color/neutral 80)}

    :else
    {:background-color (colors/get-color :color/neutral 5)
     :border-color     (colors/get-color :color/neutral 20)}))

(defn leading-icon-style [close-button title button use-15-font?]
  (cond
    (and close-button title)
    {:padding-top 5}

    (and title use-15-font?)
    {:padding-top 1}

    close-button
    (if button
      {:padding-top 3}
      {:padding-top    3
       :padding-bottom 3})

    title
    {:padding-top 3}

    :else
    {:padding-top    1
     :padding-bottom 1}))

(defn close-button-style [title button]
  (if (or title button)
    {:padding-top    4
     :padding-bottom 3}
    {:padding-top    3
     :padding-bottom 3}))

(defn leading-icon-color [theme blur? status color]
  (cond
    (= status :info)
    (colors/get-color color
                      (if (= theme :theme/dark) 60 50))

    (= status :success)
    (colors/get-color :color/success
                      (if (= theme :theme/dark) 60 50))

    (= status :warning)
    (colors/get-color :color/warning
                      (if (= theme :theme/dark) 60 50))

    (= status :error)
    (colors/get-color :color/danger
                      (if (= theme :theme/dark) 60 50))

    (and (= theme :theme/dark) blur?)
    (colors/get-color :color/white 70)

    (= theme :theme/dark)
    (colors/get-color :color/neutral 40)

    :else
    (colors/get-color :color/neutral 50)))

(defn close-button-color [theme status]
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
            (colors/themed theme :color/danger)

            (= status :warning)
            (colors/themed theme :color/warning)

            (= theme :theme/dark)
            (colors/get-color :color/white 100)

            :else
            (colors/get-color :color/neutral 100))})

(defn body-text-style [theme blur? status title]
  {:color (cond
            (= status :error)
            (colors/get-color :color/danger
                              (if (= theme :theme/dark) 60 50))

            title
            (cond
              (and (= theme :theme/dark) blur?)
              (colors/get-color :color/white 40)

              (= theme :theme/dark)
              (colors/get-color :color/neutral 40)

              :else
              (colors/get-color :color/neutral 50))

            (= theme :theme/dark)
            (colors/get-color :color/white 100)

            :else
            (colors/get-color :color/neutral 100))})
