(ns xquo.demo.navigation.bottom-nav-tab
  (:require [react-native.react.core :as react]
            [react-native.utils :refer [defstyle]]
            [xquo.context :as context]
            [xquo.core :as xquo]
            [xquo.demo.controls :as controls]
            [xquo.demo.screen :as screen]))

(defstyle ^:private preview
  {:align-items :center})

(defn- pressed! [] (js/alert "Pressed"))

(defn screen [_]
  (let [[icon set-icon!]          (react/use-state :icon/wallet)
        [active? set-active!]     (react/use-state nil)
        [badge set-badge!]        (react/use-state nil)
        [value set-value!]        (react/use-state "5")
        [color set-color!]        (react/use-state :color/primary)
        [disabled? set-disabled!] (react/use-state nil)]
    [screen/screen {:title   "Bottom nav tab"
                    :blur?   true
                    :preview [context/provider {:color color}
                              [:rn/view {:style preview}
                               [xquo/navigation.bottom-nav-tab {:icon      {:name icon}
                                                                :active?   active?
                                                                :disabled? disabled?
                                                                :badge     (case badge
                                                                             :counter {:counter {:value value}}
                                                                             :notification {:type :notification}
                                                                             nil)
                                                                :on-press  pressed!}]]]}
     [controls/choice {:label     ":icon"
                       :value     icon
                       :on-change set-icon!
                       :options   [{:value :icon/wallet :label "Wallet"}
                                   {:value :icon/messages :label "Messages"}
                                   {:value :icon/browser :label "Browser"}]}]
     [controls/choice {:label     ":badge"
                       :value     badge
                       :on-change set-badge!
                       :options   [{:value nil :label "None"}
                                   {:value :counter :label "Counter"}
                                   {:value :notification :label "Notification"}]}]
     (when (= badge :counter)
       [xquo/input {:label "Counter value" :value value :on-change-text set-value! :max-length 4}])
     [controls/choice {:label     "Customization color"
                       :value     color
                       :on-change set-color!
                       :options   [{:value :color/primary :label "Primary"}
                                   {:value :color/success :label "Green"}
                                   {:value :color/danger :label "Red"}]}]
     [controls/checkbox {:label ":active?" :value active? :on-change set-active!}]
     [controls/checkbox {:label ":disabled?" :value disabled? :on-change set-disabled!}]]))
