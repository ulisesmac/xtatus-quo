(ns xquo.demo.tag.permissions
  (:require [react-native.react.core :as react]
            [xquo.core :as xquo]
            [xquo.demo.assets :as assets]
            [xquo.demo.controls :as controls]
            [xquo.demo.screen :as screen]))

(def ^:private group-examples
  {:one          [{:tokens [{:source assets/illustration}]}]
   :two          [{:tokens [{:source assets/illustration} {:source assets/illustration}]}]
   :three        [{:tokens [{:source assets/illustration} {:source assets/illustration} {:source assets/illustration}]}]
   :more         [{:tokens [{:source assets/illustration} {:source assets/illustration}] :total 4}]
   :alternatives [{:tokens [{:source assets/illustration}]} {:tokens [{:source assets/illustration} {:source assets/illustration}]}]
   :three-groups [{:tokens [{:source assets/illustration}]} {:tokens [{:source assets/illustration}]} {:tokens [{:source assets/illustration}]}]})

(defn screen [_]
  (let [[size set-size!]             (react/use-state 24)
        [locked? set-locked!]        (react/use-state true)
        [blur? set-blur!]            (react/use-state false)
        [example set-example!]       (react/use-state :more)
        [more-groups? set-more!]     (react/use-state false)
        [separator set-separator!]   (react/use-state "or")
        [background set-background!] (react/use-state nil)]
    [screen/screen {:title   "Tag permissions"
                    :blur?   blur?
                    :preview [xquo/tag.permissions {:size             size
                                                    :locked?          locked?
                                                    :blur?            blur?
                                                    :groups           (group-examples example)
                                                    :more-groups?     more-groups?
                                                    :separator-label  separator
                                                    :background-color background
                                                    :style            {:align-self :center}}]}
     [controls/choice {:label     ":size"
                       :value     size
                       :on-change set-size!
                       :options   [{:value 24 :label "24"}
                                   {:value 32 :label "32"}]}]
     [controls/choice {:label     ":groups"
                       :value     example
                       :on-change set-example!
                       :options   [{:value :one :label "1"}
                                   {:value :two :label "2"}
                                   {:value :three :label "3"}
                                   {:value :more :label "4"}
                                   {:value :alternatives :label "1 or 2"}
                                   {:value :three-groups :label "1 or 1 or 1"}]}]
     [controls/checkbox {:label ":locked?" :value locked? :on-change set-locked!}]
     [controls/checkbox {:label ":blur?" :value blur? :on-change set-blur!}]
     [controls/checkbox {:label ":more-groups?" :value more-groups? :on-change set-more!}]
     [xquo/input {:label ":separator-label" :value separator :on-change-text set-separator!}]
     [controls/choice {:label     ":background-color"
                       :value     background
                       :on-change set-background!
                       :options   [{:value nil :label "Inherited"}
                                   {:value :color/neutral-80 :label ":color/neutral-80"}]}]]))
