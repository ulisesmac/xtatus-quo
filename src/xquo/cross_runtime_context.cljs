(ns xquo.cross-runtime-context
  (:require [applied-science.js-interop :as j]
            [react-native.runtimes.fn :as xfn]
            [reagent.core :as r]
            [xquo.context :as context]))

(defonce runtime-context (r/atom {}))

(defn- -set-runtime-context! [js-obj]
  (let [{:keys [theme color]} (j/lookup js-obj)]
    (reset! runtime-context {:theme (keyword "theme" theme)
                             :color (keyword "color" color)})
    nil))

(xfn/register-executor! :threaded.fn/set-xquo-context! -set-runtime-context!)

(def set-context!
  (xfn/get-caller! :threaded.fn/set-xquo-context! :runtime/map))

(defn provider [child]
  [context/provider @runtime-context
   child])
