(ns xquo.demo.card.wallet
  (:require [react-native.react.core :as react]
            [xquo.core :as xquo]
            [xquo.demo.card.assets :as assets]
            [xquo.demo.controls :as controls]
            [xquo.demo.screen :as screen]))

(defn- on-press! []
  (js/alert "Wallet card pressed"))

(defn- on-dismiss! []
  (js/alert "Dismiss wallet card"))

(defn screen [_]
  (let [[title set-title!]             (react/use-state "Receive")
        [description set-description!] (react/use-state "Bring your funds here")
        [dismiss? set-dismiss!]        (react/use-state true)]
    [screen/screen {:title   "Card wallet"
                    :preview [xquo/card.wallet {:title       title
                                                :description description
                                                :image       {:source assets/receive}
                                                :on-press    on-press!
                                                :on-dismiss  (when dismiss? on-dismiss!)
                                                :style       {:align-self :center}}]}
     [xquo/input {:label ":title" :value title :on-change-text set-title!}]
     [xquo/input {:label ":description" :value description :on-change-text set-description!}]
     [controls/checkbox {:label ":on-dismiss" :value dismiss? :on-change set-dismiss!}]]))
