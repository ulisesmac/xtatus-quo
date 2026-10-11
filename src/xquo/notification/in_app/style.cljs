(ns xquo.notification.in-app.style
  (:require [react-native.utils :refer [defstyle style]]
            [xquo.foundations.colors :as colors]))

(defn row [description attachment footer]
  (style {:flex-direction     :row
          :align-items        (if (or description attachment footer) "flex-start" "center")
          :padding-top        8
          :padding-bottom     (cond
                                footer                      12
                                (or description attachment) 10
                                :else                       8)
          :padding-horizontal 12
          :gap                8}))

(defn leading [details]
  (style {:margin-top (when details 4)}))

(defstyle content
  {:flex      1
   :min-width 0})

(defstyle footer
  {:margin-top     8
   :flex-direction :row
   :flex-wrap      :wrap
   :gap            8})

(defn attachment [type]
  (style {:margin-top     (if (= type :sticker) 4 8)
          :flex-direction :row
          :gap            8}))

(defn image [type]
  (style {:width         40
          :height        40
          :border-radius (when (not= type :sticker) 10)}))

(defn icon-background [{:keys [dark-theme?]}]
  (style {:width            32
          :height           32
          :border-radius    16
          :align-items      :center
          :justify-content  :center
          :background-color (colors/get-color (if dark-theme? :color/white-40 :color/neutral-80-20))}))

(defn icon-color [{:keys [dark-theme?]}]
  (colors/get-color (if dark-theme? :color/neutral-80-70 :color/white-70)))
