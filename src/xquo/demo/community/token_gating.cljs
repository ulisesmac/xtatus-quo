(ns xquo.demo.community.token-gating
  (:require [react-native.react.core :as react]
            [react-native.utils :refer [defstyle]]
            [xquo.context :as context]
            [xquo.core :as xquo]
            [xquo.demo.assets :as assets]
            [xquo.demo.controls :as controls]
            [xquo.demo.screen :as screen]))

(defstyle ^:private preview
  {:height "100%"})

(defstyle ^:private preview-content
  {:flex-grow        1
   :justify-content  :center
   :padding-vertical 12})

(defn- on-press! []
  (js/alert "Requirement pressed"))

(defn- on-info! []
  (js/alert "Requirements information"))

(defn- on-action! []
  (js/alert "Requirements action pressed"))

(def ^:private not-met-group
  {:rows [{:style {:column-gap 10}
           :items [{:image {:source assets/illustration} :label "20 USDC" :option :hold :on-press on-press!}
                   {:image {:source assets/illustration} :label "200 Dai" :style {:opacity 0.3} :on-press on-press!}
                   {:image {:source assets/illustration} :label "20 BLZ" :style {:opacity 0.3} :on-press on-press!}]}
          {:items [{:image {:source assets/illustration} :label "1 eth" :style {:opacity 0.3} :on-press on-press!}
                   {:image {:source assets/illustration} :label "10,000 DOGE" :style {:opacity 0.3} :on-press on-press!}]}]})

(def ^:private met-group
  {:style {:row-gap 12}
   :rows  [{:style {:column-gap 10}
            :items [{:image {:source assets/illustration} :label "10 RARI" :option :hold :on-press on-press!}
                    {:image {:source assets/illustration} :label "10 ETH" :option :hold :on-press on-press!}
                    {:image {:source assets/illustration} :label "20 RARE" :option :hold :on-press on-press!}]}
           {:style {:column-gap 10}
            :items [{:image {:source assets/illustration} :label "50 LRC" :option :hold :on-press on-press!}
                    {:image {:source assets/illustration} :label "10 BQX" :option :hold :on-press on-press!}]}]})

(def ^:private alternative-group
  {:rows [{:items [{:image {:source assets/illustration} :label "20 USDC" :on-press on-press!}
                   {:image {:source assets/illustration} :label "200 Dai" :on-press on-press!}]}
          {:prefix "or"
           :items  [{:image {:source assets/illustration} :label "10 ETH" :on-press on-press!}
                    {:image {:source assets/illustration} :label "20 RARE" :on-press on-press!}]}]})

(def ^:private role-section
  {:title       "You’re eligible to join as Owner"
   :description "You hodl:"
   :groups      [{:style {:padding-top 4}
                  :rows  [{:items [{:variant  :collectible
                                    :image    {:source assets/illustration}
                                    :label    "Collectible #123"
                                    :option   :hold
                                    :on-press on-press!}]}]}]})

(def ^:private titles
  {:community        {:met "You’re eligible to join" :not-met "You’re not eligible to join"}
   :channel-list     {:met "View only requirements met" :not-met "View only requirements not met"}
   :channel-detail   {:met "Post requirements met" :not-met "Post requirements not met"}
   :community-detail {:met "You’re eligible to join as Member" :not-met "You’re not eligible to join as Member"}})

(defn screen [_]
  (let [[variant set-variant!]          (react/use-state :community)
        [preset set-preset!]            (react/use-state :not-met)
        [group-count set-groups!]       (react/use-state 1)
        [two-roles? set-two-roles!]     (react/use-state false)
        [button? set-button!]           (react/use-state true)
        [information? set-information!] (react/use-state false)
        [separator set-separator!]      (react/use-state "or")
        [color set-color!]              (react/use-state nil)
        met?                            (= preset :met)
        groups                          (into [(if met? met-group not-met-group)]
                                              (repeat (dec group-count) alternative-group))
        sections                        (cond-> []
                                          (and (= variant :community-detail) two-roles?)
                                          (conj role-section)
                                          :always
                                          (conj {:title       (get-in titles [variant preset])
                                                 :description (if met? "You hodl:" "You must hodl:")
                                                 :groups      groups})
                                          (= variant :channel-list)
                                          (conj {:title       "Post requirements not met"
                                                 :description "You must hodl 1 of these:"
                                                 :groups      [not-met-group]}))]
    [context/provider {:color color}
     [screen/screen {:title   "Community token gating"
                     :preview [:rn/scroll-view {:style                        preview
                                                :content-container-style      preview-content
                                                :keyboard-should-persist-taps :handled}
                               [xquo/community.token-gating {:variant         variant
                                                             :sections        sections
                                                             :header          {:title "# onboarding" :leading-image assets/illustration}
                                                             :info            (when (= variant :community) {:on-press on-info!})
                                                             :separator-label separator
                                                             :button          (when button?
                                                                                {:label     (if (= variant :channel-detail)
                                                                                              "Manage shared addresses"
                                                                                              "Join community")
                                                                                 :icon      (when (not= variant :channel-detail)
                                                                                              {:name (if met? :icon/lock-open :icon/lock)
                                                                                               :side :left})
                                                                                 :disabled? (and (not met?) (= variant :community))
                                                                                 :on-press  on-action!})
                                                             :information     (when information?
                                                                                {:description "Connect an address that holds these tokens to meet the requirements."})
                                                             :footer-text     (when (= variant :channel-list)
                                                                                "You can view this channel, but must meet the post requirements to send messages.")}]]}
      [controls/choice {:label     ":variant"
                        :value     variant
                        :on-change set-variant!
                        :options   [{:value :community :label ":community"}
                                    {:value :channel-list :label ":channel-list"}
                                    {:value :channel-detail :label ":channel-detail"}
                                    {:value :community-detail :label ":community-detail"}]}]
      [controls/choice {:label     ":sections preset"
                        :value     preset
                        :on-change set-preset!
                        :options   [{:value :not-met :label "Not met"}
                                    {:value :met :label "Met"}]}]
      [controls/choice {:label     ":groups count"
                        :value     group-count
                        :on-change set-groups!
                        :options   (map (fn [count]
                                          {:value count :label (str count)})
                                        (range 1 6))}]
      [controls/checkbox {:label ":sections — two roles" :value two-roles? :on-change set-two-roles!}]
      [controls/checkbox {:label ":button" :value button? :on-change set-button!}]
      [controls/checkbox {:label ":information" :value information? :on-change set-information!}]
      [xquo/input {:label ":separator-label" :value separator :on-change-text set-separator!}]
      [controls/choice {:label     "context :color"
                        :value     color
                        :on-change set-color!
                        :options   [{:value nil :label "Inherited"}
                                    {:value :color/primary :label ":color/primary"}
                                    {:value :color/success :label ":color/success"}
                                    {:value :color/danger :label ":color/danger"}]}]]]))
