(ns xquo.demo.dapp.group
  (:require [react-native.react.core :as react]
            [xquo.dapp.group.view :as group]
            [xquo.demo.controls :as controls]
            [xquo.demo.dapp.data :as data]
            [xquo.demo.screen :as screen]))

(defn screen [_]
  (let [[header set-header!]    (react/use-state :dapp)
        [item-count set-count!] (react/use-state 5)
        [blur? set-blur!]       (react/use-state true)]
    [screen/screen {:title   "Dapp group"
                    :blur?   blur?
                    :preview [group/group {:header (assoc (data/headers header) :action {:on-press data/press!})
                                           :items  (take item-count (if (= header :account) data/dapps data/accounts))
                                           :blur?  blur?}]}
     [controls/choice {:label     ":header"
                       :value     header
                       :on-change set-header!
                       :options   [{:value :dapp :label "Dapp"}
                                   {:value :smart-contract :label "Smart contract"}
                                   {:value :account :label "Account"}]}]
     [controls/choice {:label     ":items count"
                       :value     item-count
                       :on-change set-count!
                       :options   [{:value 1 :label "1"} {:value 3 :label "3"} {:value 5 :label "5"}]}]
     [controls/checkbox {:label ":blur?" :value blur? :on-change set-blur!}]]))
