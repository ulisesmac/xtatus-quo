(ns xquo.components.segmented-tab.style
  (:require [react-native.utils :refer [style]]
            [xquo.foundations.colors :as colors]))

(def font {20 :font/medium-11 28 :font/medium-15})
(def ^:private radius {20 6 28 8})
(def ^:private symbol-size {20 12 28 20})
(def icon-only-size {20 16 28 20})
(def ^:private text-height {20 16 28 22})

(defn- background [theme type blur? selected?]
  (cond
    (and blur? selected?)
    (colors/themed theme :color/neutral-80-60 :color/white-20)

    blur?
    nil

    selected?
    (colors/themed theme :color/neutral-50 :color/neutral-70)

    (= type :dark-grey)
    (colors/themed theme :color/neutral-20 :color/neutral-90)

    :else
    (colors/themed theme :color/neutral-10 :color/neutral-80)))

(defn container [{:keys [theme light-theme? dark-theme?]} type size selected? disabled? blur? symbol label]
  (let [icon-only? (and (= (:type symbol) :icon) (nil? label))
        wide-icon? (and icon-only?
                        (= size 28)
                        light-theme?
                        (or selected? disabled? (= type :dark-grey)))]
    (style {:height           size
            :min-width        (when wide-icon? 51)
            :padding-left     (cond
                                (= size 20)        8
                                (and symbol label) 10
                                :else              12)
            :padding-right    (if (= size 20) 8 12)
            :border-radius    (radius size)
            :flex-direction   :row
            :align-items      :center
            :justify-content  :center
            :gap              4
            :background-color (background theme type blur? selected?)
            :opacity          (if (and disabled? (not (and (= size 20) dark-theme?))) 0.3 1)})))

(defn label [theme selected? size]
  (style {:height               (text-height size)
          :color                (if selected?
                                  (colors/get-color :color/white-100)
                                  (colors/themed theme :color/neutral-100 :color/white-100))
          :include-font-padding false
          :text-align           :center
          :text-align-vertical  :center}))

(defn icon-color [theme selected? blur?]
  (cond
    selected? (colors/get-color :color/white-100)
    blur?     (colors/themed theme :color/neutral-80-40 :color/white-40)
    :else     (colors/themed theme :color/neutral-50 :color/neutral-40)))

(defn image [size]
  (style {:width  (symbol-size size)
          :height (symbol-size size)}))

(defn emoji [size]
  (style {:width                (symbol-size size)
          :height               (symbol-size size)
          :font-size            (symbol-size size)
          :line-height          (symbol-size size)
          :text-align           :center
          :include-font-padding false}))
