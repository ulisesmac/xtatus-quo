(ns xquo.components.button.style
  (:require [reagent-extended-compiler.utils.transforms :refer [defstyle style]]
            [xquo.foundations.animations :as animations]
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

(def background-type-styles
  {:photo {:theme/light {:grey    {:icon-color (colors/get-color :color/neutral-100)
                                   :default    {:background-color (colors/get-color :color/white-40)}
                                   :pressed    {:background-color (colors/get-color :color/white-50)}
                                   :disabled   {:background-color (colors/get-color :color/white-40)
                                                :opacity          0.3}}
                         :outline {:icon-color (colors/get-color :color/neutral-100)
                                   :default    {:background-color :transparent
                                                :border-color     (colors/get-color :color/white-40)
                                                :border-width     1}
                                   :pressed    {:background-color :transparent
                                                :border-color     (colors/get-color :color/white-50)
                                                :border-width     1}
                                   :disabled   {:background-color :transparent
                                                :border-color     (colors/get-color :color/white-40)
                                                :border-width     1
                                                :opacity          0.3}}}
           :theme/dark  {:grey    {:icon-color (colors/get-color :color/white-100)
                                   :default    {:background-color (colors/get-color :color/white-20)}
                                   :pressed    {:background-color (colors/get-color :color/white-30)}
                                   :disabled   {:background-color (colors/get-color :color/white-20)
                                                :opacity          0.3}}
                         :outline {:icon-color (colors/get-color :color/white-100)
                                   :default    {:background-color :transparent
                                                :border-color     (colors/get-color :color/white-20)
                                                :border-width     1}
                                   :pressed    {:background-color :transparent
                                                :border-color     (colors/get-color :color/white-30)
                                                :border-width     1}
                                   :disabled   {:background-color :transparent
                                                :border-color     (colors/get-color :color/white-20)
                                                :border-width     1
                                                :opacity          0.3}}}}
   :blur  {:theme/light {:grey    {:icon-color (colors/get-color :color/neutral-100)
                                   :default    {:background-color (colors/get-color :color/neutral-80-5)}
                                   :pressed    {:background-color (colors/get-color :color/neutral-80-10)}
                                   :disabled   {:background-color (colors/get-color :color/neutral-80-5)
                                                :opacity          0.3}}
                         :outline {:icon-color (colors/get-color :color/neutral-100)
                                   :default    {:background-color :transparent
                                                :border-color     (colors/get-color :color/neutral-80-10)
                                                :border-width     1}
                                   :pressed    {:background-color :transparent
                                                :border-color     (colors/get-color :color/neutral-80-20)
                                                :border-width     1}
                                   :disabled   {:background-color :transparent
                                                :border-color     (colors/get-color :color/neutral-80-10)
                                                :border-width     1
                                                :opacity          0.3}}}
           :theme/dark  {:grey    {:icon-color (colors/get-color :color/white-100)}
                         :outline {:icon-color (colors/get-color :color/white-100)}}}})

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

(defn- component-state [disabled? pressed?]
  (cond
    disabled? :disabled
    pressed?  :pressed
    :else     :default))

(defn- normalized-background [background]
  (case background
    (:photo :blur) background
    :none))

(defn- primary-style [color state]
  (cond
    (= state :disabled) (style {:background-color (colors/get-color color 50)
                                :opacity          0.3})
    (= state :pressed)  (style {:background-color (colors/get-color color 60)})
    :else               (style {:background-color (colors/get-color color 50)})))

(defn pressable-type-style [theme type background color disabled? pressed?]
  (let [state (component-state disabled? pressed?)]
    (if (= type :primary)
      (primary-style color state)
      (or (get-in background-type-styles [(normalized-background background) theme type state])
          (get-in type-styles [theme type state])))))

(defn text-color [theme type background]
  (or (get-in background-type-styles [(normalized-background background) theme type :text-color])
      (get-in type-styles [theme type :text-color])))

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

(def neutral-icon-only-types
  #{:grey :dark-grey :outline :ghost})

(defstyle icon-only-rounded-square-shape
  {:border-radius (:border/size-32 borders/border-radius-values)})

(defn icon-only-shape-style [layout type]
  (when (and (= layout :icon-only) (neutral-icon-only-types type))
    icon-only-rounded-square-shape))

(defstyle pressable-base-style
  {:align-items     :center
   :justify-content :center
   :flex-direction  :row
   :align-self      :flex-start})

(defstyle pressable-default-state-style
  {:transform                  [{:scale       (:default-scale animations/press-feedback)}
                                {:translate-y (:default-translate-y animations/press-feedback)}]
   :transition-property        (:transition-property animations/press-feedback)
   :transition-duration        (:default-duration animations/press-feedback)
   :transition-timing-function (:default-timing-function animations/press-feedback)})

(defstyle pressable-pressed-state-style
  {:transform                  [{:scale       (:pressed-scale animations/press-feedback)}
                                {:translate-y (:pressed-translate-y animations/press-feedback)}]
   :transition-property        (:transition-property animations/press-feedback)
   :transition-duration        (:pressed-duration animations/press-feedback)
   :transition-timing-function (:pressed-timing-function animations/press-feedback)})

(def font-type
  {40 :font/medium-15
   32 :font/medium-15
   24 :font/medium-13})

(defn- icon-only-color [theme type background]
  (cond
    (= theme :theme/dark)                         (colors/get-color :color/white-100)
    (= (normalized-background background) :blur) (colors/get-color :color/neutral-100)
    (neutral-icon-only-types type)               (colors/get-color :color/neutral-100)
    :else                                        (colors/get-color :color/white-100)))

(defn icon-color [theme type background icon-only?]
  (if icon-only?
    (icon-only-color theme type background)
    (or (get-in background-type-styles [(normalized-background background) theme type :icon-color])
        (get-in type-styles [theme type :icon-color]))))

(defn text-style [theme type background]
  (style {:color (text-color theme type background)}))
