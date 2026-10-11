(ns xquo.demo.browser.new-tab
  (:require [react-native.react.core :as react]
            [xquo.browser.new-tab.view :as new-tab]
            [xquo.context :as context]
            [xquo.core :as xquo]
            [xquo.demo.controls :as controls]
            [xquo.demo.screen :as screen]))

(defn- press! [] (js/alert "Pressed"))

(defn screen [_]
  (let [[title set-title!] (react/use-state "New tab")
        [color set-color!] (react/use-state nil)]
    [context/provider {:color color}
     [screen/screen {:title   "Browser new tab"
                     :preview [new-tab/new-tab {:title title :on-press press! :style {:align-self :center}}]}
      [xquo/input {:label ":title" :value title :on-change-text set-title!}]
      [controls/choice {:label     "context :color"
                        :value     color
                        :on-change set-color!
                        :options   [{:value nil :label "Inherited"}
                                    {:value :color/primary :label ":color/primary"}
                                    {:value :color/success :label ":color/success"}
                                    {:value :color/danger :label ":color/danger"}]}]]]))
