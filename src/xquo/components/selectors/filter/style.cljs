(ns xquo.components.selectors.filter.style
  (:require [react-native.utils :refer [style]]
            [xquo.foundations.colors :as colors]))

(def ^:private radius {24 8, 32 10})
(def icon-size {24 12, 32 20})
(def label-font {24 :font/medium-13, 32 :font/medium-15})
(def ^:private line-height {24 18, 32 22})

(defn container [size label disabled?]
  (style {:height          size
          :width           (when-not label size)
          :border-radius   (radius size)
          :border-width    1
          :padding-left    (when label 5)
          :padding-right   (when label (if (= size 24) 7 9))
          :flex-direction  :row
          :align-items     :center
          :justify-content :center
          :gap             4
          :opacity         (if disabled? 0.3 1)}))

(defn surface [{:keys [dark-theme? color]} blur? selected?]
  (let [border (cond
                 (and dark-theme? (or blur? selected?)) :color/white-10
                 dark-theme?                          :color/neutral-80
                 (and blur? selected?)                :color/neutral-80-20
                 (or blur? selected?)                 :color/neutral-80-10
                 :else                                :color/neutral-20)]
    (style {:background-color (if selected? (colors/get-color color (if dark-theme? 60 50)) "transparent")
            :border-color     (colors/get-color border)})))

(defn foreground [{:keys [dark-theme?]} selected?]
  (colors/get-color (if (or selected? dark-theme?) :color/white :color/neutral-100)))

(defn label [size theme-color selected?]
  (style {:line-height          (line-height size)
          :include-font-padding false
          :color                (foreground theme-color selected?)}))
