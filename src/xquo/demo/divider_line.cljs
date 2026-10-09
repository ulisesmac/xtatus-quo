(ns xquo.demo.divider-line
  (:require [react-native.react.core :as react]
            [xquo.core :as xquo]
            [xquo.demo.controls :as controls]
            [xquo.demo.screen :as screen]))

(defn screen [_]
  (let [[dashed? set-dashed!] (react/use-state false)
        [blur? set-blur!]     (react/use-state false)]
    [screen/screen {:title   "Divider line"
                    :blur?   blur?
                    :preview [xquo/divider.line {:blur? blur? :dashed? dashed?}]}
     [controls/checkbox {:label ":dashed?" :value dashed? :on-change set-dashed!}]
     [controls/checkbox {:label ":blur?" :value blur? :on-change set-blur!}]]))
