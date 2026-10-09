(ns xquo.divider.strength.view
  (:require [react-native.utils :as rn.utils]
            [xquo.components.icon.view :as icon]
            [xquo.components.text.view :as text]
            [xquo.divider.strength.style :as style]
            [xquo.divider.style :as divider-style]))

(def ^:private circumference (* 2 js/Math.PI 6.5))

(defn- progress-icon [{:keys [status color]}]
  [:svg/svg {:width 16 :height 16 :viewBox "0 0 16 16" :fill "none"}
   [:svg/circle {:cx 8 :cy 8 :r 6.5 :stroke (style/track-color status) :stroke-width 1.2}]
   [:svg/circle {:cx               8
                 :cy               8
                 :r                6.5
                 :stroke           color
                 :stroke-width     1.2
                 :stroke-dasharray (str (* circumference (style/progress status)) " " circumference)
                 :transform        "rotate(-90 8 8)"}]])

(defn divider-strength [{:keys [status title]
                         :or   {status :informative}
                         :as   props}]
  (let [color (style/foreground status)]
    [:rn/view (-> props
                  (dissoc :status :title :style)
                  (assoc :style (rn.utils/add-styles style/container
                                                     (style/background status)
                                                     (when (= status :informative) style/informative-container)
                                                     (:style props))))
     (cond
       (= status :alert) [icon/view {:name :icon/alert :size 16 :color color}]
       (style/progress status) [progress-icon {:status status :color color}])
     [text/text {:font            :font/medium-13
                 :number-of-lines 1
                 :style           [divider-style/title style/title (divider-style/title-color color)]}
      title]]))
