(ns xquo.components.selectors.selector.style
  (:require [react-native.utils :refer [defstyle style]]
            [xquo.foundations.animations :as animations]
            [xquo.foundations.colors :as colors]))

(defn- active-color [{:keys [dark-theme? color]} blur?]
  (colors/get-color color (if (and dark-theme? (not blur?)) 60 50)))

(defn- off-border [{:keys [dark-theme?]} blur?]
  (let [color (cond
                (and dark-theme? blur?) :color/white-20
                blur?                  :color/neutral-80-20
                dark-theme?            :color/neutral-70
                :else                  :color/neutral-30)]
    (colors/get-color color)))

(defn- off-fill [{:keys [dark-theme?]} type blur?]
  (let [color (cond
                (and dark-theme? blur?)      :color/white-10
                (and blur? (= type :toggle)) :color/neutral-80-20
                blur?                       :color/neutral-80-10
                dark-theme?                 :color/neutral-80
                :else                       :color/neutral-30)]
    (colors/get-color color)))

(defstyle ^:private toggle
  {:width 30 :height 20 :padding 2 :border-radius 20 :justify-content :center})

(defstyle ^:private radio
  {:width 20 :height 20 :border-radius 10 :justify-content :center :align-items :center})

(defstyle ^:private checkbox
  {:width 20 :height 20 :border-radius 6 :justify-content :center :align-items :center})

(def containers
  {:toggle toggle :radio radio :checkbox checkbox :filled-checkbox checkbox})

(defn state-style [{:keys [light-theme? dark-theme?] :as theme-color} type blur? selected? disabled?]
  (let [filled?      (= type :filled-checkbox)
        toggle?      (= type :toggle)
        radio?       (= type :radio)
        active       (active-color theme-color blur?)
        light-radio? (and radio? light-theme? (not blur?) (not selected?))
        fill         (cond
                       filled?                      (off-fill theme-color type blur?)
                       (and toggle? (not selected?)) (off-fill theme-color type blur?)
                       (and selected? (not radio?))  active
                       light-radio?                 (colors/get-color :color/white-40)
                       :else                        "transparent")
        border-width (cond
                       toggle?                                 0
                       (and filled? dark-theme? (not blur?))    1.2
                       filled?                                 0
                       (and (not radio?) selected? (not blur?)) 0
                       :else                                   1.2)
        border-color (cond
                       filled?   fill
                       selected? active
                       :else     (off-border theme-color blur?))]
    (style {:background-color fill
            :border-width     border-width
            :border-color     border-color
            :opacity          (if disabled? 0.3 1)})))

(defstyle toggle-handle
  {:width                      16
   :height                     16
   :border-radius              8
   :background-color           (colors/get-color :color/white)
   :transition-property        :transform
   :transition-duration        (:toggle-duration animations/state-change)
   :transition-timing-function (:transition-timing-function animations/state-change)})

(defn handle-position [selected?]
  (style {:transform [{:translate-x (if selected? 10 0)}]}))

(defn radio-dot [theme-color blur? selected?]
  (let [duration-key (if selected? :radio-expand-duration :radio-collapse-duration)]
    (style {:width                      14
            :height                     14
            :border-radius              7
            :background-color           (active-color theme-color blur?)
            :transform                  [{:scale (if selected? 1 0)}]
            :transition-property        :transform
            :transition-duration        (get animations/state-change duration-key)
            :transition-timing-function (:transition-timing-function animations/state-change)})))

(defstyle checkmark
  {:transform [{:translate-x 0.5} {:translate-y 0.5}]})
