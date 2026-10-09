(ns xquo.demo.screen
  (:require [react-native.core :as rn]
            [react-native.react.core :as react]
            [react-native.react-navigation.native :as navigation]
            [react-native.safe-area-context.core :as safe-area]
            [react-native.utils :refer [asset-require]]
            [xquo.context :as context]
            [xquo.core :as xquo]
            [xquo.demo.style :as style]))

(def ^:private mountains (asset-require "xquo-demo/mountains.png"))

(defn screen [{:keys [title preview shell? blur? photo?]} & children]
  (let [top                         (safe-area/use-top)
        bottom                      (safe-area/use-bottom)
        device-theme                (rn/use-color-scheme)
        [selected-theme set-theme!] (react/use-state nil)
        theme                       (or selected-theme device-theme)
        next-theme                  (if (= theme :theme/dark) :theme/light :theme/dark)
        preview-theme               (if shell? :theme/dark theme)
        switch-theme!               (react/use-callback #(set-theme! next-theme) [next-theme])
        {:keys [go-back]}           (navigation/use-navigation)]
    [context/provider {:theme theme}
     [:rn/keyboard-avoiding-view {:behavior (when rn/ios? :padding)
                                  :style    (style/root theme top bottom)}
      [xquo/page-nav {:background :none
                      :left       {:icon {:name :icon/arrow-left} :on-press go-back}
                      :right      [{:icon     {:name (if (= next-theme :theme/light) :icon/light :icon/dark)}
                                    :on-press switch-theme!}]
                      :center     {:type :title :title title :text-align :center}}]
      (if preview
        [:rn/view {:style style/body}
         [context/provider {:theme preview-theme}
          [:rn/view {:style style/preview}
           (if (or shell? blur? photo?)
             [:rn/view {:style style/shell}
              [:rn/image {:source mountains :resize-mode :cover :style style/background}]
              [(if photo? :rn/view :effect/view)
               (if photo?
                 {:style style/body}
                 {:effect      :blur
                  :blur-amount 40
                  :theme       preview-theme
                  :style       (style/blur-overlay preview-theme)})
               [:rn/scroll-view {:style                        style/body
                                 :content-container-style      style/preview-content
                                 :keyboard-should-persist-taps :handled}
                preview]]]
             preview)]]
         (into [:rn/scroll-view {:style                        (style/panel theme)
                                 :content-container-style      style/content
                                 :keyboard-should-persist-taps :handled}]
               children)]
        (into [:rn/scroll-view {:style style/body :content-container-style style/content}]
              children))
      (when preview
        [:rn/view {:pointer-events :none
                   :style          (style/bottom-safe-area theme bottom)}])]]))
