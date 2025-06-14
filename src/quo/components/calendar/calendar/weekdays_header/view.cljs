(ns quo.components.calendar.calendar.weekdays-header.view
  (:require
    [quo.components.calendar.calendar.weekdays-header.style :as style]
    [quo.components.markdown.text :as text]
    [quo.context]
    [react-native.core :as rn]
    [utils.datetime :as datetime]
    [utils.i18n :as i18n]))

(defn view
  []
  (let [theme (quo.context/use-theme)]
    [rn/view
     {:style style/container-weekday-row}
     (for [weekday datetime/weekday-names]
       [rn/view
        {:style style/container-weekday
         :key   weekday}
        [text/text
         {:weight :medium
          :size   :paragraph-2
          :style  (style/text-weekdays theme)}
         (str (i18n/label weekday))]])]))
