(ns xquo.components.list-items.simple-item.view
  (:require [react-native.core :as rn]
            [react-native.utils :as rn.utils]
            [xquo.components.icon.view :as icon]
            [xquo.components.list-items.simple-item.style :as style]
            [xquo.components.list.style :as list.style]
            [xquo.components.text.view :as text]
            [xquo.context :as context]))

(defn- leading-view [{:keys [background-color emoji icon]}]
  [:rn/view {:style [style/leading
                     (style/leading-background background-color)]}
   (if emoji
     [:rn/text {:style style/emoji}
      emoji]
     [icon/view (assoc icon
                  :size  20
                  :color (:color icon style/leading-icon-color))])])

(defn simple-item
  "Simple list item with optional circular leading content and a trailing slot.

  Pass either `:icon` or `:emoji`. Use `:leading-background-color` to override
  the content-based default with a resolved color value."
  [{:keys [emoji icon leading-background-color on-press-in on-press-out right title]
    :as   props}]
  (let [color                   (context/use-color)
        [pressed? set-pressed!] (rn/use-state false)
        on-press-in!            (rn/use-callback
                                 (fn [event]
                                   (set-pressed! true)
                                   (when on-press-in
                                     (on-press-in event)))
                                 [on-press-in])
        on-press-out!           (rn/use-callback
                                 (fn [event]
                                   (set-pressed! false)
                                   (when on-press-out
                                     (on-press-out event)))
                                 [on-press-out])]
    [:rn/pressable (-> props
                       (dissoc :emoji :icon :leading-background-color :on-press-in :on-press-out :right :style :title)
                       (assoc :on-press-in  on-press-in!
                              :on-press-out on-press-out!
                              :style        (rn.utils/add-styles
                                             style/container-base
                                             list.style/pressable-element-spacing
                                             (:style props))))
     [:animated/view {:pointer-events :none
                      :style          [list.style/overlay-base
                                       (list.style/pressed-color-style color)
                                       (list.style/pressed-color-state-style pressed?)]}]
     [:animated/view {:style [(if pressed?
                                list.style/row-pressed-state-style
                                list.style/row-default-state-style)
                              style/row]}
      (when (or (:name icon) emoji)
        [leading-view {:background-color (or leading-background-color
                                             (if emoji
                                               style/emoji-background-color
                                               style/icon-background-color))
                       :emoji            emoji
                       :icon             icon}])
      [:rn/view {:style style/title}
       [text/text {:font :font/medium-15}
        title]]
      (when right
        [:rn/view {:style style/right}
         right])]]))
