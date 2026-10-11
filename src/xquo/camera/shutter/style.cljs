(ns xquo.camera.shutter.style
  (:require [react-native.utils :refer [defstyle style]]
            [xquo.foundations.animations :as animations]
            [xquo.foundations.colors :as colors]))

(defstyle container
  {:width           56
   :height          56
   :align-items     :center
   :justify-content :center})

(defstyle ring
  {:position      :absolute
   :top           0
   :right         0
   :bottom        0
   :left          0
   :border-radius 28
   :border-width  2
   :border-color  (colors/get-color :color/white-30)})

(defn circle [color action pressed?]
  (let [size         (if (and (= action :shutter) pressed?) 40 48)
        duration-key (if pressed? :pressed-duration :default-duration)
        easing-key   (if pressed? :pressed-timing-function :default-timing-function)]
    (style {:width                      size
            :height                     size
            :border-radius              24
            :align-items                :center
            :justify-content            :center
            :background-color           (if (= action :confirm)
                                          (colors/get-color color (if pressed? 70 50))
                                          (colors/get-color :color/white-100))
            :box-shadow                 [{:offset-x    0
                                          :offset-y    4
                                          :blur-radius 20
                                          :color       (colors/get-color :color/neutral-100-8)}]
            :transition-property        [:width :height :backgroundColor]
            :transition-duration        (get animations/press-feedback duration-key)
            :transition-timing-function (get animations/press-feedback easing-key)})))

(defn disabled [disabled?]
  (style {:opacity (if disabled? 0.3 1)}))
