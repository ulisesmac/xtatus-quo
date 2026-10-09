(ns xquo.divider.new.view
  (:require [react-native.utils :as rn.utils]
            [xquo.components.text.view :as text]
            [xquo.context :as context]
            [xquo.divider.new.style :as style]
            [xquo.divider.style :as divider-style]
            [xquo.foundations.colors :as colors]))

(defn divider-new [{:keys [title] :as props}]
  (let [{:keys [color dark-theme?]} (context/use-theme-color)
        level                       (if dark-theme? 60 50)
        foreground                  (colors/get-color color level)]
    [:rn/view (-> props
                  (dissoc :title :style)
                  (assoc :style (rn.utils/add-styles style/container
                                                     (divider-style/gradient color level)
                                                     (:style props))))
     [text/text {:font            :font/medium-13
                 :number-of-lines 1
                 :style           [divider-style/title (divider-style/title-color foreground)]}
      title]]))
