(ns xquo.components.selectors.disclaimer.style
  (:require [react-native.utils :refer [defstyle style]]
            [xquo.foundations.colors :as colors]))

(defstyle container
  {:width "100%" :padding-top 12 :padding-horizontal 20 :padding-bottom 8})

(defn surface [{:keys [dark-theme?]} blur? disabled?]
  (let [fill   (cond
                 (and dark-theme? blur?) :color/white-5
                 dark-theme?             :color/neutral-80-40
                 :else                   :color/neutral-5)
        border (cond
                 (and dark-theme? blur?) :color/white-10
                 dark-theme?             :color/neutral-80
                 :else                   :color/neutral-20)]
    (style {:padding          11
            :border-width     1
            :border-radius    12
            :flex-direction   :row
            :align-items      :flex-start
            :gap              8
            :background-color (colors/get-color fill)
            :border-color     (colors/get-color border)
            :opacity          (if disabled? 0.3 1)})))

(defstyle selector
  {:opacity 1})

(defstyle text-wrapper
  {:flex 1 :min-width 0})

(defstyle text
  {:line-height 18 :include-font-padding false})

(defn icon-color [{:keys [dark-theme?]} blur?]
  (colors/get-color (cond
                      (and dark-theme? blur?) :color/white-70
                      dark-theme?             :color/neutral-40
                      :else                   :color/neutral-50)))
