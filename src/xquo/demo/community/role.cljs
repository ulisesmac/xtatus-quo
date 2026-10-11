(ns xquo.demo.community.role
  (:require [react-native.react.core :as react]
            [xquo.core :as xquo]
            [xquo.demo.screen :as screen]))

(defn- on-press! []
  (js/alert "Role pressed"))

(defn screen [_]
  (let [[label set-label!]     (react/use-state "Downgrade to")
        [role-label set-role!] (react/use-state "Member")]
    [screen/screen {:title   "Community role"
                    :preview [xquo/community.role {:label label
                                                   :tag   {:label role-label :icon {:name :icon/users} :on-press on-press!}
                                                   :style {:align-self :center}}]}
     [xquo/input {:label ":label" :value label :on-change-text set-label!}]
     [xquo/input {:label "[:tag :label]" :value role-label :on-change-text set-role!}]]))
