(ns xquo.markdown.list-item.style
  (:require [react-native.utils :refer [defstyle style]]
            [xquo.foundations.colors :as colors]))

(defn container [dark-theme? title description rich? numbered? highlighted?]
  (let [both?        (and title description)
        description? (and description (not title))
        roomy?       (and numbered? (or highlighted? (and dark-theme? title)))
        padding      (cond
                       both?                           6
                       (and rich? highlighted?)        6
                       (and description? highlighted?) 8
                       (and rich? (not roomy?))        4
                       :else                           6)
        bottom       (if (and both? rich?) 4 padding)
        align        (if (or both? (and description? highlighted? (not rich?)))
                       "flex-start"
                       "center")]
    (style {:flex-direction     :row
            :align-items        align
            :padding-horizontal 20
            :padding-top        padding
            :padding-bottom     bottom
            :gap                8})))

(defstyle content
  {:flex      1
   :min-width 0
   :gap       2})

(defstyle inline
  {:flex-direction :row
   :flex-wrap      :wrap
   :align-items    :center
   :gap            4})

(defstyle text
  {:include-font-padding false
   :text-align-vertical  :center
   :line-height          18
   :flex-shrink          1})

(defstyle marker
  {:width           20
   :height          20
   :align-items     :center
   :justify-content :center})

(defn bullet [dark-theme? blur?]
  (let [token (cond
                (and dark-theme? blur?) :color/white-40
                dark-theme?             :color/neutral-50
                :else                   :color/neutral-40)]
    (style {:width            7
            :height           7
            :border-radius    3.5
            :background-color (colors/get-color token)})))
