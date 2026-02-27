(ns xquo.components.button.style
  (:require [reagent-extended-compiler.utils.transforms :refer [defstyle style]]
            [xquo.foundations.borders :as borders]
            [xquo.foundations.colors :as colors]))

(def type-styles
  {:theme/light {:primary   {:text-color (colors/get-color :color/white-100)
                             :icon-color (colors/get-color :color/white-70)
                             :default    {:background-color (colors/get-color :color/primary-50)}
                             :pressed    {:background-color (colors/get-color :color/primary-60)}
                             :disabled   {:background-color (colors/get-color :color/primary-50)
                                          :opacity          0.3}}
                 :positive  {:text-color (colors/get-color :color/white-100)
                             :icon-color (colors/get-color :color/white-70)
                             :default    {:background-color (colors/get-color :color/success-50)}
                             :pressed    {:background-color (colors/get-color :color/success-60)}
                             :disabled   {:background-color (colors/get-color :color/success-50)
                                          :opacity          0.3}}
                 :grey      {:text-color (colors/get-color :color/neutral-100)
                             :icon-color (colors/get-color :color/neutral-50)
                             :default    {:background-color (colors/get-color :color/neutral-10)}
                             :pressed    {:background-color (colors/get-color :color/neutral-20)}
                             :disabled   {:background-color (colors/get-color :color/neutral-10)
                                          :opacity          0.3}}
                 :dark-grey {:text-color (colors/get-color :color/neutral-100)
                             :icon-color (colors/get-color :color/neutral-50)
                             :default    {:background-color (colors/get-color :color/neutral-20)}
                             :pressed    {:background-color (colors/get-color :color/neutral-30)}
                             :disabled   {:background-color (colors/get-color :color/neutral-20)
                                          :opacity          0.3}}
                 :outline   {:text-color (colors/get-color :color/neutral-100)
                             :icon-color (colors/get-color :color/neutral-50)
                             :default    {:background-color :transparent
                                          :border-color     (colors/get-color :color/neutral-30)
                                          :border-width     1}
                             :pressed    {:background-color :transparent
                                          :border-color     (colors/get-color :color/neutral-40)
                                          :border-width     1}
                             :disabled   {:background-color :transparent
                                          :border-color     (colors/get-color :color/neutral-30)
                                          :border-width     1
                                          :opacity          0.3}}
                 :ghost     {:text-color (colors/get-color :color/neutral-100)
                             :icon-color (colors/get-color :color/neutral-50)
                             :default    {:background-color :transparent}
                             :pressed    {:background-color (colors/get-color :color/neutral-10)}
                             :disabled   {:background-color :transparent
                                          :opacity          0.3}}
                 :danger    {:text-color (colors/get-color :color/white-100)
                             :icon-color (colors/get-color :color/white-70)
                             :default    {:background-color (colors/get-color :color/danger-50)}
                             :pressed    {:background-color (colors/get-color :color/danger-60)}
                             :disabled   {:background-color (colors/get-color :color/danger-50)
                                          :opacity          0.3}}}
   :theme/dark  {:primary   {:text-color (colors/get-color :color/white-100)
                             :icon-color (colors/get-color :color/white-70)
                             :default    {:background-color (colors/get-color :color/primary-50)}
                             :pressed    {:background-color (colors/get-color :color/primary-60)}
                             :disabled   {:background-color (colors/get-color :color/primary-50)
                                          :opacity          0.3}}
                 :positive  {:text-color (colors/get-color :color/white-100)
                             :icon-color (colors/get-color :color/white-70)
                             :default    {:background-color (colors/get-color :color/success-50)}
                             :pressed    {:background-color (colors/get-color :color/success-60)}
                             :disabled   {:background-color (colors/get-color :color/success-50)
                                          :opacity          0.3}}
                 :grey      {:text-color (colors/get-color :color/white-100)
                             :icon-color (colors/get-color :color/neutral-40)
                             :default    {:background-color (colors/get-color :color/white-5)}
                             :pressed    {:background-color (colors/get-color :color/white-10)}
                             :disabled   {:background-color (colors/get-color :color/white-5)
                                          :opacity          0.3}}
                 :dark-grey {:text-color (colors/get-color :color/white-100)
                             :icon-color (colors/get-color :color/neutral-40)
                             :default    {:background-color (colors/get-color :color/white-10)}
                             :pressed    {:background-color (colors/get-color :color/white-20)}
                             :disabled   {:background-color (colors/get-color :color/white-10)
                                          :opacity          0.3}}
                 :outline   {:text-color (colors/get-color :color/white-100)
                             :icon-color (colors/get-color :color/neutral-40)
                             :default    {:background-color :transparent
                                          :border-color     (colors/get-color :color/white-10)
                                          :border-width     1}
                             :pressed    {:background-color :transparent
                                          :border-color     (colors/get-color :color/white-20)
                                          :border-width     1}
                             :disabled   {:background-color :transparent
                                          :border-color     (colors/get-color :color/white-5)
                                          :border-width     1
                                          :opacity          0.3}}
                 :ghost     {:text-color (colors/get-color :color/white-100)
                             :icon-color (colors/get-color :color/neutral-40)
                             :default    {:background-color :transparent}
                             :pressed    {:background-color (colors/get-color :color/white-10)}
                             :disabled   {:background-color :transparent
                                          :opacity          0.3}}
                 :danger    {:text-color (colors/get-color :color/white-100)
                             :icon-color (colors/get-color :color/white-70)
                             :default    {:background-color (colors/get-color :color/danger-50)}
                             :pressed    {:background-color (colors/get-color :color/danger-60)}
                             :disabled   {:background-color (colors/get-color :color/danger-50)
                                          :opacity          0.3}}}})

(def container-layout-styles
  (let [{:border/keys [sizes-40-56 size-32 size-24 max]} borders/border-radius-values]
    {40 {nil         (style {:padding-horizontal 16
                             :padding-vertical   9
                             :border-radius      sizes-40-56
                             :min-height         40})
         :right      (style {:padding-left     16
                             :padding-right    12
                             :padding-vertical 9
                             :border-radius    sizes-40-56
                             :min-height       40})
         :left       (style {:padding-left     12
                             :padding-right    16
                             :padding-vertical 9
                             :border-radius    sizes-40-56
                             :min-height       40})
         :left-right (style {:padding-horizontal 12
                             :padding-vertical   9
                             :border-radius      sizes-40-56
                             :min-height         40})
         :icon-only  (style {:width         40
                             :height        40
                             :border-radius max})}
     32 {nil         (style {:padding-horizontal 12
                             :padding-vertical   5
                             :border-radius      size-32
                             :min-height         32})
         :right      (style {:padding-left     12
                             :padding-right    8
                             :padding-vertical 5
                             :border-radius    size-32
                             :min-height       32})
         :left       (style {:padding-left     8
                             :padding-right    12
                             :padding-vertical 5
                             :border-radius    size-32
                             :min-height       32})
         :left-right (style {:padding-horizontal 8
                             :padding-vertical   5
                             :border-radius      size-32
                             :min-height         32})
         :icon-only  (style {:width         32
                             :height        32
                             :border-radius size-32})}
     24 {nil         (style {:padding-horizontal 8
                             :padding-top        2.5
                             :padding-bottom     3.5
                             :border-radius      size-24
                             :min-height         24})
         :right      (style {:padding-horizontal 8
                             :padding-vertical   3
                             :border-radius      size-24
                             :min-height         24})
         :left       (style {:padding-horizontal 8
                             :padding-vertical   3
                             :border-radius      size-24
                             :min-height         24})
         :left-right (style {:padding-horizontal 8
                             :padding-vertical   3
                             :border-radius      size-24
                             :min-height         24})
         :icon-only  (style {:width         24
                             :height        24
                             :border-radius size-24})}}))

(defn container-layout-style [size icon]
  (get-in container-layout-styles [size icon]))

(defn pressable-type-style [theme type disabled? pressed?]
  (let [state (cond
                disabled? :disabled
                pressed?  :pressed
                :else     :default)]
    (get-in type-styles [theme type state])))

(defn icon-size [size]
  (case size
    24 12
    40 20
    32 20
    20))

(defstyle icon-left-gap
  {:margin-right 4})

(defstyle icon-right-gap
  {:margin-left 4})

(defn icon-gap-style [side]
  (case side
    :left icon-left-gap
    :right icon-right-gap
    nil))

(defstyle pressable-base-style
  {:align-items     :center
   :justify-content :center
   :flex-direction  :row
   :align-self      :flex-start})

(defstyle pressable-transition-style
  {:transform                  [{:scale 1}
                                {:translate-y 0}]
   :transition-property        "transform"})

(defstyle pressable-transition-in-duration
  {:transition-duration        "100ms"
   :transition-timing-function "ease-out"})

(defstyle pressable-transition-out-duration
  {:transition-duration        "150ms"
   :transition-timing-function "ease-in-out"})

(defstyle pressable-pressed-style
  {:transform [{:scale 0.982}
               {:translate-y 2}]})

(def font-type
  {40 :font/medium-15
   32 :font/medium-15
   24 :font/medium-13})

(defn text-color [theme type]
  (get-in type-styles [theme type :text-color]))

(defn icon-color [theme type]
  (get-in type-styles [theme type :icon-color]))

(defn text-style [theme type]
  (style {:color (text-color theme type)}))
