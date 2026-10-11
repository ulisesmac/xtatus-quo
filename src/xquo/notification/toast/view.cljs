(ns xquo.notification.toast.view
  (:require [react-native.utils :as rn.utils :refer [style]]
            [xquo.components.button.view :as button]
            [xquo.components.icon.view :as icon]
            [xquo.components.text.view :as text]
            [xquo.context :as context]
            [xquo.notification.style :as notification-style]
            [xquo.notification.toast.style :as styles]))

(def ^:private icons
  {:positive :icon/check-circle-outline
   :negative :icon/alert-outline
   :neutral  :icon/placeholder})

(defn toast [{:keys [type icon button]
              :or   {type :neutral}
              :as   props}
             message]
  (let [{:keys [dark-theme?] :as theme} (context/use-theme-color)
        surface-theme                   (if dark-theme? :theme/light :theme/dark)]
    [context/provider {:theme surface-theme}
     [:rn/view (-> props
                   (dissoc :type :icon :button)
                   (assoc :style (rn.utils/add-styles (notification-style/frame 12 12) (:style props))))
      [:effect/view {:pointer-events :none
                     :effect         :blur
                     :blur-amount    40
                     :theme          surface-theme
                     :style          (notification-style/backdrop theme 12)}]
      [:rn/view {:style (styles/row button)}
       [:rn/view {:style (styles/info type button)}
        [icon/view (assoc icon
                          :name  (:name icon (icons type))
                          :size  (:size icon 20)
                          :color (:color icon (styles/icon-color theme type)))]
        [:rn/view {:style styles/message}
         [text/text {:font  :font/medium-13
                     :style (style {:color (notification-style/foreground theme)})}
          message]]]
       (when button
         [button/button (-> button
                            (dissoc :label)
                            (assoc :size  (:size button 24)
                                   :type  (:type button :grey)
                                   :blur? (:blur? button true)))
          (:label button)])]]]))
