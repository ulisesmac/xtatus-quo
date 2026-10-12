(ns xquo.demo.profile-input
  (:require [applied-science.js-interop :as j]
            [react-native.react.core :as react]
            [react-native.utils :refer [defstyle]]
            [xquo.context :as context]
            [xquo.core :as xquo]
            [xquo.demo.controls :as controls]
            [xquo.demo.screen :as screen]))

(defstyle ^:private preview
  {:width "100%" :max-width 335 :align-self :center})

(defn- submitted! [] (js/alert "Submitted"))

(defn screen [_]
  (let [[value set-value!]             (react/use-state "")
        [placeholder set-placeholder!] (react/use-state "Type something")
        [max-length set-max-length!]   (react/use-state 24)
        [blur? set-blur!]              (react/use-state nil)
        [disabled? set-disabled!]      (react/use-state nil)
        [color set-color!]             (react/use-state :color/primary)
        input-ref                      (react/use-ref nil)
        focus!                         (react/use-callback (fn []
                                                             (when-let [native (j/get input-ref :current)]
                                                               (j/call native :focus)))
                                                           [input-ref])
        blur!                          (react/use-callback (fn []
                                                             (when-let [native (j/get input-ref :current)]
                                                               (j/call native :blur)))
                                                           [input-ref])]
    [screen/screen {:title   "Profile input"
                    :blur?   blur?
                    :preview [context/provider {:color color}
                              [xquo/profile-input {:value             value
                                                   :on-change-text    set-value!
                                                   :placeholder       placeholder
                                                   :max-length        max-length
                                                   :disabled?         disabled?
                                                   :blur?             blur?
                                                   :input-ref         input-ref
                                                   :on-submit-editing submitted!
                                                   :style             preview}]]}
     [xquo/input {:label ":value" :value value :on-change-text set-value!}]
     [xquo/input {:label ":placeholder" :value placeholder :on-change-text set-placeholder!}]
     [controls/choice {:label     ":max-length"
                       :value     max-length
                       :on-change set-max-length!
                       :options   [{:value nil :label "No limit"}
                                   {:value 12 :label "12"}
                                   {:value 24 :label "24"}
                                   {:value 50 :label "50"}]}]
     [controls/choice {:label     "Customization color"
                       :value     color
                       :on-change set-color!
                       :options   [{:value :color/primary :label "Primary"}
                                   {:value :color/success :label "Green"}
                                   {:value :color/danger :label "Red"}]}]
     [xquo/button {:size 32 :type :outline :on-press focus! :disabled? disabled?} "Focus profile input"]
     [xquo/button {:size 32 :type :outline :on-press blur!} "Finish editing"]
     [controls/checkbox {:label ":disabled?" :value disabled? :on-change set-disabled!}]
     [controls/checkbox {:label ":blur?" :value blur? :on-change set-blur!}]]))
