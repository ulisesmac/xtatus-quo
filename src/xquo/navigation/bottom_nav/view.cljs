(ns xquo.navigation.bottom-nav.view
  (:require [react-native.utils :as rn.utils]
            [xquo.context :as context]
            [xquo.navigation.bottom-nav.style :as style]
            [xquo.navigation.bottom-nav-tab.view :as tab]))

(defn bottom-nav [{:keys [items background-color blur-amount]
                   :or   {blur-amount 40}
                   :as   props}]
  [context/provider {:theme :theme/dark}
   (into [:rn/view (-> props
                       (dissoc :items :background-color :blur-amount)
                       (assoc :style (rn.utils/add-styles (style/container (count items)) (:style props))))
          [:effect/view {:pointer-events :none
                         :effect         :blur
                         :blur-amount    blur-amount
                         :theme          :theme/dark
                         :style          (style/backdrop background-color)}]]
         (map (fn [item]
                [tab/bottom-nav-tab (assoc item :style (rn.utils/add-styles style/tab (:style item)))]))
         items)])
