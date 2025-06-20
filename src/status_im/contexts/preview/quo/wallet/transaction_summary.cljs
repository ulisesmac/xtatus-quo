(ns status-im.contexts.preview.quo.wallet.transaction-summary
  (:require
    [quo.core :as quo]
    [quo.foundations.resources :as quo.resources]
    [reagent.core :as reagent]
    [status-im.common.resources :as resources]
    [status-im.contexts.preview.quo.preview :as preview]))

(def asset-snt
  {:size   24
   :type   :token
   :token  "SNT"
   :amount 1500})

(def asset-eth
  {:size   24
   :type   :token
   :token  "ETH"
   :amount 75})

(def asset-dai
  {:size   24
   :type   :token
   :token  "DAI"
   :amount 2400})

(def asset-collectible
  {:size               24
   :type               :collectible
   :collectible        (resources/mock-images :collectible)
   :collectible-name   "Collectible"
   :collectible-number "123"})

(def trip-to-vegas
  {:size         24
   :type         :account
   :account-name "Trip to Vegas"
   :emoji        "🤑"})

(def piggy-bank
  {:size         24
   :type         :account
   :account-name "Piggy bank"
   :emoji        "🐷"})

(def collectibles-vault
  {:size                24
   :type                :account
   :account-name        "Collectibles vault"
   :customization-color :orange
   :emoji               "🎮"})

(def aretha-gosling
  {:size            24
   :type            :default
   :full-name       "Aretha Gosling"
   :profile-picture (resources/mock-images :user-picture-female2)})

(def james-bond
  {:size            24
   :type            :default
   :full-name       "James Bond"
   :profile-picture (resources/mock-images :user-picture-male4)})

(def mark-libot
  {:size            24
   :type            :default
   :full-name       "Mark Libot"
   :profile-picture (resources/mock-images :user-picture-male4)})

(def mainnet
  {:size         24
   :type         :network
   :network-logo (quo.resources/get-network :ethereum)
   :network-name "Ethereum"})

(def optimism
  {:size         24
   :type         :network
   :network-logo (quo.resources/get-network :optimism)
   :network-name "Optimism"})

(def arbitrum
  {:size         24
   :type         :network
   :network-logo (quo.resources/get-network :arbitrum)
   :network-name "Arbitrum"})

(def multinetwork
  {:size     24
   :type     :multinetwork
   :networks [(quo.resources/get-network :ethereum)
              (quo.resources/get-network :arbitrum)
              (quo.resources/get-network :optimism)]})

(def moonpay
  {:size         24
   :type         :network
   :network-logo (quo.resources/get-network :ethereum)
   :network-name "Moonpay"})

(def binance
  {:size         24
   :type         :network
   :network-logo (quo.resources/get-network :ethereum)
   :network-name "Binance"})

(def context-tags
  [{:key   asset-snt
    :value "SNT"}
   {:key   asset-eth
    :value "ETH"}
   {:key   asset-dai
    :value "UNK"}
   {:key   asset-collectible
    :value "Collectible"}
   {:key   trip-to-vegas
    :value "Account: Trip to Vegas"}
   {:key   piggy-bank
    :value "Account: Piggy bank"}
   {:key   collectibles-vault
    :value "Account: Collectibles vault"}
   {:key   aretha-gosling
    :value "Person: Aretha Gosling"}
   {:key   mark-libot
    :value "Person: Mark Libot"}
   {:key   james-bond
    :value "Person: James Bond"}
   {:key   mainnet
    :value "Network: Mainnet"}
   {:key   optimism
    :value "Network: Optimism"}
   {:key   arbitrum
    :value "Network: Arbitrum"}
   {:key   multinetwork
    :value "Network: Multinetwork"}
   {:key   moonpay
    :value "Market: Moonpay"}
   {:key   binance
    :value "Market: Binance"}])

(def prefixes
  [{:key   :t/to
    :value "To"}
   {:key   :t/in
    :value "In"}
   {:key   :t/via
    :value "Via"}
   {:key   :t/from
    :value "From"}
   {:key   :t/on
    :value "On"}
   {:key   :t/at
    :value "At"}])

(def descriptor
  (concat
   [{:key     :transaction
     :type    :select
     :options [{:key :send}
               {:key :swap}
               {:key :bridge}]}
    {:label   "Slot 1"
     :key     :first-tag
     :type    :select
     :options context-tags}
    {:label   "Slot 2 prefix"
     :key     :second-tag-prefix
     :type    :select
     :options prefixes}
    {:label   "Slot 2"
     :key     :second-tag
     :type    :select
     :options context-tags}
    {:label   "Slot 3 prefix"
     :key     :third-tag-prefix
     :type    :select
     :options prefixes}
    {:label   "Slot 3"
     :key     :third-tag
     :type    :select
     :options context-tags}
    {:label   "Slot 4 prefix"
     :key     :fourth-tag-prefix
     :type    :select
     :options prefixes}
    {:label   "Slot 4"
     :key     :fourth-tag
     :type    :select
     :options context-tags}
    {:label   "Slot 5"
     :key     :fifth-tag
     :type    :select
     :options context-tags}
    {:key  :max-fees
     :type :text}
    {:key  :nonce
     :type :number}
    {:key  :input-data
     :type :text}]))

(defn view
  []
  (let [component-state (reagent/atom {:transaction       :send
                                       :first-tag         asset-snt
                                       :second-tag-prefix :t/from
                                       :second-tag        piggy-bank
                                       :third-tag-prefix  nil
                                       :third-tag         aretha-gosling
                                       :fourth-tag-prefix :t/via
                                       :fourth-tag        mainnet
                                       :fifth-tag         optimism
                                       :max-fees          "€55.57"
                                       :nonce             26
                                       :input-data        "Hello from Porto"})]
    (fn []
      [preview/preview-container
       {:state                     component-state
        :descriptor                descriptor
        :show-blur-background?     true
        :component-container-style {:padding-bottom 50}}
       [quo/transaction-summary
        (merge {:on-press #(js/alert "Dropdown pressed")}
               @component-state)]])))
