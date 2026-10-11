(ns xquo.demo.community.channel-action
  (:require [react-native.react.core :as react]
            [xquo.context :as context]
            [xquo.core :as xquo]
            [xquo.demo.controls :as controls]
            [xquo.demo.screen :as screen]))

(defn- on-press! []
  (js/alert "Channel action pressed"))

(defn screen [_]
  (let [[title set-title!]        (react/use-state "Pinned\nMessages")
        [counter set-counter!]    (react/use-state "5")
        [counter? set-counter?]   (react/use-state true)
        [width set-width!]        (react/use-state 104)
        [disabled? set-disabled!] (react/use-state false)
        [color set-color!]        (react/use-state nil)]
    [context/provider {:color color}
     [screen/screen {:title   "Community channel action"
                     :preview [xquo/community.channel-action {:title     title
                                                              :icon      {:name :icon/placeholder}
                                                              :counter   (when counter? counter)
                                                              :disabled? disabled?
                                                              :on-press  on-press!
                                                              :style     {:width width :align-self :center}}]}
      [xquo/input {:label ":title" :value title :on-change-text set-title!}]
      [xquo/input {:label ":counter" :value counter :on-change-text set-counter!}]
      [controls/checkbox {:label ":counter present" :value counter? :on-change set-counter?}]
      [controls/checkbox {:label ":disabled?" :value disabled? :on-change set-disabled!}]
      [controls/choice {:label     "[:style :width]"
                        :value     width
                        :on-change set-width!
                        :options   [{:value 104 :label "104"} {:value 216 :label "216"}]}]
      [controls/choice {:label     "context :color"
                        :value     color
                        :on-change set-color!
                        :options   [{:value nil :label "Inherited"}
                                    {:value :color/success :label ":color/success"}
                                    {:value :color/danger :label ":color/danger"}]}]]]))
