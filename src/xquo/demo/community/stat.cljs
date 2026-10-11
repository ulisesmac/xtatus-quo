(ns xquo.demo.community.stat
  (:require [react-native.react.core :as react]
            [xquo.core :as xquo]
            [xquo.demo.controls :as controls]
            [xquo.demo.screen :as screen]))

(defn screen [_]
  (let [[value set-value!] (react/use-state "4.9M")
        [icon set-icon!]   (react/use-state :icon/users)]
    [screen/screen {:title   "Community stat"
                    :preview [xquo/community.stat {:icon {:name icon} :style {:align-self :center}} value]}
     [xquo/input {:label "value" :value value :on-change-text set-value!}]
     [controls/choice {:label     "[:icon :name]"
                       :value     icon
                       :on-change set-icon!
                       :options   [{:value :icon/users :label ":icon/users"}
                                   {:value :icon/flash :label ":icon/flash"}
                                   {:value :icon/user :label ":icon/user"}]}]]))
