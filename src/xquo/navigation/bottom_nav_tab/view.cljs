(ns xquo.navigation.bottom-nav-tab.view
  (:require [applied-science.js-interop :as j]
            [react-native.react.core :as react]
            [react-native.utils :as rn.utils]
            [xquo.components.counter.view :as counter]
            [xquo.components.icon.view :as icon]
            [xquo.context :as context]
            [xquo.navigation.bottom-nav-tab.style :as style]))

(defn- badge-icon [{:keys [icon type counter-width counter-props]}]
  (let [{:keys [size value] :or {size :default}} counter-props
        vertical-inset                           (style/counter-inset size)
        horizontal-inset                         (if (and (= size :default) (= (count (str value)) 1)) 2 0)]
    [:svg/svg {:width 24 :height 24 :view-box "0 0 24 24"}
     [:svg/defs
      [:svg/mask {:id "bottom-nav-tab-badge" :x 0 :y 0 :width 24 :height 24 :mask-units :userSpaceOnUse}
       [:svg/rect {:width 24 :height 24 :fill :white}]
       (if (= type :notification)
         [:svg/circle {:cx 22 :cy 2 :r 5 :fill :black}]
         [:svg/rect {:x      (+ 11 horizontal-inset)
                     :y      (- vertical-inset 9)
                     :width  (+ 2 (- counter-width (* 2 horizontal-inset)))
                     :height (+ 2 (- 20 (* 2 vertical-inset)))
                     :rx     (inc (style/counter-radius size))
                     :fill   :black}])]]
     [:svg/g {:mask "url(#bottom-nav-tab-badge)"}
      [:svg/foreign-object {:width 24 :height 24}
       [:rn/view {:collapsable false :style style/icon}
        [icon/view icon]]]]]))

(defn bottom-nav-tab [{:keys      [icon active? badge disabled? on-press-in on-press-out]
                       root-style :style
                       :as        props}]
  (let [{:keys [theme] :as theme-color}    (context/use-theme-color)
        {:keys [type] :or {type :counter}} badge
        {counter-props :counter}           badge
        [pressed? set-pressed!]            (react/use-state false)
        [counter-width set-counter-width!] (react/use-state 20)
        pressed?                           (and pressed? (not disabled?))
        icon-props                         (assoc icon :size  (:size icon 24)
                                                  :color (:color icon (style/icon-color theme active?)))
        press-in!                          (react/use-callback (fn [event]
                                                                 (set-pressed! true)
                                                                 (when on-press-in (on-press-in event)))
                                                               [on-press-in])
        press-out!                         (react/use-callback (fn [event]
                                                                 (set-pressed! false)
                                                                 (when on-press-out (on-press-out event)))
                                                               [on-press-out])
        measure!                           (react/use-callback #(set-counter-width! (j/get-in % [:nativeEvent :layout :width])) [])]
    [:animated/pressable (-> props
                             (dissoc :icon :active? :badge :disabled?)
                             (assoc :disabled     disabled?
                                    :on-press-in  press-in!
                                    :on-press-out press-out!
                                    :style        (rn.utils/add-styles style/container
                                                                       (style/appearance theme pressed? disabled?)
                                                                       root-style)))
     [:rn/view {:pointer-events :none :style style/icon}
      (if badge
        [badge-icon {:icon icon-props :type type :counter-width counter-width :counter-props counter-props}]
        [icon/view icon-props])
      (when badge
        [:rn/view {:on-layout (when (= type :counter) measure!) :style style/badge}
         (case type
           :notification [:rn/view {:style (style/notification theme-color pressed?)}]
           :counter (let [{:keys [value]} counter-props]
                      [counter/counter (dissoc counter-props :value) value])
           nil)])]]))
