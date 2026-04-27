(ns xquo.components.list-items.result-list.view
  (:require [reagent-extended-compiler.utils.transforms :as rec.xf]
            [xquo.components.icon.view :as icon]
            [xquo.components.list-items.result-list.style :as style]
            [xquo.components.settings.item.style :as settings-item.style]
            [xquo.components.text.view :as text]
            [xquo.context :as context]
            [xquo.react-native-reanimated :as rnr]
            [xquo.react-native :as rn]))

(defn- title-view [{:keys [theme title]}]
  [text/text {:font            :font/medium-15
              :number-of-lines 1
              :style           (style/title-style theme)}
   title])

(defn result-list
  "Result list item component.

  API:
  - `props` map
    - `:title` top text label (default `\"Title\"`)
    - `:content` bottom slot rendered as-is
    - `:color` optional color keyword used for active and pressed overlays.
      Defaults to the current `xquo` context color
    - `:image-source` leading image source for `:rn/image`
    - `:icon` leading `xquo/icon` props map when `:image-source` is not used
    - `:image-background` optional renderable node shown behind the image when
      `:image-source` or `:icon` is present
    - `:image-style` optional caller style for the rendered image or icon
    - `:image-tint` optional color keyword passed to `colors/get-color` and
      applied as the image tint color or icon color
    - `:right` optional trailing slot rendered as-is
    - `:background` one of `:none` or `:blur` (default `:none`)
    - `:active?` optional boolean (default `false`)
    - `:disabled?` optional boolean that disables interaction and dims the item
    - `:unpressable?` optional boolean that renders a static row without any
      `on-press*` handling
    - `:style` optional caller style (map/vector/js style)
    - `:on-press-in` optional callback `(fn [event] ...)`
    - `:on-press-out` optional callback `(fn [event] ...)`
    - Any additional keys are forwarded to the root container."
  [{result-color     :color
    :keys            [active? background content disabled? icon image-background image-source image-style
                      image-tint on-press-in on-press-out right title unpressable?]
    :or   {background :none
           title      "Title"}
    :as   props}]
  (let [{context-color :color
         theme         :theme} (context/use-theme-color)

        overlay-color          (or result-color context-color)
        _ (def --oc overlay-color)
        [pressed? set-pressed!] (rn/use-state false)
        on-press-in!            (rn/use-callback (fn [event]
                                                   (set-pressed! true)
                                                   (when on-press-in
                                                     (on-press-in event)))
                                                 [on-press-in])
        on-press-out!           (rn/use-callback (fn [event]
                                                   (set-pressed! false)
                                                   (when on-press-out
                                                     (on-press-out event)))
                                                 [on-press-out])
        pressed-now?            (and pressed? (not disabled?) (not unpressable?))
        root-props              (cond-> props
                                  :always
                                  (dissoc :active? :background :color :content :disabled?
                                          :icon :image-background :image-source :image-style
                                          :image-tint :on-press-in :on-press-out :right
                                          :style :title :unpressable?)
                                  :always
                                  (assoc :style (rec.xf/add-styles
                                                 style/container-base
                                                 (when disabled? style/disabled-state)
                                                 (:style props)))
                                  (not unpressable?)
                                  (assoc :disabled     disabled?
                                         :on-press-in  (when-not disabled? on-press-in!)
                                         :on-press-out (when-not disabled? on-press-out!)))]
    [(if unpressable? :rn/view :rn/pressable) root-props
     [:animated/view {:pointer-events :none
                      :style          [style/overlay-base
                                       (style/container-color-style theme background)]}]
     [:animated/view {:pointer-events :none
                      :style          [style/overlay-base
                                       (style/active-color-style theme background overlay-color)
                                       (style/active-overlay-state-style active?)]}]
     (when pressed-now?
       [:animated/view {:pointer-events :none
                        :entering       (rnr/appear-in)
                        :exiting        (rnr/disappear-out)
                        :style          [style/overlay-base
                                         (style/pressed-color-style theme background overlay-color)]}])
     [:animated/view {:style [(if pressed-now?
                               settings-item.style/row-pressed-state-style
                               settings-item.style/row-default-state-style)
                              style/content-row]}
     (when (or image-source (:name icon))
       [:rn/view {:style style/image-slot}
        (when image-background
          [:rn/view {:style style/image-background-slot}
           image-background])
        (if image-source
          [:rn/image {:style  (rec.xf/add-styles style/image
                                                 (style/image-tint-style image-tint)
                                                 image-style)
                      :source image-source}]
          [icon/view (merge {:style (rec.xf/add-styles style/image image-style)
                             :size  20
                             :color image-tint}
                            icon)])])
     [:rn/view {:style style/content-column}
      [:rn/view {:style style/title}
       [title-view {:theme theme
                    :title title}]]
      [:rn/view {:style style/content}
       content]]
     (when right
       [:rn/view {:style style/right-slot}
        right])]]))
