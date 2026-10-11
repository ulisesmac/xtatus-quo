(ns xquo.demo.camera.controls
  (:require [react-native.react.core :as react]
            [xquo.camera.controls.view :as camera]
            [xquo.demo.controls :as controls]
            [xquo.demo.screen :as screen]))

(defn- press! [] (js/alert "Pressed"))

(defn screen [_]
  (let [[zoom set-zoom!]               (react/use-state 1)
        [flash? set-flash!]            (react/use-state false)
        [orientation set-orientation!] (react/use-state :vertical)
        change-zoom!                   (react/use-callback (fn [value]
                                                             (set-zoom! value)
                                                             (js/alert "Pressed"))
                                                           [])
        change-flash!                  (react/use-callback (fn []
                                                             (set-flash! not)
                                                             (js/alert "Pressed"))
                                                           [])]
    [screen/screen {:title   "Camera controls"
                    :photo?  true
                    :preview [camera/controls {:zoom           zoom
                                               :zoom-options   [{:value 0.5 :label ".5"}
                                                                {:value 1 :label (if (= orientation :horizontal) "1×" "1x")}
                                                                {:value 2 :label "2"}]
                                               :on-zoom-change change-zoom!
                                               :flash?         flash?
                                               :flash          {:on-press change-flash!}
                                               :flip           {:on-press press!}
                                               :shutter        {:on-press press!}
                                               :orientation    orientation}]}
     [controls/choice {:label     ":zoom"
                       :value     zoom
                       :on-change set-zoom!
                       :options   [{:value 0.5 :label "0.5"} {:value 1 :label "1"} {:value 2 :label "2"}]}]
     [controls/checkbox {:label     ":flash?"
                         :value     flash?
                         :on-change set-flash!}]
     [controls/choice {:label     ":orientation"
                       :value     orientation
                       :on-change set-orientation!
                       :options   [{:value :vertical :label ":vertical"} {:value :horizontal :label ":horizontal"}]}]]))
