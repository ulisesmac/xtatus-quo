(ns xquo.components.avatar.style
  (:require [react-native.utils :refer [defstyle style]]
            [xquo.foundations.animations :as animations]
            [xquo.foundations.colors :as colors]))

(defn frame [size]
  (style {:width       size
          :height      size
          :flex-shrink 0}))

(defstyle content
  {:position        :absolute
   :top             0
   :right           0
   :bottom          0
   :left            0
   :align-items     :center
   :justify-content :center})

(defn press-state [pressed?]
  (let [scale-key    (if pressed? :pressed-scale :default-scale)
        duration-key (if pressed? :pressed-duration :default-duration)
        easing-key   (if pressed? :pressed-timing-function :default-timing-function)]
    (style {:transform                  [{:scale (get animations/press-feedback scale-key)}]
            :transition-property        :transform
            :transition-duration        (get animations/press-feedback duration-key)
            :transition-timing-function (get animations/press-feedback easing-key)})))

(defn press-overlay [pressed?]
  (let [duration-key (if pressed? :pressed-duration :default-duration)
        easing-key   (if pressed? :pressed-timing-function :default-timing-function)]
    (style {:opacity                    (if pressed? 1 0)
            :transition-property        :opacity
            :transition-duration        (get animations/press-feedback duration-key)
            :transition-timing-function (get animations/press-feedback easing-key)})))

(def ^:private soft-fill-opacity
  {:circle 20 :rounded-square 5 :hexagon 20})

(defn fill-color [theme blur? color variant shape content-type]
  (cond
    (nil? variant)
    "transparent"

    (= variant :solid)
    (colors/themed theme color)

    (and blur? (= color :color/neutral))
    "transparent"

    (and (= color :color/neutral)
         (= content-type :icon))
    (colors/themed theme :color/white-100 :color/neutral-95)

    (= color :color/neutral)
    (colors/themed theme :color/white-100 :color/neutral-90)

    (and (= shape :circle)
         (#{:emoji :emoji-image} content-type))
    (colors/get-color color 50 10)

    :else
    (colors/get-color color 50 (soft-fill-opacity shape))))

(defn foreground-color [theme color variant]
  (cond
    (= variant :solid)
    (colors/get-color :color/white-70)

    (= color :color/neutral)
    (colors/themed theme :color/neutral-100 :color/white-100)

    :else
    (colors/themed theme color)))

(defn border-color [theme blur? border content color]
  (cond
    blur?
    (colors/themed theme :color/neutral-80-5 :color/white-5)

    (nil? content)
    (colors/themed theme :color/neutral-10 :color/neutral-80)

    (= border :dashed)
    (colors/themed theme :color/neutral-30 :color/neutral-80)

    (and (= border :solid)
         (= color :color/neutral)
         (= (:type content) :icon))
    (colors/themed theme :color/neutral-20 :color/neutral-80)

    :else
    (colors/themed theme :color/neutral-80-5 :color/white-5)))

(def corner-radius
  {16 4, 20 6, 24 8, 28 8, 32 10, 36 10, 48 12, 56 14, 64 16, 80 16})

(def icon-size
  {16 12, 20 12, 24 16, 28 16, 32 20, 36 20, 48 20, 56 20, 64 32, 80 32})

(def solid-icon-size
  (assoc icon-size 32 16))

(def initials-font
  {16 :font/medium-11
   20 :font/semibold-11
   24 :font/semibold-13
   28 :font/semibold-13
   32 :font/semibold-13
   36 :font/medium-19
   48 :font/medium-19
   56 :font/medium-19
   64 :font/medium-27
   80 :font/medium-27})

(def soft-initials-font
  (assoc initials-font 20 :font/medium-11 48 :font/semibold-15))

(defstyle notification-initials
  {:font-size      14.25
   :line-height    19.2375
   :letter-spacing -0.228})

(defn initials [color]
  (style {:include-font-padding false
          :text-align           :center
          :color                color}))

(def ^:private emoji-size
  {16 12, 20 12, 24 12, 28 16, 32 20, 36 20, 48 32, 56 32, 64 40, 80 48})

(def ^:private channel-emoji-size
  (assoc emoji-size 64 32))

(defn emoji [size shape]
  (let [sizes (if (= shape :circle) channel-emoji-size emoji-size)]
    (style {:font-size            (sizes size)
            :line-height          size
            :include-font-padding false
            :text-align           :center})))

(defn emoji-image [size shape]
  (let [sizes      (if (= shape :circle) channel-emoji-size emoji-size)
        image-size (sizes size)]
    (style {:width  image-size
            :height image-size})))

(def status-badge
  {16 {:size 4 :offset 0 :gap 1}
   20 {:size 4 :offset 1 :gap 1}
   24 {:size 6 :offset 0 :gap 1}
   28 {:size 6 :offset 1 :gap 1}
   32 {:size 8 :offset 0 :gap 2}
   36 {:size 8 :offset 1 :gap 2}
   48 {:size 8 :offset 2 :gap 2}
   56 {:size 8 :offset 4 :gap 2}
   64 {:size 10 :offset 4 :gap 3}
   80 {:size 12 :offset 6 :gap 4}})

(def activity-status-badge
  {16 {:size 6 :offset 0 :gap 0}
   20 {:size 8 :offset 0 :gap 0}
   24 {:size 9 :offset 0 :gap 0}
   28 {:size 10 :offset 0 :gap 0}
   32 {:size 12 :offset 0 :gap 0}
   36 {:size 14 :offset 0 :gap 0}
   48 {:size 18 :offset 0 :gap 0}
   56 {:size 21 :offset 0 :gap 0}
   64 {:size 24 :offset 0 :gap 0}
   80 {:size 30 :offset 0 :gap 0}})

(defn activity-status-color [theme blur? color status]
  (case status
    :pending (if blur?
               (colors/themed theme :color/neutral-80-40 :color/white-40)
               (colors/get-color :color/neutral-50))
    :confirmed (colors/themed theme :color/success)
    :error (colors/themed theme :color/danger)
    :finalized [(colors/themed theme color) (colors/get-color :color/white-100)]))

(def image-badge
  {16 {:size 8 :offset -2 :gap 1}
   20 {:size 10 :offset -2 :gap 1}
   24 {:size 12 :offset -3 :gap 1}
   28 {:size 14 :offset -3 :gap 1}
   32 {:size 16 :offset -4 :gap 1}
   36 {:size 16 :offset -3 :gap 1}
   48 {:size 20 :offset -2 :gap 2}
   56 {:size 20 :offset -1 :gap 2}
   64 {:size 20 :offset 0 :gap 2}
   80 {:size 24 :offset 0 :gap 2}})

(def icon-badge
  {16 {:size 8 :offset -2 :gap 0}
   20 {:size 12 :offset -3 :gap 0}
   24 {:size 16 :offset -4 :gap 0}
   28 {:size 16 :offset -4 :gap 0}
   32 {:size 16 :offset -4 :gap 0}
   36 {:size 16 :offset -3 :gap 0}
   48 {:size 20 :offset -2 :gap 0}
   56 {:size 20 :offset -1 :gap 0}
   64 {:size 20 :offset 0 :gap 0}
   80 {:size 24 :offset 0 :gap 0}})

(defn badge-position [offset]
  (style {:position :absolute
          :right    offset
          :bottom   offset}))

(defn badge [size fill]
  (style {:width            size
          :height           size
          :border-radius    (/ size 2)
          :overflow         :hidden
          :align-items      :center
          :justify-content  :center
          :background-color fill}))
