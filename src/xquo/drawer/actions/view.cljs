(ns xquo.drawer.actions.view
  (:require [react-native.utils :as rn.utils :refer [defstyle]]
            [xquo.drawer.action.view :as action]
            [xquo.drawer.divider.view :as divider]))

(defstyle ^:private container
  {:padding-bottom 8})

(defn actions [{:keys [items blur?] :as props}]
  (into [:rn/view (-> props
                      (dissoc :items :blur?)
                      (assoc :style (rn.utils/add-styles container (:style props))))]
        (map (fn [item]
               (if (= item :divider)
                 [divider/divider {:blur? blur?}]
                 [action/action (-> item
                                    (dissoc :key)
                                    (assoc :blur? (:blur? item blur?)))])))
        items))
