(ns xquo.components.info.information-box.style
  (:require [react-native.utils :refer [defstyle style]]
            [xquo.foundations.colors :as colors]))

(def font {13 :font/regular-13 15 :font/regular-15})
(def ^:private line-height {13 18 15 22})
(def ^:private status-color
  {:success :color/success :warning :color/warning :error :color/danger})

(defn container [{:keys [theme color dark-theme?]} blur? status rich? close-button]
  (let [color (if (= status :info) color (status-color status))]
    (style {:flex-direction     :row
            :align-items        (if (and (not rich?) dark-theme? close-button) "center" "flex-start")
            :gap                8
            :border-width       1
            :border-radius      12
            :padding-horizontal 11
            :padding-top        (if rich? 9 10)
            :padding-bottom     (if rich? 11 10)
            :background-color   (cond
                                  color (colors/get-color color 50 5)
                                  blur? (colors/themed theme :color/neutral-80-5 :color/white-5)
                                  :else (colors/themed theme :color/neutral-5 :color/neutral-80-40))
            :border-color       (cond
                                  color (colors/get-color color 50 10)
                                  blur? (colors/themed theme :color/neutral-80-10 :color/white-10)
                                  :else (colors/themed theme :color/neutral-20 :color/neutral-80))})))

(defstyle content
  {:flex        1
   :min-width   0
   :align-items :flex-start
   :gap         8})

(defstyle copy
  {:align-self :stretch})

(defn leading-icon-slot [title size icon-size]
  (style {:align-self  :stretch
          :padding-top (max 0 (/ (- (if title 22 (line-height size)) icon-size) 2))}))

(defn close-slot [rich?]
  (style {:padding-top    (if rich? 4 3)
          :padding-bottom 3}))

(defn foreground [{:keys [theme]}]
  (colors/themed theme :color/neutral-100 :color/white-100))

(defn secondary [{:keys [theme]} blur?]
  (if blur?
    (colors/themed theme :color/neutral-80-40 :color/white-40)
    (colors/themed theme :color/neutral-50 :color/neutral-40)))

(defn icon-color [{:keys [theme color dark-theme?]} blur? status]
  (if-let [color (if (= status :info) color (status-color status))]
    (colors/get-color color (if dark-theme? 60 50))
    (if blur?
      (colors/themed theme :color/neutral-80-70 :color/white-70)
      (colors/themed theme :color/neutral-50 :color/neutral-40))))

(defn title-style [theme status]
  (style {:include-font-padding false
          :line-height          22
          :color                (if (#{:error :warning} status)
                                  (icon-color theme nil status)
                                  (foreground theme))}))

(defn body-style [theme blur? status title size]
  (style {:include-font-padding false
          :line-height          (line-height size)
          :color                (cond
                                  (= status :error) (icon-color theme blur? status)
                                  title             (secondary theme blur?)
                                  :else             (foreground theme))}))
