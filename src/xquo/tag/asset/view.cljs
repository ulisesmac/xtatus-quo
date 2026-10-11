(ns xquo.tag.asset.view
  (:require [applied-science.js-interop :as j]
            [react-native.react.core :as react]
            [react-native.utils :as rn.utils]
            [xquo.components.button.style :as button-style]
            [xquo.components.icon.view :as icon]
            [xquo.components.text.view :as text]
            [xquo.context :as context]
            [xquo.foundations.colors :as colors]
            [xquo.tag.asset.style :as style]))

(defn- background [{:keys [variant size option width fill success dark-theme? blur?]}]
  (let [path          (style/shape-path variant size option width)
        badge-offset  (style/badge-offset size)
        badge-y       (+ (:top badge-offset) 10)
        cutout-radius (if (= variant :token) 7.75 8)]
    [:svg/svg {:width          (+ width 2)
               :height         (+ size 2)
               :view-box       (str "-1 -1 " (+ width 2) " " (+ size 2))
               :style          style/background
               :pointer-events :none}
     (when option
       [:svg/defs
        [:svg/mask {:id         "asset-fill-cutout"
                    :x          -1
                    :y          -1
                    :width      (+ width 2)
                    :height     (+ size 2)
                    :mask-units :userSpaceOnUse}
         [:svg/rect {:x -1 :y -1 :width (+ width 2) :height (+ size 2) :fill :white}]
         [:svg/circle {:cx (- width (if (= size 24) 2 4)) :cy badge-y :r cutout-radius :fill :black}]]
        [:svg/mask {:id         "asset-outline-cutout"
                    :x          -1
                    :y          -1
                    :width      (+ width 2)
                    :height     (+ size 2)
                    :mask-units :userSpaceOnUse}
         [:svg/rect {:x -1 :y -1 :width (+ width 2) :height (+ size 2) :fill :white}]
         [:svg/circle {:cx (- width (if (= size 24) 2 4)) :cy badge-y :r 8 :fill :black}]]])
     (when (and (= variant :collectible) dark-theme? (not blur?) option)
       [:svg/rect {:width width :height size :rx (if (= size 24) 8 16) :fill fill}])
     (when (= option :hold)
       [:svg/path {:d path :fill :none :stroke success :stroke-width 2 :mask "url(#asset-outline-cutout)"}])
     [:svg/path {:d path :fill fill :mask (when option "url(#asset-fill-cutout)")}]]))

(defn asset-tag [{:keys [variant size option image label blur? on-press on-press-in on-press-out on-layout]
                  :or   {variant :token size 24}
                  :as   props}]
  (let [dark-theme?             (:dark-theme? (context/use-theme-color))
        fill                    (style/background-color dark-theme? blur?)
        success                 (colors/get-color :color/success (if dark-theme? 60 50))
        white                   (colors/get-color :color/white-100)
        [width set-width!]      (react/use-state 0)
        [pressed? set-pressed!] (react/use-state false)
        on-layout!              (react/use-callback (fn [event]
                                                      (set-width! (j/get-in event [:nativeEvent :layout :width]))
                                                      (when on-layout
                                                        (on-layout event)))
                                                    [on-layout])
        on-press-in!            (react/use-callback (fn [event]
                                                      (set-pressed! true)
                                                      (when on-press-in
                                                        (on-press-in event)))
                                                    [on-press-in])
        on-press-out!           (react/use-callback (fn [event]
                                                      (set-pressed! false)
                                                      (when on-press-out
                                                        (on-press-out event)))
                                                    [on-press-out])]
    [(if on-press :animated/pressable :rn/view)
     (cond-> (-> props
                 (dissoc :variant :size :option :image :label :blur? :style :on-press-in :on-press-out)
                 (assoc :on-layout on-layout!
                        :style     (rn.utils/add-styles style/container
                                                        (style/source-opacity variant size dark-theme? blur?)
                                                        (when on-press
                                                          (if pressed?
                                                            button-style/pressable-pressed-state-style
                                                            button-style/pressable-default-state-style))
                                                        (:style props))))
       on-press (assoc :on-press-in  on-press-in!
                       :on-press-out on-press-out!))
     (when (pos? width)
       [background {:variant     variant
                    :size        size
                    :option      option
                    :width       width
                    :fill        fill
                    :success     success
                    :dark-theme? dark-theme?
                    :blur?       blur?}])
     [:rn/view {:style [style/content (style/content-style size)]}
      [:rn/image {:source      (or (:source image) {:uri (:url image)})
                  :resize-mode :cover
                  :style       (rn.utils/add-styles (style/image variant size) (:style image))}]
      [text/text {:font            (get-in style/label-font [variant size])
                  :number-of-lines 1
                  :style           (style/title variant size)}
       label]]
     (when option
       [:rn/view {:style [style/badge (style/badge-offset size)] :pointer-events :none}
        (case option
          :hold    [icon/view {:name :icon/check-circle :size 20 :color #js [success white]}]
          :add     [icon/view {:name :icon/add-circle :size 20 :color #js [(colors/get-color :color/neutral-50) white]}]
          :loading [icon/view {:name :icon/loading-circle :size 20}])])]))
