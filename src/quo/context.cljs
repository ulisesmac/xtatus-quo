(ns quo.context
  (:require
    ["react" :as react]
    [oops.core :as oops]
    [react-native.core :as rn]))

(defonce ^:private context (react/createContext nil))

(defn provider
  [data & children]
  (into [:> (.-Provider context) {:value #js {:cljData data}}]
        children))

(defn merge-provider
  [new-data & children]
  (let [previous-data (-> (rn/use-context context)
                          (oops/oget :cljData))]
    (into
     [:> (.-Provider context) {:value #js{:cljData (merge previous-data new-data)}}]
     children)))

(defn use-theme
  "A hook that returns the current theme keyword."
  []
  (or (some-> context (rn/use-context) (oops/oget :cljData) :theme)
      :theme/light))

(defn use-color
  "A hook that returns the current color keyword."
  []
  (or (some-> context (rn/use-context) (oops/oget :cljData) :color)
      :color/primary))

(defn use-screen-id
  "A hook that returns the current screen id."
  []
  (when-let [data (rn/use-context context)]
    (:screen-id (oops/oget data :cljData))))

(defn use-screen-params
  "A hook that returns the current screen params"
  []
  (when-let [data (rn/use-context context)]
    (:screen-params (oops/oget data :cljData))))
