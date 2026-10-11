(ns xquo.demo.tag.standard
  (:require [react-native.react.core :as react]
            [xquo.core :as xquo]
            [xquo.demo.assets :as assets]
            [xquo.demo.controls :as controls]
            [xquo.demo.screen :as screen]))

(defn- on-press! []
  (js/alert "Tag pressed"))

(defn screen [_]
  (let [[label set-label!]        (react/use-state "Crypto")
        [size set-size!]          (react/use-state 24)
        [content set-content!]    (react/use-state :emoji)
        [selected? set-selected!] (react/use-state false)
        [disabled? set-disabled!] (react/use-state false)
        [blur? set-blur!]         (react/use-state false)]
    [screen/screen {:title   "Tag standard"
                    :blur?   blur?
                    :preview [xquo/tag.standard {:size      size
                                                 :content   (case content
                                                              :image {:type :image :image {:source assets/illustration}}
                                                              :icon {:type :icon :icon {:name :icon/placeholder}}
                                                              :emoji {:type :emoji :text "💸"}
                                                              nil)
                                                 :selected? selected?
                                                 :disabled? disabled?
                                                 :blur?     blur?
                                                 :on-press  on-press!
                                                 :style     {:align-self :center}}
                              (when (seq label) label)]}
     [xquo/input {:label "label" :value label :on-change-text set-label!}]
     [controls/choice {:label     ":size"
                       :value     size
                       :on-change set-size!
                       :options   [{:value 24 :label "24"} {:value 32 :label "32"}]}]
     [controls/choice {:label     ":content"
                       :value     content
                       :on-change set-content!
                       :options   [{:value :image :label ":image"}
                                   {:value :icon :label ":icon"}
                                   {:value :emoji :label ":emoji"}
                                   {:value nil :label "nil"}]}]
     [controls/checkbox {:label ":selected?" :value selected? :on-change set-selected!}]
     [controls/checkbox {:label ":disabled?" :value disabled? :on-change set-disabled!}]
     [controls/checkbox {:label ":blur?" :value blur? :on-change set-blur!}]]))
