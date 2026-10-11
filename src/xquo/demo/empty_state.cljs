(ns xquo.demo.empty-state
  (:require [react-native.react.core :as react]
            [xquo.core :as xquo]
            [xquo.demo.assets :as assets]
            [xquo.demo.controls :as controls]
            [xquo.demo.screen :as screen]))

(defn- pressed! [] (js/alert "Pressed"))

(defn screen [_]
  (let [[title set-title!]               (react/use-state "A big friendly title")
        [description set-description!]   (react/use-state "Some cool description will be here")
        [image? set-image!]              (react/use-state true)
        [actions set-actions!]           (react/use-state 2)
        [primary-label set-primary!]     (react/use-state "Send community link")
        [secondary-label set-secondary!] (react/use-state "Invite friend to Status")
        [button-size set-button-size!]   (react/use-state 32)
        [disabled? set-disabled!]        (react/use-state false)
        [blur? set-blur!]                (react/use-state false)]
    [screen/screen {:title   "Empty state"
                    :blur?   blur?
                    :preview [xquo/empty-state {:title       title
                                                :description description
                                                :image       (when image? assets/illustration)
                                                :blur?       blur?
                                                :buttons     (when (pos? actions)
                                                               (cond-> [{:label     primary-label
                                                                         :size      button-size
                                                                         :disabled? disabled?
                                                                         :on-press  pressed!}]
                                                                 (= actions 2) (conj {:label     secondary-label
                                                                                      :size      button-size
                                                                                      :disabled? disabled?
                                                                                      :on-press  pressed!})))}]}
     [xquo/input {:label ":title" :value title :on-change-text set-title!}]
     [xquo/input {:label ":description" :value description :on-change-text set-description! :multiline true}]
     [controls/checkbox {:label ":image" :value image? :on-change set-image!}]
     [controls/choice {:label     "Actions"
                       :value     actions
                       :on-change set-actions!
                       :options   [{:value 0 :label "0"}
                                   {:value 1 :label "1"}
                                   {:value 2 :label "2"}]}]
     (when (pos? actions)
       [:<>
        [xquo/input {:label "Primary label" :value primary-label :on-change-text set-primary!}]
        (when (= actions 2)
          [xquo/input {:label "Secondary label" :value secondary-label :on-change-text set-secondary!}])
        [controls/choice {:label     "Button :size"
                          :value     button-size
                          :on-change set-button-size!
                          :options   [{:value 32 :label "32 (Figma)"}
                                      {:value 40 :label "40 (override)"}]}]
        [controls/checkbox {:label "Buttons :disabled?" :value disabled? :on-change set-disabled!}]])
     [controls/checkbox {:label ":blur?" :value blur? :on-change set-blur!}]]))
