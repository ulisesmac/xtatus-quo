(ns xquo.components.tags.context-tag.view
  (:require [reagent-extended-compiler.utils.transforms :as rec.xf]
            [xquo.components.icon.view :as icon]
            [xquo.components.tags.context-tag.style :as style]
            [xquo.components.text.view :as text]
            [xquo.context :as context]
            [xquo.foundations.colors :as colors]))

(def ^:private text-font
  {24 :font/medium-13
   32 :font/medium-15})

(def ^:private inline-icon-size
  {24 12
   32 20})

(def ^:private filled-icon-size
  {24 12
   32 20})

(def ^:private filled-icon-scale
  {24 1
   32 0.8})

(def ^:private chevron-icon-scale
  {24 0.8
   32 1})

(def ^:private multi-icon-size
  {24 16
   32 20})

(def ^:private multi-count-font
  {24 :font/medium-11
   32 :font/medium-13})

(defn- image-view [{:keys [blur? image-source selected? shape size type]}]
  (let [border-style (style/media-border size shape type blur? selected?)]
    [:rn/view {:style (style/media-frame size shape selected?)}
     [:rn/image {:style  (style/media-image size selected?)
                 :source image-source}]
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
  [icon/icon {:style (style/scaled-icon scale)
              :icon  icon
              :size  size
              :color color}])

(defn- multi-image-view [{:keys [image-source outlined? shape size slot-index]}]
  (let [border-style (style/multi-media-border size shape outlined?)]
    [:rn/view {:style (style/multi-item-slot size slot-index)}
     [:rn/view {:style (style/multi-media-wrapper size)}
      [:rn/view {:style (style/media-frame size shape false)}
       [:rn/image {:style  (style/media-image size false)
                   :source image-source}]]
      (when border-style
        [:rn/view {:style          border-style
                   :pointer-events :none}])]]))

(defn- multi-number-text [number number-position]
  (if (= number-position :start)
    (str number "+")
    (str "+" number)))

(defn- multi-number-view
  [{:keys [blur? dark-theme? number number-position shape size slot-index]}]
  [:rn/view {:style (style/multi-item-slot size slot-index)}
   [:rn/view {:style [(style/media-frame size shape false)
                      (style/multi-count-surface size shape dark-theme? blur?)]}
    [text/text {:style           (style/multi-content-style dark-theme? blur?)
                :font            (get multi-count-font size)
                :number-of-lines 1
                :ellipsize-mode  :clip}
     (multi-number-text number number-position)]]])

(defn- multi-leading-view
  [{:keys [blur? dark-theme? icon image-sources number number-position shape size]}]
  (let [content-style         (style/multi-content-style dark-theme? blur?)
        visible-image-sources (if number
                                (take 2 image-sources)
                                (take 3 image-sources))
        image-count           (count visible-image-sources)
        slot-count            (+ image-count (if number 1 0))
        outlined-slot-index   (when (= slot-count 3) 1)
        stack-items           (concat
                               (when (and number (= number-position :start))
                                 [{:kind :number}])
                               (map (fn [image-source]
                                      {:kind         :image
                                       :image-source image-source})
                                    visible-image-sources)
                               (when (and number (= number-position :end))
                                 [{:kind :number}]))]
    [:<>
     (when icon
       [icon-node {:color    (:color content-style)
                   :icon     icon
                   :scale    1
                   :size     (get multi-icon-size size)}])
     (into [:rn/view {:style style/multi-stack-row}]
           (map-indexed
            (fn [slot-index {:keys [image-source kind]}]
              (if (= kind :number)
                [multi-number-view {:blur?       blur?
                                    :dark-theme? dark-theme?
                                    :number      number
                                    :number-position number-position
                                    :shape       shape
                                    :size        size
                                    :slot-index  slot-index}]
                [multi-image-view {:image-source image-source
                                   :outlined?    (= slot-index outlined-slot-index)
                                   :shape        shape
                                   :size         size
                                   :slot-index   slot-index}])))
           stack-items)]))

(defn- leading-view
  [{:keys [blur? color dark-theme? emoji icon image-source image-sources number number-position
           selected? shape size type]}]
  (let [secondary-text-style (style/secondary-text-style dark-theme? blur?)]
    (cond
      (= type :multi)
      [multi-leading-view {:blur?         blur?
                           :dark-theme?   dark-theme?
                           :icon          icon
                           :image-sources image-sources
                           :number        number
                           :number-position number-position
                           :shape         shape
                           :size          size}]

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

      (and (= type :group) icon)
      [:rn/view {:style (style/filled-icon-surface size color)}
       [icon-node {:color    (if (= size 24)
                               (colors/get-color :color/white-70)
                               (colors/get-color :color/white-100))
                   :icon     icon
                   :scale    (get filled-icon-scale size)
                   :size     (get filled-icon-size size)}]]

      (and (= type :audio) icon)
      [:rn/view {:style (style/filled-icon-surface size color)}
       [icon-node {:color    (colors/get-color :color/white-100)
                   :icon     icon
                   :scale    (get filled-icon-scale size)
                   :size     (get filled-icon-size size)}]]

      (and (= type :icon) icon)
      [icon-node {:color    (:color secondary-text-style)
                  :icon     icon
                  :scale    1
                  :size     (get inline-icon-size size)}])))

(defn- label-view [{:keys [blur? dark-theme? label size suffix]}]
  (let [title-text-style     (style/title-text-style dark-theme?)
        secondary-text-style (style/secondary-text-style dark-theme? blur?)]
    [:rn/view {:style style/label-row}
     [text/text {:style           [style/label-primary-text
                                   title-text-style]
                 :font            (get text-font size)
                 :number-of-lines 1
                 :ellipsize-mode  :tail}
      label]
     (when suffix
       [icon/icon {:style (style/scaled-icon (get chevron-icon-scale size))
                   :icon  :icon/chevron-right
                   :size  20
                   :color (:color secondary-text-style)}])
     (when suffix
       [text/text {:style           title-text-style
                   :font            (get text-font size)
                   :number-of-lines 1
                   :ellipsize-mode  :tail}
        suffix])]))

(defn- context-tag-body
  [{:keys [blur? color dark-theme? emoji icon image-source image-sources number number-position
           root-props root-style selected-border-style selected? shape size suffix type]}
   label]
  [:rn/view (assoc root-props
                   :style root-style)
   (when selected-border-style
     [:rn/view {:style          selected-border-style
                :pointer-events :none}])
   [leading-view {:blur?           blur?
                  :color           color
                  :dark-theme?     dark-theme?
                  :emoji           emoji
                  :icon            icon
                  :image-source    image-source
                  :image-sources   image-sources
                  :number          number
                  :number-position number-position
                  :selected?       selected?
                  :shape           shape
                  :size            size
                  :type            type}]
   (when (and (not= type :multi) (some? label))
     [label-view {:blur?       blur?
                  :dark-theme? dark-theme?
                  :label       label
                  :size        size
                  :suffix      suffix}])])

(defn context-tag
  "Context tag component.

  API:
  - `props` map
    - `:type` one of `:default`, `:image`, `:group`, `:icon`, `:audio`, `:multi`
    - `:size` one of `24` or `32` (default `24`)
    - `:state` one of `:default` or `:selected` (default `:default`)
    - `:blur?` optional boolean that uses the blur background treatment (default `false`)
    - `:shape` one of `:circle` or `:squircle` for `:image` and `:multi`
      (default `:circle`)
    - `:image-source` image source for `:default` and `:image`
    - `:image-sources` vector of image sources for `:multi`
    - `:number` optional number rendered as `+number` for `:multi`;
      when present, the first two `:image-sources` are rendered with it
    - `:number-position` one of `:start` or `:end` for `:multi`
      (default `:end`)
    - `:emoji` optional emoji fallback for `:image` with `:shape :squircle`
    - `:icon` icon keyword for `:group`, `:icon`, `:audio`, and `:multi`
    - `:suffix` optional trailing text rendered after the label; when present,
      a chevron is inserted automatically between label and suffix
    - `:style` optional caller style (map/vector/js style)
    - Any additional keys are forwarded to the root `:rn/view`.
  - `label` optional child content rendered in the main text slot for non-`multi`
    variants."
  ([props]
   (context-tag props nil))
  ([{:keys [blur? emoji icon image-source image-sources number number-position shape size state
            style suffix type]
     :or   {blur? false
            number-position :end
            shape :circle
            size  24
            state :default
            type  :default}
     :as   props}
    label]
   (let [{:keys [color dark-theme?]} (context/use-theme-color)
         root-props                  (dissoc props :blur? :emoji :icon :image-source :image-sources
                                            :number :number-position :shape :size
                                            :state :style :suffix :type)]
     [context-tag-body {:blur?                 blur?
                        :color                 color
                        :dark-theme?           dark-theme?
                        :emoji                 emoji
                        :icon                  icon
                        :image-source          image-source
                        :image-sources         image-sources
                        :number                number
                        :number-position       number-position
                        :root-props            root-props
                        :root-style            (rec.xf/add-styles
                                                style/root-base
                                                (style/container size type shape dark-theme? blur? icon)
                                                style)
                        :selected-border-style (when (= state :selected)
                                                 (style/selected-border size type shape color))
                        :selected?             (= state :selected)
                        :shape                 shape
                        :size                  size
                        :suffix                suffix
                        :type                  type}
      label])))
