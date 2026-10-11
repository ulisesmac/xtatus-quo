(ns xquo.community.role.view
  (:require [react-native.utils :refer [add-styles]]
            [xquo.community.role.style :as style]
            [xquo.components.tags.context-tag.view :as context-tag]
            [xquo.components.text.view :as text]
            [xquo.context :as context]))

(defn role [{:keys [label tag] :as props}]
  (let [theme (context/use-theme)]
    [:rn/view {:style (add-styles (style/container theme) (:style props))}
     [text/text {:font :font/regular-13 :style style/label} label]
     [context-tag/context-tag (-> tag
                                  (dissoc :label)
                                  (assoc :type (:type tag :icon)
                                         :size (:size tag 24)))
      (:label tag)]]))
