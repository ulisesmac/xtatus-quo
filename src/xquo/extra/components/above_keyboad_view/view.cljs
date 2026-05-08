(ns xquo.extra.components.above-keyboad-view.view
  (:require
   [applied-science.js-interop :as j]
   [reagent-extended-compiler.utils.transforms :as xf :refer [style]]
   [reagent-extended.react :as react]
   [reagent-extended.react-native :as rn]
   [reagent.core :as r]
   ["react-native-reanimated" :refer [useSharedValue withSpring]]
   ["react-native-safe-area-context" :refer [useSafeAreaInsets]]))

(defonce keyboard-height (r/atom 0))

(defn set-kb-height! [_event]
  (reset! keyboard-height (or (:height (rn/keyboard-metrics)) 0)))

(defn use-keyboard-height-listener []
  (react/use-effect
   (fn []
     (let [show-sub (rn/add-keyboard-listener! :keyboardDidShow set-kb-height!)
           hide-sub (rn/add-keyboard-listener! :keyboardDidHide set-kb-height!)]
       (fn []
         (j/call show-sub :remove)
         (j/call hide-sub :remove))))
   []))

(def animation-params
  #js{:mass               0.95
      :damping            23
      :stiffness          300
      :overshootClamping  false
      :restSpeedThreshold 0.7})

(defn- animate-view! [shared-value to]
  (set! (.-value shared-value) (withSpring to animation-params)))

(defn view-style [shared-value]
  (style {:position :absolute
          :bottom   0
          :left     0
          :right    0
          :transform [{:translate-y shared-value}]}))

(defn view [p1 & params]
  (let [{:keys [open-inset]
         :as   props} (when (map? p1) p1)
        children     (if props params (conj params p1))
        transform-y  (useSharedValue @keyboard-height)
        bottom-inset (-> (useSafeAreaInsets) (j/get :bottom))
        target-y     (if (rn/keyboard-visible?)
                       (- (+ @keyboard-height bottom-inset (- open-inset)))
                       0)]
    (react/use-effect
     (fn []
       (animate-view! transform-y target-y))
     [target-y open-inset])
    (into [:animated/view (-> props
                              (update :style xf/add-styles (view-style transform-y))
                              (dissoc :open-inset)
                              (assoc :pointer-events :box-none))]
          children)))

(comment
 (not (identical? "object" (goog/typeOf --shv)))
 (not (identical? "object" (goog/typeOf #js{})))
 (reagent.impl.util/js-val? 1)
 (js-keys --shv)
 (reagent-extended-compiler.compiler/reanimated-val? #js{})
 )
