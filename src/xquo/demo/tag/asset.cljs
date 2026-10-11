(ns xquo.demo.tag.asset
  (:require [react-native.react.core :as react]
            [xquo.core :as xquo]
            [xquo.demo.assets :as assets]
            [xquo.demo.controls :as controls]
            [xquo.demo.screen :as screen]))

(defn- on-press! []
  (js/alert "Asset tag pressed"))

(defn screen [_]
  (let [[variant set-variant!] (react/use-state :token)
        [size set-size!]       (react/use-state 24)
        [option set-option!]   (react/use-state :hold)
        [label set-label!]     (react/use-state "10 ETH")
        [blur? set-blur!]      (react/use-state false)
        on-variant-change!     (react/use-callback (fn [variant]
                                                     (set-variant! variant)
                                                     (set-label! (if (= variant :token) "10 ETH" "Collectible #123")))
                                                   [])]
    [screen/screen {:title   "Tag asset"
                    :blur?   blur?
                    :preview [xquo/tag.asset {:variant  variant
                                              :size     size
                                              :option   option
                                              :image    {:source assets/illustration}
                                              :label    label
                                              :blur?    blur?
                                              :on-press on-press!
                                              :style    {:align-self :center}}]}
     [controls/choice {:label     ":variant"
                       :value     variant
                       :on-change on-variant-change!
                       :options   [{:value :token :label ":token"} {:value :collectible :label ":collectible"}]}]
     [controls/choice {:label     ":size"
                       :value     size
                       :on-change set-size!
                       :options   [{:value 24 :label "24"} {:value 32 :label "32"}]}]
     [controls/choice {:label     ":option"
                       :value     option
                       :on-change set-option!
                       :options   [{:value nil :label "nil"}
                                   {:value :hold :label ":hold"}
                                   {:value :add :label ":add"}
                                   {:value :loading :label ":loading"}]}]
     [xquo/input {:label ":label" :value label :on-change-text set-label!}]
     [controls/checkbox {:label ":blur?" :value blur? :on-change set-blur!}]]))
