(ns xquo.browser.navigation.view
  (:require [react-native.utils :as rn.utils]
            [xquo.browser.input.view :as input]
            [xquo.browser.navigation.style :as style]
            [xquo.components.avatar.view :as avatar]
            [xquo.components.button.view :as button]))

(defn- navigation-button [{:keys [props icon blur?]}]
  [button/button (assoc props
                        :size  (:size props 32)
                        :type  (:type props :outline)
                        :blur? (:blur? props blur?)
                        :icon  (:icon props {:name icon}))])

(defn navigation [{:keys        [back forward reload blur?]
                   input-props  :input
                   avatar-props :avatar
                   :as          props}]
  [:rn/view (-> props
                (dissoc :back :forward :reload :blur? :input :avatar)
                (assoc :style (rn.utils/add-styles style/container (:style props))))
   [input/input (assoc input-props :blur? (:blur? input-props blur?))]
   [:rn/view {:style style/actions}
    [avatar/avatar (assoc avatar-props
                          :size    (:size avatar-props 32)
                          :shape   (:shape avatar-props :rounded-square)
                          :variant (:variant avatar-props :solid))]
    [:rn/view {:style style/trailing}
     [:rn/view {:style style/history}
      [navigation-button {:props back :icon :icon/chevron-left :blur? blur?}]
      [navigation-button {:props forward :icon :icon/chevron-right :blur? blur?}]]
     [navigation-button {:props reload :icon :icon/refresh :blur? blur?}]]]])
