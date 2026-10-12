(ns xquo.demo.avatar
  (:require [react-native.react.core :as react]
            [xquo.core :as xquo]
            [xquo.demo.assets :as assets]
            [xquo.demo.controls :as controls]
            [xquo.demo.screen :as screen]))

(defn- on-press! []
  (js/alert "Pressed"))

(defn screen [_]
  (let [[size set-size!]                   (react/use-state 80)
        [shape set-shape!]                 (react/use-state :circle)
        [content-type set-content!]        (react/use-state :image)
        [profile-image set-profile-image!] (react/use-state assets/profile-testing-1)
        [initials set-initials!]           (react/use-state "AY")
        [emoji set-emoji!]                 (react/use-state "🍑")
        [icon set-icon!]                   (react/use-state :icon/user)
        [variant set-variant!]             (react/use-state :soft)
        [color set-color!]                 (react/use-state :color/primary)
        [border set-border!]               (react/use-state nil)
        [badge set-badge!]                 (react/use-state nil)
        [blur? set-blur!]                  (react/use-state false)
        [pressable? set-pressable!]        (react/use-state true)]
    [screen/screen {:title   "Avatar"
                    :blur?   blur?
                    :preview [xquo/avatar {:size     size
                                           :shape    shape
                                           :variant  variant
                                           :color    color
                                           :border   border
                                           :blur?    blur?
                                           :style    {:align-self :center}
                                           :content  (case content-type
                                                       :image {:type :image :image {:source profile-image}}
                                                       :icon {:type :icon :icon {:name icon}}
                                                       :initials {:type :initials :text initials}
                                                       :emoji {:type :emoji :text emoji}
                                                       nil)
                                           :badge    (case badge
                                                       :online {:status :online}
                                                       :offline {:status :offline}
                                                       :icon {:icon {:name :icon/lock}}
                                                       :image {:image {:source assets/illustration}}
                                                       nil)
                                           :on-press (when pressable? on-press!)}]}
     [controls/choice {:label     ":content"
                       :value     content-type
                       :on-change set-content!
                       :options   [{:value :image :label "Image"}
                                   {:value :icon :label "Icon"}
                                   {:value :initials :label "Initials"}
                                   {:value :emoji :label "Emoji"}
                                   {:value nil :label "None"}]}]
     (case content-type
       :image [controls/choice {:label     "Profile image"
                                :value     profile-image
                                :on-change set-profile-image!
                                :options   [{:value assets/profile-testing-1 :label "Profile 1"}
                                            {:value assets/profile-testing-2 :label "Profile 2"}
                                            {:value assets/profile-testing-3 :label "Profile 3"}]}]
       :initials [xquo/input {:label "[:content :text]" :value initials :on-change-text set-initials!}]
       :emoji [xquo/input {:label "[:content :text]" :value emoji :on-change-text set-emoji!}]
       :icon [controls/choice {:label     "[:content :icon :name]"
                               :value     icon
                               :on-change set-icon!
                               :options   [{:value :icon/user :label "User"}
                                           {:value :icon/external :label "External"}
                                           {:value :icon/info :label "Info"}]}]
       nil)
     [controls/choice {:label     ":shape"
                       :value     shape
                       :on-change set-shape!
                       :options   [{:value :circle :label "Circle"}
                                   {:value :rounded-square :label "Rounded square"}
                                   {:value :hexagon :label "Hexagon"}]}]
     [controls/choice {:label     ":size"
                       :value     size
                       :on-change set-size!
                       :options   (map (fn [size]
                                         {:value size :label (str size)})
                                       [16 20 24 28 32 36 48 56 64 80])}]
     [controls/choice {:label     ":variant"
                       :value     variant
                       :on-change set-variant!
                       :options   [{:value :solid :label "Solid"}
                                   {:value :soft :label "Soft"}
                                   {:value nil :label "None"}]}]
     [controls/choice {:label     ":color"
                       :value     color
                       :on-change set-color!
                       :options   [{:value :color/primary :label "Primary"}
                                   {:value :color/success :label "Success"}
                                   {:value :color/warning :label "Warning"}
                                   {:value :color/neutral :label "Neutral"}]}]
     [controls/choice {:label     ":border"
                       :value     border
                       :on-change set-border!
                       :options   [{:value nil :label "None"}
                                   {:value :solid :label "Solid"}
                                   {:value :dashed :label "Dashed"}]}]
     [controls/choice {:label     ":badge"
                       :value     badge
                       :on-change set-badge!
                       :options   [{:value nil :label "None"}
                                   {:value :online :label "Online"}
                                   {:value :offline :label "Offline"}
                                   {:value :icon :label "Icon"}
                                   {:value :image :label "Image"}]}]
     [controls/checkbox {:label ":blur?" :value blur? :on-change set-blur!}]
     [controls/checkbox {:label ":on-press" :value pressable? :on-change set-pressable!}]]))
