(ns xquo.components.text.combination.style
  (:require [react-native.utils :refer [defstyle style]]
            [xquo.foundations.colors :as colors]))

(defstyle row
  {:min-height 32 :flex-direction :row :align-items :flex-start :gap 20})

(defstyle body
  {:flex 1 :min-width 0 :flex-direction :row :align-items :flex-start :gap 6})

(defstyle heading
  {:flex-shrink          1
   :min-width            0
   :line-height          32
   :include-font-padding false})

(defstyle caption
  {:flex-shrink 1 :min-width 0 :padding-top 10})

(defn secondary [{:keys [light-theme?]} blur?]
  (cond
    light-theme? (colors/get-color :color/neutral-60)
    blur?                 (colors/get-color :color/white-40)
    :else                 (colors/get-color :color/neutral-40)))

(defn caption-text [theme-color blur?]
  (style {:line-height          22
          :include-font-padding false
          :color                (secondary theme-color blur?)}))

(defn icons [type caption? {:keys [dark-theme?]}]
  (let [username? (= type :username)
        gap       (if username? 0 4)
        spacing   (cond
                    (and username? caption? dark-theme?) 6
                    username?                                    4
                    :else                                        6)]
    (style {:flex-direction :row
            :flex-shrink    0
            :padding-top    (if caption? 10 8)
            :gap            gap
            :margin-left    (- spacing 6)})))

(defn icon-color [{:keys [light-theme?]} blur?]
  (cond
    light-theme? (colors/get-color :color/neutral-50)
    blur?                 (colors/get-color :color/white-40)
    :else                 (colors/get-color :color/neutral-40)))

(defstyle tag
  {:padding-top 6 :padding-bottom 2 :flex-shrink 0})

(defstyle right-text
  {:padding-top 12 :padding-bottom 2 :flex-shrink 0})

(defn counter-text [theme blur?]
  (style {:line-height          18
          :include-font-padding false
          :color                (colors/themed theme :color/neutral-40 (if blur? :color/white-40 :color/neutral-50))}))
