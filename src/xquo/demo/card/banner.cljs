(ns xquo.demo.card.banner
  (:require [react-native.react.core :as react]
            [xquo.core :as xquo]
            [xquo.demo.card.assets :as assets]
            [xquo.demo.controls :as controls]
            [xquo.demo.screen :as screen]))

(defn- on-press! []
  (js/alert "Banner card pressed"))

(defn screen [_]
  (let [[title set-title!]             (react/use-state "Discover")
        [description set-description!] (react/use-state "Your favourite communities")
        [image set-image!]             (react/use-state :communities)]
    [screen/screen {:title   "Card banner"
                    :preview [xquo/card.banner {:title       title
                                                :description description
                                                :image       {:source (if (= image :communities)
                                                                        assets/discover-communities
                                                                        assets/invite-friends)}
                                                :on-press    on-press!
                                                :style       {:align-self :center}}]}
     [xquo/input {:label ":title" :value title :on-change-text set-title!}]
     [xquo/input {:label ":description" :value description :on-change-text set-description!}]
     [controls/choice {:label     ":image"
                       :value     image
                       :on-change set-image!
                       :options   [{:value :communities :label "Discover communities"}
                                   {:value :friends :label "Invite friends"}]}]]))
