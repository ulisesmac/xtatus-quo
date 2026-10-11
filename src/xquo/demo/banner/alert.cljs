(ns xquo.demo.banner.alert
  (:require [react-native.react.core :as react]
            [xquo.core :as xquo]
            [xquo.demo.controls :as controls]
            [xquo.demo.screen :as screen]))

(defn- on-press! []
  (js/alert "Banner action pressed"))

(defn screen [_]
  (let [[title set-title!]               (react/use-state "Alert text")
        [button? set-button!]            (react/use-state true)
        [button-label set-button-label!] (react/use-state "Button")
        [disabled? set-disabled!]        (react/use-state false)]
    [screen/screen {:title   "Banner alert"
                    :preview [xquo/banner.alert {:title  title
                                                 :button (when button?
                                                           {:label     button-label
                                                            :disabled? disabled?
                                                            :on-press  on-press!})}]}
     [xquo/input {:label ":title" :value title :on-change-text set-title! :multiline? true}]
     [controls/choice {:label     ":title examples"
                       :value     title
                       :on-change set-title!
                       :options   [{:value "Alert text" :label "One line"}
                                   {:value "Alert text that can take more space like this example here"
                                    :label "Multiple lines"}]}]
     [controls/checkbox {:label ":button" :value button? :on-change set-button!}]
     [xquo/input {:label ":button :label" :value button-label :on-change-text set-button-label!}]
     [controls/checkbox {:label ":button :disabled?" :value disabled? :on-change set-disabled!}]]))
