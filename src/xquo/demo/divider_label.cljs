(ns xquo.demo.divider-label
  (:require [react-native.react.core :as react]
            [xquo.core :as xquo]
            [xquo.demo.controls :as controls]
            [xquo.demo.screen :as screen]))

(defn screen [_]
  (let [[title set-title!]        (react/use-state "Messages")
        [compact? set-compact!]   (react/use-state true)
        [chevron set-chevron!]    (react/use-state :left)
        [counter? set-counter!]   (react/use-state false)
        [counter set-value!]      (react/use-state "5")
        [open? set-open!]         (react/use-state true)
        [line? set-line!]         (react/use-state true)
        [blur? set-blur!]         (react/use-state false)
        [disabled? set-disabled!] (react/use-state false)
        on-press!                 (react/use-callback (fn []
                                                        (set-open! not)
                                                        (js/alert "Pressed"))
                                                      [])]
    [screen/screen {:title   "Divider label"
                    :blur?   blur?
                    :preview [xquo/divider.label {:title         title
                                                  :compact?      compact?
                                                  :chevron       chevron
                                                  :counter       (when counter? counter)
                                                  :open?         open?
                                                  :divider-line? line?
                                                  :blur?         blur?
                                                  :disabled?     disabled?
                                                  :on-press      on-press!}]}
     [xquo/input {:label ":title" :value title :on-change-text set-title!}]
     [controls/choice {:label     ":chevron"
                       :value     chevron
                       :on-change set-chevron!
                       :options   [{:value nil :label "nil"}
                                   {:value :left :label ":left"}
                                   {:value :right :label ":right"}]}]
     [controls/checkbox {:label ":counter" :value counter? :on-change set-counter!}]
     (when counter?
       [xquo/input {:label ":counter" :value counter :on-change-text set-value!}])
     [controls/checkbox {:label ":compact?" :value compact? :on-change set-compact!}]
     [controls/checkbox {:label ":open?" :value open? :on-change set-open!}]
     [controls/checkbox {:label ":divider-line?" :value line? :on-change set-line!}]
     [controls/checkbox {:label ":blur?" :value blur? :on-change set-blur!}]
     [controls/checkbox {:label ":disabled?" :value disabled? :on-change set-disabled!}]]))
