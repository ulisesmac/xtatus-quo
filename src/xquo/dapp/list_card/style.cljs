(ns xquo.dapp.list-card.style
  (:require [react-native.utils :refer [defstyle style]]
            [xquo.foundations.animations :as animations]
            [xquo.foundations.colors :as colors]))

(defstyle info
  {:flex           1
   :min-width      0
   :flex-direction :row
   :align-items    :center
   :gap            8})

(defstyle text
  {:flex      1
   :min-width 0})

(defstyle title
  {:height               18
   :include-font-padding false
   :text-align-vertical  :center})

(defstyle monospace
  {:font-variant ["tabular-nums"]})

(defstyle description-row
  {:flex-direction :row
   :align-items    :center
   :gap            4})

(defstyle action
  {:width  20
   :height 20})

(defn container [{:keys [theme light-theme?]} variant blur? pressed? action]
  (let [duration-key (if pressed? :pressed-duration :default-duration)
        easing-key   (if pressed? :pressed-timing-function :default-timing-function)]
    (style {:height                     48
            :border-radius              12
            :padding-left               8
            :padding-right              (cond
                                          (and light-theme? (not= variant :account) pressed?)
                                          8

                                          (and light-theme? (not= variant :account))
                                          12

                                          action
                                          12

                                          :else
                                          8)
            :padding-vertical           (if (= variant :smart-contract) 8 6)
            :flex-direction             :row
            :align-items                :center
            :gap                        16
            :background-color           (cond
                                          (and blur? pressed?)
                                          (colors/themed theme :color/neutral-80-10 :color/white-10)

                                          blur?
                                          (colors/themed theme :color/neutral-80-5 :color/white-5)

                                          (and light-theme? pressed?)
                                          (colors/get-color :color/neutral-5)

                                          light-theme?
                                          (colors/get-color :color/neutral-2.5)

                                          pressed?
                                          (colors/get-color :color/neutral 80 60)

                                          :else
                                          (colors/get-color :color/neutral 80 40))
            :transition-property        :backgroundColor
            :transition-duration        (get animations/press-feedback duration-key)
            :transition-timing-function (get animations/press-feedback easing-key)})))

(defn border [theme pressed?]
  (style {:position      :absolute
          :top           0
          :right         0
          :bottom        0
          :left          0
          :border-radius 12
          :border-width  1
          :border-color  (if pressed?
                           (colors/themed theme :color/neutral-20 :color/neutral-70)
                           (colors/themed theme :color/neutral-10 :color/neutral-80))}))

(defn secondary-color [theme blur?]
  (if blur?
    (colors/themed theme :color/neutral-80-40 :color/white-40)
    (colors/themed theme :color/neutral-50 :color/neutral-40)))

(defn description [theme blur?]
  (style {:height               18
          :flex-shrink          1
          :color                (secondary-color theme blur?)
          :include-font-padding false
          :text-align-vertical  :center}))

(defn action-color [theme variant blur?]
  (if (and (= variant :account) blur?)
    (colors/themed theme :color/neutral-80-70 :color/white-70)
    (secondary-color theme blur?)))
