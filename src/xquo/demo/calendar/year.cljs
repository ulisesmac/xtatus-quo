(ns xquo.demo.calendar.year
  (:require [react-native.react.core :as react]
            [xquo.core :as xquo]
            [xquo.demo.controls :as controls]
            [xquo.demo.screen :as screen]))

(defn- on-press! []
  (js/alert "Calendar year pressed"))

(defn screen [_]
  (let [[year set-year!]          (react/use-state "2023")
        [selected? set-selected!] (react/use-state true)
        [disabled? set-disabled!] (react/use-state false)]
    [screen/screen {:title   "Calendar year"
                    :preview [xquo/calendar.year {:selected? selected?
                                                  :disabled? disabled?
                                                  :on-press  on-press!
                                                  :style     {:align-self :center}}
                              year]}
     [xquo/input {:label "year" :value year :on-change-text set-year!}]
     [controls/checkbox {:label ":selected?" :value selected? :on-change set-selected!}]
     [controls/checkbox {:label ":disabled?" :value disabled? :on-change set-disabled!}]]))
