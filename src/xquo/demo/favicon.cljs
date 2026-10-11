(ns xquo.demo.favicon
  (:require [react-native.react.core :as react]
            [xquo.components.favicon.view :as favicon]
            [xquo.core :as xquo]
            [xquo.demo.assets :as assets]
            [xquo.demo.controls :as controls]
            [xquo.demo.screen :as screen]))

(defn screen [_]
  (let [[image? set-image!] (react/use-state true)
        [text set-text!]    (react/use-state "R")
        [blur? set-blur!]   (react/use-state false)]
    [screen/screen {:title   "Favicon"
                    :blur?   blur?
                    :preview [favicon/favicon {:content (if image?
                                                          {:type :image :image {:source assets/illustration}}
                                                          {:type :initials :text text})
                                               :blur?   blur?
                                               :style   {:align-self :center}}]}
     [controls/checkbox {:label ":content :image" :value image? :on-change set-image!}]
     (when-not image?
       [xquo/input {:label ":content :text" :value text :on-change-text set-text!}])
     [controls/checkbox {:label ":blur?" :value blur? :on-change set-blur!}]]))
