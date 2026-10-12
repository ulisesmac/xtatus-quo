(ns xquo.profile.link-card.style
  (:require [react-native.utils :refer [defstyle style]]
            [xquo.foundations.animations :as animations]
            [xquo.foundations.colors :as colors]))

(defstyle container
  {:height        88
   :border-radius 16
   :overflow      :hidden
   :padding       12
   :gap           4})

(defstyle backdrop
  {:position :absolute :top 0 :right 0 :bottom 0 :left 0})

(defn border [theme pressed?]
  (let [duration-key (if pressed? :pressed-duration :default-duration)
        easing-key   (if pressed? :pressed-timing-function :default-timing-function)
        color        (colors/themed theme :color/neutral-80 :color/white)]
    (style {:position                   :absolute
            :top                        0
            :right                      0
            :bottom                     0
            :left                       0
            :border-radius              16
            :border-width               1
            :border-color               (colors/get-color color 50 (if pressed? 20 5))
            :transition-property        :borderColor
            :transition-duration        (get animations/press-feedback duration-key)
            :transition-timing-function (get animations/press-feedback easing-key)})))

(defstyle top-row
  {:height 20 :flex-direction :row :align-items :center :gap 4})

(defstyle image
  {:width 20 :height 20})

(defn text [theme secondary?]
  (let [height (if secondary? 18 22)]
    (style {:height               height
            :line-height          height
            :include-font-padding false
            :text-align-vertical  :center
            :color                (if secondary?
                                    (colors/themed theme :color/neutral-50 :color/neutral-40)
                                    (colors/themed theme :color/neutral-100 :color/white))})))

(defn disabled [disabled?]
  (style {:opacity (if disabled? 0.3 1)}))
