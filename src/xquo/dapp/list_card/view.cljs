(ns xquo.dapp.list-card.view
  (:require [react-native.react.core :as react]
            [react-native.utils :as rn.utils]
            [xquo.components.avatar.view :as avatar]
            [xquo.components.icon.view :as icon]
            [xquo.components.text.view :as text]
            [xquo.context :as context]
            [xquo.dapp.list-card.style :as style]))

(defn list-card [{:keys        [variant title description description-icon action blur? on-press on-press-in on-press-out]
                  avatar-props :avatar
                  :or          {variant :dapp}
                  :as          props}]
  (let [{:keys [theme] :as theme-color} (context/use-theme-color)
        [pressed? set-pressed!]         (react/use-state false)
        press-in!                       (react/use-callback (fn [event]
                                                              (set-pressed! true)
                                                              (when on-press-in (on-press-in event)))
                                                            [on-press-in])
        press-out!                      (react/use-callback (fn [event]
                                                              (set-pressed! false)
                                                              (when on-press-out (on-press-out event)))
                                                            [on-press-out])]
    [(if on-press :animated/pressable :animated/view)
     (cond-> (-> props
                 (dissoc :variant :title :description :description-icon :action :blur? :avatar :on-press-in :on-press-out)
                 (assoc :style (rn.utils/add-styles (style/container theme-color variant blur? pressed? action)
                                                    (:style props))))
       on-press (assoc :on-press-in press-in! :on-press-out press-out!))
     (when-not blur?
       [:rn/view {:pointer-events :none :style (style/border theme pressed?)}])
     [:rn/view {:style style/info}
      [avatar/avatar (assoc avatar-props
                            :size  (:size avatar-props 32)
                            :shape (:shape avatar-props (if (= variant :account) :rounded-square :circle))
                            :blur? (:blur? avatar-props blur?))]
      [:rn/view {:style style/text}
       [text/text {:font            (if (= variant :smart-contract) :font/medium-13 :font/semibold-13)
                   :number-of-lines 1
                   :style           [style/title (when (= variant :smart-contract) style/monospace)]}
        title]
       (when description
         [:rn/view {:style style/description-row}
          [text/text {:font            (if (= variant :account) :font/monospace-13 :font/regular-13)
                      :number-of-lines 1
                      :style           (style/description theme blur?)}
           description]
          (when description-icon
            [icon/view (assoc description-icon
                              :size  (:size description-icon 12)
                              :color (:color description-icon (style/secondary-color theme blur?)))])])]]
     (when action
       [:rn/pressable (assoc (dissoc action :icon)
                             :style    (rn.utils/add-styles style/action (:style action))
                             :hit-slop (:hit-slop action 6))
        [icon/view (assoc (:icon action)
                          :name  (get-in action [:icon :name] (if (= variant :approval) :icon/chevron-right :icon/options))
                          :size  (get-in action [:icon :size] 20)
                          :color (get-in action [:icon :color] (style/action-color theme variant blur?)))]])]))
