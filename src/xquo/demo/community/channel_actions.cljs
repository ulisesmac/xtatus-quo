(ns xquo.demo.community.channel-actions
  (:require [react-native.react.core :as react]
            [xquo.core :as xquo]
            [xquo.demo.controls :as controls]
            [xquo.demo.screen :as screen]))

(defn- on-press! []
  (js/alert "Channel action pressed"))

(def ^:private items
  [{:id :pinned :title "Pinned\nMessages" :icon {:name :icon/pin} :on-press on-press!}
   {:id :files :title "Shared\nFiles" :icon {:name :icon/file} :on-press on-press!}
   {:id :members :title "Channel\nMembers" :icon {:name :icon/users} :on-press on-press!}
   {:id :notifications :title "Mute\nChannel" :icon {:name :icon/bell} :on-press on-press!}
   {:id :search :title "Search\nMessages" :icon {:name :icon/search} :on-press on-press!}
   {:id :invite :title "Invite\nFriends" :icon {:name :icon/user-add} :on-press on-press!}])

(defn screen [_]
  (let [[item-count set-count!] (react/use-state 3)
        [columns set-columns!]  (react/use-state 3)]
    [screen/screen {:title   "Community channel actions"
                    :preview [xquo/community.channel-actions {:items   (take item-count items)
                                                              :columns columns
                                                              :style   {:align-self :center}}]}
     [controls/choice {:label     ":items count"
                       :value     item-count
                       :on-change set-count!
                       :options   [{:value 2 :label "2"} {:value 3 :label "3"} {:value 6 :label "6"}]}]
     [controls/choice {:label     ":columns"
                       :value     columns
                       :on-change set-columns!
                       :options   [{:value 2 :label "2"} {:value 3 :label "3"}]}]]))
