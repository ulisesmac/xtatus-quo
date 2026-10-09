(ns xquo.components.counter.style
  (:require [react-native.utils :refer [defstyle style]]
            [xquo.foundations.colors :as colors]))

(def ^:private radius
  {:default 6
   :large   8})

(def ^:private layouts
  {:default {:one              {:width 20 :left 2 :right 2}
             :two              {:width 20 :left 0 :right 0}
             :three            {:width 27 :left 2 :right 2}
             :ninety-nine-plus {:width 28 :left 3 :right 2}
             :negative         {:width 28 :left 2 :right 3}
             :four             {:width 34 :left 0 :right 0}}
   :large   {:one              {:width 20 :left 5.5 :right 5.5}
             :two              {:width 23 :left 4 :right 4}
             :three            {:width 34 :left 4.5 :right 4.5}
             :ninety-nine-plus {:width 32 :left 4 :right 2}
             :negative         {:width 32 :left 4 :right 5}
             :four             {:width 42 :left 4.5 :right 4.5}}})

(defstyle container
  {:height   20
   :overflow :hidden})

(defstyle surface
  {:position :absolute})

(defn container-style [color size layout-key type dark-theme? blur? characters]
  (let [width (:width (get-in layouts [size layout-key]))]
    (style {:width            (when (<= characters 4) width)
            :min-width        width
            :border-radius    (radius size)
            :background-color (when (and (= size :default) (= type :default) dark-theme? blur?)
                                (colors/get-color color 60))})))

(defn- outline-border-color [dark-theme? blur?]
  (cond
    (and dark-theme? blur?) (colors/get-color :color/white-10)
    dark-theme?            (colors/get-color :color/neutral-80)
    blur?                  (colors/get-color :color/neutral-80-5)
    :else                  (colors/get-color :color/neutral-20)))

(defn surface-style [color size layout-key type dark-theme? blur?]
  (let [default-size?    (= size :default)
        vertical-inset   (if default-size? 2 0)
        horizontal-inset (if (and default-size? (= layout-key :one)) 2 0)
        level            (if (and dark-theme? (not (and default-size? blur?))) 60 50)]
    (style {:top              vertical-inset
            :bottom           vertical-inset
            :left             horizontal-inset
            :right            horizontal-inset
            :border-radius    (radius size)
            :background-color (case type
                                :default (colors/get-color color level)
                                :secondary (colors/get-color (if dark-theme? :color/white-5 :color/neutral-80-5))
                                :grey (cond
                                        (and dark-theme? blur?) (colors/get-color :color/white-10)
                                        dark-theme?            (colors/get-color :color/neutral-80)
                                        blur?                  (colors/get-color :color/neutral-80-10)
                                        :else                  (colors/get-color :color/neutral-10))
                                :warning (colors/get-color :color/warning-50-10)
                                :error (colors/get-color :color/danger-50-10)
                                nil)
            :border-width     (when (#{:outline :outline-color :warning :error} type) 1)
            :border-color     (case type
                                :outline (outline-border-color dark-theme? blur?)
                                :outline-color (if (and dark-theme? blur?)
                                                 (colors/get-color :color/white-10)
                                                 (colors/get-color color 50 20))
                                :warning (colors/get-color :color/warning-50-20)
                                :error (colors/get-color :color/danger-50-20)
                                nil)})))

(defn value-style [color size layout-key type dark-theme? blur?]
  (let [{:keys [left right]} (get-in layouts [size layout-key])
        vertical-inset       (cond
                               (= size :default)    2
                               (= layout-key :four) 0.5
                               :else                0)
        level                (if dark-theme? 60 50)]
    (style {:margin-top           vertical-inset
            :margin-left          left
            :margin-right         right
            :height               (- 20 (* 2 vertical-inset))
            :text-align           :center
            :text-align-vertical  :center
            :include-font-padding false
            :color                (case type
                                    :default (colors/get-color :color/white-100)
                                    :outline-color (cond
                                                     (and dark-theme? blur?) (colors/get-color :color/white-40)
                                                     ;; The single-digit Default fixture uses color-50 in dark.
                                                     (and (= size :default) (= layout-key :one)) (colors/get-color color 50)
                                                     :else (colors/get-color color level))
                                    :warning (colors/get-color :color/warning level)
                                    :error (colors/get-color :color/danger level)
                                    (colors/get-color (if dark-theme? :color/white-100 :color/neutral-100)))})))
