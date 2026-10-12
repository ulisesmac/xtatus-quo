(ns xquo.url-preview.view
  (:require [applied-science.js-interop :as j]
            [react-native.react.core :as react]
            [react-native.utils :as rn.utils]
            [xquo.components.button.style :as button-style]
            [xquo.components.favicon.view :as favicon]
            [xquo.components.icon.view :as icon]
            [xquo.components.text.view :as text]
            [xquo.context :as context]
            [xquo.foundations.colors :as colors]
            [xquo.url-preview.style :as style]))

(defn- remove-button [{:keys [theme on-remove]}]
  (let [[pressed? set-pressed!] (react/use-state false)
        press-in!               (react/use-callback #(set-pressed! true) [])
        press-out!              (react/use-callback #(set-pressed! false) [])]
    [:animated/pressable {:on-press     on-remove
                          :on-press-in  press-in!
                          :on-press-out press-out!
                          :hit-slop     6
                          :style        [style/clear
                                         (if pressed?
                                           button-style/pressable-pressed-state-style
                                           button-style/pressable-default-state-style)]}
     [icon/view {:name  :icon/cross-circle
                 :size  20
                 :color [(colors/themed theme :color/neutral-50 :color/neutral-60)
                         (colors/get-color :color/white-100)]}]]))

(defn- loading-view [{:keys [theme]}]
  (let [[width set-width!] (react/use-state 0)
        measure!           (react/use-callback #(set-width! (j/get-in % [:nativeEvent :layout :width])) [])]
    [:rn/view {:pointer-events :none :on-layout measure! :style style/border}
     (when (pos? width)
       [:svg/svg {:width width :height 56}
        [:svg/rect {:x                0.5
                    :y                0.5
                    :width            (- width 1)
                    :height           55
                    :rx               11.5
                    :fill             :none
                    :stroke           (style/border-color theme)
                    :stroke-width     1
                    :stroke-dasharray "3 4"}]])]))

(defn url-preview [{:keys [title url favicon image channel loading? loading-label on-remove]
                    :or   {loading-label "Generating preview"}
                    :as   props}]
  (let [{:keys [theme dark-theme?]} (context/use-theme-color)]
    [:rn/view (-> props
                  (dissoc :title :url :favicon :image :channel :loading? :loading-label :on-remove)
                  (assoc :style (rn.utils/add-styles (style/container theme loading? dark-theme?) (:style props))))
     (if loading?
       [:<>
        [loading-view {:theme theme}]
        [icon/view {:name :icon/loading :size 12 :color (colors/themed theme :color/neutral-50 :color/neutral-40)}]
        [text/text {:font :font/medium-13 :style [style/text (style/secondary theme)]} loading-label]]
       [:<>
        (when favicon
          [favicon/favicon (assoc favicon :style (rn.utils/add-styles style/favicon (:style favicon)))])
        [:rn/view {:style style/content}
         (if channel
           [:rn/view {:style style/channel}
            [text/text {:font :font/semibold-13 :number-of-lines 1 :style [style/text style/channel-text]}
             (:community channel)]
            [icon/view {:name  :icon/chevron-right
                        :size  16
                        :color (colors/themed theme :color/neutral-50 :color/neutral-40)}]
            [text/text {:font :font/semibold-13 :number-of-lines 1 :style [style/text style/channel-text]}
             (:name channel)]]
           [text/text {:font :font/semibold-13 :number-of-lines 1 :style style/text} title])
         [text/text {:font :font/medium-13 :number-of-lines 1 :style [style/text (style/secondary theme)]} url]]
        (when image
          [:rn/image (-> image
                         (dissoc :url)
                         (assoc :source      (or (:source image) {:uri (:url image)})
                                :resize-mode (:resize-mode image :cover)
                                :style       (rn.utils/add-styles style/image (:style image))))])
        (when on-remove
          [remove-button {:theme theme :on-remove on-remove}])])]))
