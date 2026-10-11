(ns xquo.demo.segmented-tab
  (:require [react-native.react.core :as react]
            [xquo.components.segmented-tab.view :as segmented-tab]
            [xquo.core :as xquo]
            [xquo.demo.camera.assets :as assets]
            [xquo.demo.controls :as controls]
            [xquo.demo.screen :as screen]))

(defn screen [_]
  (let [[label set-label!]        (react/use-state "Tab")
        [type set-type!]          (react/use-state :grey)
        [size set-size!]          (react/use-state 28)
        [selected? set-selected!] (react/use-state false)
        [disabled? set-disabled!] (react/use-state false)
        [blur? set-blur!]         (react/use-state false)
        [symbol set-symbol!]      (react/use-state nil)
        [only? set-only!]         (react/use-state false)
        press!                    (react/use-callback (fn []
                                                        (set-selected! not)
                                                        (js/alert "Pressed"))
                                                      [])]
    [screen/screen {:title   "Segmented tab"
                    :blur?   blur?
                    :preview [segmented-tab/segmented-tab {:type      type
                                                           :size      size
                                                           :selected? selected?
                                                           :disabled? disabled?
                                                           :blur?     blur?
                                                           :symbol    (case symbol
                                                                        :icon {:type :icon :icon {:name :icon/placeholder}}
                                                                        :image {:type :image :image {:source assets/pig}}
                                                                        nil)
                                                           :on-press  press!
                                                           :style     {:align-self :center}}
                              (when-not only? label)]}
     [xquo/input {:label "label" :value label :on-change-text set-label!}]
     [controls/choice {:label     ":size"
                       :value     size
                       :on-change set-size!
                       :options   [{:value 28 :label "28"} {:value 20 :label "20"}]}]
     [controls/choice {:label     ":type"
                       :value     type
                       :on-change set-type!
                       :options   [{:value :grey :label ":grey"} {:value :dark-grey :label ":dark-grey"}]}]
     [controls/choice {:label     ":symbol"
                       :value     symbol
                       :on-change set-symbol!
                       :options   [{:value nil :label "nil"} {:value :icon :label ":icon"} {:value :image :label ":image"}]}]
     [controls/checkbox {:label     "symbol only"
                         :value     only?
                         :on-change set-only!}]
     [controls/checkbox {:label     ":selected?"
                         :value     selected?
                         :on-change set-selected!}]
     [controls/checkbox {:label     ":disabled?"
                         :value     disabled?
                         :on-change set-disabled!}]
     [controls/checkbox {:label     ":blur?"
                         :value     blur?
                         :on-change set-blur!}]]))
