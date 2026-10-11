(ns xquo.community.channel-actions.view
  (:require [react-native.utils :refer [add-styles]]
            [xquo.community.channel-action.view :as channel-action]
            [xquo.community.channel-actions.style :as style]
            [xquo.context :as context]))

(defn channel-actions [{:keys [items columns] :or {columns 3} :as props}]
  (let [theme-color (context/use-theme-color)
        rows        (partition-all columns items)]
    [:rn/view {:style (add-styles (style/container theme-color (> (count rows) 1)) (:style props))}
     (for [[row-index row] (map-indexed vector rows)]
       ^{:key row-index}
       [:rn/view {:style style/row}
        (for [[item-index item] (map-indexed vector row)]
          ^{:key (or (:id item) item-index)}
          [channel-action/channel-action (assoc item
                                                :style (add-styles (when-not (and (= (count row) 2) (= item-index 1)) style/fill)
                                                                   (:style item)))])])]))
