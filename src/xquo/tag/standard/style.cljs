(ns xquo.tag.standard.style
  (:require [react-native.utils :refer [defstyle style]]
            [xquo.foundations.colors :as colors]))

(def font
  {24 :font/medium-13
   32 :font/medium-15})

(def media-size
  {24 12
   32 20})

(defn container [size type label]
  (let [large? (= size 32)
        left   (cond
                 (nil? label) 0
                 (not large?) 7
                 type         9
                 :else        11)
        right  (cond
                 (nil? label) 0
                 large?       11
                 :else        7)]
    (style {:height          size
            :width           (when-not label size)
            :align-self      :flex-start
            :flex-direction  :row
            :align-items     :center
            :justify-content :center
            :padding-left    left
            :padding-right   right
            :gap             (if (and large? (= type :icon)) 2 4)
            :border-width    1
            :border-radius   20})))

(defn state-style [{:keys [theme dark-theme?]} color blur? selected? pressed? disabled?]
  (let [border-color (cond
                       selected?
                       (colors/themed theme color)

                       (and blur? dark-theme? pressed?)
                       (colors/get-color :color/white-20)

                       (and blur? dark-theme?)
                       (colors/get-color :color/white-10)

                       (and blur? pressed?)
                       (colors/get-color :color/neutral-80-10)

                       blur?
                       (colors/get-color :color/neutral-80-5)

                       (and dark-theme? pressed?)
                       (colors/get-color :color/neutral-60)

                       dark-theme?
                       (colors/get-color :color/neutral-80)

                       pressed?
                       (colors/get-color :color/neutral-30)

                       :else
                       (colors/get-color :color/neutral-20))]
    (style {:border-color border-color
            :opacity      (if disabled? 0.3 1)})))

(defn icon-color [theme blur?]
  (if blur?
    (colors/themed theme :color/neutral-80-70 :color/white-70)
    (colors/themed theme :color/neutral-50 :color/neutral-40)))

(defn media [size]
  (let [edge (media-size size)]
    (style {:width  edge
            :height edge})))

(defn emoji [size]
  (let [edge (media-size size)]
    (style {:width                edge
            :height               edge
            :text-align           :center
            :font-size            edge
            :line-height          edge
            :include-font-padding false})))

(defstyle label
  {:include-font-padding false})
