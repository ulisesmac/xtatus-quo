(ns xquo.components.selectors.selector.style
  (:require [reagent-extended-compiler.utils.transforms :refer [defstyle]]
            [xquo.foundations.animations :as animations]
            [xquo.foundations.borders :as borders]
            [xquo.foundations.colors :as colors]))

(defn- dark-theme? [theme]
  (= theme :theme/dark))

(defn- blur-background? [background]
  (= background :blur))

(defn- active-color [theme background color]
  (if (and (dark-theme? theme) (not (blur-background? background)))
    (colors/get-color color 60)
    (colors/get-color color 50)))

(defn- toggle-off-color [theme background]
  (cond
    (and (blur-background? background)
         (dark-theme? theme))     (colors/get-color :color/white-10)
    (blur-background? background) (colors/get-color :color/neutral-80-20)
    (dark-theme? theme)           (colors/get-color :color/neutral-80)
    :else                         (colors/get-color :color/neutral-30)))

(defn- radio-off-border-color [theme background]
  (cond
    (and (blur-background? background) (dark-theme? theme)) (colors/get-color :color/white-20)
    (blur-background? background) (colors/get-color :color/neutral-80-20)
    (dark-theme? theme) (colors/get-color :color/neutral-70)
    :else (colors/get-color :color/neutral-30)))

(defn- radio-off-background-color [theme background]
  (cond
    (blur-background? background) (colors/get-color :color/white-5)
    (dark-theme? theme) (colors/get-color :color/neutral-80-40)
    :else (colors/get-color :color/white-40)))

(defn- filled-background-color [theme background]
  (cond
    (and (blur-background? background)
         (dark-theme? theme)) (colors/get-color :color/white-10)
    (blur-background? background) (colors/get-color :color/neutral-80-10)
    (dark-theme? theme) (colors/get-color :color/neutral-80)
    :else (colors/get-color :color/neutral-30)))

(defn- state-opacity [disabled?]
  (if disabled? 0.3 1))

(defstyle toggle-base
  {:width          30
   :height         20
   :padding        2
   :border-radius  (:border/drawer borders/border-radius-values)
   :flex-direction :row
   :align-items    :center})

(defstyle radio-base
  {:width           20
   :height          20
   :border-radius   (:border/drawer borders/border-radius-values)
   :justify-content :center
   :align-items     :center})

(defstyle checkbox-base
  {:width           20
   :height          20
   :border-radius   (:border/size-16 borders/border-radius-values)
   :justify-content :center
   :align-items     :center})

(defstyle filled-checkbox-base
  {:width           20
   :height          20
   :border-radius   (:border/size-16 borders/border-radius-values)
   :justify-content :center
   :align-items     :center
   :overflow        :hidden})

(defstyle toggle-handle-base
  {:width                      16
   :height                     16
   :border-radius              (:border/drawer borders/border-radius-values)
   :background-color           (colors/get-color :color/white-100)
   :transition-property        (:transition-property animations/state-change)
   :transition-duration        (:toggle-duration animations/state-change)
   :transition-timing-function (:transition-timing-function animations/state-change)})

(defn toggle-handle-state-style [selected?]
  {:transform [{:translate-x (if selected? 10 0)}]})

(defstyle radio-dot-base
  {:width                      14
   :height                     14
   :border-radius              (:border/drawer borders/border-radius-values)
   :transition-property        (:transition-property animations/state-change)
   :transition-timing-function (:transition-timing-function animations/state-change)})

(defn radio-dot-state-style [selected?]
  {:transform           [{:scale (if selected? 1 0)}]
   :transition-duration (if selected?
                          (:radio-expand-duration animations/state-change)
                          (:radio-collapse-duration animations/state-change))})

(defstyle checkmark-image-base
  {:width     12
   :height    12
   :transform [{:rotate "180deg"}]})

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

(defn container-style [type]
  (case type
    :toggle toggle-base
    :radio radio-base
    :checkbox checkbox-base
    :filled-checkbox filled-checkbox-base
    checkbox-base))

(defn state-style [theme type background selected? disabled? color]
  (cond
    (= type :toggle)
    {:background-color (if selected?
                         (active-color theme background color)
                         (toggle-off-color theme background))
     :justify-content  :flex-start
     :opacity          (state-opacity disabled?)}

    (= type :radio)
    {:background-color (if selected?
                         :transparent
                         (radio-off-background-color theme background))
     :border-width     1.2
     :border-color     (if selected?
                         (active-color theme background color)
                         (radio-off-border-color theme background))
     :opacity          (state-opacity disabled?)}

    (= type :checkbox)
    {:background-color (if selected?
                         (active-color theme background color)
                         :transparent)
     :border-width     (if selected?
                         (if (blur-background? background) 1.2 0)
                         1.2)
     :border-color     (if selected?
                         (if (blur-background? background)
                           (active-color theme background color)
                           :transparent)
                         (radio-off-border-color theme background))
     :opacity          (state-opacity disabled?)}

    :else
    (let [background-color (filled-background-color theme background)]
      (if (and (dark-theme? theme) (not (blur-background? background)))
        {:background-color background-color
         :border-width     1.2
         :border-color     background-color
         :opacity          (state-opacity disabled?)}
        {:background-color background-color
         :opacity          (state-opacity disabled?)}))))

(defn radio-dot-style [theme background color]
  {:background-color (active-color theme background color)})
