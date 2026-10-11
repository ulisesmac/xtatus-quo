(ns xquo.audio.recorder.style
  (:require [react-native.utils :refer [defstyle style]]
            [xquo.foundations.colors :as colors]))

(def ^:private offsets
  {:default           [0 0]
   :locking           [-30 -30]
   :release-to-lock   [-56 -55.5]
   :locked            [0 0]
   :deleting          [-38 0]
   :release-to-delete [-76 0]
   :sending           [0 -38]
   :release-to-send   [0 -76]})

(defn container [playback?]
  (style {:width            375
          :max-width        "100%"
          :height           56
          :padding-vertical 12
          :padding-left     (if playback? 20 0)
          :padding-right    (if playback? 20 96)
          :flex-direction   :row
          :align-items      :center
          :gap              (if playback? 12 0)}))

(defstyle fill
  {:flex  1
   :width nil})

(defstyle playback-track
  {:flex               1
   :width              nil
   :padding-horizontal 0})

(defstyle playback
  {:flex           1
   :flex-direction :row
   :align-items    :center
   :gap            12})

(defstyle send
  {:position :absolute
   :right    20
   :top      -64})

(defstyle lock
  {:position :absolute
   :right    76
   :top      -44})

(defn recording-position [state]
  (let [[x y] (offsets state)]
    (style {:position :absolute
            :right    (- 8 x)
            :top      y
            :width    56
            :height   56})))

(defn wave [size color opacity]
  (style {:position      :absolute
          :left          (/ (- 56 size) 2)
          :top           (/ (- 56 size) 2)
          :width         size
          :height        size
          :border-radius (/ size 2)
          :border-width  1
          :border-color  color
          :opacity       opacity}))

(defstyle record-button
  {:width            56
   :height           56
   :border-radius    28
   :padding          0
   :overflow         :visible
   :background-color :transparent})

(defn recording-fill [theme color state]
  (cond
    (#{:locking :release-to-lock} state)
    (colors/themed theme :color/neutral-20 :color/neutral-80)

    (#{:deleting :release-to-delete} state)
    (colors/themed theme :color/danger)

    :else
    (colors/themed theme color)))

(defn circle [fill]
  (style {:position         :absolute
          :width            56
          :height           56
          :border-radius    28
          :background-color fill}))

(defn shape [left top]
  (style {:position :absolute
          :left     left
          :top      top}))
