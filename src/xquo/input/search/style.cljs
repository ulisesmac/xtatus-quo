(ns xquo.input.search.style
  (:require [react-native.utils :refer [defstyle style]]
            [xquo.foundations.colors :as colors]))

(defn root [multiple? resting?]
  (style {:height         32
          :min-width      0
          :align-self     :stretch
          :flex-direction :row
          :align-items    :center
          :gap            (if (and multiple? resting?) 32 8)}))

(defstyle tags
  {:flex-shrink 1
   :min-width   0
   :height      32
   :overflow    :hidden})

(defn tag-content [multiple?]
  (style {:flex-grow       1
          :flex-direction  :row
          :align-items     :center
          :justify-content (if multiple? "flex-end" "flex-start")
          :gap             8}))

(defstyle growing
  {:flex 1})

(defstyle resting-input
  {:position :absolute
   :left     0
   :width    1
   :opacity  0})

(defn input [multiple? resting? width]
  (style {:flex-grow   (if (or multiple? resting?) 0 1)
          :flex-shrink (if resting? 0 1)
          :min-width   0
          :width       (when (or multiple? resting?) (max 1 width))}))

(defn field [multiple? resting?]
  (style {:height           32
          :min-height       32
          :border-width     0
          :background-color :transparent
          :padding-left     0
          :padding-right    0
          :padding-vertical 5
          :gap              (cond
                              resting?  0
                              multiple? 32
                              :else     8)
          :opacity          1}))

(defn text-opacity [{:keys [light-theme?]} blur? disabled?]
  (style {:opacity (cond
                     (and disabled? light-theme? blur?) 0.4
                     disabled?                          0.3
                     :else                              1)}))

(defn placeholder-color [{:keys [theme dark-theme?]} blur? focused?]
  (cond
    (and blur? focused?)    (colors/themed theme :color/neutral-80-20 :color/white-20)
    (and blur? dark-theme?) (colors/get-color :color/white-30)
    blur?                   (colors/get-color :color/neutral-80-40)
    focused?                (colors/themed theme :color/neutral-30 :color/neutral-60)
    :else                   (colors/themed theme :color/neutral-40 :color/neutral-50)))

(defn cursor-color [{:keys [color dark-theme?]}]
  (colors/get-color color (if dark-theme? 60 50)))

(defn selection-color [{:keys [color dark-theme?]}]
  (colors/get-color color (if dark-theme? 60 50) 20))

(defstyle measure
  {:position             :absolute
   :opacity              0
   :include-font-padding false
   :line-height          22
   :height               22})
