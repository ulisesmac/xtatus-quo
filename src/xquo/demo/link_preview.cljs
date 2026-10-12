(ns xquo.demo.link-preview
  (:require [react-native.react.core :as react]
            [react-native.utils :refer [defstyle]]
            [xquo.core :as xquo]
            [xquo.context :as context]
            [xquo.demo.preview-assets :as assets]
            [xquo.demo.controls :as controls]
            [xquo.demo.screen :as screen]))

(defstyle ^:private preview
  {:width 295 :max-width "100%" :align-self :center})

(defn- pressed! [] (js/alert "Pressed"))

(defn screen [_]
  (let [[example set-example!]         (react/use-state :coast)
        [type set-type!]               (react/use-state :default)
        [title set-title!]             (react/use-state "Wanderlight — Coastal trails")
        [description set-description!] (react/use-state "Find quiet paths, hidden coves and a slower way to explore the coast.")
        [url set-url!]                 (react/use-state "wanderlight.example")
        [image? set-image!]            (react/use-state true)
        [favicon? set-favicon!]        (react/use-state true)
        [color set-color!]             (react/use-state :color/primary)
        choose-example!                (react/use-callback (fn [next-example]
                                                             (let [{:keys [title url description]} (assets/examples next-example)]
                                                               (set-example! next-example)
                                                               (set-title! title)
                                                               (set-url! url)
                                                               (set-description! description)))
                                                           [])]
    [screen/screen {:title   "Link preview"
                    :preview [context/provider {:color color}
                              [xquo/link-preview {:type        type
                                                  :title       title
                                                  :description description
                                                  :url         url
                                                  :favicon     (when favicon? (:favicon (assets/examples example)))
                                                  :image       (when image? (:image (assets/examples example)))
                                                  :on-press    pressed!
                                                  :style       preview}]]}
     [controls/choice {:label     "Example"
                       :value     example
                       :on-change choose-example!
                       :options   [{:value :coast :label "Travel journal"}
                                   {:value :ceramics :label "Design studio"}]}]
     [controls/choice {:label     ":type"
                       :value     type
                       :on-change set-type!
                       :options   [{:value :default :label "Default / video"}
                                   {:value :detail :label "Detail"}
                                   {:value :url :label "URL only"}]}]
     [xquo/input {:label ":title" :value title :on-change-text set-title!}]
     [xquo/input {:label ":description" :value description :on-change-text set-description!}]
     [xquo/input {:label ":url" :value url :on-change-text set-url!}]
     [controls/choice {:label     "Customization color"
                       :value     color
                       :on-change set-color!
                       :options   [{:value :color/primary :label "Primary"}
                                   {:value :color/success :label "Green"}
                                   {:value :color/danger :label "Red"}]}]
     [controls/checkbox {:label "Image" :value image? :on-change set-image!}]
     [controls/checkbox {:label "Favicon" :value favicon? :on-change set-favicon!}]]))
