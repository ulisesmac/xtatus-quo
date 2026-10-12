(ns xquo.demo.markdown.list-item
  (:require [react-native.react.core :as react]
            [react-native.utils :refer [defstyle]]
            [xquo.context :as context]
            [xquo.core :as xquo]
            [xquo.demo.assets :as assets]
            [xquo.demo.controls :as controls]
            [xquo.demo.screen :as screen]))

(defstyle ^:private preview
  {:width      "100%"
   :max-width  375
   :align-self :center})

(defn- pressed! [] (js/alert "Pressed"))

(defn- inline-control [{:keys [type blur?]}]
  (case type
    :tag [xquo/context-tag {:size         24
                            :blur?        blur?
                            :image-source assets/profile-testing-1
                            :on-press     pressed!}
          "dolor"]
    :button [xquo/button {:size     24
                          :type     :outline
                          :blur?    blur?
                          :on-press pressed!}
             "Button"]
    nil))

(defn- preview-item [{:keys [title description numbered? highlighted? value inline blur?]}]
  [xquo/markdown.list-item {:title       (when (seq title) title)
                            :description (cond
                                           inline            [description
                                                              [inline-control {:type inline :blur? blur?}]]
                                           (seq description) description
                                           :else             nil)
                            :step        (when numbered?
                                           {:value value
                                            :type  (if highlighted? :complete :neutral)})
                            :blur?       blur?
                            :style       preview}])

(defn screen [_]
  (let [[title set-title!]              (react/use-state "Be respectful")
        [description set-description!]  (react/use-state "Lorem ipsum dolor sit amet.")
        [numbered? set-numbered!]       (react/use-state true)
        [highlighted? set-highlighted!] (react/use-state false)
        [value set-value!]              (react/use-state "1")
        [inline set-inline!]            (react/use-state nil)
        [blur? set-blur!]               (react/use-state false)
        [color set-color!]              (react/use-state nil)]
    [context/provider {:color color}
     [screen/screen {:title   "Markdown list item"
                     :blur?   blur?
                     :preview [preview-item {:title        title
                                             :description  description
                                             :numbered?    numbered?
                                             :highlighted? highlighted?
                                             :value        value
                                             :inline       inline
                                             :blur?        blur?}]}
      [xquo/input {:label "Title" :value title :on-change-text set-title!}]
      [xquo/input {:label          "Description"
                   :value          description
                   :multiline?     true
                   :on-change-text set-description!}]
      [controls/checkbox {:label ":step" :value numbered? :on-change set-numbered!}]
      (when numbered?
        [:<>
         [xquo/input {:label "Step value" :value value :on-change-text set-value!}]
         [controls/checkbox {:label     "Highlighted step"
                             :value     highlighted?
                             :on-change set-highlighted!}]])
      [controls/choice {:label     "Inline content"
                        :value     inline
                        :on-change set-inline!
                        :options   [{:value nil :label "Text"}
                                    {:value :tag :label "Context tag"}
                                    {:value :button :label "Button"}]}]
      [controls/choice {:label     "Context color"
                        :value     color
                        :on-change set-color!
                        :options   [{:value nil :label "Inherited"}
                                    {:value :color/success :label "Success"}
                                    {:value :color/danger :label "Danger"}]}]
      [controls/checkbox {:label ":blur?" :value blur? :on-change set-blur!}]]]))
