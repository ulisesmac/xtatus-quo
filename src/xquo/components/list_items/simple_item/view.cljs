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

(defn- description-view [{:keys [description]}]
  (if (string? description)
    [text/text {:font :font/regular-13}
     description]
    description))

(defn simple-item
  "Simple list item with optional circular leading content and a trailing slot.

  Pass either `:icon` or `:emoji`. Use `:leading-background-color` to override
  the content-based default with a resolved color value. Items without
  `:on-press` are static and do not show press feedback. `:description` accepts
  text or a renderable node, and `:title-props` are forwarded to the title text."
  [{:keys [description emoji icon leading-background-color on-press on-press-in on-press-out
           right title title-props]
    :as   props}]
  (let [color                   (context/use-color)
        pressable?              on-press
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
    [(if pressable? :rn/pressable :rn/view)
     (cond-> (-> props
                 (dissoc :description :emoji :icon :leading-background-color :on-press :on-press-in
                         :on-press-out :right :style :title :title-props)
                 (assoc :style (rn.utils/add-styles
                                style/container-base
                                list.style/pressable-element-spacing
                                (:style props))))
       pressable?
       (assoc :on-press     on-press
              :on-press-in  on-press-in!
              :on-press-out on-press-out!))
     [:animated/view {:pointer-events :none
                      :style          [list.style/overlay-base
                                       (list.style/pressed-color-style color)
                                       (list.style/pressed-color-state-style
                                        (and pressable? pressed?))]}]
     [:animated/view {:style [(if (and pressable? pressed?)
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
      [:rn/view {:style style/content}
       [:rn/view {:style style/title}
        [text/text (assoc title-props :font (:font title-props :font/medium-15))
         title]]
       (when description
         [description-view {:description description}])]
      (when right
        [:rn/view {:style style/right}
         right])]]))
