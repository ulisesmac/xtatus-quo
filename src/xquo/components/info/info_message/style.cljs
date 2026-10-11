(ns xquo.components.info.info-message.style
  (:require [react-native.utils :refer [style]]
            [xquo.foundations.colors :as colors]))

(def legacy-size {:default 13 :tiny 11})
(def font {11 :font/regular-11 13 :font/regular-13})
(def icon-size {11 12 13 16})
(def ^:private line-height {11 16 13 18})
(def ^:private icon-padding {11 2 13 1})
(def ^:private status-color
  {:success :color/success :error :color/danger :warning :color/orange})

(defn container [size]
  (style {:flex-direction :row
          :align-items    (if (= size 11) "center" "flex-start")
          :gap            4}))

(defn icon-slot [size]
  (style {:align-self       :stretch
          :padding-vertical (icon-padding size)}))

(defn text-style [size color]
  (style {:flex                 1
          :min-width            0
          :include-font-padding false
          :line-height          (line-height size)
          :color                color}))

(defn message-color [{:keys [theme dark-theme?]} blur? status]
  (if-let [color (status-color status)]
    (colors/get-color color (if dark-theme? 60 50))
    (if blur?
      (colors/themed theme :color/neutral-80-40 :color/white-40)
      (colors/themed theme :color/neutral-50 :color/neutral-40))))
