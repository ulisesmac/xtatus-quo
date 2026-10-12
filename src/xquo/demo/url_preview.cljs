(ns xquo.demo.url-preview
  (:require [react-native.react.core :as react]
            [react-native.utils :refer [defstyle]]
            [xquo.core :as xquo]
            [xquo.demo.preview-assets :as assets]
            [xquo.demo.controls :as controls]
            [xquo.demo.screen :as screen]))

(defstyle ^:private preview
  {:width 335 :max-width "100%" :align-self :center})

(defn- pressed! [] (js/alert "Pressed"))

(defn screen [_]
  (let [[example set-example!]       (react/use-state :coast)
        [title set-title!]           (react/use-state "Wanderlight — Coastal trails")
        [url set-url!]               (react/use-state "wanderlight.example")
        [community set-community!]   (react/use-state "Wanderlight")
        [channel set-channel!]       (react/use-state "# design")
        [channel? set-channel-mode!] (react/use-state nil)
        [image? set-image!]          (react/use-state nil)
        [loading? set-loading!]      (react/use-state nil)
        [removable? set-removable!]  (react/use-state true)
        choose-example!              (react/use-callback (fn [next-example]
                                                           (let [{:keys [title url community]} (assets/examples next-example)]
                                                             (set-example! next-example)
                                                             (set-title! title)
                                                             (set-url! url)
                                                             (set-community! community)))
                                                         [])]
    [screen/screen {:title   "URL preview"
                    :preview [xquo/url-preview {:title     title
                                                :url       url
                                                :favicon   (:favicon (assets/examples example))
                                                :channel   (when channel? {:community community :name channel})
                                                :image     (when image? (:image (assets/examples example)))
                                                :loading?  loading?
                                                :on-remove (when removable? pressed!)
                                                :style     preview}]}
     [controls/choice {:label     "Example"
                       :value     example
                       :on-change choose-example!
                       :options   [{:value :coast :label "Travel journal"}
                                   {:value :ceramics :label "Design studio"}]}]
     [xquo/input {:label ":title" :value title :on-change-text set-title!}]
     [xquo/input {:label ":url" :value url :on-change-text set-url!}]
     [controls/checkbox {:label "Channel" :value channel? :on-change set-channel-mode!}]
     (when channel?
       [:<>
        [xquo/input {:label "Community" :value community :on-change-text set-community!}]
        [xquo/input {:label "Channel" :value channel :on-change-text set-channel!}]])
     [controls/checkbox {:label "Image" :value image? :on-change set-image!}]
     [controls/checkbox {:label ":loading?" :value loading? :on-change set-loading!}]
     [controls/checkbox {:label "Remove action" :value removable? :on-change set-removable!}]]))
