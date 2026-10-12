(ns xquo.demo.profile.tile
  (:require [react-native.react.core :as react]
            [react-native.utils :refer [defstyle style]]
            [xquo.context :as context]
            [xquo.core :as xquo]
            [xquo.demo.assets :as assets]
            [xquo.demo.controls :as controls]
            [xquo.demo.preview-assets :as preview-assets]
            [xquo.demo.screen :as screen]
            [xquo.foundations.colors :as colors]))

(defstyle ^:private preview
  {:align-items :center})

(defstyle ^:private accounts-button
  {:width 56 :height 28 :padding-horizontal 7 :padding-vertical 5 :border-radius 10})

(defstyle ^:private accounts
  {:flex-direction :row})

(defn- pressed! [] (js/alert "Pressed"))

(defn- accounts-footer []
  [xquo/button {:type :outline :size 24 :on-press pressed! :style accounts-button}
   [:rn/view {:style accounts}
    [xquo/avatar {:size 16 :content {:type :image :image {:source assets/profile-testing-1}}}]
    [xquo/avatar {:size 16 :content {:type :image :image {:source assets/profile-testing-2}} :style (style {:margin-left -4})}]
    [xquo/avatar {:size 16 :content {:type :image :image {:source assets/profile-testing-3}} :style (style {:margin-left -4})}]]])

(defn- joined-footer []
  (let [color (colors/get-color :color/success 50)]
    [xquo/tag.standard {:size    24
                        :content {:type :icon :icon {:name :icon/check :color color}}
                        :style   (style {:background-color (colors/get-color :color/success 50 10)
                                         :border-color     (colors/get-color :color/success 50 20)})}
     [xquo/text {:font :font/medium-13 :style (style {:color color})} "You’re there too"]]))

(defn screen [_]
  (let [[example set-example!]         (react/use-state :community)
        [cover set-cover!]             (react/use-state :image)
        [footer set-footer!]           (react/use-state :joined)
        [loading? set-loading!]        (react/use-state nil)
        [disabled? set-disabled!]      (react/use-state nil)
        [color set-color!]             (react/use-state :color/primary)
        [title set-title!]             (react/use-state "Wanderlight")
        [description set-description!] (react/use-state "Member")
        community?                     (= example :community)
        change-example!                (react/use-callback (fn [value]
                                                             (set-example! value)
                                                             (set-title! (if (= value :community) "Wanderlight" "Travel account"))
                                                             (set-description! (if (= value :community) "Member" "0x21a...49e")))
                                                           [set-example! set-title! set-description!])]
    [screen/screen {:title   "Profile tile"
                    :preview [context/provider {:color color}
                              [:rn/view {:style preview}
                               [xquo/profile.tile {:title             title
                                                   :description       description
                                                   :description-props (when-not community? {:font :font/monospace-13})
                                                   :avatar            (if community?
                                                                        {:content {:type :image :image {:source assets/profile}}}
                                                                        {:shape   :rounded-square
                                                                         :variant :solid
                                                                         :content {:type :emoji :text "🌙"}})
                                                   :cover             (cond
                                                                        (and community? (= cover :image)) {:type :image :image {:source preview-assets/coast}}
                                                                        community? {:type :color}
                                                                        :else nil)
                                                   :footer            (cond
                                                                        (not community?) [accounts-footer]
                                                                        (= footer :joined) [joined-footer]
                                                                        (= footer :permissions) [xquo/tag.permissions {:size 24 :locked? true :groups [{:tokens [{:source preview-assets/ceramics}]}]}]
                                                                        :else nil)
                                                   :loading?          loading?
                                                   :disabled?         disabled?
                                                   :on-press          pressed!}]]]}
     [controls/choice {:label     "Example"
                       :value     example
                       :on-change change-example!
                       :options   [{:value :community :label "Community"} {:value :account :label "Account"}]}]
     [xquo/input {:label "Title" :value title :on-change-text set-title!}]
     [xquo/input {:label "Description" :value description :on-change-text set-description!}]
     (when community?
       [:<>
        [controls/choice {:label     "Cover"
                          :value     cover
                          :on-change set-cover!
                          :options   [{:value :image :label "Image"} {:value :color :label "Color"}]}]
        [controls/choice {:label     "Footer"
                          :value     footer
                          :on-change set-footer!
                          :options   [{:value nil :label "None"} {:value :joined :label "Joined"} {:value :permissions :label "Permissions"}]}]])
     [controls/choice {:label     "Customization color"
                       :value     color
                       :on-change set-color!
                       :options   [{:value :color/primary :label "Primary"} {:value :color/success :label "Green"} {:value :color/danger :label "Red"}]}]
     [controls/checkbox {:label "Loading" :value loading? :on-change set-loading!}]
     [controls/checkbox {:label "Disabled" :value disabled? :on-change set-disabled!}]]))
