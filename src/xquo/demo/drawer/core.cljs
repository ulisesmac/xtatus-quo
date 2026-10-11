(ns xquo.demo.drawer.core
  (:require [applied-science.js-interop :as j]
            [react-native.react-navigation.true-sheet :as true-sheet]
            [xquo.demo.drawer.config :as config]
            [xquo.demo.drawer.sheet :as sheet]))

(def ^:private navigator
  (j/call (true-sheet/create-true-sheet-navigator {:initial-route-name :screen/xquo-demo.drawer-config
                                                   :screens            {:screen/xquo-demo.drawer-config #'config/screen
                                                                        :screen/xquo-demo.drawer-sheet  {:screen #'sheet/screen :options sheet/options}}})
          :getComponent))

(defn screen [_]
  [:> navigator])
