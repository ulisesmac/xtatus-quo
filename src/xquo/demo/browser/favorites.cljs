(ns xquo.demo.browser.favorites
  (:require [react-native.react.core :as react]
            [xquo.browser.favorites.view :as favorites]
            [xquo.demo.assets :as assets]
            [xquo.demo.controls :as controls]
            [xquo.demo.screen :as screen]))

(defn- press! [] (js/alert "Pressed"))

(def ^:private items
  [{:key :aave :title "Aave" :image {:source assets/illustration} :on-press press!}
   {:key :coingecko :title "CoinGecko" :image {:source assets/illustration} :on-press press!}
   {:key :rarible :title "Rarible" :image {:source assets/illustration} :on-press press!}
   {:key :zapper :title "Zapper" :image {:source assets/illustration} :on-press press!}
   {:key :zerion :title "Zerion" :image {:source assets/illustration} :on-press press!}])

(defn screen [_]
  (let [[item-count set-count!] (react/use-state 5)]
    [screen/screen {:title   "Browser favorites"
                    :preview [favorites/favorites {:items (take item-count items)}]}
     [controls/choice {:label     ":items count"
                       :value     item-count
                       :on-change set-count!
                       :options   [{:value 1 :label "1"}
                                   {:value 3 :label "3"}
                                   {:value 5 :label "5"}]}]]))
