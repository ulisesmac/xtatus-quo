(ns xquo.components.counter.step.view
  (:require [react-native.utils :as rn.utils]
            [xquo.components.counter.step.style :as style]
            [xquo.components.text.view :as text]
            [xquo.context :as context]))

(defn step [{:keys [type blur?]
             :or   {type :neutral}
             :as   props}
            value]
  (let [{:keys [color dark-theme?]} (context/use-theme-color)
        label                       (str value)
        characters                  (max 1 (count label))]
    [:rn/view (-> props
                  (dissoc :type :blur? :style)
                  (assoc :style (rn.utils/add-styles style/container
                                                     (style/container-width-style characters)
                                                     (:style props))))
     [:rn/view {:style [style/surface
                        (style/surface-style color type dark-theme? blur? characters)]}]
     [text/text {:font            :font/medium-11
                 :number-of-lines 1
                 :style           (style/value-style type dark-theme? characters)}
      label]]))
