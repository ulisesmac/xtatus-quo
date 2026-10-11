(ns xquo.demo.browser.input
  (:require [react-native.react.core :as react]
            [xquo.browser.input.view :as input]
            [xquo.core :as xquo]
            [xquo.demo.assets :as assets]
            [xquo.demo.controls :as controls]
            [xquo.demo.screen :as screen]))

(defn- submit! [_] (js/alert "Submitted"))

(defn screen [_]
  (let [[value set-value!]             (react/use-state "https://rarible.com")
        [placeholder set-placeholder!] (react/use-state "Search or enter dapp domain")
        [image? set-image!]            (react/use-state true)
        [secure? set-secure!]          (react/use-state true)
        [blur? set-blur!]              (react/use-state false)]
    [screen/screen {:title   "Browser input"
                    :blur?   blur?
                    :preview [input/input {:value             value
                                           :on-change-text    set-value!
                                           :on-submit-editing submit!
                                           :placeholder       placeholder
                                           :image             (when image? {:source assets/illustration})
                                           :secure?           secure?
                                           :blur?             blur?}]}
     [xquo/input {:label ":value" :value value :on-change-text set-value!}]
     [xquo/input {:label ":placeholder" :value placeholder :on-change-text set-placeholder!}]
     [controls/checkbox {:label ":image" :value image? :on-change set-image!}]
     [controls/checkbox {:label ":secure?" :value secure? :on-change set-secure!}]
     [controls/checkbox {:label ":blur?" :value blur? :on-change set-blur!}]]))
