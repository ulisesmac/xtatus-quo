(ns xquo.drawer.label.view
  (:require [react-native.utils :as rn.utils :refer [defstyle style]]
            [xquo.components.counter.view :as counter]
            [xquo.components.text.view :as text]
            [xquo.context :as context]
            [xquo.drawer.style :as drawer-style]))

(defstyle ^:private container
  {:padding-horizontal 20
   :padding-bottom     12
   :flex-direction     :row
   :align-items        :center
   :gap                12})

(defn label [{:keys [label counter blur?] :as props}]
  (let [theme (context/use-theme)]
    [:rn/view (-> props
                  (dissoc :label :counter :blur?)
                  (assoc :style (rn.utils/add-styles container (:style props))))
     [text/text {:font  :font/medium-13
                 :style (style {:flex  1
                                :color (drawer-style/secondary theme blur?)})}
      label]
     (when counter
       [counter/counter {:blur? blur?} counter])]))
