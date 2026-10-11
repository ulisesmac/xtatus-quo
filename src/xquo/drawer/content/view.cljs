(ns xquo.drawer.content.view
  (:require [react-native.safe-area-context.core :as safe-area]
            [react-native.utils :as rn.utils]
            [xquo.context :as context]
            [xquo.drawer.style :as style]))

(defn content [{:keys [theme blur?]
                :as   props}
               & children]
  (let [{:keys [theme]} (context/use-theme-color (if blur? :theme/dark theme))
        bottom          (safe-area/use-bottom)]
    [context/provider {:theme theme}
     (into [:rn/view (-> props
                         (dissoc :theme :blur? :handle?)
                         (assoc :style (rn.utils/add-styles style/content
                                                            (:style props))))
            (when blur?
              [:effect/view {:pointer-events :none
                             :effect         :blur
                             :blur-amount    40
                             :theme          :theme/dark
                             :style          (style/backdrop bottom)}])]
           children)]))
