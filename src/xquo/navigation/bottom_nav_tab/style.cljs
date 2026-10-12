(ns xquo.navigation.bottom-nav-tab.style
  (:require [react-native.utils :refer [defstyle style]]
            [xquo.foundations.animations :as animations]
            [xquo.foundations.colors :as colors]))

(def counter-inset {:default 2 :large 0})
(def counter-radius {:default 6 :large 8})

(defstyle container
  {:width           90
   :height          40
   :border-radius   10
   :align-items     :center
   :justify-content :center})

(defstyle icon
  {:width 24 :height 24 :align-items :center :justify-content :center})

(defstyle badge
  {:position        :absolute
   :left            12
   :top             -8
   :min-width       20
   :height          20
   :align-items     :center
   :justify-content :center})

(defn appearance [theme pressed? disabled?]
  (let [duration-key (if pressed? :pressed-duration :default-duration)
        easing-key   (if pressed? :pressed-timing-function :default-timing-function)]
    (style {:background-color           (if pressed?
                                          (colors/themed theme :color/neutral-80-5 :color/white-5)
                                          "transparent")
            :opacity                    (if disabled? 0.3 1)
            :transition-property        :backgroundColor
            :transition-duration        (get animations/press-feedback duration-key)
            :transition-timing-function (get animations/press-feedback easing-key)})))

(defn icon-color [theme active?]
  (if active?
    (colors/themed theme :color/neutral-100 :color/white-100)
    (colors/themed theme :color/neutral-80-40 :color/white-40)))

(defn notification [{:keys [color dark-theme?]} pressed?]
  (style {:width            8
          :height           8
          :border-radius    4
          :background-color (colors/get-color color (if (and dark-theme? (not pressed?)) 60 50))}))
