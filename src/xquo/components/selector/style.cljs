(ns xquo.components.selector.style
  (:require [reagent-extended-compiler.utils.transforms :refer [defstyle style]]
            [xquo.foundations.borders :as borders]
            [xquo.foundations.colors :as colors]))

(defn- dark-theme? [theme]
  (= theme :theme/dark))

(defn- blur-background? [background]
  (= background :blur))

(defn- active-color [theme background]
  (if (and (dark-theme? theme) (not (blur-background? background)))
    (colors/get-color :color/blue 60)
    (colors/get-color :color/blue 50)))

(defn- toggle-off-color [theme background]
  (cond
    (and (blur-background? background) (dark-theme? theme)) (colors/get-color :color/white-10)
    (blur-background? background)                           (colors/get-color :color/neutral-80-20)
    (dark-theme? theme)                                    (colors/get-color :color/neutral-80)
    :else                                                  (colors/get-color :color/neutral-30)))

(defn- radio-off-border-color [theme background]
  (cond
    (and (blur-background? background) (dark-theme? theme)) (colors/get-color :color/white-20)
    (blur-background? background)                           (colors/get-color :color/neutral-80-20)
    (dark-theme? theme)                                    (colors/get-color :color/neutral-70)
    :else                                                  (colors/get-color :color/neutral-30)))

(defn- radio-off-background-color [theme background]
  (cond
    (blur-background? background) (colors/get-color :color/white-5)
    (dark-theme? theme)           (colors/get-color :color/neutral-80-40)
    :else                         (colors/get-color :color/white-40)))

(defn- filled-background-color [theme background]
  (cond
    (and (blur-background? background) (dark-theme? theme)) (colors/get-color :color/white-10)
    (blur-background? background)                           (colors/get-color :color/neutral-80-10)
    (dark-theme? theme)                                    (colors/get-color :color/neutral-80)
    :else                                                  (colors/get-color :color/neutral-30)))

(defn- state-opacity [disabled?]
  (if disabled?
    0.3
    1))

(defstyle toggle-base
  {:width         30
   :height        20
   :padding       2
   :border-radius (:border/drawer borders/border-radius-values)
   :flex-direction :row
   :align-items   :center})

(defstyle radio-base
  {:width          20
   :height         20
   :border-radius  (:border/drawer borders/border-radius-values)
   :justify-content :center
   :align-items    :center})

(defstyle checkbox-base
  {:width          20
   :height         20
   :border-radius  (:border/size-16 borders/border-radius-values)
   :justify-content :center
   :align-items    :center})

(defstyle filled-checkbox-base
  {:width          20
   :height         20
   :border-radius  (:border/size-16 borders/border-radius-values)
   :justify-content :center
   :align-items    :center
   :overflow       :hidden})

(defstyle toggle-handle-base
  {:width          16
   :height         16
   :border-radius  (:border/drawer borders/border-radius-values)
   :background-color (colors/get-color :color/white-100)})

(defstyle radio-dot-base
  {:width          14
   :height         14
   :border-radius  (:border/drawer borders/border-radius-values)})

(defstyle checkmark-image-base
  {:width     12
   :height    12
   :transform [{:rotate "180deg"}]})

(defn container-style [type]
  (case type
    :toggle          toggle-base
    :radio           radio-base
    :checkbox        checkbox-base
    :filled-checkbox filled-checkbox-base
    checkbox-base))

(defn state-style [theme type background enabled? disabled?]
  (let [active? enabled?]
    (cond
      (= type :toggle)
      (style {:background-color (if active?
                                  (active-color theme background)
                                  (toggle-off-color theme background))
              :justify-content  (if active? :flex-end :flex-start)
              :opacity          (state-opacity disabled?)})

      (= type :radio)
      (style {:background-color (if active?
                                  :transparent
                                  (radio-off-background-color theme background))
              :border-width     1.2
              :border-color     (if active?
                                  (active-color theme background)
                                  (radio-off-border-color theme background))
              :opacity          (state-opacity disabled?)})

      (= type :checkbox)
      (style {:background-color (if active?
                                  (active-color theme background)
                                  :transparent)
              :border-width     (if active?
                                  (if (blur-background? background) 1.2 0)
                                  1.2)
              :border-color     (if active?
                                  (if (blur-background? background)
                                    (active-color theme background)
                                    :transparent)
                                  (radio-off-border-color theme background))
              :opacity          (state-opacity disabled?)})

      :else
      (let [background-color (filled-background-color theme background)]
        (if (and (dark-theme? theme) (not (blur-background? background)))
          (style {:background-color background-color
                  :border-width     1.2
                  :border-color     background-color
                  :opacity          (state-opacity disabled?)})
          (style {:background-color background-color
                  :opacity          (state-opacity disabled?)}))))))

(def toggle-handle-style
  toggle-handle-base)

(defn radio-dot-style [theme background]
  (style {:background-color (active-color theme background)}))
