(ns xquo.list.item.style
  (:require [react-native.utils :refer [defstyle style]]
            [xquo.foundations.animations :as animations]
            [xquo.foundations.colors :as colors]))

(def ^:private corner-radius {:default 12 :outline 16 :grey 16 :elevated 12})
(def title-font
  {:default  {:regular :font/semibold-15 :compact :font/medium-15}
   :outline  {:regular :font/semibold-15 :compact :font/semibold-13}
   :grey     {:regular :font/medium-15 :compact :font/semibold-13}
   :elevated {:regular :font/semibold-15 :compact :font/semibold-15}})

(defn- vertical-padding [type compact?]
  (cond
    (and compact? (#{:outline :grey} type)) 6
    (and compact? (= type :default))       4
    :else                                 8))

(defn leading-gap [type compact? media?]
  (cond
    (= type :elevated)                 10
    (and (= type :grey) (not compact?)) 12
    media?                            12
    (and (= type :default) compact?)   12
    :else                             8))

(defn container [type compact? disabled? right?]
  (let [radius  (if compact? 12 (corner-radius type))
        inset?  (and compact? (#{:outline :grey} type))
        padding (if inset? 8 12)]
    (style {:align-self       :stretch
            :min-width        0
            :border-radius    radius
            :padding-left     padding
            :padding-right    (cond
                                (and inset? right?) 12
                                inset?             8
                                :else              12)
            :padding-vertical (vertical-padding type compact?)
            :opacity          (if disabled? 0.3 1)
            :gap              8})))

(defn- background [{:keys [theme dark-theme?]} color type compact? blur? pressed? active? selected?]
  (cond
    (and (= type :elevated) blur? pressed?)
    "transparent"

    (and (= type :elevated) blur?)
    (colors/themed theme :color/neutral-80-5 :color/white-5)

    (and (= type :elevated) dark-theme? pressed?)
    "transparent"

    (= type :elevated)
    (colors/themed theme :color/white-100 :color/neutral-90)

    (and (or (= type :grey) (and (= type :outline) compact?)) blur? pressed?)
    (colors/themed theme :color/neutral-80-10 :color/white-10)

    (and (or (= type :grey) (and (= type :outline) compact?)) blur?)
    (colors/themed theme :color/neutral-80-5 :color/white-5)

    (and (= type :outline) compact? dark-theme? pressed?)
    (colors/get-color :color/neutral-80-60)

    (and (= type :outline) compact? dark-theme?)
    (colors/get-color :color/neutral-80-40)

    (and (or (= type :grey) (and (= type :outline) compact?)) pressed?)
    (colors/themed theme :color/neutral-5 :color/neutral-80)

    (or (= type :grey) (and (= type :outline) compact?))
    (colors/themed theme :color/neutral-2.5 :color/neutral-90)

    (and active? dark-theme? blur?)
    (colors/get-color :color/white-10)

    active?
    (colors/get-color color 50 10)

    (and (or pressed? selected?) dark-theme? blur?)
    (colors/get-color :color/white-5)

    (or pressed? selected?)
    (colors/get-color color 50 5)

    :else
    "transparent"))

(defn appearance [theme-color color type compact? blur? pressed? active? selected?]
  (let [duration-key (if pressed? :pressed-duration :default-duration)
        easing-key   (if pressed? :pressed-timing-function :default-timing-function)]
    (style {:background-color           (background theme-color color type compact? blur? pressed? active? selected?)
            :transition-property        [:transform :backgroundColor]
            :transition-duration        (get animations/press-feedback duration-key)
            :transition-timing-function (get animations/press-feedback easing-key)})))

(defn border [theme type compact? blur? pressed?]
  (let [duration-key (if pressed? :pressed-duration :default-duration)
        easing-key   (if pressed? :pressed-timing-function :default-timing-function)]
    (when (and (= type :outline) (not (and compact? blur?)))
      (style {:position                   :absolute
              :top                        0
              :right                      0
              :bottom                     0
              :left                       0
              :border-radius              (if compact? 12 (corner-radius type))
              :border-width               1
              :border-color               (cond
                                            (and blur? pressed?) (colors/themed theme :color/neutral-80-10 :color/white-10)
                                            blur?                (colors/themed theme :color/neutral-80-5 :color/white-5)
                                            pressed?             (colors/themed theme :color/neutral-20 :color/neutral-70)
                                            :else                (colors/themed theme :color/neutral-10 :color/neutral-80))
              :transition-property        :borderColor
              :transition-duration        (get animations/press-feedback duration-key)
              :transition-timing-function (get animations/press-feedback easing-key)}))))

(defn shadow [{:keys [dark-theme?]} type blur? pressed?]
  (when (and (= type :elevated) (not blur?) (not pressed?))
    (style {:box-shadow #js [(style {:offset-x        0
                                     :offset-y        8
                                     :blur-radius     (if dark-theme? 40 30)
                                     :spread-distance 0
                                     :color           (colors/get-color :color/neutral 100 (if dark-theme? 64 12))})]})))

(defn row [type compact? left? paired? balanced?]
  (style {:flex-direction :row
          :align-items    :center
          :min-height     (- (if compact? 48 56) (* 2 (vertical-padding type compact?)))
          :gap            (cond
                            left?                                      8
                            (= type :elevated)                          10
                            (and compact? (= type :outline))           8
                            (or compact? paired? balanced?
                                (#{:outline :grey} type))               12
                            :else                                      16)}))

(defstyle info
  {:flex           1
   :min-width      0
   :flex-direction :row
   :align-items    :center})

(defstyle leading-group
  {:flex-direction :row
   :align-items    :center
   :gap            8
   :flex-shrink    0})

(defstyle text-column
  {:flex      1
   :min-width 0})

(defstyle slots
  {:flex-direction :row
   :align-items    :center
   :flex-shrink    0})

(defstyle balanced-values
  {:flex 1 :min-width 0 :justify-content :flex-end})

(defstyle image
  {:width 40 :height 40 :border-radius 10})

(defstyle icon
  {:align-items :center :justify-content :center})

(defstyle inherited-disabled
  {:opacity 1})

(defn secondary [theme blur?]
  (if blur?
    (colors/themed theme :color/neutral-80-40 :color/white-40)
    (colors/themed theme :color/neutral-50 :color/neutral-40)))

(defn icon-color [theme blur?]
  (if blur?
    (colors/themed theme :color/neutral-80-70 :color/white-70)
    (colors/themed theme :color/neutral-50 :color/neutral-40)))

(defn text [color line-height single-line?]
  (style {:color                color
          :height               (when single-line? (js/Math.round line-height))
          :min-height           (js/Math.round line-height)
          :line-height          line-height
          :include-font-padding false
          :text-align-vertical  :center}))

(defn caption [indent]
  (style {:margin-left    indent
          :flex-direction :row
          :align-items    :center
          :gap            2}))

(defn values [change?]
  (style {:align-items :flex-end
          :min-width   0
          :gap         (if change? 2 0)}))

(defstyle change
  {:flex-direction  :row
   :align-items     :center
   :justify-content :flex-end
   :gap             4})

(defstyle change-separator
  {:width 1 :height 8})

(defn change-color [theme blur? direction]
  (case direction
    :positive (colors/get-color :color/success 50)
    :negative (colors/get-color :color/danger 50)
    (secondary theme blur?)))
