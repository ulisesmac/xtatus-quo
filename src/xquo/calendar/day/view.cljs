(ns xquo.calendar.day.view
  (:require [react-native.utils :as rn.utils]
            [xquo.calendar.day.style :as style]
            [xquo.components.text.view :as text]
            [xquo.context :as context]))

(defn calendar-day [{:keys [selected? today? disabled?] :as props} label]
  (let [{:keys [color dark-theme?]} (context/use-theme-color)]
    [:rn/pressable (-> props
                       (dissoc :selected? :today? :disabled? :style)
                       (assoc :disabled disabled?
                              :style    (rn.utils/add-styles style/container
                                                             (style/container-style color dark-theme? selected? disabled?)
                                                             (:style props))))
     [text/text {:font            :font/medium-13
                 :number-of-lines 1
                 :style           [style/title (style/title-style dark-theme? selected?)]}
      label]
     (when today?
       [:rn/view {:pointer-events :none
                  :style          [style/today (style/today-style color dark-theme? selected?)]}])]))
