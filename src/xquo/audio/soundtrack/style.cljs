(ns xquo.audio.soundtrack.style
  (:require [react-native.utils :refer [defstyle style]]
            [xquo.foundations.colors :as colors]))

(defn container [time state]
  (style {:width              (cond
                                (nil? time)          164
                                (= state :listening) 249
                                :else                263)
          :height             32
          :flex-direction     :row
          :align-items        :center
          :gap                12
          :padding-horizontal (if time 20 0)}))

(defstyle timer
  {:flex-direction :row
   :align-items    :center
   :gap            2})

(defstyle indicator-slot
  {:width           12
   :height          12
   :align-items     :center
   :justify-content :center})

(defn indicator [theme]
  (style {:width            8
          :height           8
          :border-radius    4
          :background-color (colors/themed theme :color/danger)}))

(defn time-label [theme state]
  (style {:width                33
          :text-align           :center
          :color                (if (= state :deleting)
                                  (colors/themed theme :color/danger)
                                  (colors/themed theme :color/neutral-100 :color/white-100))
          :include-font-padding false}))

(defstyle track-container
  {:flex            1
   :height          32
   :justify-content :center})

(defn track [theme track-color]
  (style {:height           4
          :border-radius    2
          :background-color (or track-color
                                (colors/themed theme :color/neutral-20 :color/neutral-80))}))

(defn progress [progress color listening?]
  (style {:position         :absolute
          :top              14
          :left             0
          :height           4
          :width            (str (* 100 progress) "%")
          :min-width        (if listening? 0 4)
          :border-radius    2
          :background-color color}))

(defn thumb [progress color]
  (style {:position         :absolute
          :top              10
          :left             (str (* 100 progress) "%")
          :margin-left      -6
          :width            12
          :height           12
          :border-radius    6
          :background-color color}))
