(ns xquo.components.list-items.simple-item.view
  (:require [react-native.core :as rn]
            [react-native.utils :as rn.utils]
            [xquo.components.icon.view :as icon]
            [xquo.components.list-items.simple-item.style :as style]
            [xquo.components.list.style :as list.style]
            [xquo.components.text.view :as text]
            [xquo.context :as context]))

(defn- leading-view [{:keys [background-color emoji icon]
                     {:keys [url source] image-style :style} :image}]
  [:rn/view {:style [style/leading
                     (style/leading-background background-color)]}
   (cond
     (or source url)
     [:rn/image {:source (or source {:uri url})
                  :resize-mode :contain
                  :style (rn.utils/add-styles style/leading-image image-style)}]

     emoji
     [:rn/text {:style style/emoji}
      emoji]

     :else
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

  Pass `:icon`, `:emoji`, or `:image {:url ...}` / `:image {:source ...}`.
  Images render at 20×20 inside the 32px circle; `:image :style` allows image styling.
  `:drag-handle` accepts an optional
  renderable node before the circle, separated by 12px. `:leading-gap` controls
  circle-to-text spacing (default 12). Use `:leading-background-color` to override
  the content-based default with a resolved color value. Items without
  `:on-press` are static and do not show press feedback. `:description` accepts
  text or a renderable node, `:title-props` are forwarded to the title text,
  and `:blur?` enables blur press feedback.
  `:variant :reorder` adds a drag handle, trailing chevron, themed neutral
  background, 16px radius, and an 8px leading gap. Explicit `:right nil` hides
  the default chevron. Drag gestures are caller-owned."
  [{:keys [blur? description drag-handle emoji icon image leading-background-color leading-gap on-press on-press-in on-press-out
           right title title-props variant]
    :as   props}]
  (let [{:keys [color theme]}   (context/use-theme-color)
        reorder?                (= variant :reorder)
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
                 (dissoc :blur? :description :drag-handle :emoji :icon :image :leading-background-color :leading-gap :on-press
                         :on-press-in :on-press-out :right :style :title :title-props :variant)
                 (assoc :style (rn.utils/add-styles
                                style/container-base
                                list.style/pressable-element-spacing
                                (when reorder? (style/reorder-container theme blur?))
                                (:style props))))
       pressable?
       (assoc :on-press     on-press
              :on-press-in  on-press-in!
              :on-press-out on-press-out!))
     [:animated/view {:pointer-events :none
                      :style          [list.style/overlay-base
                                       (list.style/pressed-color-style color blur?)
                                       (list.style/pressed-color-state-style
                                        (and pressable? pressed?))]}]
     [:animated/view {:style [(if (and pressable? pressed?)
                                list.style/row-pressed-state-style
                                list.style/row-default-state-style)
                              style/row]}
      (when (or drag-handle reorder?)
        [:rn/view {:style style/drag-handle}
         (or drag-handle
             [icon/view {:name :icon/menu :size 20 :color (style/muted-color theme)}])])
      [:rn/view {:style [style/body (style/leading-gap (or leading-gap (if reorder? 8 12)))]}
       (when (or (:name icon) emoji image)
         [leading-view {:background-color (or leading-background-color
                                              (if emoji
                                                style/emoji-background-color
                                                style/icon-background-color))
                        :emoji            emoji
                        :icon             icon
                        :image            image}])
       [:rn/view {:style style/content}
        [:rn/view {:style style/title}
         [text/text (assoc title-props :font (:font title-props :font/medium-15))
          title]]
        (when description
          [description-view {:description description}])]]
      (when (or right (and reorder? (not (contains? props :right))))
        [:rn/view {:style style/right}
         (or right
             [icon/view {:name :icon/chevron-right :size 20 :color (style/muted-color theme)}])])]]))
