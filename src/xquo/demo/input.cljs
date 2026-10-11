(ns xquo.demo.input
  (:require [react-native.react.core :as react]
            [react-native.utils :refer [defstyle]]
            [xquo.core :as xquo]
            [xquo.demo.assets :as assets]
            [xquo.demo.controls :as controls]
            [xquo.demo.screen :as screen]))

(defstyle ^:private preview-style
  {:width      288
   :max-width  "100%"
   :align-self :center})

(defn- pressed! [] (js/alert "Pressed"))

(defn- example [{:keys [controlled?] :as props}]
  (let [[value set-value!] (react/use-state "")]
    [:rn/view {:style preview-style}
     [xquo/input (cond-> (dissoc props :controlled?)
                   controlled? (assoc :value value :on-change-text set-value!))]]))

(defn screen [_]
  (let [[size set-size!]               (react/use-state 40)
        [label? set-label!]            (react/use-state true)
        [counter? set-counter!]        (react/use-state false)
        [leading set-leading!]         (react/use-state nil)
        [icon-name set-icon!]          (react/use-state :icon/envelope)
        [clearable? set-clearable!]    (react/use-state true)
        [button? set-button!]          (react/use-state false)
        [button-size set-button-size!] (react/use-state 24)
        [button-type set-button-type!] (react/use-state :outline)
        [error? set-error!]            (react/use-state false)
        [disabled? set-disabled!]      (react/use-state false)
        [blur? set-blur!]              (react/use-state false)
        [multiline? set-multiline!]    (react/use-state false)
        [bounded? set-bounded!]        (react/use-state false)
        [controlled? set-controlled!]  (react/use-state false)
        [secure? set-secure!]          (react/use-state false)]
    [screen/screen {:title   "Input"
                    :blur?   blur?
                    :preview [example {:key               controlled?
                                       :controlled?       controlled?
                                       :size              size
                                       :label             (when label? "Label")
                                       :max-length        (when counter? 280)
                                       :placeholder       "Type something"
                                       :content           (case leading
                                                            :icon {:type :icon :icon {:name icon-name}}
                                                            :image {:type :image :image {:source assets/illustration}}
                                                            nil)
                                       :clearable?        clearable?
                                       :button            (when button? {:label "Button" :type button-type :size button-size :on-press pressed!})
                                       :error?            error?
                                       :disabled?         disabled?
                                       :blur?             blur?
                                       :multiline?        multiline?
                                       :min-height        (when bounded? 80)
                                       :max-height        (when bounded? 124)
                                       :secure-text-entry secure?
                                       :auto-capitalize   :none}]}
     [controls/choice {:label     ":size"
                       :value     size
                       :on-change set-size!
                       :options   [{:value 40 :label "40"} {:value 32 :label "32"}]}]
     [controls/choice {:label     ":content"
                       :value     leading
                       :on-change set-leading!
                       :options   [{:value nil :label "None"} {:value :icon :label "Icon"} {:value :image :label "Image"}]}]
     (when (= leading :icon)
       [controls/choice {:label     ":icon :name"
                         :value     icon-name
                         :on-change set-icon!
                         :options   [{:value :icon/envelope :label "Envelope"}
                                     {:value :icon/search :label "Search"}
                                     {:value :icon/user :label "User"}]}])
     [controls/checkbox {:label ":label" :value label? :on-change set-label!}]
     [controls/checkbox {:label ":max-length (280)" :value counter? :on-change set-counter!}]
     [controls/checkbox {:label ":clearable?" :value clearable? :on-change set-clearable!}]
     [controls/checkbox {:label ":button" :value button? :on-change set-button!}]
     (when button?
       [:<>
        [controls/choice {:label     "Button :size"
                          :value     button-size
                          :on-change set-button-size!
                          :options   [{:value 24 :label "24"} {:value 32 :label "32 (override)"}]}]
        [controls/choice {:label     "Button :type"
                          :value     button-type
                          :on-change set-button-type!
                          :options   [{:value :outline :label "Outline"} {:value :grey :label "Grey"} {:value :primary :label "Primary"}]}]])
     [controls/checkbox {:label ":error?" :value error? :on-change set-error!}]
     [controls/checkbox {:label ":disabled?" :value disabled? :on-change set-disabled!}]
     [controls/checkbox {:label ":blur?" :value blur? :on-change set-blur!}]
     [controls/checkbox {:label ":multiline?" :value multiline? :on-change set-multiline!}]
     (when multiline?
       [controls/checkbox {:label ":min-height 80 / :max-height 124" :value bounded? :on-change set-bounded!}])
     [controls/checkbox {:label "Controlled value" :value controlled? :on-change set-controlled!}]
     [controls/checkbox {:label ":secure-text-entry" :value secure? :on-change set-secure!}]]))
