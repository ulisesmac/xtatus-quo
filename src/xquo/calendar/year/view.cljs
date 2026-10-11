(ns xquo.calendar.year.view
  (:require [react-native.utils :as rn.utils]
            [xquo.calendar.year.style :as style]
            [xquo.components.text.view :as text]
            [xquo.context :as context]))

(defn calendar-year [{:keys [selected? disabled?] :as props} value]
  (let [dark-theme? (:dark-theme? (context/use-theme-color))]
    [:rn/pressable (-> props
                       (dissoc :selected? :disabled? :style)
                       (assoc :disabled disabled?
                              :style    (rn.utils/add-styles style/container
                                                             (style/container-style dark-theme? selected? disabled?)
                                                             (:style props))))
     [text/text {:font            :font/medium-13
                 :number-of-lines 1
                 :style           [style/title (style/title-style dark-theme? selected?)]}
      value]]))
