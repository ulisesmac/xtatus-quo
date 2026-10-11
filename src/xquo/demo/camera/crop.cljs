(ns xquo.demo.camera.crop
  (:require [react-native.react.core :as react]
            [xquo.camera.crop.view :as crop]
            [xquo.demo.controls :as controls]
            [xquo.demo.screen :as screen]))

(defn- press! [] (js/alert "Pressed"))

(defn screen [_]
  (let [[orientation set-orientation!] (react/use-state :vertical)]
    [screen/screen {:title         "Camera crop"
                    :preview-theme :theme/dark
                    :blur?         true
                    :preview       [crop/crop {:confirm     {:on-press press!}
                                               :rotate      {:on-press press!}
                                               :orientation orientation}]}
     [controls/choice {:label     ":orientation"
                       :value     orientation
                       :on-change set-orientation!
                       :options   [{:value :vertical :label ":vertical"} {:value :horizontal :label ":horizontal"}]}]]))
