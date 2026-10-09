(ns xquo.components.button.style
  (:require [react-native.utils :refer [defstyle style]]
            [xquo.foundations.animations :as animations]
            [xquo.foundations.borders :as borders]
            [xquo.foundations.colors :as colors]))

(def ^:private type-styles
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
                 :black     {:text-color (colors/get-color :color/white-100)
                             :icon-color (colors/get-color :color/white-70)
                             :default    {:background-color (colors/get-color :color/neutral-100)}
                             :pressed    {:background-color (colors/get-color :color/neutral-80)}
                             :disabled   {:background-color (colors/get-color :color/neutral-100)
                                          :opacity          0.3}}
                 :danger    {:text-color (colors/get-color :color/white-100)
                             :icon-color (colors/get-color :color/white-70)
                             :default    {:background-color (colors/get-color :color/danger-50)}
                             :pressed    {:background-color (colors/get-color :color/danger-60)}
                             :disabled   {:background-color (colors/get-color :color/danger-50)
                                          :opacity          0.3}}}
   :theme/dark  {:primary   {:text-color (colors/get-color :color/white-100)
                             :icon-color (colors/get-color :color/white-70)
                             :default    {:background-color (colors/get-color :color/primary-60)}
                             :pressed    {:background-color (colors/get-color :color/primary-50)}
                             :disabled   {:background-color (colors/get-color :color/primary-60)
                                          :opacity          0.3}}
                 :positive  {:text-color (colors/get-color :color/white-100)
                             :icon-color (colors/get-color :color/white-70)
                             :default    {:background-color (colors/get-color :color/success-60)}
                             :pressed    {:background-color (colors/get-color :color/success-50)}
                             :disabled   {:background-color (colors/get-color :color/success-60)
                                          :opacity          0.3}}
                 :grey      {:text-color (colors/get-color :color/white-100)
                             :icon-color (colors/get-color :color/neutral-40)
                             :default    {:background-color (colors/get-color :color/neutral-80)}
                             :pressed    {:background-color (colors/get-color :color/neutral-60)}
                             :disabled   {:background-color (colors/get-color :color/neutral-80)
                                          :opacity          0.3}}
                 :dark-grey {:text-color (colors/get-color :color/white-100)
                             :icon-color (colors/get-color :color/neutral-40)
                             :default    {:background-color (colors/get-color :color/neutral-90)}
                             :pressed    {:background-color (colors/get-color :color/neutral-60)}
                             :disabled   {:background-color (colors/get-color :color/neutral-90)
                                          :opacity          0.3}}
                 :outline   {:text-color (colors/get-color :color/white-100)
                             :icon-color (colors/get-color :color/neutral-40)
                             :default    {:background-color :transparent
                                          :border-color     (colors/get-color :color/neutral-70)
                                          :border-width     1}
                             :pressed    {:background-color :transparent
                                          :border-color     (colors/get-color :color/neutral-60)
                                          :border-width     1}
                             :disabled   {:background-color :transparent
                                          :border-color     (colors/get-color :color/neutral-70)
                                          :border-width     1
                                          :opacity          0.3}}
                 :ghost     {:text-color (colors/get-color :color/white-100)
                             :icon-color (colors/get-color :color/neutral-40)
                             :default    {:background-color :transparent}
                             :pressed    {:background-color (colors/get-color :color/neutral-80)}
                             :disabled   {:background-color :transparent
                                          :opacity          0.3}}
                 :black     {:text-color (colors/get-color :color/white-100)
                             :icon-color (colors/get-color :color/neutral-40)
                             :default    {:background-color (colors/get-color :color/neutral-90)}
                             :pressed    {:background-color (colors/get-color :color/neutral-80)}
                             :disabled   {:background-color (colors/get-color :color/neutral-90)
                                          :opacity          0.3}}
                 :danger    {:text-color (colors/get-color :color/white-100)
                             :icon-color (colors/get-color :color/white-70)
                             :default    {:background-color (colors/get-color :color/danger-60)}
                             :pressed    {:background-color (colors/get-color :color/danger-50)}
                             :disabled   {:background-color (colors/get-color :color/danger-60)
                                          :opacity          0.3}}}})

(def ^:private background-type-styles
  {:photo {:theme/light {:grey    {:icon-color (colors/get-color :color/neutral-80-40)
                                   :default    {:background-color (colors/get-color :color/white-40)}
                                   :pressed    {:background-color (colors/get-color :color/white-50)}
                                   :disabled   {:background-color (colors/get-color :color/white-40)
                                                :opacity          0.3}}
                         :outline {:icon-color (colors/get-color :color/neutral-80-40)
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
           :theme/dark  {:grey    {:icon-color (colors/get-color :color/white-40)
                                   :default    {:background-color (colors/get-color :color/neutral-80-40)}
                                   :pressed    {:background-color (colors/get-color :color/neutral-80-50)}
                                   :disabled   {:background-color (colors/get-color :color/neutral-80-40)
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
   :blur  {:theme/light {:grey    {:icon-color (colors/get-color :color/neutral-80-40)
                                   :default    {:background-color (colors/get-color :color/neutral-80-5)}
                                   :pressed    {:background-color (colors/get-color :color/neutral-80-10)}
                                   :disabled   {:background-color (colors/get-color :color/neutral-80-5)
                                                :opacity          0.3}}
                         :outline {:icon-color (colors/get-color :color/neutral-80-40)
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
           :theme/dark  {:grey    {:icon-color (colors/get-color :color/white-70)
                                   :default    {:background-color (colors/get-color :color/white-5)}
                                   :pressed    {:background-color (colors/get-color :color/white-10)}
                                   :disabled   {:background-color (colors/get-color :color/white-5)
                                                :opacity          0.3}}
                         :outline {:icon-color (colors/get-color :color/white-70)
                                   :default    {:background-color :transparent
                                                :border-color     (colors/get-color :color/white-10)
                                                :border-width     1}
                                   :pressed    {:background-color :transparent
                                                :border-color     (colors/get-color :color/white-20)
                                                :border-width     1}
                                   :disabled   {:background-color :transparent
                                                :border-color     (colors/get-color :color/white-5)
                                                :border-width     1
                                                :opacity          0.3}}}}})

(def ^:private background-type-state-icon-colors
  {:blur {:theme/dark {:grey    {:default  (colors/get-color :color/white-70)
                                 :pressed  (colors/get-color :color/white-40)
                                 :disabled (colors/get-color :color/white-40)}
                       :outline {:default  (colors/get-color :color/white-70)
                                 :pressed  (colors/get-color :color/white-40)
                                 :disabled (colors/get-color :color/white-40)}}}})

(def ^:private horizontal-padding
  {40 {:text 16 :icon 12}
   32 {:text 12 :icon 8}
   24 {:text 8 :icon 8}})

(def ^:private vertical-padding
  {40 9, 32 5, 24 3})

(defn container-layout-style [size layout type]
  (let [icon-only? (= layout :icon-only)
        border     (if (and (= type :outline) (not icon-only?)) 1 0)
        left-key   (if (#{:left :both} layout) :icon :text)
        right-key  (if (#{:right :both} layout) :icon :text)
        top        (if (and (= size 24) (nil? layout)) 2.5 (vertical-padding size))
        bottom     (if (and (= size 24) (nil? layout)) 3.5 (vertical-padding size))]
    (style {:width          (when icon-only? size)
            :height         size
            :border-radius  (borders/radius size)
            :padding-left   (if icon-only? 0 (- (get-in horizontal-padding [size left-key]) border))
            :padding-right  (if icon-only? 0 (- (get-in horizontal-padding [size right-key]) border))
            :padding-top    (if icon-only? 0 (- top border))
            :padding-bottom (if icon-only? 0 (- bottom border))})))

(defn- component-state [disabled? pressed?]
  (cond
    disabled? :disabled
    pressed? :pressed
    :else :default))

(defn- primary-style [theme color state]
  (cond
    (and (= theme :theme/dark) (= state :disabled)) (style {:background-color (colors/get-color color 60)
                                                            :opacity          0.3})
    (and (= theme :theme/dark) (= state :pressed)) (style {:background-color (colors/get-color color 50)})
    (= theme :theme/dark) (style {:background-color (colors/get-color color 60)})
    (= state :disabled) (style {:background-color (colors/get-color color 50)
                                :opacity          0.3})
    (= state :pressed) (style {:background-color (colors/get-color color 60)})
    :else (style {:background-color (colors/get-color color 50)})))

(defn pressable-type-style [theme type background color disabled? pressed?]
  (let [state (component-state disabled? pressed?)]
    (if (= type :primary)
      (primary-style theme color state)
      (or (get-in background-type-styles [background theme type state])
          (get-in type-styles [theme type state])))))

(defn text-color [theme type background]
  (or (get-in background-type-styles [background theme type :text-color])
      (get-in type-styles [theme type :text-color])))

(def icon-size
  {24 12, 32 20, 40 20})

(defstyle icon-left-gap
  {:margin-right 4})

(defstyle icon-right-gap
  {:margin-left 4})

(def ^:private neutral-icon-only-types
  #{:grey :dark-grey :outline :ghost})

(def ^:private circular-icon-only-types
  #{:primary :positive :danger})

(defstyle ^:private circular-icon-only-shape
  {:border-radius (borders/radius :max)})

(defn icon-only-shape-style [layout type]
  (when (and (= layout :icon-only) (circular-icon-only-types type))
    circular-icon-only-shape))

(defstyle pressable-base-style
  {:align-items     :center
   :justify-content :center
   :flex-direction  :row})

(defstyle pressable-default-state-style
  {:transform                  [{:scale (:default-scale animations/press-feedback)}
                                {:translate-y (:default-translate-y animations/press-feedback)}]
   :transition-property        (:transition-property animations/press-feedback)
   :transition-duration        (:default-duration animations/press-feedback)
   :transition-timing-function (:default-timing-function animations/press-feedback)})

(defstyle pressable-pressed-state-style
  {:transform                  [{:scale (:pressed-scale animations/press-feedback)}
                                {:translate-y (:pressed-translate-y animations/press-feedback)}]
   :transition-property        (:transition-property animations/press-feedback)
   :transition-duration        (:pressed-duration animations/press-feedback)
   :transition-timing-function (:pressed-timing-function animations/press-feedback)})

(def font-type
  {40 :font/medium-15
   32 :font/medium-15
   24 :font/medium-13})

(defn- icon-only-color [theme type background disabled?]
  (cond
    (= theme :theme/dark)                         (colors/get-color :color/white-100)
    (and disabled? (= background :photo) (= type :grey)) (colors/get-color :color/neutral-50)
    (neutral-icon-only-types type)               (colors/get-color :color/neutral-100)
    :else                                        (colors/get-color :color/white-100)))

(defn icon-color [theme type background icon-only? disabled? pressed?]
  (if icon-only?
    (icon-only-color theme type background disabled?)
    (let [state (component-state disabled? pressed?)]
      (or (get-in background-type-state-icon-colors [background theme type state])
          (get-in background-type-styles [background theme type :icon-color])
          (get-in type-styles [theme type :icon-color])))))

(defn text-style [theme type background size]
  (style {:color                (text-color theme type background)
          :include-font-padding false
          :flex-shrink          1
          :height               (if (= size 24) 18 22)
          :line-height          (if (= size 24) 18 21.75)}))
