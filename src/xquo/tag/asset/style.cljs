(ns xquo.tag.asset.style
  (:require [react-native.utils :refer [defstyle style]]
            [xquo.foundations.colors :as colors]))

(def media-size
  {24 20
   32 28})

(def label-font
  {:token       {24 :font/medium-13 32 :font/medium-13}
   :collectible {24 :font/medium-13 32 :font/medium-15}})

(def label-height
  {:token       {24 18 32 18}
   :collectible {24 18 32 22}})

(def badge-offset
  {24 {:right -8 :top -8}
   32 {:right -6 :top -6}})

(def ^:private spacing
  {24 6
   32 8})

(def ^:private right-padding
  {24 10
   32 12})

(def ^:private image-radius
  {24 6
   32 8})

(def ^:private corner-radius
  {:token       {24 12 32 16}
   :collectible {24 8 32 10}})

(defstyle container
  {:align-self :flex-start
   :max-width  "100%"})

(defstyle content
  {:flex-direction :row
   :align-items    :center
   :padding-left   2
   :padding-top    2
   :padding-bottom 2})

(defstyle background
  {:position :absolute
   :left     -1
   :top      -1})

(defstyle badge
  {:position :absolute
   :width    20
   :height   20})

(defn background-color [dark-theme? blur?]
  (colors/get-color (cond
                      (and dark-theme? blur?) :color/white-5
                      dark-theme?             :color/neutral-90
                      blur?                   :color/neutral-80-5
                      :else                   :color/neutral-10)))

(defn content-style [size]
  (style {:height        size
          :gap           (spacing size)
          :padding-right (right-padding size)}))

(defn source-opacity [variant size dark-theme? blur?]
  (style {:opacity (if (and (= variant :collectible) (= size 24) dark-theme? (not blur?)) 0.8 1)}))

(defn image [variant size]
  (let [diameter (media-size size)]
    (style {:width         diameter
            :height        diameter
            :border-radius (if (= variant :token) (/ diameter 2) (image-radius size))})))

(defn title [variant size]
  (style {:height               (get-in label-height [variant size])
          :flex-shrink          1
          :include-font-padding false
          :text-align-vertical  :center}))

(defn shape-path [variant size option width]
  (let [radius  (if (and (= variant :collectible) option) 8 (get-in corner-radius [variant size]))
        right-x (if (and (= variant :token) option (= size 32)) 14 radius)]
    (str "M"
         radius
         " 0H"
         (- width right-x)
         "A"
         right-x
         " "
         radius
         " 0 0 1 "
         width
         " "
         radius
         "V"
         (- size radius)
         "A"
         right-x
         " "
         radius
         " 0 0 1 "
         (- width right-x)
         " "
         size
         "H"
         radius
         "A"
         radius
         " "
         radius
         " 0 0 1 0 "
         (- size radius)
         "V"
         radius
         "A"
         radius
         " "
         radius
         " 0 0 1 "
         radius
         " 0Z")))
