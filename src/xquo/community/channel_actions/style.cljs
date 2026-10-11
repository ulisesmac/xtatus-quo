(ns xquo.community.channel-actions.style
  (:require [react-native.utils :refer [defstyle style]]))

(defn container [{:keys [dark-theme?]} multiple-rows?]
  (let [dark-multiple? (and multiple-rows? dark-theme?)]
    (style {:width              375
            :max-width          "100%"
            :padding-top        8
            :padding-horizontal 20
            :padding-bottom     (if dark-multiple? 32 20)
            :gap                (if dark-multiple? 28 16)})))

(defstyle row
  {:flex-direction :row
   :gap            16})

(defstyle fill
  {:flex  1
   :width nil})
