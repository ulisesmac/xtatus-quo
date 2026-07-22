(ns xquo.components.tags.context-tag.style
  (:require-macros [react-native.utils :refer [defstyle style]])
  (:require [xquo.foundations.borders :as borders]
            [xquo.foundations.colors :as colors]))

(def ^:private media-size
  {24 20
   32 28})

(def ^:private squircle-image-radius
  {24 (borders/radius 16)
   32 (borders/radius 24)})

(def ^:private squircle-container-radius
  {24 (borders/radius 24)
   32 (borders/radius 32)})

(def ^:private multi-container-radius
  {24 (borders/radius 24)
   32 (borders/radius 32)})

(def ^:private squircle-icon-edge-padding 4)

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

(def ^:private multi-layout
  {24 {true  {:padding-left   3
              :padding-right  1
              :padding-top    1
              :padding-bottom 1
              :gap            4}
       false {:padding-left   1
              :padding-right  1
              :padding-top    1
              :padding-bottom 1
              :gap            0}}
   32 {true  {:padding-left   3
              :padding-right  1
              :padding-top    1
              :padding-bottom 1
              :gap            4}
       false {:padding-left   1
              :padding-right  1
              :padding-top    1
              :padding-bottom 1
              :gap            0}}})

(def ^:private multi-overlap
  {24 6
   32 10})

(defstyle root-base
  {:align-self :flex-start
   :position   :relative
   :overflow   :visible
   :min-width  1
   :flex-shrink 1})

(defstyle label-row
  {:flex-direction :row
   :align-items    :center
   :min-width      1
   :flex-shrink    1})

(defstyle label-primary-text
  {:min-width   1
   :flex-shrink 1})

(defstyle prefix-text
  {:min-width   1
   :flex-shrink 0})

(defstyle prefix-slot
  {:min-width   1
   :flex-shrink 0})

(defn prefix-separator [dark-theme? size]
  (style {:background-color (colors/get-color (if dark-theme? :color/neutral-60 :color/neutral-30))
          :height           size
          :width            1}))

(defstyle suffix-chevron-slot
  {:flex-shrink 0})

(defstyle suffix-slot
  {:min-width   1
   :flex-shrink 1})

(defstyle multi-stack-row
  {:flex-direction :row
   :align-items    :center})

(defn title-text-style [dark-theme?]
  {:color (if dark-theme?
            (colors/get-color :color/white-100)
            (colors/get-color :color/neutral-100))})

(defn prefix-text-style [dark-theme? blur?]
  {:color (cond
            blur?        (colors/get-color :color/white-70)
            dark-theme?  (colors/get-color :color/neutral-40)
            :else        (colors/get-color :color/neutral-50))})

(defn- container-background-color [border dark-theme? blur? embedded?]
  (cond
    (or embedded? (= border :outline)) "transparent"
    (and dark-theme? blur?)            (colors/get-color :color/white-5)
    dark-theme?                        (colors/get-color :color/neutral-90)
    blur?                              (colors/get-color :color/neutral-80-5)
    :else                              (colors/get-color :color/neutral-10)))

(defn secondary-text-style [dark-theme? blur?]
  {:color (cond
            (and dark-theme? blur?) (colors/get-color :color/white-70)
            dark-theme?             (colors/get-color :color/neutral-40)
            blur?                   (colors/get-color :color/neutral-80-70)
            :else                   (colors/get-color :color/neutral-50))})

(defn multi-content-style [dark-theme? blur?]
  {:color (cond
            (and dark-theme? blur?) (colors/get-color :color/white-80)
            dark-theme?             (colors/get-color :color/neutral-40)
            blur?                   (colors/get-color :color/neutral-80-40)
            :else                   (colors/get-color :color/neutral-50))})

(defn- resolved-media-size [size _selected?]
  (get media-size size))

(defn- image-border-radius [size shape _selected?]
  (if (= shape :squircle)
    (get squircle-image-radius size)
    (/ (get media-size size) 2)))

(defn- container-border-radius [size type shape]
  (cond
    (= type :multi)
    (if (= shape :squircle)
      (get multi-container-radius size)
      (/ size 2))

    (and (= type :image) (= shape :squircle))
    (get squircle-container-radius size)

    (and (= type :icon) (= shape :squircle))
    (get squircle-container-radius size)

    :else
    (/ size 2)))

(defn container [size type shape border dark-theme? blur? embedded? icon label prefix prefix-icon suffix]
  (let [prefix-content? (or prefix prefix-icon)
        icon-only?      (and (= type :icon) (nil? label) (nil? prefix-content?))
        layout          (cond
                          (= type :icon)  (get icon-layout size)
                          (= type :multi) (get-in multi-layout [size (some? icon)])
                          :else           (get leading-layout size))
        padding-right   (cond
                          embedded?                  0
                          (and (= type :icon) (= shape :squircle) label (nil? suffix))
                          squircle-icon-edge-padding
                          (and (= shape :squircle) suffix) squircle-icon-edge-padding
                          (and (= type :multi) label) (:padding-right (get leading-layout size))
                          :else                      (:padding-right layout))
        padding-left    (cond
                          (and (= shape :squircle) prefix-icon) squircle-icon-edge-padding
                          prefix-content?     padding-right
                          (= border :outline) (dec (:padding-left layout))
                          :else               (:padding-left layout))
        gap             (cond
                          (and (= shape :squircle) prefix-icon) squircle-icon-edge-padding
                          :else                                 (:gap layout))
        padding-top     (if (and (= border :outline) (= type :icon))
                          (dec (:padding-top layout))
                          (:padding-top layout))
        padding-bottom  (if (and (= border :outline) (= type :icon))
                          (dec (:padding-bottom layout))
                          (:padding-bottom layout))
        border-radius   (container-border-radius size type shape)]
    (if icon-only?
      (style {:width            size
              :height           size
              :padding-left     0
              :padding-right    0
              :padding-top      0
              :padding-bottom   0
              :gap              0
              :flex-direction   :row
              :align-items      :center
              :justify-content  :center
              :background-color (container-background-color border dark-theme? blur? embedded?)
              :border-radius    border-radius})
      (style {:padding-left     padding-left
              :padding-right    padding-right
              :padding-top      padding-top
              :padding-bottom   padding-bottom
              :gap              gap
              :height           size
              :flex-direction   :row
              :align-items      :center
              :background-color (container-background-color border dark-theme? blur? embedded?)
              :border-radius    border-radius}))))

(defn outline-border [size type shape theme color blur? selected? pressed?]
  (style {:border-width  1
          :border-color  (cond
                           selected? (colors/get-color color 50)
                           blur?     (colors/themed theme :color/neutral-80-5 :color/white-10)
                           :else     (colors/themed theme
                                                    (if pressed? :color/neutral-30 :color/neutral-20)
                                                    (if pressed? :color/neutral-60 :color/neutral-80)))
          :border-radius (container-border-radius size type shape)}))

(defn selected-border [size type shape color]
  (style {:position      :absolute
          :top           -1
          :right         -1
          :bottom        -1
          :left          -1
          :border-radius (+ (container-border-radius size type shape) 1)
          :border-width  1
          :border-color  (colors/get-color color 50)}))

(defn media-frame [size shape selected?]
  (style {:width         (resolved-media-size size selected?)
          :height        (resolved-media-size size selected?)
          :overflow      :hidden
          :position      :relative
          :border-radius (image-border-radius size shape selected?)}))

(defn media-image [size selected?]
  (style {:width  (resolved-media-size size selected?)
          :height (resolved-media-size size selected?)}))

(defn media-border [size shape type blur? selected?]
  (when (and (= type :image) (= shape :circle) (not blur?))
    (style {:position      :absolute
            :top           0
            :right         0
            :bottom        0
            :left          0
            :border-radius (image-border-radius size shape selected?)
            :border-width  1
            :border-color  (colors/get-color :color/neutral-80-5)})))

(defn- multi-stack-item-size [size]
  (+ (get media-size size) 2))

(defn multi-stack-item-surface [size shape border dark-theme? blur?]
  (style {:width         (multi-stack-item-size size)
          :height        (multi-stack-item-size size)
          :overflow      :hidden
          :border-width  1
          :border-color  (if (= border :outline)
                           "transparent"
                           (container-background-color nil dark-theme? blur? false))
          :border-radius (container-border-radius size :multi shape)}))

(defn squircle-surface [size color]
  (style {:width            (get media-size size)
          :height           (get media-size size)
          :overflow         :hidden
          :align-items      :center
          :justify-content  :center
          :border-radius    (get squircle-image-radius size)
          :background-color (colors/get-color color 50 20)}))

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

(defn multi-item-slot [size slot-index]
  (style {:margin-left (when (pos? slot-index)
                         (- (get multi-overlap size)))}))

(defn multi-count-surface [_size _shape dark-theme? blur?]
  (style {:align-items      :center
          :justify-content  :center
          :background-color (cond
                              (and dark-theme? blur?) (colors/get-color :color/white-10)
                              dark-theme? (colors/get-color :color/neutral-80)
                              blur?                   (colors/get-color :color/neutral-80-10)
                              :else                   (colors/get-color :color/neutral-20))}))

(defn scaled-icon [scale]
  (style {:transform [{:scale scale}]}))
