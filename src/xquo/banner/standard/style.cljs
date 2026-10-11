(ns xquo.banner.standard.style
  (:require [react-native.utils :refer [defstyle style]]
            [xquo.foundations.animations :as animations]
            [xquo.foundations.colors :as colors]))

(defstyle container
  {:width              "100%"
   :height             40
   :flex-direction     :row
   :align-items        :center
   :padding-horizontal 20
   :padding-vertical   10
   :gap                8})

(defstyle title
  {:flex                 1
   :min-width            0
   :height               18
   :include-font-padding false
   :text-align-vertical  :center})

(defn state-style [color pressed?]
  (let [duration-key (if pressed? :pressed-duration :default-duration)
        easing-key   (if pressed? :pressed-timing-function :default-timing-function)]
    (style {:background-color           (colors/get-color color 50 (if pressed? 40 20))
            :transition-property        :backgroundColor
            :transition-duration        (get animations/press-feedback duration-key)
            :transition-timing-function (get animations/press-feedback easing-key)})))
