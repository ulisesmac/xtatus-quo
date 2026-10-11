(ns xquo.drawer.bottom-actions.view
  (:require [react-native.utils :as rn.utils]
            [xquo.components.button.view :as button]
            [xquo.components.icon.view :as icon]
            [xquo.components.tags.context-tag.view :as context-tag]
            [xquo.components.text.view :as text]
            [xquo.context :as context]
            [xquo.drawer.bottom-actions.style :as style]))

(defn- description-view [{:keys [blur? scroll? description context-tag]}]
  (let [theme-color                    (context/use-theme-color)
        {:keys [position status text]} description
        error?                         (= status :error)
        text-style                     (style/text theme-color blur? scroll? position error?)]
    [:rn/view {:style (style/description position error?)}
     (when error?
       [icon/view {:name  :icon/alert-outline
                   :size  16
                   :color (.-color text-style)}])
     [text/text {:font :font/regular-13 :style text-style} text]
     (when context-tag
       [context-tag/context-tag (-> context-tag
                                    (dissoc :label)
                                    (assoc :blur? (:blur? context-tag blur?)))
        (:label context-tag)])]))

(defn bottom-actions [{:keys [buttons description context-tag blur? scroll?] :as props}]
  (let [{:keys [dark-theme?]} (context/use-theme-color)
        description-position  (:position description)
        dark-solid?           (and dark-theme? (not blur?) (not scroll?))
        secondary-type        (if (and dark-solid? (not= description-position :top)) :dark-grey :grey)
        secondary-blur?       (or scroll? (and dark-theme? blur?))]
    [:rn/view (-> props
                  (dissoc :buttons :description :context-tag :blur? :scroll?)
                  (assoc :style (rn.utils/add-styles style/container (:style props))))
     (when (= description-position :top)
       [description-view {:blur?       blur?
                          :scroll?     scroll?
                          :description description
                          :context-tag context-tag}])
     (into [:rn/view {:style style/actions}]
           (map-indexed (fn [index action]
                          (let [primary? (or (= (count buttons) 1) (pos? index))]
                            [button/button (-> action
                                               (dissoc :label)
                                               (assoc :size  (:size action 40)
                                                      :type  (:type action (if primary? :primary secondary-type))
                                                      :blur? (:blur? action (and (not primary?)
                                                                                 (not (:photo? action))
                                                                                 secondary-blur?))
                                                      :style (rn.utils/add-styles style/action (:style action))))
                             (:label action)])))
           buttons)
     (when (= description-position :bottom)
       [description-view {:blur?       blur?
                          :scroll?     scroll?
                          :description description}])]))
