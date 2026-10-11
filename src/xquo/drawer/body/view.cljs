(ns xquo.drawer.body.view
  (:require [react-native.utils :as rn.utils :refer [defstyle]]
            [xquo.components.text.view :as text]))

(defstyle ^:private container
  {:padding-horizontal 20
   :padding-bottom     12})

(defn body [props & children]
  (into [:rn/view (-> props
                      (dissoc :blur?)
                      (assoc :style (rn.utils/add-styles container (:style props))))]
        (map (fn [child]
               (if (string? child)
                 [text/text {:font :font/regular-13} child]
                 child)))
        children))
