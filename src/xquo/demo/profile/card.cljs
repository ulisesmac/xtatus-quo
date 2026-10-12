(ns xquo.demo.profile.card
  (:require [react-native.react.core :as react]
            [react-native.utils :refer [defstyle]]
            [xquo.context :as context]
            [xquo.core :as xquo]
            [xquo.demo.assets :as assets]
            [xquo.demo.controls :as controls]
            [xquo.demo.screen :as screen]))

(defstyle ^:private preview
  {:width "100%" :max-width 335 :align-self :center})

(def ^:private portraits
  {1 assets/profile-testing-1
   2 assets/profile-testing-2
   3 assets/profile-testing-3})

(defn- pressed! [] (js/alert "Pressed"))

(defn screen [_]
  (let [[name set-name!]                 (react/use-state "Alex Morgan")
        [description set-description!]   (react/use-state "zQ3k83euenmcikw7474hfu73t5N")
        [description? set-description?!] (react/use-state true)
        [portrait set-portrait!]         (react/use-state 1)
        [name-icon? set-name-icon!]      (react/use-state true)
        [tag? set-tag!]                  (react/use-state true)
        [button? set-button!]            (react/use-state nil)
        [color set-color!]               (react/use-state :color/primary)]
    [screen/screen {:title   "Profile card"
                    :shell?  true
                    :preview [context/provider {:color color}
                              [xquo/profile.card {:name        name
                                                  :description (when description? description)
                                                  :avatar      {:content {:type  :image
                                                                          :image {:source (portraits portrait)}}}
                                                  :name-icon   (when name-icon? {:name :icon/keycard})
                                                  :tag         (when tag?
                                                                 {:label   "Logged in"
                                                                  :content {:type :icon :icon {:name :icon/check}}})
                                                  :button      (when button?
                                                                 {:icon {:name :icon/options} :on-press pressed!})
                                                  :style       preview}]]}
     [xquo/input {:label "Name" :value name :on-change-text set-name!}]
     [controls/checkbox {:label "Description" :value description? :on-change set-description?!}]
     (when description?
       [xquo/input {:label "Description text" :value description :on-change-text set-description!}])
     [controls/choice {:label     "Portrait"
                       :value     portrait
                       :on-change set-portrait!
                       :options   [{:value 1 :label "1"}
                                   {:value 2 :label "2"}
                                   {:value 3 :label "3"}]}]
     [controls/choice {:label     "Customization color"
                       :value     color
                       :on-change set-color!
                       :options   [{:value :color/primary :label "Primary"}
                                   {:value :color/success :label "Green"}
                                   {:value :color/danger :label "Red"}]}]
     [controls/checkbox {:label "Name icon" :value name-icon? :on-change set-name-icon!}]
     [controls/checkbox {:label "Status tag" :value tag? :on-change set-tag!}]
     [controls/checkbox {:label "Action button" :value button? :on-change set-button!}]]))
