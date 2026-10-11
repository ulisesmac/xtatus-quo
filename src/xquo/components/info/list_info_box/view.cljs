(ns xquo.components.info.list-info-box.view
  (:require [react-native.utils :as rn.utils]
            [xquo.components.info.list-info-box.style :as style]
            [xquo.components.list.view :as list]
            [xquo.components.text.view :as text]
            [xquo.context :as context]))

(defn- item-content [content]
  (if (string? content)
    [text/text {:font :font/regular-13 :style style/description} content]
    content))

(defn list-info-box [{:keys [title items] :as props}]
  (let [theme (context/use-theme)]
    [:rn/view (-> props
                  (dissoc :title :items)
                  (assoc :style (rn.utils/add-styles (style/container theme) (:style props))))
     (when title
       [text/text {:font :font/regular-13 :style (style/title theme)} title])
     [list/view {:style style/items
                 :items (mapv (fn [{:keys [description title] :as item}]
                                (cond-> (assoc item :style (rn.utils/add-styles style/item (:style item)))
                                  description (assoc :description [item-content description])
                                  (and (not description) title) (assoc :title [item-content title])))
                              items)}]]))
