(ns xquo.community.token-gating.style
  (:require [react-native.utils :refer [defstyle style]]
            [xquo.foundations.colors :as colors]))

(defstyle container
  {:width      "100%"
   :align-self :center})

(defstyle heading-row
  {:flex-direction :row
   :align-items    :center})

(defstyle title
  {:flex                 1
   :height               22
   :include-font-padding false
   :text-align-vertical  :center})

(defstyle description
  {:height               18
   :include-font-padding false
   :text-align-vertical  :center})

(defstyle row
  {:flex-direction :row
   :align-items    :center
   :flex-wrap      :wrap
   :column-gap     8
   :row-gap        10})

(defstyle inline-prefix
  {:height               18
   :include-font-padding false
   :text-align-vertical  :center})

(defstyle button
  {:align-self :stretch})

(defstyle information
  {:padding-horizontal 20
   :padding-top        16
   :padding-bottom     12})

(defn root [variant dark-theme?]
  (let [card? (= variant :community)]
    (style {:width            (if card? 335 "100%")
            :max-width        "100%"
            :padding-top      (when card? 10)
            :border-radius    (when card? 16)
            :background-color (when card?
                                (colors/get-color (if dark-theme? :color/neutral-95 :color/white-100)))})))

(defn card-border [dark-theme?]
  (style {:position      :absolute
          :top           -1
          :bottom        -1
          :left          -1
          :right         -1
          :border-radius 17
          :border-width  1
          :border-color  (colors/get-color (if dark-theme? :color/neutral-80 :color/neutral-20))}))

(defn heading [variant index]
  (style {:padding-horizontal (if (= variant :community) 12 20)
          :padding-top        (cond
                                (= variant :channel-list) 8
                                (pos? index)              4
                                :else                     0)
          :padding-bottom     (if (= variant :community) 16 8)}))

(defn group [variant first? last?]
  (let [card? (= variant :community)]
    (style {:padding-horizontal (if card? 12 20)
            :padding-top        (cond
                                  card?  0
                                  first? 8
                                  :else  4)
            :padding-bottom     (cond
                                  (and card? first?)        12
                                  card?                     16
                                  (not last?)               16
                                  (= variant :channel-list) 8
                                  :else                     12)
            :row-gap            10})))

(defn separator-wrapper [variant]
  (style {:padding-horizontal (when-not (= variant :community) 20)}))

(defn separator [variant dark-theme?]
  (let [card? (= variant :community)]
    (style {:height             (if card? 34 28)
            :padding-horizontal (if card? 12 0)
            :padding-top        3
            :padding-bottom     (if card? 12 8)
            :border-top-width   1
            :border-top-color   (colors/get-color (if dark-theme? :color/neutral-90 :color/neutral-10))})))

(defn separator-text [dark-theme?]
  (style {:height               16
          :include-font-padding false
          :color                (colors/get-color (if dark-theme? :color/neutral-40 :color/neutral-50))}))

(defn action [variant]
  (style {:padding-horizontal (if (= variant :community) 12 20)
          :padding-top        (if (= variant :channel-list) 16 8)
          :padding-bottom     (if (= variant :channel-detail) 8 12)
          :gap                12}))

(defn footer [dark-theme?]
  (style {:padding-horizontal   20
          :text-align           :center
          :include-font-padding false
          :color                (colors/get-color (if dark-theme? :color/neutral-40 :color/neutral-50))}))
