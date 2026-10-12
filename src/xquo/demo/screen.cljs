(ns xquo.demo.screen
  (:require [react-native.core :as rn]
            [react-native.react.core :as react]
            [react-native.safe-area-context.core :as safe-area]
            [react-native.utils :refer [asset-require style]]
            [xquo.context :as context]
            [xquo.demo.navbar :as navbar]
            [xquo.demo.style :as styles]))

(def ^:private mountains (asset-require "xquo-demo/mountains.png"))

(defn screen [{:keys [title preview preview-theme shell? blur? photo?]} & children]
  (let [top                         (safe-area/use-top)
        bottom                      (safe-area/use-bottom)
        {:keys [height]}            (rn/use-window-dimensions)
        device-theme                (rn/use-color-scheme)
        [selected-theme set-theme!] (react/use-state nil)
        {:keys [theme]}             (context/use-theme-color (or selected-theme device-theme))
        preview-theme               (or preview-theme (if shell? :theme/dark theme))
        preview-height              (/ (- height top bottom 56) 2)
        preview-background          (if photo? :rn/view :effect/view)]
    [context/provider {:theme theme}
     [:rn/keyboard-avoiding-view {:behavior (when rn/ios? :padding)
                                  :style    (styles/root theme 0 bottom)}
      (if preview
        [:rn/scroll-view {:style                             styles/body
                          :content-container-style           (styles/scroll-content top)
                          :keyboard-should-persist-taps      :handled
                          :content-inset-adjustment-behavior :never}
         [context/provider {:theme preview-theme}
          [:rn/view {:style (styles/preview preview-height)}
           (if (or shell? blur? photo?)
             [:rn/view {:style styles/shell}
              [:rn/image {:source mountains :resize-mode :cover :style styles/background}]
              [preview-background (if photo?
                                    {:style styles/body}
                                    {:effect      :blur
                                     :blur-amount 40
                                     :theme       preview-theme
                                     :style       (styles/blur-overlay preview-theme)})
               [:rn/scroll-view {:style                        styles/body
                                 :content-container-style      styles/preview-content
                                 :keyboard-should-persist-taps :handled
                                 :nested-scroll-enabled        true}
                preview]]]
             preview)]]
         (into [:rn/view {:style [(styles/panel theme)
                                  styles/content
                                  (style {:min-height preview-height})]}]
               children)]
        (into [:rn/scroll-view {:style                             styles/body
                                :content-container-style           [styles/content (style {:padding-top (+ top 56 20)})]
                                :keyboard-should-persist-taps      :handled
                                :content-inset-adjustment-behavior :never}]
              children))
      [:rn/view {:pointer-events :none :style (styles/bottom-safe-area theme bottom)}]
      [navbar/view {:title title :top top :on-theme-change set-theme!}]]]))
