(ns xquo.demo.option-card
  (:require [react-native.react.core :as react]
            [xquo.core :as xquo]
            [xquo.demo.assets :as assets]
            [xquo.demo.controls :as controls]
            [xquo.demo.screen :as screen]))

(defn- on-press! []
  (js/alert "Pressed"))

(defn screen [_]
  (let [[variant set-variant!]         (react/use-state :small)
        [title set-title!]             (react/use-state "Title")
        [description set-description!] (react/use-state "Subtitle")
        [right-icon set-right-icon!]   (react/use-state :icon/external)
        [label set-label!]             (react/use-state "Let's go!")
        [primary? set-primary!]        (react/use-state false)
        [disabled? set-disabled!]      (react/use-state false)]
    [screen/screen {:title   "Option card"
                    :shell?  true
                    :preview [xquo/shell.option-card {:variant     variant
                                                      :title       title
                                                      :description description
                                                      :image       {:source assets/illustration}
                                                      :right-icon  (when right-icon {:name right-icon})
                                                      :disabled?   disabled?
                                                      :on-press    on-press!
                                                      :button      {:label     label
                                                                    :type      (if primary? :primary :grey)
                                                                    :disabled? disabled?
                                                                    :on-press  on-press!}}]}
     [controls/choice {:label     ":variant"
                       :value     variant
                       :on-change set-variant!
                       :options   [{:value :small :label ":small"}
                                   {:value :big :label ":big"}]}]
     [xquo/input {:label ":title" :value title :on-change-text set-title!}]
     [xquo/input {:label ":description" :value description :on-change-text set-description!}]
     (if (= variant :small)
       [:<>
        [controls/icon-picker {:label ":right-icon" :value right-icon :on-change set-right-icon!}]
        [controls/checkbox {:label ":disabled?" :value disabled? :on-change set-disabled!}]]
       [:<>
        [xquo/input {:label "[:button :label]" :value label :on-change-text set-label!}]
        [controls/checkbox {:label "[:button :type] :primary" :value primary? :on-change set-primary!}]
        [controls/checkbox {:label "[:button :disabled?]" :value disabled? :on-change set-disabled!}]])]))
