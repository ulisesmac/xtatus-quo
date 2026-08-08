(ns xquo.components.tags.context-tag.view
  (:require [react-native.utils :as rn.utils]
            [xquo.components.button.style :as button.style]
            [xquo.components.icon.view :as icon]
            [xquo.components.tags.context-tag.style :as style]
            [xquo.components.text.view :as text]
            [xquo.context :as context]
            [xquo.foundations.colors :as colors]
            [react-native.core :as rn]))

(def ^:private text-font
  {24 :font/medium-13
   32 :font/medium-15})

(def ^:private inline-icon-size
  {24 12
   32 20})

(def ^:private prefix-icon-size
  {24 16
   32 20})

(def ^:private filled-icon-size
  {24 12
   32 20})

(def ^:private filled-icon-scale
  {24 1
   32 0.8})

(def ^:private chevron-icon-size
  {24 16
   32 20})

(def ^:private multi-icon-size
  {24 16
   32 20})

(def ^:private multi-count-font
  {24 :font/medium-11
   32 :font/medium-13})

(defn- image-view [{:keys [blur? image-source selected? shape size type]}]
  (let [border-style (style/media-border size shape type blur? selected?)]
    [:rn/view {:style (style/media-frame size shape selected?)}
     [:rn/image {:style       (style/media-image size selected?)
                 :resize-mode :contain
                 :source      image-source}]
     (when border-style
       [:rn/view {:style          border-style
                  :pointer-events :none}])]))

(defn- squircle-image-view [{:keys [blur? color emoji image-source selected? size]}]
  (if image-source
    [image-view {:blur?        blur?
                 :image-source image-source
                 :selected?    selected?
                 :shape        :squircle
                 :size         size
                 :type         :image}]
    [:rn/view {:style (style/squircle-surface size color)}
     (when emoji
       [:rn/text {:style (style/emoji-style size)}
        emoji])]))

(defn- icon-node [{:keys [color icon scale size]}]
  [icon/view (merge {:style (style/scaled-icon scale)
                     :size  size
                     :color color}
                    icon)])

(defn- multi-image-view
  [{:keys [blur? border dark-theme? image-source shape size slot-index]}]
  [:rn/view {:style (style/multi-item-slot size slot-index)}
   [:rn/view {:style (style/multi-stack-item-surface size shape border dark-theme? blur?)}
    [:rn/view {:style (style/media-frame size shape false)}
     [:rn/image {:style       (style/media-image size false)
                 :resize-mode :contain
                 :source      image-source}]]]])

(defn- multi-icon-view
  [{:keys [blur? border dark-theme? shape size slot-index]
    {background-color :background-color :as icon} :icon}]
  [:rn/view {:style (style/multi-item-slot size slot-index)}
   [:rn/view {:style (style/multi-stack-item-surface size shape border dark-theme? blur?)}
    [:rn/view {:style [(style/media-frame size shape false)
                       (style/multi-icon-surface background-color)]}
     [icon-node {:color (if (= size 24)
                          (colors/get-color :color/white-70)
                          (colors/get-color :color/white-100))
                 :icon  (dissoc icon :background-color)
                 :scale 1
                 :size  (get filled-icon-size size)}]]]])

(defn- multi-number-text [number]
  (str "+" number))


(defn- multi-number-view
  [{:keys [blur? border dark-theme? number shape size slot-index]}]
  [:rn/view {:style (style/multi-item-slot size slot-index)}
   [:rn/view {:style (style/multi-stack-item-surface size shape border dark-theme? blur?)}
    [:rn/view {:style [(style/media-frame size shape false)
                       (style/multi-count-surface size shape dark-theme? blur?)]}
     [text/text {:style           (style/multi-content-style dark-theme? blur?)
                 :font            (get multi-count-font size)
                 :number-of-lines 1
                 :ellipsize-mode  :clip}
      (multi-number-text number)]]]])

(defn- multi-stack-items [icons image-sources number number-position]
  (into []
        (concat
         (when (and (some? number) (= number-position :start))
           [{:kind :number}])
         (map (fn [image-source]
                {:kind         :image
                 :image-source image-source})
              image-sources)
         (map (fn [icon]
                {:kind :icon
                 :icon icon})
              icons)
         (when (and (some? number) (= number-position :end))
           [{:kind :number}]))))

(defn- multi-leading-view
  [{:keys [blur? border dark-theme? icon icons image-sources number number-position shape size]}]
  (let [content-style (style/multi-content-style dark-theme? blur?)
        stack-items   (multi-stack-items icons image-sources number number-position)]
    [:<>
     (when (:name icon)
       [icon-node {:color (:color content-style)
                   :icon  icon
                   :scale 1
                   :size  (get multi-icon-size size)}])
     (into [:rn/view {:style style/multi-stack-row}]
           (map-indexed (fn [slot-index {:keys [icon image-source kind]}]
                          (case kind
                            :number
                            ^{:key (str "number-" slot-index)}
                            [multi-number-view {:blur?           blur?
                                                :border          border
                                                :dark-theme?     dark-theme?
                                                :number          number
                                                :number-position number-position
                                                :shape           shape
                                                :size            size
                                                :slot-index      slot-index}]

                            :image
                            ^{:key (str "image-" slot-index)}
                            [multi-image-view {:blur?        blur?
                                               :border       border
                                               :dark-theme?  dark-theme?
                                               :image-source image-source
                                               :shape        shape
                                               :size         size
                                               :slot-index   slot-index}]

                            :icon
                            ^{:key (str "icon-" slot-index)}
                            [multi-icon-view {:blur?       blur?
                                              :border      border
                                              :dark-theme? dark-theme?
                                              :icon        icon
                                              :shape       shape
                                              :size        size
                                              :slot-index  slot-index}]

                            nil))
           stack-items))]))

(defn- inline-icon-view [{:keys [blur? dark-theme? icon size]}]
  (let [secondary-text-style (style/secondary-text-style dark-theme? blur?)]
    [icon-node {:color (:color secondary-text-style)
                :icon  icon
                :scale 1
                :size  size}]))

(defn- leading-view
  [{:keys [blur? border color dark-theme? emoji icon icons image-source image-sources number number-position
           selected? shape size type]}]
  (cond
    (= type :multi)
    [multi-leading-view {:blur?           blur?
                         :border          border
                         :dark-theme?     dark-theme?
                         :icon            icon
                         :icons           icons
                         :image-sources   image-sources
                         :number          number
                         :number-position number-position
                         :shape           shape
                         :size            size}]

    (and (= type :image) (= shape :squircle))
    [squircle-image-view {:blur?        blur?
                          :color        color
                          :emoji        emoji
                          :image-source image-source
                          :selected?    selected?
                          :size         size}]

    (and (or (= type :default) (= type :image)) image-source)
    [image-view {:blur?        blur?
                 :image-source image-source
                 :selected?    selected?
                 :shape        shape
                 :size         size
                 :type         type}]

    (and (= type :group) (:name icon))
    [:rn/view {:style (style/filled-icon-surface size color)}
     [icon-node {:color (if (= size 24)
                          (colors/get-color :color/white-70)
                          (colors/get-color :color/white-100))
                 :icon  icon
                 :scale (get filled-icon-scale size)
                 :size  (get filled-icon-size size)}]]

    (and (= type :audio) (:name icon))
    [:rn/view {:style (style/filled-icon-surface size color)}
     [icon-node {:color (colors/get-color :color/white-100)
                 :icon  icon
                 :scale (get filled-icon-scale size)
                 :size  (get filled-icon-size size)}]]

    (and (= type :icon) (:name icon))
    [inline-icon-view {:blur?       blur?
                       :dark-theme? dark-theme?
                       :icon        icon
                       :size        (get inline-icon-size size)}]))

(defn- prefix-view [{:keys [blur? dark-theme? prefix size]}]
  (let [prefix-text-style (style/prefix-text-style dark-theme? blur?)]
    [:rn/view {:style style/prefix-slot}
     (if (string? prefix)
       [text/text {:style           [style/prefix-text prefix-text-style]
                   :font            (get text-font size)}
        prefix]
       prefix)]))

(defn- label-view [{:keys [blur? chevron-icon chevron-style dark-theme? label size suffix]}]
  (let [title-text-style     (style/title-text-style dark-theme?)
        secondary-text-style (style/secondary-text-style dark-theme? blur?)]
    [:rn/view {:style style/label-row}
     (if (string? label)
       [text/text {:style           [style/label-primary-text
                                     title-text-style]
                   :font            (get text-font size)
                   :number-of-lines 1
                   :ellipsize-mode  :tail}
        label]
       label)
     (when suffix
       [:rn/view {:style style/suffix-chevron-slot}
        [icon/view {:style chevron-style
                    :name  chevron-icon
                    :size  (get chevron-icon-size size)
                    :color (:color secondary-text-style)}]])
     (when suffix
       [:rn/view {:style style/suffix-slot}
        (if (string? suffix)
          [text/text {:style           title-text-style
                      :font            (get text-font size)
                      :number-of-lines 1
                      :ellipsize-mode  :tail}
           suffix]
          suffix)])]))

(defn- context-tag-body
  [{:keys [blur? border chevron-icon chevron-style color dark-theme? emoji icon icons image-source image-sources number
           number-position on-press-in! on-press-out! prefix prefix-divider? prefix-icon pressed? pressable? root-props
           root-style selected-border-style selected? shape size suffix type]}
   label]
  (let [prefix-icon?      (:name prefix-icon)
        prefix-separator? (and prefix-divider? (or prefix prefix-icon?))
        root-component    (if pressable? :rn/pressable :rn/view)
        root-props        (cond-> (assoc root-props :style root-style)
                            pressable?
                            (assoc :on-press-in  on-press-in!
                                   :on-press-out on-press-out!))]
    [(if pressable? :animated/view :rn/view)
     (when pressable?
       {:style (if pressed?
                 button.style/pressable-pressed-state-style
                 button.style/pressable-default-state-style)})
     [root-component root-props
      (when selected-border-style
        [:rn/view {:style          selected-border-style
                   :pointer-events :none}])
      (when prefix-icon?
        [inline-icon-view {:blur?       blur?
                           :dark-theme? dark-theme?
                           :icon        prefix-icon
                           :size        (get prefix-icon-size size)}])
      (when prefix
        [prefix-view {:blur?       blur?
                      :dark-theme? dark-theme?
                      :prefix      prefix
                      :size        size}])
      (when prefix-separator?
        [:rn/view {:style (style/prefix-separator dark-theme? size)}])
      [leading-view {:blur?           blur?
                     :border          border
                     :color           color
                     :dark-theme?     dark-theme?
                     :emoji           emoji
                     :icon            icon
                     :icons           icons
                     :image-source    image-source
                     :image-sources   image-sources
                     :number          number
                     :number-position number-position
                     :selected?       selected?
                     :shape           shape
                     :size            size
                     :type            type}]
      (when (some? label)
        [label-view {:blur?         blur?
                     :chevron-icon  chevron-icon
                     :chevron-style chevron-style
                     :dark-theme?   dark-theme?
                     :label         label
                     :size          size
                     :suffix        suffix}])]]))

(defn context-tag
  "Context tag component.

  API:
  - `props` map
    - `:color` optional color keyword or string; when omitted, uses the
                current `xquo` context color
    - `:type` one of `:default`, `:image`, `:group`, `:icon`, `:audio`, `:multi`
    - `:size` one of `24` or `32` (default `24`)
    - `:state` one of `:default` or `:selected` (default `:default`)
    - `:border` optional `:outline`
    - `:blur?` optional boolean that uses the blur background treatment (default `false`)
    - `:shape` one of `:circle` or `:squircle` for `:icon`, `:image`, and `:multi`
               (default `:circle`)
    - `:image-source` image source for `:default` and `:image`
    - `:image-sources` vector of 1 to 3 image sources for `:multi`
    - `:icons` vector of icon props maps for `:multi`; each map also accepts `:background-color`.
               Icon sizes default to `12` in a 24px tag and `20` in a 32px tag, and can be overridden.
    - `:number` optional number rendered as `+number` for `:multi`;
                when present, `:number-position` places the count slot at the start or end
    - `:number-position` one of `:start` or `:end` for the `:multi` count slot (default `:end`)
    - `:emoji` optional emoji fallback for `:image` with `:shape :squircle`
    - `:icon` icon props map for `:group`, `:icon`, `:audio`, and `:multi`
    - `:suffix` optional trailing string or renderable node; when a string is
                provided it is rendered with the built-in text styling, and
                when present a chevron is inserted automatically between label
                and suffix
    - `:prefix` optional leading string or renderable node before the leading media and label;
                strings use the secondary text styling
    - `:prefix-divider?` optional boolean for showing the divider after `:prefix` or
                         `:prefix-icon` (default `true`)
    - `:prefix-icon` optional icon props map rendered before `:prefix` or leading media using the
                     same inline icon style as `:type :icon`
    - `:embedded?` optional boolean for tags rendered inside another context tag.
                   Embedded tags keep their content styling but remove their own
                   background and right padding.
    - `:chevron-icon` optional icon used between label and suffix
                      (default `:icon/chevron-right`)
    - `:chevron-style` optional style passed to the chevron icon
    - `:style` optional caller style (map/vector/js style)
    - Any additional keys are forwarded to the root `:rn/view`.
  - `label` optional child content; strings use the built-in text styling and
    non-strings are rendered directly."
  ([props]
   (context-tag props nil))
  ([{:keys [blur? border chevron-icon chevron-style color embedded? emoji icon icons image-source image-sources number
            number-position on-press on-press-in on-press-out prefix prefix-divider? prefix-icon shape size state style
            suffix type]
     :or   {blur?               false
            chevron-icon        :icon/chevron-right
            number-position     :end
            prefix-divider?     true
            size                24
            state               :default
            type                :default}
     :as   props}
    label]
   (let [{context-color :color
         :keys         [dark-theme? theme]} (context/use-theme-color)
         resolved-color      (or color context-color)
         pressable?          on-press
         [pressed?
          set-pressed!]      (rn/use-state false)
         on-press-in!        (rn/use-callback
                              (fn [event]
                                (set-pressed! true)
                                (when on-press-in
                                  (on-press-in event)))
                              [on-press-in])
         on-press-out!       (rn/use-callback
                              (fn [event]
                                (set-pressed! false)
                                (when on-press-out
                                  (on-press-out event)))
                              [on-press-out])
         shape               (or shape
                                 (when (and emoji (= type :image))
                                   :squircle)
                                 :circle)
         root-props          (cond-> (dissoc props :blur? :border :embedded? :emoji :icon :icons
                                             :image-source :image-sources
                                             :chevron-icon :chevron-style :color
                                             :number :number-position :on-press-in :on-press-out :shape
                                             :prefix :prefix-divider? :prefix-icon :size :state :style :suffix :type)
                               (not pressable?)
                               (dissoc :on-press))]
     [context-tag-body {:blur?                 blur?
                        :border                border
                        :color                 resolved-color
                        :dark-theme?           dark-theme?
                        :emoji                 emoji
                        :icon                  icon
                        :icons                 icons
                        :image-source          image-source
                        :image-sources         image-sources
                        :number                number
                        :number-position       number-position
                        :on-press-in!          on-press-in!
                        :on-press-out!         on-press-out!
                        :pressed?              pressed?
                        :pressable?            pressable?
                        :root-props            root-props
                        :root-style            (rn.utils/add-styles
                                                style/root-base
                                                (style/container size type shape border dark-theme? blur? embedded?
                                                                 (:name icon) label prefix (:name prefix-icon) suffix)
                                                (when (and pressable? (not= border :outline))
                                                  (button.style/pressable-type-style theme :grey nil resolved-color false pressed?))
                                                (when (= border :outline)
                                                  (style/outline-border size type shape theme resolved-color
                                                                        blur? (= state :selected) pressed?))
                                                style)
                        :selected-border-style (when (and (= state :selected)
                                                         (not= border :outline))
                                                 (style/selected-border size type shape resolved-color))
                        :selected?             (= state :selected)
                        :shape                 shape
                        :size                  size
                        :suffix                suffix
                        :prefix                prefix
                        :prefix-divider?       prefix-divider?
                        :prefix-icon           prefix-icon
                        :chevron-icon          chevron-icon
                        :chevron-style         chevron-style
                        :type                  type}
      label])))
