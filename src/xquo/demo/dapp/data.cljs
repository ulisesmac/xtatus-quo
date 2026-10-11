(ns xquo.demo.dapp.data
  (:require [xquo.demo.assets :as assets]
            [xquo.foundations.colors :as colors]))

(defn press! [] (js/alert "Pressed"))

(def account-avatar
  {:shape   :rounded-square
   :variant :solid
   :content {:type :emoji-image :image {:source assets/illustration}}})

(def contract-avatar
  {:color :color/neutral :content {:type :icon :icon {:name :icon/file}}})

(def headers
  {:dapp           {:title            "Aave"
                    :description      "aave.com"
                    :avatar           {:content {:type :image :image {:source assets/illustration}}}
                    :description-icon {:name  :icon/check-verified
                                       :color [(colors/get-color :color/success-60) (colors/get-color :color/white-100)]}}
   :smart-contract {:title            "0x826...5f2"
                    :avatar           contract-avatar
                    :description-icon {:name  :icon/check-verified
                                       :color [(colors/get-color :color/success-60) (colors/get-color :color/white-100)]}}
   :account        {:title "Trip to Vegas" :description "eth:0x21a...49e" :avatar account-avatar}})

(def accounts
  [{:variant     :account
    :avatar      account-avatar
    :title       "Trip to Vegas"
    :description "0x21a...49e"
    :on-press    press!
    :action      {:on-press press!}}
   {:variant     :account
    :avatar      account-avatar
    :title       "My savings"
    :description "0xJa6...hj7"
    :on-press    press!
    :action      {:on-press press!}}
   {:variant     :account
    :avatar      account-avatar
    :title       "Coin vault"
    :description "0xFeB...afF"
    :on-press    press!
    :action      {:on-press press!}}
   {:variant     :account
    :avatar      account-avatar
    :title       "Crypto fortress"
    :description "0x4C1...c5D"
    :on-press    press!
    :action      {:on-press press!}}
   {:variant     :account
    :avatar      account-avatar
    :title       "Block treasure"
    :description "0x67B...e3E"
    :on-press    press!
    :action      {:on-press press!}}])

(def dapps
  [{:variant     :dapp
    :title       "Coingecko"
    :description "coingecko.com"
    :avatar      {:content {:type :image :image {:source assets/illustration}}}
    :on-press    press!
    :action      {:on-press press!}}
   {:variant     :dapp
    :title       "1inch"
    :description "1inch.io"
    :avatar      {:content {:type :image :image {:source assets/illustration}}}
    :on-press    press!
    :action      {:on-press press!}}
   {:variant     :dapp
    :title       "Aave"
    :description "aave.com"
    :avatar      {:content {:type :image :image {:source assets/illustration}}}
    :on-press    press!
    :action      {:on-press press!}}
   {:variant     :dapp
    :title       "Uniswap"
    :description "uniswap.org"
    :avatar      {:content {:type :image :image {:source assets/illustration}}}
    :on-press    press!
    :action      {:on-press press!}}
   {:variant     :dapp
    :title       "Zapper"
    :description "zapper.xyz"
    :avatar      {:content {:type :image :image {:source assets/illustration}}}
    :on-press    press!
    :action      {:on-press press!}}])

(def approvals
  [{:variant          :approval
    :title            "USDT"
    :description      "Unlimited"
    :avatar           {:content {:type :image :image {:source assets/illustration}} :badge {:image {:source assets/illustration}}}
    :description-icon {:name :icon/alert-outline}
    :on-press         press!
    :action           {:on-press press!}}
   {:variant     :approval
    :title       "DAI"
    :description "500 DAI"
    :avatar      {:content {:type :image :image {:source assets/illustration}} :badge {:image {:source assets/illustration}}}
    :on-press    press!
    :action      {:on-press press!}}
   {:variant     :approval
    :title       "ETH"
    :description "800 ETH"
    :avatar      {:content {:type :image :image {:source assets/illustration}} :badge {:image {:source assets/illustration}}}
    :on-press    press!
    :action      {:on-press press!}}])

(def list-cards
  {:account        (first accounts)
   :dapp           (first dapps)
   :smart-contract {:variant  :smart-contract
                    :title    "0x826...5f2"
                    :avatar   contract-avatar
                    :on-press press!
                    :action   {:on-press press!}}
   :approval       (first approvals)})
