(ns xquo.demo.button
  (:require [react-native.react.core :as react]
            [xquo.core :as xquo]
            [xquo.demo.controls :as controls]
            [xquo.demo.screen :as screen]))

(defn- on-press! []
  (js/alert "Pressed"))

(defn screen [_]
  (let [[content set-content!]         (react/use-state "Button")
        [type set-type!]               (react/use-state :primary)
        [size set-size!]               (react/use-state 40)
        [color set-color!]             (react/use-state nil)
        [icon-layout set-icon-layout!] (react/use-state nil)
        [icon-name set-icon-name!]     (react/use-state :icon/external)
        [right-icon set-right-icon!]   (react/use-state :icon/arrow-right)
        [disabled? set-disabled!]      (react/use-state false)
        [photo? set-photo!]            (react/use-state false)
        [blur? set-blur!]              (react/use-state false)
        on-photo-change!               (react/use-callback (fn [value]
                                                             (set-photo! value)
                                                             (when value
                                                               (set-blur! false)))
                                                           [])
        on-blur-change!                (react/use-callback (fn [value]
                                                             (set-blur! value)
                                                             (when value
                                                               (set-photo! false)))
                                                           [])]
    [screen/screen {:title   "Button"
                    :photo?  photo?
                    :blur?   blur?
                    :preview [xquo/button {:type      type
                                           :size      size
                                           :color     color
                                           :photo?    photo?
                                           :blur?     blur?
                                           :disabled? disabled?
                                           :on-press  on-press!
                                           :style     {:align-self :center}
                                           :icon      (when (#{:left :right :only} icon-layout)
                                                        {:name icon-name
                                                         :side (if (= icon-layout :left) :left :right)})
                                           :icons     (when (= icon-layout :both)
                                                        {:left  {:name icon-name}
                                                         :right {:name right-icon}})}
                              (when-not (= icon-layout :only) content)]}
     [xquo/input {:label "content" :value content :on-change-text set-content!}]
     [controls/choice {:label     ":type"
                       :value     type
                       :on-change set-type!
                       :options   (map (fn [type]
                                         {:value type :label (str type)})
                                       [:primary :positive :grey :dark-grey :outline :ghost :danger :black])}]
     [controls/choice {:label     ":size"
                       :value     size
                       :on-change set-size!
                       :options   [{:value 40 :label "40"}
                                   {:value 32 :label "32"}
                                   {:value 24 :label "24"}]}]
     [controls/choice {:label     ":color"
                       :value     color
                       :on-change set-color!
                       :options   [{:value nil :label "Context"}
                                   {:value :color/primary :label ":color/primary"}
                                   {:value :color/success :label ":color/success"}
                                   {:value :color/warning :label ":color/warning"}
                                   {:value :color/danger :label ":color/danger"}]}]
     [controls/choice {:label     ":icon / :icons"
                       :value     icon-layout
                       :on-change set-icon-layout!
                       :options   [{:value nil :label "nil"}
                                   {:value :left :label ":icon, :side :left"}
                                   {:value :right :label ":icon, :side :right"}
                                   {:value :both :label ":icons, :left + :right"}
                                   {:value :only :label ":icon, no content"}]}]
     (when icon-layout
       [controls/choice {:label     (if (= icon-layout :both) "[:icons :left :name]" "[:icon :name]")
                         :value     icon-name
                         :on-change set-icon-name!
                         :options   [{:value :icon/external :label "External"}
                                     {:value :icon/user :label "User"}
                                     {:value :icon/info :label "Info"}]}])
     (when (= icon-layout :both)
       [controls/choice {:label     "[:icons :right :name]"
                         :value     right-icon
                         :on-change set-right-icon!
                         :options   [{:value :icon/arrow-right :label "Arrow"}
                                     {:value :icon/external :label "External"}
                                     {:value :icon/info :label "Info"}]}])
     [controls/checkbox {:label ":photo?" :value photo? :on-change on-photo-change!}]
     [controls/checkbox {:label ":blur?" :value blur? :on-change on-blur-change!}]
     [controls/checkbox {:label ":disabled?" :value disabled? :on-change set-disabled!}]]))
