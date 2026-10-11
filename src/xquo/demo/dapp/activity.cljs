(ns xquo.demo.dapp.activity
  (:require [react-native.react.core :as react]
            [xquo.core :as xquo]
            [xquo.dapp.activity.view :as activity]
            [xquo.demo.assets :as assets]
            [xquo.demo.controls :as controls]
            [xquo.demo.dapp.data :as data]
            [xquo.demo.screen :as screen]))

(def ^:private uniswap
  {:props {:type :default :image-source assets/illustration} :label "app.uniswap.org"})

(def ^:private account
  {:props {:type :image :shape :squircle :image-source assets/illustration}
   :label "Trip to Vegas"})

(def ^:private savings
  {:props {:type :image :shape :squircle :image-source assets/illustration}
   :label "My savings"})

(def ^:private optimism
  {:props {:type :default :image-source assets/illustration} :label "Optimism"})

(def ^:private usdt
  {:props {:type :default :image-source assets/illustration} :label "100 USDT"})

(def ^:private examples
  {:connection       {:title "Connected"
                      :icon  :icon/connect
                      :parts [uniswap "connected to " account "via"
                              {:props {:type :default :image-source assets/illustration} :label "WalletConnect"}]}
   :sign             {:title "Sign in"
                      :icon  :icon/signature
                      :parts [uniswap "via" optimism]}
   :spending-cap     {:title "Set spending cap"
                      :icon  :icon/coins
                      :parts [usdt "in" account "for" uniswap "via" optimism]}
   :transfer         {:title "Transfer"
                      :icon  :icon/arrow-top-fade
                      :parts [usdt "from" account "to" uniswap "via" optimism]}
   :switched-account {:title "Switched account"
                      :icon  :icon/arrows-left-right
                      :parts ["From" account "to" savings]}
   :gas              {:title "Gas only transaction"
                      :icon  :icon/gas
                      :parts ["From" account "to"
                              {:props {:type :default} :label "0x1a2...B45"}
                              "via" {:props {:type :default :image-source assets/illustration} :label "Ethereum"}]}
   :revoke           {:title "Revoke approval"
                      :icon  :icon/unlink
                      :parts ["From" savings "to" uniswap]}
   :revoke-bundle    {:title   "Revoke approval"
                      :icon    :icon/unlink
                      :counter "x3"
                      :unread? true
                      :parts   ["From"
                                {:props {:type          :multi
                                         :shape         :squircle
                                         :image-sources [assets/illustration
                                                         assets/illustration
                                                         assets/illustration]}}
                                "to"
                                {:props {:type :multi :image-sources [assets/illustration assets/illustration assets/illustration]}}]}})

(defn- preview [{:keys [example title timestamp status blur?]}]
  (let [fixture (examples example)]
    (into [activity/activity {:title     (or title (:title fixture))
                              :timestamp timestamp
                              :icon      {:name (:icon fixture)}
                              :status    status
                              :counter   (:counter fixture)
                              :unread?   (:unread? fixture)
                              :blur?     blur?
                              :on-press  data/press!}]
          (map (fn [part]
                 (if (string? part)
                   part
                   [xquo/context-tag (assoc (:props part) :blur? blur? :on-press data/press!) (:label part)])))
          (:parts fixture))))

(defn screen [_]
  (let [[example set-example!]     (react/use-state :connection)
        [title set-title!]         (react/use-state nil)
        [timestamp set-timestamp!] (react/use-state "Today 22:50")
        [status set-status!]       (react/use-state nil)
        [blur? set-blur!]          (react/use-state false)]
    [screen/screen {:title   "Dapp activity"
                    :blur?   blur?
                    :preview [preview {:example   example
                                       :title     title
                                       :timestamp timestamp
                                       :status    status
                                       :blur?     blur?}]}
     [controls/choice {:label     "Example"
                       :value     example
                       :on-change set-example!
                       :options   (map (fn [value]
                                         {:value value :label (name value)})
                                       [:connection :sign :spending-cap :transfer :switched-account :gas :revoke :revoke-bundle])}]
     [xquo/input {:label ":title" :value (or title (:title (examples example))) :on-change-text set-title!}]
     [xquo/input {:label ":timestamp" :value timestamp :on-change-text set-timestamp!}]
     [controls/choice {:label     ":status"
                       :value     status
                       :on-change set-status!
                       :options   [{:value nil :label "nil"}
                                   {:value :pending :label ":pending"}
                                   {:value :confirmed :label ":confirmed"}
                                   {:value :finalized :label ":finalized"}
                                   {:value :error :label ":error"}]}]
     [controls/checkbox {:label ":blur?" :value blur? :on-change set-blur!}]]))
