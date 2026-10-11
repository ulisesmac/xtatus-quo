(ns xquo.drawer.bottom-context.view
  (:require [react-native.utils :as rn.utils :refer [defstyle]]
            [xquo.components.button.view :as button]))

(defstyle ^:private container
  {:flex-direction     :row
   :justify-content    :space-between
   :padding-horizontal 20
   :padding-vertical   12})

(defstyle ^:private side
  {:flex-direction :row
   :align-items    :center
   :gap            12})

(defn- buttons [{:keys [items blur?]}]
  (into [:rn/view {:style side}]
        (map (fn [item]
               [button/button (-> item
                                  (dissoc :label)
                                  (assoc :size  (:size item 32)
                                         :type  (:type item :outline)
                                         :blur? (:blur? item (and blur? (not (:photo? item))))))
                (:label item)]))
        items))

(defn bottom-context [{:keys [left right blur?] :as props}]
  [:rn/view (-> props
                (dissoc :left :right :blur?)
                (assoc :style (rn.utils/add-styles container (:style props))))
   [buttons {:items left :blur? blur?}]
   [buttons {:items right :blur? blur?}]])
