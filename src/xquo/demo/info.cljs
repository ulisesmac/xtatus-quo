(ns xquo.demo.info
  (:require [clojure.string :as string]
            [react-native.react.core :as react]
            [react-native.utils :refer [defstyle]]
            [xquo.context :as context]
            [xquo.core :as xquo]
            [xquo.demo.controls :as controls]
            [xquo.demo.screen :as screen]))

(defstyle ^:private preview-style
  {:width      335
   :max-width  "100%"
   :align-self :center})

(defstyle ^:private list-preview-style
  {:width      350
   :max-width  "100%"
   :align-self :center})

(def ^:private status-options
  [{:value :default :label "Default"}
   {:value :success :label "Success"}
   {:value :warning :label "Warning"}
   {:value :error :label "Error"}])

(def ^:private icon-options
  [{:value :icon/info-outline :label "Info"}
   {:value :icon/placeholder :label "Placeholder"}
   {:value :icon/bell :label "Bell"}])

(def ^:private list-copy
  (string/join "\n" (repeat 5 "Lorem ipsum dolor sit amet.")))

(defn- pressed! [] (js/alert "Pressed"))

(defn- showcase [{:keys [box?]}]
  (let [[description set-description!] (react/use-state (if box?
                                                          "If you registered a stateofus.eth name you might be eligible to connect $ENS"
                                                          "This is an informative message"))
        [status set-status!]           (react/use-state :default)
        [size set-size!]               (react/use-state 13)
        [icon-name set-icon!]          (react/use-state :icon/info-outline)
        [title? set-title-visible!]    (react/use-state true)
        [title set-title!]             (react/use-state "Title here")
        [button? set-button!]          (react/use-state true)
        [button-label set-label!]      (react/use-state "Button")
        [close? set-close!]            (react/use-state false)
        [blur? set-blur!]              (react/use-state false)
        [color set-color!]             (react/use-state nil)]
    [context/provider {:color color}
     [screen/screen {:title   (if box? "Information box" "Info message")
                     :blur?   blur?
                     :preview (if box?
                                [xquo/information-box {:status       status
                                                       :size         size
                                                       :icon         {:name icon-name}
                                                       :title        (when title? title)
                                                       :description  description
                                                       :button       (when button? {:label button-label :on-press pressed!})
                                                       :close-button (when close? {:on-press pressed!})
                                                       :blur?        blur?
                                                       :style        preview-style}]
                                [xquo/info-message {:status status
                                                    :size   size
                                                    :icon   {:name icon-name}
                                                    :blur?  blur?
                                                    :style  preview-style}
                                 description])}
      [xquo/input {:label "Text" :value description :on-change-text set-description! :multiline? true}]
      [controls/choice {:label     ":status"
                        :value     status
                        :on-change set-status!
                        :options   (cond-> status-options box? (conj {:value :info :label "Context"}))}]
      [controls/choice {:label     ":size"
                        :value     size
                        :on-change set-size!
                        :options   (if box?
                                     [{:value 13 :label "13"} {:value 15 :label "15"}]
                                     [{:value 13 :label "13"} {:value 11 :label "11"}])}]
      [controls/choice {:label ":icon" :value icon-name :on-change set-icon! :options icon-options}]
      (when box?
        [:<>
         [controls/checkbox {:label ":title" :value title? :on-change set-title-visible!}]
         (when title?
           [xquo/input {:label "Title" :value title :on-change-text set-title!}])
         [controls/checkbox {:label ":button" :value button? :on-change set-button!}]
         (when button?
           [xquo/input {:label "Button label" :value button-label :on-change-text set-label!}])
         [controls/checkbox {:label ":close-button" :value close? :on-change set-close!}]])
      [controls/checkbox {:label ":blur?" :value blur? :on-change set-blur!}]
      (when (= status :info)
        [controls/choice {:label     "Context color"
                          :value     color
                          :on-change set-color!
                          :options   [{:value nil :label "Inherited"}
                                      {:value :color/success :label "Success"}
                                      {:value :color/danger :label "Danger"}]}])]]))

(defn message-screen [_]
  [showcase {:box? false}])

(defn box-screen [_]
  [showcase {:box? true}])

(defn list-screen [_]
  (let [[title set-title!]     (react/use-state "Title")
        [content set-content!] (react/use-state list-copy)
        [icon-name set-icon!]  (react/use-state :icon/bullet)]
    [screen/screen {:title   "List info box"
                    :preview [xquo/list-info-box {:title title
                                                  :items (mapv (fn [line]
                                                                 {:description line :icon {:name icon-name}})
                                                               (string/split-lines content))
                                                  :style list-preview-style}]}
     [xquo/input {:label ":title" :value title :on-change-text set-title!}]
     [xquo/input {:label "Items (one per line)" :value content :on-change-text set-content! :multiline? true}]
     [controls/choice {:label     "Item :icon"
                       :value     icon-name
                       :on-change set-icon!
                       :options   [{:value :icon/bullet :label "Bullet"}
                                   {:value :icon/info-outline :label "Info"}
                                   {:value :icon/check :label "Check"}]}]]))
