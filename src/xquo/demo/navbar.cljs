(ns xquo.demo.navbar
  (:require [react-native.react.core :as react]
            [react-native.react-navigation.native :as navigation]
            [react-native.utils :refer [style]]
            [xquo.context :as context]
            [xquo.core :as xquo]
            [xquo.demo.style :as demo-style]
            [xquo.foundations.colors :as colors]))

(defn- container [top]
  (style {:position    :absolute
          :top         0
          :left        0
          :right       0
          :padding-top top}))

(defn- backdrop [theme]
  (style {:background-color (colors/themed theme :color/white-70 :color/neutral-95-70)}))

(defn view [{:keys [title top on-theme-change]}]
  (let [{:keys [theme dark-theme?]} (context/use-theme-color)
        {:keys [go-back]}           (navigation/use-navigation)
        next-theme                  (if dark-theme? :theme/light :theme/dark)
        switch-theme!               (react/use-callback #(on-theme-change next-theme) [on-theme-change next-theme])]
    [:rn/view {:pointer-events :box-none :style (container top)}
     [:effect/view {:pointer-events :none
                    :effect         :blur
                    :blur-amount    40
                    :theme          theme
                    :style          [demo-style/background (backdrop theme)]}]
     [xquo/navigation.page-nav {:blur?  true
                                :left   {:icon {:name :icon/arrow-left} :on-press go-back}
                                :right  [{:icon     {:name (if dark-theme? :icon/light :icon/dark)}
                                          :on-press switch-theme!}]
                                :center {:title title :text-align :center}}]]))
