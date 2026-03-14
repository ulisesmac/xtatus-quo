(ns xquo.components.counter.style
  (:require-macros [reagent-extended-compiler.utils.transforms :refer [defstyle style]])
  (:require [xquo.foundations.colors :as colors]))

(def ^:private default-radius 6)
(def ^:private large-radius 8)

(def ^:private default-widths
  {:one               20
   :two               20
   :three             27
   :ninety-nine-plus  28
   :negative          28
   :four              34})

(def ^:private large-layouts
  {:one               {:width 20 :padding-left 5.5 :padding-right 5.5}
   :two               {:width 23 :padding-left 4 :padding-right 4}
   :three             {:width 34 :padding-left 4.5 :padding-right 4.5}
   :ninety-nine-plus  {:width 32 :padding-left 4 :padding-right 2}
   :negative          {:width 32 :padding-left 4 :padding-right 5}
   :four              {:width 42 :padding-left 4.5 :padding-right 4.5}})

(def ^:private default-value-slot-insets
  {:one               {:top 2 :right 2 :bottom 2 :left 2}
   :two               {:top 2 :right 0 :bottom 2 :left 0}
   :three             {:top 2 :right 2 :bottom 2 :left 2}
   :ninety-nine-plus  {:top 2 :right 2 :bottom 2 :left 3}
   :negative          {:top 2 :right 3 :bottom 2 :left 2}
   :four              {:top 2 :right 0 :bottom 2 :left 0}})

(defstyle default-root-base
  {:height        20
   :overflow      :hidden
   :position      :relative
   :border-radius default-radius})

(defstyle default-surface-base
  {:position      :absolute
   :border-radius default-radius})

(defstyle default-value-slot-base
  {:position        :absolute
   :justify-content :center
   :align-items     :center})

(defstyle large-root-base
  {:height           20
   :overflow         :hidden
   :flex-direction   :row
   :justify-content  :center
   :align-items      :center
   :border-radius    large-radius})

(defn- outline-color-border-color [color dark-theme? blur?]
  (cond
    (and dark-theme? blur?) (colors/get-color :color/white 10)
    :else                   (colors/get-color color 50 20)))

(defn- outline-color-text-color [color dark-theme? blur?]
  (cond
    (and dark-theme? blur?) (colors/get-color :color/white 40)
    dark-theme?            (colors/get-color color 60)
    :else                  (colors/get-color color 50)))

(defn- outline-border-color [dark-theme? blur?]
  (cond
    (and dark-theme? blur?) (colors/get-color :color/white 10)
    dark-theme?            (colors/get-color :color/neutral 80)
    blur?                  (colors/get-color :color/neutral 80 5)
    :else                  (colors/get-color :color/neutral 20)))

(defn- outline-text-color [dark-theme?]
  (if dark-theme?
    (colors/get-color :color/white 100)
    (colors/get-color :color/neutral 100)))

(defn- default-fill-color [color dark-theme? blur?]
  (cond
    (and dark-theme? blur?) (colors/get-color color 50)
    dark-theme?            (colors/get-color color 60)
    :else                  (colors/get-color color 50)))

(defn- default-type-style [color type dark-theme? blur?]
  (case type
    :default
    {:background-color (default-fill-color color dark-theme? blur?)}

    :secondary
    {:background-color (if dark-theme?
                         (colors/get-color :color/white 5)
                         (colors/get-color :color/neutral 80 5))}

    :grey
    {:background-color (cond
                         (and dark-theme? blur?) (colors/get-color :color/white 10)
                         dark-theme?            (colors/get-color :color/neutral 80)
                         blur?                  (colors/get-color :color/neutral 80 10)
                         :else                  (colors/get-color :color/neutral 10))}

    :outline
    {:border-width 1
     :border-color (outline-border-color dark-theme? blur?)}

    :outline-color
    {:border-width 1
     :border-color (outline-color-border-color color dark-theme? blur?)}

    :warning
    {:background-color (colors/get-color :color/warning 50 10)
     :border-width     1
     :border-color     (colors/get-color :color/warning 50 20)}

    :error
    {:background-color (colors/get-color :color/danger 50 10)
     :border-width     1
     :border-color     (colors/get-color :color/danger 50 20)}

    {:background-color (cond
                         (and dark-theme? blur?) (colors/get-color color 50)
                         dark-theme?            (colors/get-color color 60)
                         :else                  (colors/get-color color 50))}))

(defn- large-type-style [color type dark-theme? blur?]
  (case type
    :default
    {:background-color (if dark-theme?
                         (colors/get-color color 60)
                         (colors/get-color color 50))}

    :secondary
    {:background-color (if dark-theme?
                         (colors/get-color :color/white 5)
                         (colors/get-color :color/neutral 80 5))}

    :grey
    {:background-color (cond
                         (and dark-theme? blur?) (colors/get-color :color/white 10)
                         dark-theme?            (colors/get-color :color/neutral 80)
                         blur?                  (colors/get-color :color/neutral 80 10)
                         :else                  (colors/get-color :color/neutral 10))}

    :outline
    {:border-width 1
     :border-color (outline-border-color dark-theme? blur?)}

    :outline-color
    {:border-width 1
     :border-color (outline-color-border-color color dark-theme? blur?)}

    :warning
    {:background-color (colors/get-color :color/warning 50 10)
     :border-width     1
     :border-color     (colors/get-color :color/warning 50 20)}

    :error
    {:background-color (colors/get-color :color/danger 50 10)
     :border-width     1
     :border-color     (colors/get-color :color/danger 50 20)}

    {:background-color (if dark-theme?
                         (colors/get-color color 60)
                         (colors/get-color color 50))}))

(defn default-root-style [color layout-key type dark-theme? blur?]
  (style {:width            (get default-widths layout-key)
          :background-color (when (and (= type :default) dark-theme? blur?)
                              (colors/get-color color 60))}))

(defn default-surface-style [color layout-key type dark-theme? blur?]
  (let [horizontal-inset (if (= layout-key :one) 2 0)]
    (style (merge {:top    2
                   :right  horizontal-inset
                   :bottom 2
                   :left   horizontal-inset}
                  (default-type-style color type dark-theme? blur?)))))

(defn default-value-slot-style [layout-key]
  (style (get default-value-slot-insets layout-key)))

(defn large-root-style [color layout-key type dark-theme? blur?]
  (let [{:keys [width padding-left padding-right]} (get large-layouts layout-key)]
    (style (merge {:width         width
                   :padding-left  padding-left
                   :padding-right padding-right}
                  (large-type-style color type dark-theme? blur?)))))

(defn large-value-text-style [layout-key]
  (case layout-key
    :four
    (style {:flex-shrink 0})

    :one
    (style {:width      "100%"
            :flex-grow  1
            :flex-shrink 0
            :flex-basis 0
            :min-width  1
            :min-height 1})

    :ninety-nine-plus
    (style {:width      "100%"
            :flex-grow  1
            :flex-shrink 0
            :flex-basis 0
            :min-width  1
            :min-height 1})

    (style {:height     "100%"
            :flex-grow  1
            :flex-shrink 0
            :flex-basis 0
            :min-width  1
            :min-height 1})))

(defn value-text-style [color type dark-theme? blur?]
  (style {:color     (case type
                       :default       (colors/get-color :color/white 100)
                       :outline       (outline-text-color dark-theme?)
                       :outline-color (outline-color-text-color color dark-theme? blur?)
                       :warning       (if dark-theme?
                                        (colors/get-color :color/warning 60)
                                        (colors/get-color :color/warning 50))
                       :error         (if dark-theme?
                                        (colors/get-color :color/danger 60)
                                        (colors/get-color :color/danger 50))
                       (if dark-theme?
                         (colors/get-color :color/white 100)
                         (colors/get-color :color/neutral 100)))
          :text-align           :center
          :text-align-vertical  :center
          :include-font-padding false}))
