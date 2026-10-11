(ns xquo.demo.dapp.list-card
  (:require [react-native.react.core :as react]
            [xquo.core :as xquo]
            [xquo.dapp.list-card.view :as list-card]
            [xquo.demo.controls :as controls]
            [xquo.demo.dapp.data :as data]
            [xquo.demo.screen :as screen]))

(defn screen [_]
  (let [[variant set-variant!]         (react/use-state :account)
        [title set-title!]             (react/use-state nil)
        [description set-description!] (react/use-state nil)
        [action? set-action!]          (react/use-state true)
        [blur? set-blur!]              (react/use-state false)
        item                           (data/list-cards variant)]
    [screen/screen {:title   "Dapp list card"
                    :blur?   blur?
                    :preview [list-card/list-card (assoc item
                                                         :title       (or title (:title item))
                                                         :description (or description (:description item))
                                                         :action      (when action? (:action item))
                                                         :blur?       blur?)]}
     [controls/choice {:label     ":variant"
                       :value     variant
                       :on-change set-variant!
                       :options   [{:value :account :label ":account"}
                                   {:value :dapp :label ":dapp"}
                                   {:value :smart-contract :label ":smart-contract"}
                                   {:value :approval :label ":approval"}]}]
     [xquo/input {:label ":title" :value (or title (:title item)) :on-change-text set-title!}]
     [xquo/input {:label          ":description"
                  :value          (or description (:description item) "")
                  :on-change-text set-description!}]
     [controls/checkbox {:label ":action" :value action? :on-change set-action!}]
     [controls/checkbox {:label ":blur?" :value blur? :on-change set-blur!}]]))
