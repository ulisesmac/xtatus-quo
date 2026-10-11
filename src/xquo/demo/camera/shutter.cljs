(ns xquo.demo.camera.shutter
  (:require [react-native.react.core :as react]
            [xquo.camera.shutter.view :as shutter]
            [xquo.demo.controls :as controls]
            [xquo.demo.screen :as screen]))

(defn- press! [] (js/alert "Pressed"))

(defn screen [_]
  (let [[action set-action!]      (react/use-state :shutter)
        [disabled? set-disabled!] (react/use-state false)]
    [screen/screen {:title   "Camera shutter"
                    :photo?  true
                    :preview [shutter/shutter {:action    action
                                               :disabled? disabled?
                                               :on-press  press!
                                               :style     {:align-self :center}}]}
     [controls/choice {:label     ":action"
                       :value     action
                       :on-change set-action!
                       :options   [{:value :shutter :label ":shutter"} {:value :confirm :label ":confirm"}]}]
     [controls/checkbox {:label     ":disabled?"
                         :value     disabled?
                         :on-change set-disabled!}]]))
