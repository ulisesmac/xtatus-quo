(ns xquo.divider.line.view
  (:require [react-native.utils :as rn.utils]
            [xquo.context :as context]
            [xquo.divider.line.style :as style]))

(defn divider-line [{:keys [blur? dashed?] :as props}]
  (let [theme (context/use-theme)]
    [:rn/view (-> props
                  (dissoc :blur? :dashed? :style :layout :entering :exiting)
                  (assoc :style (rn.utils/add-styles style/container-base (:style props))))
     [:svg/svg {:width "100%" :height 1 :style style/line-base}
      [:svg/line {:x1               0
                  :x2               "100%"
                  :y1               0.5
                  :y2               0.5
                  :stroke           (style/line-color theme blur? dashed?)
                  :stroke-width     1
                  :stroke-dasharray (when dashed? "3 4")}]]]))
