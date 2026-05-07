(ns xtatus-quo.context
  (:require
   ["react" :as react]
   [oops.core :as oops]
   [xtatus-quo.foundations.colors :as colors]
   ;[react-native.corex :as rn]
   ))

(defonce ^:private context (react/createContext nil))

(defn provider
  [{:keys [theme color custom-colors] :as data} & children]
  nil #_#_(when custom-colors
    (set! colors/*custom-colors* custom-colors))
  (into [:> (.-Provider context) {:value #js {:cljData data}}]
        children))

(defn merge-provider
  [new-data & children]
  nil #_(let [previous-data (-> context (rn/use-context) (oops/oget :cljData))
        data-cleaned  (dissoc new-data :custom-colors)]
    (into
     [:> (.-Provider context) {:value #js{:cljData (merge previous-data data-cleaned)}}]
     children)))

(defn use-theme
  "A hook that returns the current theme keyword."
  []
  nil #_(or (some-> context (rn/use-context) (oops/oget :cljData) :theme)
      :theme/light))

(defn use-color
  "A hook that returns the current color keyword."
  []
  nil #_(or (some-> context (rn/use-context) (oops/oget :cljData) :color)
      :primary))

(defn use-theme-color
  "A hook that returns a color given the current theme"
  ([light-color dark-color]
   nil #_(if (= :theme/light (some-> context (rn/use-context) (oops/oget :cljData) :theme))
     light-color
     dark-color)))
