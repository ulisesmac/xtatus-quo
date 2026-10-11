(ns xquo.demo.dapp.approvals
  (:require [react-native.react.core :as react]
            [xquo.dapp.group.view :as group]
            [xquo.demo.controls :as controls]
            [xquo.demo.dapp.data :as data]
            [xquo.demo.screen :as screen]))

(defn screen [_]
  (let [[header set-header!] (react/use-state :dapp)
        [blur? set-blur!]    (react/use-state true)]
    [screen/screen {:title   "Dapp approvals"
                    :blur?   blur?
                    :preview [group/group {:header (data/headers header)
                                           :items  data/approvals
                                           :blur?  blur?}]}
     [controls/choice {:label     ":header"
                       :value     header
                       :on-change set-header!
                       :options   [{:value :dapp :label "Dapp"}
                                   {:value :smart-contract :label "Smart contract"}
                                   {:value :account :label "Account"}]}]
     [controls/checkbox {:label ":blur?" :value blur? :on-change set-blur!}]]))
