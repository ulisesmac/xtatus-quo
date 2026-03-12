(ns xquo.components.list-items.result-list.view
  (:require [reagent-extended-compiler.utils.transforms :as rec.xf]
            [xquo.components.list-items.result-list.style :as style]
            [xquo.components.settings.item.style :as settings-item.style]
            [xquo.components.text.view :as text]
            [xquo.context :as context]
            [xquo.react-native-reanimated :as rnr]
            [xquo.react-native :as rn]))

(defn- title-view [{:keys [theme title]}]
  [text/text {:font            :font/medium-15
              :number-of-lines 1
              :style           {:color (style/title-color theme)}}
   title])

(defn result-list
  "Result list item component.

  API:
  - `props` map
    - `:title` top text label (default `\"Title\"`)
    - `:content` bottom slot rendered as-is
    - `:image-source` leading image source for `:rn/image`
    - `:background` one of `:none` or `:blur` (default `:none`)
    - `:active?` optional boolean (default `false`)
    - `:style` optional caller style (map/vector/js style)
    - `:on-press-in` optional callback `(fn [event] ...)`
    - `:on-press-out` optional callback `(fn [event] ...)`
    - Any additional keys are forwarded to `:rn/pressable`."
  [{:keys [active? background content image-source on-press-in on-press-out title]
    :or   {active?    false
           background :none
           title      "Title"}
    :as   props}]
  (let [{:keys [color theme]}   (context/use-theme-color)
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
                       (dissoc :active? :background :color :content :image-source :on-press-in :on-press-out :style :title)
                       (assoc :on-press-in  on-press-in!
                              :on-press-out on-press-out!
                              :style        (rec.xf/add-styles style/container-base (:style props))))
     [:animated/view {:pointer-events :none
                      :style          [style/overlay-base
                                       (style/container-color-style theme background)]}]
     [:animated/view {:pointer-events :none
                      :style          [style/overlay-base
                                       (style/active-color-style theme background color)
                                       (style/active-overlay-state-style active?)]}]
     (when pressed?
       [:animated/view {:pointer-events :none
                        :entering       (rnr/appear-in)
                        :exiting        (rnr/disappear-out)
                        :style          [style/overlay-base
                                         (style/pressed-color-style theme background color)]}])
     [:animated/view {:style [(if pressed?
                               settings-item.style/row-pressed-state-style
                               settings-item.style/row-default-state-style)
                              style/content-row]}
     (when image-source
       [:rn/view {:style style/image-slot}
        [:rn/image {:source image-source
                    :style  style/image}]])
     [:rn/view {:style style/content-column}
      [:rn/view {:style style/title}
       [title-view {:theme theme
                    :title title}]]
       [:rn/view {:style style/content}
        content]]]]))
