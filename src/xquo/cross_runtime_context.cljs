(ns xquo.cross-runtime-context
  (:require [applied-science.js-interop :as j]
            [reagent.core :as r]
            [xquo.context :as context]
            [react-native.runtimes.core :as runtimes]))

(defonce runtime-context (r/atom {}))

(defn- -set-runtime-context! [js-obj]
  (let [{:keys [theme color]} (j/lookup js-obj)]
    (reset! runtime-context {:theme (keyword "theme" theme)
                             :color (keyword "color" color)})
    nil))

(runtimes/register-fn! :threaded.fn/set-xquo-context! -set-runtime-context!)

(def set-context!
  (runtimes/get-caller-fn! :threaded.fn/set-xquo-context! :runtime/map))

(defn provider [child]
  [context/provider @runtime-context
   child])
