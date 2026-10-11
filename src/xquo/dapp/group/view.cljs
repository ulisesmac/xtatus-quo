(ns xquo.dapp.group.view
  (:require [react-native.utils :as rn.utils]
            [xquo.components.avatar.view :as avatar]
            [xquo.components.button.view :as button]
            [xquo.components.icon.view :as icon]
            [xquo.components.text.view :as text]
            [xquo.context :as context]
            [xquo.dapp.group.style :as style]
            [xquo.dapp.list-card.style :as card-style]
            [xquo.dapp.list-card.view :as list-card]))

(defn- header-view [{:keys        [title description description-icon action blur?]
                     avatar-props :avatar}]
  (let [theme (context/use-theme)]
    [:rn/view {:style style/header}
     [avatar/avatar (assoc avatar-props :size (:size avatar-props 32))]
     [:rn/view {:style style/text}
      [:rn/view {:style style/title-row}
       [text/text {:font :font/semibold-15 :number-of-lines 1 :style style/title} title]
       (when (and description-icon (nil? description))
         [icon/view (assoc description-icon
                           :size  (:size description-icon 12)
                           :color (:color description-icon (card-style/secondary-color theme blur?)))])]
      (when description
        [:rn/view {:style card-style/description-row}
         [text/text {:font :font/regular-13 :number-of-lines 1 :style (card-style/description theme blur?)} description]
         (when description-icon
           [icon/view (assoc description-icon
                             :size  (:size description-icon 12)
                             :color (:color description-icon (card-style/secondary-color theme blur?)))])])]
     (when action
       [button/button (assoc action
                             :size  (:size action 32)
                             :type  (:type action :outline)
                             :blur? (:blur? action blur?)
                             :icon  (:icon action {:name :icon/options}))])]))

(defn group [{:keys [header items blur?] :as props}]
  (let [theme (context/use-theme)]
    [:rn/view (-> props
                  (dissoc :header :items :blur?)
                  (assoc :style (rn.utils/add-styles style/container (:style props))))
     [:rn/view {:pointer-events :none :style (style/border theme blur?)}]
     [header-view (assoc header :blur? blur?)]
     (into [:rn/view {:style style/items}]
           (map (fn [item]
                  [list-card/list-card (-> item
                                           (dissoc :key)
                                           (assoc :blur? (:blur? item blur?)
                                                  :style (rn.utils/add-styles (when (and (:action item)
                                                                                         (#{:account :dapp} (:variant item :dapp)))
                                                                                style/list-card)
                                                                              (:style item))))]))
           items)]))
