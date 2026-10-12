(ns xquo.navigation.page-nav.style
  (:require [react-native.utils :refer [defstyle style]]
            [xquo.foundations.animations :as animations]
            [xquo.foundations.colors :as colors]))

(defstyle container
  {:width "100%"})

(defstyle row
  {:height             56
   :padding-horizontal 20
   :flex-direction     :row
   :align-items        :center
   :gap                12})

(defstyle actions
  {:flex-direction :row
   :align-items    :center
   :gap            12})

(defstyle text-column
  {:flex-shrink 1 :min-width 0})

(defstyle title-row
  {:flex-direction :row :align-items :center :gap 4})

(defstyle icons
  {:flex-direction :row :align-items :center :gap 2})

(defn side [width right?]
  (style {:width       width
          :flex-shrink 0
          :align-items (if right? "flex-end" "flex-start")}))

(defn center [centered?]
  (style {:flex            1
          :min-width       0
          :flex-direction  :row
          :justify-content (if centered? "center" "flex-start")
          :align-items     :center}))

(defn content [size disabled?]
  (style {:max-width      "100%"
          :flex-direction :row
          :align-items    :center
          :gap            (if (< size 24) 4 8)
          :opacity        (if disabled? 0.3 1)}))

(defn image [size]
  (style {:width size :height size}))

(defn title-color [theme pressed?]
  (if pressed?
    (colors/themed theme :color/neutral-80-70 :color/white-70)
    (colors/themed theme :color/neutral-100 :color/white-100)))

(defn secondary [theme blur? photo?]
  (cond
    photo? (colors/themed theme :color/neutral-80-80 :color/white-70)
    blur?  (colors/themed theme :color/neutral-80-50 :color/white-40)
    :else  (colors/themed theme :color/neutral-50 :color/neutral-40)))

(defn icon-color [theme blur?]
  (if blur?
    (colors/themed theme :color/neutral-80-40 :color/white-40)
    (colors/themed theme :color/neutral-50 :color/neutral-40)))

(defn text [color height pressed?]
  (let [duration-key (if pressed? :pressed-duration :default-duration)
        easing-key   (if pressed? :pressed-timing-function :default-timing-function)]
    (style {:color                      color
            :height                     height
            :line-height                height
            :include-font-padding       false
            :text-align-vertical        :center
            :flex-shrink                1
            :transition-property        :color
            :transition-duration        (get animations/press-feedback duration-key)
            :transition-timing-function (get animations/press-feedback easing-key)})))
