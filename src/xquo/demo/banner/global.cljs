(ns xquo.demo.banner.global
  (:require [react-native.react.core :as react]
            [xquo.core :as xquo]
            [xquo.demo.controls :as controls]
            [xquo.demo.screen :as screen]))

(defn screen [_]
  (let [[type set-type!]                 (react/use-state :error)
        [title set-title!]               (react/use-state "Error text")
        [second-title set-second-title!] (react/use-state "Alert text")
        [stacked? set-stacked!]          (react/use-state true)]
    [screen/screen {:title   "Banner global"
                    :preview [xquo/banner.global {:items (cond-> [{:type type :title title}]
                                                           stacked? (conj {:type :alert :title second-title}))}]}
     [controls/choice {:label     ":items[0] :type"
                       :value     type
                       :on-change set-type!
                       :options   [{:value :error :label ":error"}
                                   {:value :alert :label ":alert"}]}]
     [xquo/input {:label ":items[0] :title" :value title :on-change-text set-title!}]
     [controls/checkbox {:label ":items[1]" :value stacked? :on-change set-stacked!}]
     [xquo/input {:label ":items[1] :title" :value second-title :on-change-text set-second-title!}]]))
