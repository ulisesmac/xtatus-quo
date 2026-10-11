(ns xquo.camera.controls.view
  (:require [react-native.react.core :as react]
            [react-native.utils :as rn.utils]
            [xquo.camera.controls.style :as style]
            [xquo.camera.shutter.view :as shutter]
            [xquo.camera.style :as camera-style]
            [xquo.components.button.view :as button]
            [xquo.components.segmented-tab.view :as segmented-tab]
            [xquo.context :as context]))

(defn- zoom-option [{:keys [option zoom on-zoom-change orientation]}]
  (let [value     (:value option)
        selected? (= value zoom)
        select!   (react/use-callback #(on-zoom-change value) [on-zoom-change value])]
    [:rn/view {:style (style/zoom-slot orientation)}
     [segmented-tab/segmented-tab {:size        28
                                   :type        :dark-grey
                                   :blur?       true
                                   :selected?   selected?
                                   :on-press    select!
                                   :style       (style/zoom-tab orientation selected?)
                                   :label-style (style/zoom-label selected?)}
      (:label option)]]))

(defn controls [{:keys         [zoom zoom-options on-zoom-change flash? orientation]
                 flash-props   :flash
                 flip-props    :flip
                 shutter-props :shutter
                 :or           {orientation :vertical}
                 :as           props}]
  [context/provider {:theme :theme/dark}
   [:rn/view (-> props
                 (dissoc :zoom :zoom-options :on-zoom-change :flash? :orientation :flash :flip :shutter)
                 (assoc :style (rn.utils/add-styles style/container (:style props))))
    [:rn/view {:style style/zoom-area}
     (into [:effect/view {:effect      :blur
                          :blur-amount 40
                          :theme       :theme/dark
                          :style       (style/zoom-track orientation)}]
           (map (fn [option]
                  [zoom-option {:option         option
                                :zoom           zoom
                                :on-zoom-change on-zoom-change
                                :orientation    orientation}]))
           zoom-options)]
    [:rn/view {:style camera-style/actions}
     [:rn/view {:style (camera-style/orientation orientation)}
      [button/button (assoc flash-props
                            :size   (:size flash-props 32)
                            :type   (:type flash-props :grey)
                            :photo? (:photo? flash-props true)
                            :icon   (:icon flash-props {:name (if flash? :icon/flash :icon/flash-off)}))]]
     [:rn/view {:style (camera-style/orientation orientation)}
      [shutter/shutter shutter-props]]
     [:rn/view {:style (camera-style/orientation orientation)}
      [button/button (assoc flip-props
                            :size   (:size flip-props 32)
                            :type   (:type flip-props :grey)
                            :photo? (:photo? flip-props true)
                            :icon   (:icon flip-props {:name :icon/flip}))]]]]])
