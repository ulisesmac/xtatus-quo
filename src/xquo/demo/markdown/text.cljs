(ns xquo.demo.markdown.text
  (:require [react-native.react.core :as react]
            [react-native.utils :refer [defstyle]]
            [xquo.core :as xquo]
            [xquo.demo.controls :as controls]
            [xquo.demo.screen :as screen]))

(defstyle ^:private preview
  {:width      "100%"
   :max-width  375
   :align-self :center})

(def ^:private types
  [:body :body-bold :h1 :h2 :h3 :h3-regular])

(defn screen [_]
  (let [[type set-type!]              (react/use-state :body)
        [content set-content!]        (react/use-state "Make yourself at home.")
        [selectable? set-selectable!] (react/use-state false)]
    [screen/screen {:title   "Markdown text"
                    :preview [xquo/markdown.text {:type       type
                                                  :selectable selectable?
                                                  :style      preview}
                              content]}
     [controls/choice {:label     ":type"
                       :value     type
                       :on-change set-type!
                       :options   (map (fn [type] {:value type :label (name type)}) types)}]
     [xquo/input {:label          "Text"
                  :value          content
                  :multiline?     true
                  :on-change-text set-content!}]
     [controls/checkbox {:label     ":selectable"
                         :value     selectable?
                         :on-change set-selectable!}]]))
