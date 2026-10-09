(ns xquo.demo.divider-new
  (:require [react-native.react.core :as react]
            [xquo.context :as context]
            [xquo.core :as xquo]
            [xquo.demo.controls :as controls]
            [xquo.demo.screen :as screen]))

(def ^:private color-options
  [{:value nil :label "Inherited"}
   {:value :color/primary :label ":color/primary"}
   {:value :color/success :label ":color/success"}
   {:value :color/warning :label ":color/warning"}
   {:value :color/danger :label ":color/danger"}])

(defn screen [_]
  (let [[title set-title!] (react/use-state "New messages")
        [color set-color!] (react/use-state nil)]
    [context/provider {:color color}
     [screen/screen {:title   "Divider new"
                     :preview [xquo/divider.new {:title title}]}
      [xquo/input {:label ":title" :value title :on-change-text set-title!}]
      [controls/choice {:label     "context :color"
                        :value     color
                        :on-change set-color!
                        :options   color-options}]]]))
