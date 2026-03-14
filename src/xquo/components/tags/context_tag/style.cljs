(ns xquo.components.tags.context-tag.style
  (:require-macros [reagent-extended-compiler.utils.transforms :refer [defstyle style]])
  (:require [xquo.foundations.borders :as borders]
            [xquo.foundations.colors :as colors]))

(def ^:private media-size
  {24 20
   32 28})

(def ^:private squircle-image-radius
  {24 (:border/size-16 borders/border-radius-values)
   32 (:border/size-24 borders/border-radius-values)})

(def ^:private squircle-container-radius
  {24 (:border/size-24 borders/border-radius-values)
   32 (:border/size-32 borders/border-radius-values)})

(def ^:private leading-layout
  {24 {:padding-left   2
       :padding-right  8
       :padding-top    2
       :padding-bottom 2
       :gap            4}
   32 {:padding-left   2
       :padding-right  12
       :padding-top    2
       :padding-bottom 2
       :gap            8}})

(def ^:private icon-layout
  {24 {:padding-left   8
       :padding-right  8
       :padding-top    3
       :padding-bottom 3
       :gap            4}
   32 {:padding-left   10
       :padding-right  12
       :padding-top    5
       :padding-bottom 5
       :gap            2}})

(defstyle root-base
  {:align-self :flex-start})

(defstyle label-row
  {:flex-direction :row
   :align-items    :center})

(defn title-text-style [dark-theme?]
  {:color (if dark-theme?
            (colors/get-color :color/white-100)
            (colors/get-color :color/neutral-100))})

(defn secondary-text-style [dark-theme? blur?]
  {:color (cond
            (and dark-theme? blur?) (colors/get-color :color/white-40)
            dark-theme?             (colors/get-color :color/neutral-40)
            blur?                   (colors/get-color :color/neutral-80-40)
            :else                   (colors/get-color :color/neutral-50))})

(defn- image-border-radius [size shape]
  (if (= shape :squircle)
    (get squircle-image-radius size)
    (/ (get media-size size) 2)))

(defn- container-border-radius [size type shape]
  (if (and (= type :image) (= shape :squircle))
    (get squircle-container-radius size)
    (/ size 2)))

(defn container [size type shape dark-theme? blur? state color]
  (let [layout        (if (= type :icon)
                        (get icon-layout size)
                        (get leading-layout size))
        border-radius (container-border-radius size type shape)]
    (style {:padding-left     (:padding-left layout)
            :padding-right    (:padding-right layout)
            :padding-top      (:padding-top layout)
            :padding-bottom   (:padding-bottom layout)
            :gap              (:gap layout)
            :height           size
            :flex-direction   :row
            :align-items      :center
            :background-color (cond
                                (and dark-theme? blur?) (colors/get-color :color/white-5)
                                dark-theme?             (colors/get-color :color/neutral-90)
                                blur?                   (colors/get-color :color/neutral-80-5)
                                :else                   (colors/get-color :color/neutral-10))
            :border-radius    border-radius
            :border-width     (when (= state :selected) 1)
            :border-color     (when (= state :selected)
                                (colors/get-color color 50))})))

(defn media-image [size type shape blur?]
  (style {:width         (get media-size size)
          :height        (get media-size size)
          :overflow      :hidden
          :border-radius (image-border-radius size shape)
          :border-width  (when (and (= type :image) (= shape :circle) (not blur?)) 1)
          :border-color  (when (and (= type :image) (= shape :circle) (not blur?))
                           (colors/get-color :color/neutral-80-5))}))

(defn squircle-surface [size color]
  (style {:width            (get media-size size)
          :height           (get media-size size)
          :overflow         :hidden
          :align-items      :center
          :justify-content  :center
          :border-radius    (get squircle-image-radius size)
          :background-color (colors/get-color color 50)}))

(defn filled-icon-surface [size color]
  (style {:width            (get media-size size)
          :height           (get media-size size)
          :align-items      :center
          :justify-content  :center
          :border-radius    (/ (get media-size size) 2)
          :background-color (colors/get-color color 50)}))

(defn emoji-style [size]
  (style {:font-size   (if (= size 24) 12 16)
          :line-height (if (= size 24) 12 16)}))

(defn scaled-icon [scale]
  (style {:transform [{:scale scale}]}))
