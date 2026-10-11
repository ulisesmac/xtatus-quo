(ns xquo.demo.browser.navigation
  (:require [react-native.react.core :as react]
            [xquo.browser.navigation.view :as navigation]
            [xquo.core :as xquo]
            [xquo.demo.assets :as assets]
            [xquo.demo.controls :as controls]
            [xquo.demo.screen :as screen]))

(defn- press! [] (js/alert "Pressed"))
(defn- submit! [_] (js/alert "Submitted"))

(defn screen [_]
  (let [[value set-value!]      (react/use-state "https://rarible.com")
        [back? set-back!]       (react/use-state true)
        [forward? set-forward!] (react/use-state false)
        [blur? set-blur!]       (react/use-state false)]
    [screen/screen {:title   "Browser navigation"
                    :blur?   blur?
                    :preview [navigation/navigation {:input   {:value             value
                                                               :on-change-text    set-value!
                                                               :on-submit-editing submit!
                                                               :image             {:source assets/illustration}
                                                               :secure?           true}
                                                     :avatar  {:content  {:type :emoji-image :image {:source assets/illustration}}
                                                               :on-press press!}
                                                     :back    {:on-press press! :disabled? (not back?)}
                                                     :forward {:on-press press! :disabled? (not forward?)}
                                                     :reload  {:on-press press!}
                                                     :blur?   blur?}]}
     [xquo/input {:label ":input :value" :value value :on-change-text set-value!}]
     [controls/checkbox {:label ":back enabled" :value back? :on-change set-back!}]
     [controls/checkbox {:label ":forward enabled" :value forward? :on-change set-forward!}]
     [controls/checkbox {:label ":blur?" :value blur? :on-change set-blur!}]]))
