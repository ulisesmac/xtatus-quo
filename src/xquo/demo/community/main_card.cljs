(ns xquo.demo.community.main-card
  (:require [react-native.react.core :as react]
            [xquo.context :as context]
            [xquo.core :as xquo]
            [xquo.demo.community.assets :as assets]
            [xquo.demo.controls :as controls]
            [xquo.demo.screen :as screen]))

(defn- on-press! []
  (js/alert "Community card pressed"))

(def ^:private tags
  [{:label "Crypto" :content {:type :emoji :text "💸"}}
   {:label "DeFi" :content {:type :emoji :text "📈"}}
   {:label "Markets" :content {:type :emoji :text "💎"}}])

(def ^:private stats
  [{:icon {:name :icon/users} :value "629.2K"}
   {:icon {:name :icon/flash} :value "112.1K"}])

(defn screen [_]
  (let [[title set-title!]              (react/use-state "Status.im")
        [description set-description!]  (react/use-state "Your portal to Web3. Secure wallet. dApp")
        [cover? set-cover!]             (react/use-state true)
        [loading? set-loading!]         (react/use-state false)
        [permissions? set-permissions!] (react/use-state true)
        [locked? set-locked!]           (react/use-state true)
        [color set-color!]              (react/use-state nil)]
    [context/provider {:color color}
     [screen/screen {:title   "Community main card"
                     :preview [xquo/community.main-card {:title       title
                                                         :description description
                                                         :avatar      {:content {:type :image :image {:source assets/status}}}
                                                         :cover       (when cover? {:source assets/cover})
                                                         :permissions (when permissions?
                                                                        {:locked? locked?
                                                                         :groups  [{:tokens [{:source assets/status}]}]})
                                                         :stats       stats
                                                         :tags        tags
                                                         :loading?    loading?
                                                         :on-press    on-press!
                                                         :style       {:align-self :center}}]}
      [xquo/input {:label ":title" :value title :on-change-text set-title!}]
      [xquo/input {:label ":description" :value description :on-change-text set-description!}]
      [controls/checkbox {:label ":cover" :value cover? :on-change set-cover!}]
      [controls/checkbox {:label ":loading?" :value loading? :on-change set-loading!}]
      [controls/checkbox {:label ":permissions" :value permissions? :on-change set-permissions!}]
      [controls/checkbox {:label "[:permissions :locked?]" :value locked? :on-change set-locked!}]
      [controls/choice {:label     "context :color"
                        :value     color
                        :on-change set-color!
                        :options   [{:value nil :label "Inherited"}
                                    {:value :color/success :label ":color/success"}
                                    {:value :color/danger :label ":color/danger"}]}]]]))
