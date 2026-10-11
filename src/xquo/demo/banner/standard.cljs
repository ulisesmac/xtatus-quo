(ns xquo.demo.banner.standard
  (:require [react-native.react.core :as react]
            [xquo.context :as context]
            [xquo.core :as xquo]
            [xquo.demo.controls :as controls]
            [xquo.demo.screen :as screen]))

(defn- on-press! []
  (js/alert "Banner pressed"))

(defn screen [_]
  (let [[title set-title!]       (react/use-state "Be respectful of fellow community members")
        [icon set-icon!]         (react/use-state :icon/pin)
        [counter set-counter!]   (react/use-state "2")
        [counter? set-counter?!] (react/use-state true)
        [color set-color!]       (react/use-state nil)
        [blur? set-blur!]        (react/use-state true)]
    [context/provider {:color color}
     [screen/screen {:title   "Banner standard"
                     :blur?   blur?
                     :preview [xquo/banner.standard {:title    title
                                                     :icon     (when icon {:name icon})
                                                     :counter  (when counter? counter)
                                                     :blur?    blur?
                                                     :on-press on-press!}]}
      [xquo/input {:label ":title" :value title :on-change-text set-title!}]
      [controls/choice {:label     ":icon"
                        :value     icon
                        :on-change set-icon!
                        :options   [{:value nil :label "nil"}
                                    {:value :icon/pin :label ":icon/pin"}
                                    {:value :icon/info :label ":icon/info"}
                                    {:value :icon/arrow-right :label ":icon/arrow-right"}]}]
      [controls/checkbox {:label ":counter" :value counter? :on-change set-counter?!}]
      [xquo/input {:label ":counter value" :value counter :on-change-text set-counter!}]
      [controls/choice {:label     "context :color"
                        :value     color
                        :on-change set-color!
                        :options   [{:value nil :label "Inherited"}
                                    {:value :color/primary :label ":color/primary"}
                                    {:value :color/success :label ":color/success"}
                                    {:value :color/warning :label ":color/warning"}
                                    {:value :color/danger :label ":color/danger"}]}]
      [controls/checkbox {:label ":blur?" :value blur? :on-change set-blur!}]]]))
