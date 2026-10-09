(ns xquo.shell.option-card.style
  (:require [react-native.utils :refer [defstyle style]]
            [xquo.foundations.animations :as animations]
            [xquo.foundations.colors :as colors]))

(defstyle small-container
  {:align-self         :stretch
   :flex-direction     :row
   :align-items        :center
   :gap                8
   :padding-horizontal 12
   :padding-vertical   8
   :border-radius      16})

(defn state-style [pressed? disabled?]
  (let [duration-key (if pressed? :pressed-duration :default-duration)
        easing-key   (if pressed? :pressed-timing-function :default-timing-function)]
    (style {:background-color           (colors/get-color (if pressed? :color/white-10 :color/white-5))
            :opacity                    (if disabled? 0.3 1)
            :transition-property        :backgroundColor
            :transition-duration        (get animations/press-feedback duration-key)
            :transition-timing-function (get animations/press-feedback easing-key)})))

(defstyle small-image
  {:width 32 :height 32})

(defstyle content
  {:flex 1 :min-width 0})

(defstyle title
  {:color (colors/get-color :color/white-100)})

(defstyle description
  {:color (colors/get-color :color/white-70)})

(defstyle big-container
  {:align-self       :stretch
   :border-radius    16
   :background-color (colors/get-color :color/white-5)})

(defstyle top
  {:padding-horizontal 12
   :padding-top        10
   :padding-bottom     12})

(defstyle big-image
  {:width "100%" :height 204})

(defstyle actions
  {:padding 12})

(defstyle button
  {:align-self :stretch})
