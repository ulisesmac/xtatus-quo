(ns xquo.demo.calendar.day
  (:require [react-native.react.core :as react]
            [xquo.context :as context]
            [xquo.core :as xquo]
            [xquo.demo.controls :as controls]
            [xquo.demo.screen :as screen]))

(defn- on-press! []
  (js/alert "Calendar day pressed"))

(defn screen [_]
  (let [[label set-label!]        (react/use-state "10")
        [selected? set-selected!] (react/use-state false)
        [today? set-today!]       (react/use-state true)
        [disabled? set-disabled!] (react/use-state false)
        [color set-color!]        (react/use-state nil)]
    [context/provider {:color color}
     [screen/screen {:title   "Calendar day"
                     :preview [xquo/calendar.day {:selected? selected?
                                                  :today?    today?
                                                  :disabled? disabled?
                                                  :on-press  on-press!
                                                  :style     {:align-self :center}}
                               label]}
      [xquo/input {:label "day" :value label :on-change-text set-label!}]
      [controls/checkbox {:label ":selected?" :value selected? :on-change set-selected!}]
      [controls/checkbox {:label ":today?" :value today? :on-change set-today!}]
      [controls/checkbox {:label ":disabled?" :value disabled? :on-change set-disabled!}]
      [controls/choice {:label     "context :color"
                        :value     color
                        :on-change set-color!
                        :options   [{:value nil :label "Inherited"}
                                    {:value :color/primary :label ":color/primary"}
                                    {:value :color/success :label ":color/success"}
                                    {:value :color/danger :label ":color/danger"}]}]]]))
