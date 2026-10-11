(ns xquo.calendar.picker.style
  (:require [react-native.utils :refer [defstyle style]]
            [xquo.foundations.colors :as colors]))

(defstyle container
  {:width      335
   :max-width  "100%"
   :align-self :center})

(defstyle content
  {:flex-direction :row
   :border-radius  12
   :overflow       :hidden})

(defstyle years
  {:width       64
   :flex-shrink 0})

(defstyle year-content
  {:flex-grow          1
   :justify-content    :flex-end
   :padding-horizontal 8
   :padding-bottom     8
   :gap                4})

(defn year-frame [height]
  (style {:height height}))

(defstyle scroll
  {:flex 1})

(defstyle sidebar-line
  {:position :absolute
   :top      0
   :bottom   0
   :right    0})

(defstyle month
  {:flex      1
   :min-width 0})

(defstyle header
  {:height          48
   :padding         12
   :flex-direction  :row
   :align-items     :center
   :justify-content :space-between
   :gap             4})

(defstyle month-title
  {:flex                 1
   :height               22
   :text-align           :center
   :text-align-vertical  :center
   :include-font-padding false})

(defstyle days
  {:padding-horizontal 8
   :padding-bottom     8
   :gap                4})

(defstyle row
  {:height         32
   :flex-direction :row})

(defstyle cell
  {:flex 1})

(defstyle last-cell
  {:width 32})

(defn content-style [dark-theme?]
  (style {:background-color (colors/get-color (if dark-theme? :color/neutral-80-40 :color/white-100))}))

(defn border [dark-theme?]
  (let [offset (if dark-theme? 0 -1)]
    (style {:position      :absolute
            :top           offset
            :left          offset
            :right         offset
            :bottom        offset
            :border-width  1
            :border-radius (if dark-theme? 12 13)
            :border-color  (colors/get-color (if dark-theme? :color/neutral-80 :color/neutral-20))})))

(defn year-gradient [dark-theme?]
  (let [color (if dark-theme? :color/neutral :color/white)
        level (if dark-theme? 95 50)]
    (style {:position         :absolute
            :left             0
            :top              0
            :width            63
            :height           80
            :background-image (str "linear-gradient(180deg, "
                                   (colors/get-color color level 100)
                                   " 0%, "
                                   (colors/get-color color level 0)
                                   " 100%)")})))

(defn weekday [dark-theme?]
  (style {:width                32
          :height               32
          :text-align           :center
          :text-align-vertical  :center
          :include-font-padding false
          :color                (colors/get-color (if dark-theme? :color/neutral-40 :color/neutral-50))}))

(defn range-fill [dark-theme? start-index end-index]
  (style {:position         :absolute
          :top              0
          :height           32
          :left             (if start-index (str (/ (* start-index 100) 6) "%") 0)
          :margin-left      (when start-index (- 16 (/ (* start-index 32) 6)))
          :right            (if end-index (str (/ (* (- 6 end-index) 100) 6) "%") 0)
          :margin-right     (when end-index (- (/ (* end-index 32) 6) 16))
          :background-color (colors/get-color (if dark-theme? :color/neutral-80 :color/neutral-5))}))
