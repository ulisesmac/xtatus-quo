(ns xquo.demo.browser.dapp-item
  (:require [react-native.react.core :as react]
            [xquo.browser.dapp-item.view :as dapp-item]
            [xquo.core :as xquo]
            [xquo.demo.assets :as assets]
            [xquo.demo.screen :as screen]))

(defn- press! [] (js/alert "Pressed"))

(defn screen [_]
  (let [[title set-title!] (react/use-state "Rarible")]
    [screen/screen {:title   "Browser dapp item"
                    :preview [dapp-item/dapp-item {:image    {:source assets/illustration}
                                                   :title    title
                                                   :on-press press!
                                                   :style    {:align-self :center}}]}
     [xquo/input {:label ":title" :value title :on-change-text set-title!}]]))
