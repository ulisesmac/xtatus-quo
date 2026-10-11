(ns xquo.camera.crop.view
  (:require [react-native.utils :as rn.utils]
            [xquo.camera.crop.style :as style]
            [xquo.camera.shutter.view :as shutter]
            [xquo.camera.style :as camera-style]
            [xquo.components.button.view :as button]
            [xquo.context :as context]))

(defn crop [{:keys         [orientation]
             confirm-props :confirm
             rotate-props  :rotate
             :or           {orientation :vertical}
             :as           props}]
  [context/provider {:theme :theme/dark}
   [:rn/view (-> props
                 (dissoc :orientation :confirm :rotate)
                 (assoc :style (rn.utils/add-styles style/container (:style props))))
    [:rn/view {:style (camera-style/orientation orientation)}
     [shutter/shutter (assoc confirm-props :action :confirm)]]
    [:rn/view {:style [style/rotate-slot (camera-style/orientation orientation)]}
     [button/button (assoc rotate-props
                           :size  (:size rotate-props 32)
                           :type  (:type rotate-props :grey)
                           :blur? (:blur? rotate-props true)
                           :icon  (:icon rotate-props {:name :icon/rotate}))]]]])
