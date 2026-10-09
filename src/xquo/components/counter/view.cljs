(ns xquo.components.counter.view
  (:require [react-native.utils :as rn.utils]
            [xquo.components.counter.style :as style]
            [xquo.components.text.view :as text]
            [xquo.context :as context]))

(def ^:private value-font
  {:default :font/medium-11
   :large   :font/medium-13})

(defn- value-layout-key [label]
  (cond
    (= label "99+")      :ninety-nine-plus
    (= label "-99")      :negative
    (<= (count label) 1) :one
    (= (count label) 2)  :two
    (= (count label) 3)  :three
    :else               :four))

(defn counter [{:keys [blur? size type]
                :or   {size :default
                       type :default}
                :as   props}
               value]
  (let [{:keys [color dark-theme?]} (context/use-theme-color)
        label                       (str value)
        layout-key                  (value-layout-key label)
        ;; Figma's negative Grey/Outline variants retain normal dark colors on blur.
        blur?                       (and blur?
                                         (not (and dark-theme?
                                                   (= layout-key :negative)
                                                   (#{:grey :outline :outline-color} type))))]
    [:rn/view (-> props
                  (dissoc :blur? :size :type :style)
                  (assoc :style (rn.utils/add-styles style/container
                                                     (style/container-style color size layout-key type dark-theme? blur? (count label))
                                                     (:style props))))
     [:rn/view {:style [style/surface
                        (style/surface-style color size layout-key type dark-theme? blur?)]}]
     [text/text {:font            (value-font size)
                 :number-of-lines 1
                 :style           (style/value-style color size layout-key type dark-theme? blur?)}
      label]]))
