(ns xquo.components.input.style
  (:require [react-native.utils :refer [defstyle style]]
            [xquo.foundations.colors :as colors]))

(def ^:private gap {32 4, 40 8})
(def ^:private radius {32 10, 40 12})
(def ^:private plain-padding {32 5, 40 9})
(def ^:private button-padding {32 4, 40 8})

(defstyle root
  {:align-self  :stretch
   :min-width   0
   :flex-shrink 1})

(defstyle labels
  {:flex-direction :row
   :margin-bottom  8})

(defstyle label-slot
  {:flex      1
   :min-width 0})

(defstyle counter-slot
  {:flex       1
   :min-width  0
   :text-align :right})

(defn label-text [{:keys [theme]} blur?]
  (style {:include-font-padding false
          :line-height          18
          :color                (if blur?
                                  (colors/themed theme :color/neutral-80-40 :color/white-40)
                                  (colors/themed theme :color/neutral-50 :color/neutral-40))}))

(defn vertical-padding [size button?]
  (let [padding (if button? button-padding plain-padding)]
    (* 2 (padding size))))

(defn field-layout [{:keys [dark-theme?]}
                    {:keys [size leading? clearable? button? blur? error? focused? filled? disabled? multiline?]}]
  (let [large? (= size 40)
        left   (cond
                 (and leading? large?)
                 12

                 leading?
                 8

                 (and large? dark-theme? blur? button? (not error?) (not focused?) (not filled?) (not disabled?))
                 12

                 large?
                 16

                 :else
                 12)
        right  (cond
                 (and button? large?)
                 8

                 button?
                 4

                 (not large?)
                 8

                 (and leading? (not clearable?) (or (not blur?) error?))
                 16

                 :else
                 12)]
    (style {:flex-direction   :row
            :align-items      (if multiline? "flex-start" "center")
            :align-self       :stretch
            :min-height       size
            :gap              (gap size)
            :border-width     1
            :border-radius    (radius size)
            :padding-left     (dec left)
            :padding-right    (dec right)
            :padding-vertical (dec (/ (vertical-padding size button?) 2))})))

(defn field-color [{:keys [theme dark-theme?]} blur? error? focused? disabled?]
  (style {:opacity          (if disabled? 0.3 1)
          :background-color (if blur? "transparent" (colors/themed theme :color/white-100 :color/neutral-95))
          :border-color     (cond
                              error?
                              (colors/get-color :color/danger-50-40)

                              (and dark-theme? blur? focused?)
                              (colors/get-color :color/white-40)

                              (and dark-theme? blur?)
                              (colors/get-color :color/white-10)

                              (and blur? focused?)
                              (colors/get-color :color/neutral-80-20)

                              blur?
                              (colors/get-color :color/neutral-80-10)

                              focused?
                              (colors/themed theme :color/neutral-40 :color/neutral-60)

                              :else
                              (colors/themed theme :color/neutral-20 :color/neutral-70))}))

(defn bounds [min-height max-height]
  (style {:min-height min-height
          :max-height max-height}))

(defstyle leading-slot
  {:width           20
   :height          22
   :align-items     :center
   :justify-content :center
   :flex-shrink     0})

(defstyle leading-image
  {:width  20
   :height 20})

(defn leading-color [{:keys [theme]} blur?]
  (if blur?
    (colors/themed theme :color/neutral-80-70 :color/white-70)
    (colors/themed theme :color/neutral-50 :color/neutral-40)))

(defn clear-colors [{:keys [theme]} blur?]
  [(if blur?
     (colors/themed theme :color/neutral-80-30 :color/white-10)
     (colors/themed theme :color/neutral-40 :color/neutral-60))
   (colors/get-color :color/white-100)])

(defstyle clear-slot
  {:width           20
   :height          22
   :align-items     :center
   :justify-content :center
   :flex-shrink     0})

(defn clear-visibility [hidden?]
  (style {:opacity             (if hidden? 0 1)
          :transition-property :opacity
          :transition-duration 120}))

(defstyle inherited-disabled-button
  {:opacity 1})

(defn placeholder-color [{:keys [dark-theme?]} blur? focused?]
  (cond
    (and dark-theme? blur? focused?)
    (colors/get-color :color/white-20)

    (and dark-theme? blur?)
    (colors/get-color :color/white-40)

    (and dark-theme? focused?)
    (colors/get-color :color/neutral-60)

    dark-theme?
    (colors/get-color :color/neutral-50)

    (and blur? focused?)
    (colors/get-color :color/neutral-80-20)

    blur?
    (colors/get-color :color/neutral-80-40)

    focused?
    (colors/get-color :color/neutral-30)

    :else
    (colors/get-color :color/neutral-40)))

(defn cursor-color [{:keys [color dark-theme?]} blur?]
  (if (and dark-theme? blur?)
    (colors/get-color :color/white-100)
    (colors/get-color color (if dark-theme? 60 50))))

(defstyle text-input-base
  {:flex                 1
   :min-width            0
   :include-font-padding false
   :padding              0
   :margin               0})

(defn text-input-layout [{:keys [theme]} multiline? height line-height]
  (style {:height              height
          :min-height          line-height
          :line-height         line-height
          :text-align-vertical (if multiline? "top" "center")
          :color               (colors/themed theme :color/neutral-100 :color/white-100)}))
