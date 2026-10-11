(ns xquo.notification.toast.style
  (:require [react-native.utils :refer [defstyle style]]
            [xquo.foundations.colors :as colors]))

(defn row [button]
  (style {:flex-direction   :row
          :align-items      :flex-start
          :min-height       40
          :padding-left     10
          :padding-right    (if button 8 12)
          :padding-vertical (if button 8 10)
          :gap              12}))

(defn info [type button]
  (style {:flex             1
          :min-width        0
          :align-self       :center
          :flex-direction   :row
          :align-items      :flex-start
          :gap              4
          :padding-vertical (when (and button (= type :negative)) 2)}))

(defstyle message
  {:flex            1
   :min-width       0
   :min-height      20
   :justify-content :center})

(defn icon-color [{:keys [dark-theme?]} type]
  (case type
    :positive (colors/get-color :color/success (if dark-theme? 50 60))
    :negative (colors/get-color :color/danger (if dark-theme? 50 60))
    (colors/get-color (if dark-theme? :color/neutral-80-40 :color/white-40))))
