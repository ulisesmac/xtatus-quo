(ns xquo.demo.dropdown
  (:require [react-native.react.core :as react]
            [react-native.utils :refer [defstyle]]
            [xquo.context :as context]
            [xquo.core :as xquo]
            [xquo.demo.assets :as assets]
            [xquo.demo.controls :as controls]
            [xquo.demo.screen :as screen]))

(defstyle ^:private button-preview
  {:align-self :center})

(defstyle ^:private input-preview
  {:align-self :center
   :width      288
   :max-width  "100%"})

(defn- pressed! [] (js/alert "Pressed"))

(defn- showcase [{:keys [kind]}]
  (let [input?                       (= kind :input)
        [value set-value!]           (react/use-state "Dropdown")
        [label set-label!]           (react/use-state "Label")
        [label? set-label-visible!]  (react/use-state true)
        [type set-type!]             (react/use-state :outline)
        [size set-size!]             (react/use-state 40)
        [content set-content!]       (react/use-state nil)
        [icon-name set-icon!]        (react/use-state :icon/placeholder)
        [active? set-active!]        (react/use-state false)
        [disabled? set-disabled!]    (react/use-state false)
        [background set-background!] (react/use-state nil)
        [color set-color!]           (react/use-state nil)
        leading                      (case content
                                       :icon {:type :icon :icon {:name icon-name}}
                                       :image {:type :image :image {:source assets/illustration}}
                                       nil)
        blur?                        (= background :blur)
        photo?                       (= background :photo)]
    [context/provider {:color color}
     [screen/screen {:title   (if input? "Dropdown input" "Dropdown button")
                     :blur?   blur?
                     :photo?  photo?
                     :preview (if input?
                                [xquo/dropdown.input {:label     (when label? label)
                                                      :content   leading
                                                      :active?   active?
                                                      :disabled? disabled?
                                                      :blur?     blur?
                                                      :on-press  pressed!
                                                      :style     input-preview}
                                 value]
                                [xquo/dropdown.button {:type      type
                                                       :size      size
                                                       :content   leading
                                                       :active?   active?
                                                       :disabled? disabled?
                                                       :blur?     blur?
                                                       :photo?    photo?
                                                       :on-press  pressed!
                                                       :style     button-preview}
                                 (when (not= content :image) value)])}
      (when (or input? (not= content :image))
        [xquo/input {:label "Text" :value value :on-change-text set-value!}])
      (if input?
        [:<>
         [controls/checkbox {:label ":label" :value label? :on-change set-label-visible!}]
         (when label?
           [xquo/input {:label "Label text" :value label :on-change-text set-label!}])]
        [:<>
         [controls/choice {:label     ":type"
                           :value     type
                           :on-change set-type!
                           :options   [{:value :outline :label "Outline"}
                                       {:value :grey :label "Grey"}
                                       {:value :customisation :label "Customisation"}
                                       {:value :ghost :label "Ghost"}]}]
         [controls/choice {:label     ":size"
                           :value     size
                           :on-change set-size!
                           :options   [{:value 40 :label "40"}
                                       {:value 32 :label "32"}
                                       {:value 24 :label "24"}]}]])
      [controls/choice {:label     ":content"
                        :value     content
                        :on-change set-content!
                        :options   [{:value nil :label "Text"}
                                    {:value :icon :label "Icon"}
                                    {:value :image :label (if input? "Image" "Image only")}]}]
      (when (= content :icon)
        [controls/choice {:label     "Icon"
                          :value     icon-name
                          :on-change set-icon!
                          :options   [{:value :icon/placeholder :label "Placeholder"}
                                      {:value :icon/calendar :label "Calendar"}
                                      {:value :icon/user :label "User"}]}])
      [controls/checkbox {:label ":active?" :value active? :on-change set-active!}]
      [controls/checkbox {:label ":disabled?" :value disabled? :on-change set-disabled!}]
      [controls/choice {:label     "Background"
                        :value     background
                        :on-change set-background!
                        :options   (cond-> [{:value nil :label "Plain"}
                                            {:value :blur :label "Blur"}]
                                     (not input?) (conj {:value :photo :label "Photo"}))}]
      (when (and (not input?) (= type :customisation))
        [controls/choice {:label     "Context color"
                          :value     color
                          :on-change set-color!
                          :options   [{:value nil :label "Inherited"}
                                      {:value :color/primary :label "Primary"}
                                      {:value :color/success :label "Success"}
                                      {:value :color/danger :label "Danger"}]}])]]))

(defn button-screen [_]
  [showcase {:kind :button}])

(defn input-screen [_]
  [showcase {:kind :input}])
