(ns xquo.demo.selectors
  (:require [react-native.react.core :as react]
            [react-native.utils :refer [defstyle]]
            [xquo.context :as context]
            [xquo.core :as xquo]
            [xquo.demo.controls :as controls]
            [xquo.demo.screen :as screen]))

(defstyle ^:private centered
  {:align-items :center :gap 24 :width "100%"})

(defstyle ^:private samples
  {:flex-direction :row :align-items :center :gap 24})

(def ^:private titles
  {:selector "Selector" :disclaimer "Disclaimer" :filter "Filter selector"})

(defn- pressed! [] (js/alert "Pressed"))

(defn- demo [{:keys [kind]}]
  (let [[type set-type!]          (react/use-state :toggle)
        [selected? set-selected!] (react/use-state nil)
        [disabled? set-disabled!] (react/use-state nil)
        [blur? set-blur!]         (react/use-state nil)
        [color set-color!]        (react/use-state :color/primary)
        [size set-size!]          (react/use-state 32)
        [label? set-label!]       (react/use-state true)
        [icon? set-icon!]         (react/use-state true)
        select!                   (react/use-callback (fn [selected?]
                                                        (set-selected! selected?)
                                                        (js/alert "Pressed"))
                                                      [])]
    [screen/screen {:title   (titles kind)
                    :blur?   blur?
                    :preview [context/provider {:color color}
                              [:rn/view {:style centered}
                               (case kind
                                 :selector
                                 [:<>
                                  [xquo/selector {:type      type
                                                  :selected? selected?
                                                  :disabled? disabled?
                                                  :blur?     blur?
                                                  :on-select set-selected!
                                                  :on-press  pressed!}]
                                  [:rn/view {:pointer-events :none :style samples}
                                   [xquo/selector {:type type :selected? false :blur? blur?}]
                                   [xquo/selector {:type type :selected? true :blur? blur?}]
                                   [xquo/selector {:type type :selected? false :disabled? true :blur? blur?}]
                                   [xquo/selector {:type type :selected? true :disabled? true :blur? blur?}]]]

                                 :filter
                                 [xquo/filter {:size      size
                                               :selected? selected?
                                               :disabled? disabled?
                                               :blur?     blur?
                                               :icon      {:name :icon/unread}
                                               :on-select set-selected!
                                               :on-press  pressed!}
                                  (when label? "Unread")]

                                 :disclaimer
                                 [xquo/disclaimer {:selected? selected?
                                                   :disabled? disabled?
                                                   :blur?     blur?
                                                   :icon      (when icon? {:name :icon/lock})
                                                   :on-select select!}
                                  "I agree with the community rules"])]]}
     (when (= kind :selector)
       [controls/choice {:label     "Type"
                         :value     type
                         :on-change set-type!
                         :options   [{:value :toggle :label "Toggle"}
                                     {:value :radio :label "Radio"}
                                     {:value :checkbox :label "Checkbox"}
                                     {:value :filled-checkbox :label "Filled checkbox"}]}])
     (when (= kind :filter)
       [:<>
        [controls/choice {:label     "Size"
                          :value     size
                          :on-change set-size!
                          :options   [{:value 24 :label "24"} {:value 32 :label "32"}]}]
        [controls/checkbox {:label "Label" :value label? :on-change set-label!}]])
     (when (= kind :disclaimer)
       [controls/checkbox {:label "Icon" :value icon? :on-change set-icon!}])
     [controls/checkbox {:label "Selected" :value selected? :on-change set-selected!}]
     [controls/checkbox {:label "Disabled" :value disabled? :on-change set-disabled!}]
     [controls/checkbox {:label "Blur" :value blur? :on-change set-blur!}]
     [controls/choice {:label     "Customization color"
                       :value     color
                       :on-change set-color!
                       :options   [{:value :color/primary :label "Primary"}
                                   {:value :color/success :label "Green"}
                                   {:value :color/danger :label "Red"}]}]]))

(defn selector-screen [_] [demo {:kind :selector}])
(defn filter-screen [_] [demo {:kind :filter}])
(defn disclaimer-screen [_] [demo {:kind :disclaimer}])
