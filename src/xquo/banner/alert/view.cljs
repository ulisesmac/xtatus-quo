(ns xquo.banner.alert.view
  (:require [react-native.utils :as rn.utils]
            [xquo.banner.alert.style :as style]
            [xquo.components.button.view :as button]
            [xquo.components.icon.view :as icon]
            [xquo.components.text.view :as text]
            [xquo.context :as context]
            [xquo.foundations.colors :as colors]))

(defn banner-alert [{:keys [title icon button] :as props}]
  (let [dark-theme? (:dark-theme? (context/use-theme-color))
        foreground  (colors/get-color :color/danger (if dark-theme? 60 50))]
    [:rn/view (-> props
                  (dissoc :title :icon :button :style)
                  (assoc :style (rn.utils/add-styles style/container
                                                     (style/container-style dark-theme? button)
                                                     (:style props))))
     [:rn/view {:style [style/label (style/label-style button)]}
      [:rn/view {:style style/label-content}
       [icon/view (assoc icon
                         :name  (:name icon :icon/alert)
                         :size  (:size icon 16)
                         :color (:color icon #js [foreground (colors/get-color :color/white-100)])
                         :style (rn.utils/add-styles style/icon (:style icon)))]
       [text/text {:font :font/regular-13 :style (style/title-style foreground)}
        title]]]
     (when button
       [button/button (-> button
                          (dissoc :label)
                          (assoc :type (:type button :danger)
                                 :size (:size button 24)))
        (:label button)])]))
