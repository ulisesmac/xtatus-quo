(ns xquo.profile.link-card.view
  (:require [react-native.react.core :as react]
            [react-native.utils :as rn.utils]
            [xquo.components.button.style :as button-style]
            [xquo.components.icon.view :as icon]
            [xquo.profile.link-card.style :as style]
            [xquo.components.text.view :as text]
            [xquo.context :as context]
            [xquo.foundations.colors :as colors]))

(defn- background [{:keys [color]}]
  [:rn/view {:pointer-events :none :style style/backdrop}
   [:svg/svg {:width                "100%"
              :height               "100%"
              :view-box             "0 0 160 88"
              :preserve-aspect-ratio :none}
    [:svg/defs
     [:svg/radial-gradient {:id                 "link-card-fill"
                           :cx                 0
                           :cy                 0
                           :r                  1
                           :gradient-units     :userSpaceOnUse
                           :gradient-transform "matrix(-136.79 65.5417 -119.167 -214.242 136.79 22.4583)"}
      [:svg/stop {:stop-color (colors/get-color color) :stop-opacity 0.06}]
      [:svg/stop {:offset 1 :stop-color (colors/get-color :color/white) :stop-opacity 0.02}]]]
    [:svg/rect {:width 160 :height 88 :fill "url(#link-card-fill)"}]]])

(defn- content-view [{:keys [content theme]}]
  (case (:type content)
    :icon
    (let [{:keys [size color] :or {size 20 color (colors/themed theme :color/neutral-50 :color/neutral-40)}} (:icon content)]
      [icon/view (assoc (:icon content) :size size :color color)])

    :image
    (let [{:keys [source url resize-mode] :or {resize-mode :contain} :as props} (:image content)]
      [:rn/image (-> props
                     (dissoc :url)
                     (assoc :source      (or source {:uri url})
                            :resize-mode resize-mode
                            :style       (rn.utils/add-styles style/image (:style props))))])

    nil))

(defn link-card [{:keys [title description content color disabled? on-press on-long-press on-press-in on-press-out]
                 :as   props}]
  (let [{:keys [theme] accent :color} (context/use-theme-color)
        [pressed? set-pressed!]      (react/use-state false)
        pressable?                  (or on-press on-long-press)
        pressed?                    (and pressed? pressable? (not disabled?))
        component                   (if pressable? :animated/pressable :rn/view)
        press-style                 (if pressed?
                                      button-style/pressable-pressed-state-style
                                      button-style/pressable-default-state-style)
        press-in!                   (react/use-callback (fn [event]
                                                         (set-pressed! true)
                                                         (when on-press-in (on-press-in event)))
                                                       [on-press-in])
        press-out!                  (react/use-callback (fn [event]
                                                         (set-pressed! false)
                                                         (when on-press-out (on-press-out event)))
                                                       [on-press-out])]
    [component (cond-> props
                 :always (dissoc :title :description :content :color :disabled? :on-press-in :on-press-out)
                 :always (assoc :style (rn.utils/add-styles style/container
                                                          (style/disabled disabled?)
                                                          (when pressable? press-style)
                                                          (:style props)))
                 pressable? (assoc :disabled     disabled?
                                   :on-press-in  press-in!
                                   :on-press-out press-out!))
     [background {:color (or color accent)}]
     [:animated/view {:pointer-events :none :style (style/border theme pressed?)}]
     [:rn/view {:style style/top-row}
      [content-view {:content content :theme theme}]]
     [:rn/view
      [text/text {:font            :font/semibold-15
                  :number-of-lines 1
                  :ellipsize-mode  :tail
                  :style           (style/text theme nil)}
       title]
      [text/text {:font            :font/regular-13
                  :number-of-lines 1
                  :ellipsize-mode  :tail
                  :style           (style/text theme true)}
       description]]]))
