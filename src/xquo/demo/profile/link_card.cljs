(ns xquo.demo.profile.link-card
  (:require [react-native.react.core :as react]
            [react-native.utils :refer [defstyle]]
            [xquo.context :as context]
            [xquo.core :as xquo]
            [xquo.demo.controls :as controls]
            [xquo.demo.preview-assets :as assets]
            [xquo.demo.screen :as screen]))

(defstyle ^:private preview
  {:align-items :center})

(defstyle ^:private card
  {:width 160})

(defn- pressed! [] (js/alert "Pressed"))

(defn screen [_]
  (let [[title set-title!]             (react/use-state "Website")
        [description set-description!] (react/use-state "wanderlight.example")
        [content set-content!]         (react/use-state :icon)
        [color set-color!]             (react/use-state :color/primary)
        [disabled? set-disabled!]      (react/use-state nil)]
    [screen/screen {:title   "Link card"
                    :preview [context/provider {:color color}
                              [:rn/view {:style preview}
                               [xquo/profile.link-card {:title       title
                                                :description description
                                                :content     (if (= content :icon)
                                                               {:type :icon :icon {:name :icon/link}}
                                                               {:type :image :image {:source assets/ceramics}})
                                                :disabled?   disabled?
                                                :on-press    pressed!
                                                :style       card}]]]}
     [xquo/input {:label "Title" :value title :on-change-text set-title!}]
     [xquo/input {:label "Description" :value description :on-change-text set-description!}]
     [controls/choice {:label     "Content"
                       :value     content
                       :on-change set-content!
                       :options   [{:value :icon :label "Icon"} {:value :image :label "Image"}]}]
     [controls/choice {:label     "Customization color"
                       :value     color
                       :on-change set-color!
                       :options   [{:value :color/primary :label "Primary"} {:value :color/success :label "Green"} {:value :color/danger :label "Red"}]}]
     [controls/checkbox {:label "Disabled" :value disabled? :on-change set-disabled!}]]))
