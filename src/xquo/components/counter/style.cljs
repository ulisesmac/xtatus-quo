(ns xquo.components.counter.style
  (:require [reagent-extended-compiler.utils.transforms :refer [defstyle style]]
            [xquo.foundations.colors :as colors]))

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

(def ^:private value-heights
  {:default 16
   :large   20})

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
   :position         :relative
   :flex-direction   :row
   :justify-content  :center
   :align-items      :center
   :border-radius    large-radius})

(defstyle large-surface-base
  {:position      :absolute
   :top           0
   :right         0
   :bottom        0
   :left          0
   :border-radius large-radius})

(defstyle large-value-slot-base
  {:position        :absolute
   :top             0
   :right           0
   :bottom          0
   :left            0
   :justify-content :center
   :align-items     :center})

(defn- outline-color-border-color [color dark-theme? blur?]
  (cond
    (and dark-theme? blur?) (colors/get-color :color/white 10)
    :else                   (colors/get-color color 50 20)))

(defn- outline-color-text-color [color dark-theme? _blur?]
  (colors/get-color color (if dark-theme? 60 50)))

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

(defn default-root-style [color layout-key type dark-theme? blur?]
  (style {:width            (get default-widths layout-key)
          :background-color (when (and (= type :default) dark-theme? blur?)
                              (colors/get-color color 60))}))

(defn default-surface-style [color layout-key type dark-theme? blur?]
  (let [horizontal-inset (if (= layout-key :one) 2 0)]
    (style {:top              2
            :right            horizontal-inset
            :bottom           2
            :left             horizontal-inset
            :background-color (case type
                                :default (default-fill-color color dark-theme? blur?)
                                :secondary (if dark-theme?
                                             (colors/get-color :color/white 5)
                                             (colors/get-color :color/neutral 80 5))
                                :grey (cond
                                        (and dark-theme? blur?) (colors/get-color :color/white 10)
                                        dark-theme?            (colors/get-color :color/neutral 80)
                                        blur?                  (colors/get-color :color/neutral 80 10)
                                        :else                  (colors/get-color :color/neutral 10))
                                :outline-color (colors/get-color color (if dark-theme? 60 50) 10)
                                :warning (colors/get-color :color/warning 50 10)
                                :error (colors/get-color :color/danger 50 10)
                                (cond
                                  (and dark-theme? blur?) (colors/get-color color 50)
                                  dark-theme?            (colors/get-color color 60)
                                  :else                  (colors/get-color color 50)))
            :border-width     (case type
                                (:outline :outline-color :warning :error) 1
                                nil)
            :border-color     (case type
                                :outline (outline-border-color dark-theme? blur?)
                                :outline-color (outline-color-border-color color dark-theme? blur?)
                                :warning (colors/get-color :color/warning 50 20)
                                :error (colors/get-color :color/danger 50 20)
                                nil)})))

(defn default-value-slot-style [layout-key]
  (let [horizontal-inset (if (= layout-key :one) 2 0)]
    (style {:top    2
            :right  horizontal-inset
            :bottom 2
            :left   horizontal-inset})))

(defn large-root-style [_color layout-key _type _dark-theme? _blur?]
  (style {:width (:width (get large-layouts layout-key))}))

(defn large-surface-style [color _layout-key type dark-theme? blur?]
  (style {:background-color (case type
                              :default (if dark-theme?
                                         (colors/get-color color 60)
                                         (colors/get-color color 50))
                              :secondary (if dark-theme?
                                           (colors/get-color :color/white 5)
                                           (colors/get-color :color/neutral 80 5))
                              :grey (cond
                                      (and dark-theme? blur?) (colors/get-color :color/white 10)
                                      dark-theme?            (colors/get-color :color/neutral 80)
                                      blur?                  (colors/get-color :color/neutral 80 10)
                                      :else                  (colors/get-color :color/neutral 10))
                              :outline-color (colors/get-color color (if dark-theme? 60 50) 10)
                              :warning (colors/get-color :color/warning 50 10)
                              :error (colors/get-color :color/danger 50 10)
                              (if dark-theme?
                                (colors/get-color color 60)
                                (colors/get-color color 50)))
          :border-width     (case type
                              (:outline :outline-color :warning :error) 1
                              nil)
          :border-color     (case type
                              :outline (outline-border-color dark-theme? blur?)
                              :outline-color (outline-color-border-color color dark-theme? blur?)
                              :warning (colors/get-color :color/warning 50 20)
                              :error (colors/get-color :color/danger 50 20)
                              nil)}))

(defn value-text-style [color size type dark-theme? blur?]
  (let [height (get value-heights size)]
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
          :height               height
          :line-height          height
          :text-align           :center
          :text-align-vertical  :center
          :include-font-padding false})))
