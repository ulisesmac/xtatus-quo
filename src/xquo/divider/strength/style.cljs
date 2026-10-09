(ns xquo.divider.strength.style
  (:require [react-native.utils :refer [defstyle]]
            [xquo.divider.style :as divider-style]
            [xquo.foundations.colors :as colors]))

(def progress
  {:very-weak   0.2
   :weak        0.4
   :okay        0.6
   :strong      0.8
   :very-strong 1})

(def ^:private status-colors
  {:informative {:color :color/white :level 50}
   :alert       {:color :color/danger :level 60}
   :very-weak   {:color :color/danger :level 60}
   :weak        {:color :color/warning :level 60}
   :okay        {:color :color/warning :level 50}
   :strong      {:color :color/success :level 60}
   :very-strong {:color :color/success :level 60}})

(defstyle container
  {:width          "100%"
   :height         42
   :padding-left   20
   :padding-right  20
   :flex-direction :row
   :align-items    :center
   :column-gap     4})

(defstyle informative-container
  {:padding-right 36})

(defstyle title
  {:flex 1})

(defn foreground [status]
  (let [{:keys [color level]} (status-colors status)]
    (if (= status :informative)
      (colors/get-color :color/white-40)
      (colors/get-color color level))))

(defn track-color [status]
  (let [{:keys [color]} (status-colors status)]
    (colors/get-color color 50 20)))

(defn background [status]
  (let [{:keys [color level]} (status-colors status)]
    (divider-style/gradient color level)))
