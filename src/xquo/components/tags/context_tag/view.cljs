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

(defn- image-view [{:keys [background image-source size type]}]
  (when image-source
    [:rn/image {:source image-source
                :style  (style/media-image size type background)}]))

(defn- account-view [{:keys [color emoji image-source size]}]
  (if image-source
    [image-view {:background   :solid
                 :image-source image-source
                 :size         size
                 :type         :account}]
    [:rn/view {:style (style/account-surface size color)}
     (when emoji
       [:rn/text {:style (style/emoji-style size)}
        emoji])]))

(defn- icon-node [{:keys [color icon scale size]}]
  (when icon
    [icon/icon {:icon  icon
                :size  size
                :color color
                :style (style/scaled-icon scale)}]))

(defn- leading-view
  [{:keys [background color icon image-source emoji size type dark-theme?]}]
  (cond
    (#{:default :image} type)
    [image-view {:background   background
                 :image-source image-source
                 :size         size
                 :type         type}]

    (= type :account)
    [account-view {:color               color
                   :emoji               emoji
                   :image-source        image-source
                   :size                size}]

    (= type :group)
    [:rn/view {:style (style/filled-icon-surface size color)}
     [icon-node {:color    (if (= size 24)
                             (colors/get-color :color/white-70)
                             (colors/get-color :color/white-100))
                 :icon     icon
                 :scale    (get filled-icon-scale size)
                 :size     (get filled-icon-size size)}]]

    (= type :audio)
    [:rn/view {:style (style/filled-icon-surface size color)}
     [icon-node {:color    (colors/get-color :color/white-100)
                 :icon     icon
                 :scale    (get filled-icon-scale size)
                 :size     (get filled-icon-size size)}]]

    (= type :icon)
    [icon-node {:color    (style/secondary-color dark-theme? background)
                :icon     icon
                :scale    1
                :size     (get inline-icon-size size)}]

    :else
    nil))

(defn- label-view
  [{:keys [background dark-theme? label size suffix]}]
  [:rn/view {:style style/label-row}
   [text/text {:font            (get text-font size)
               :number-of-lines 1
               :style           {:color (style/title-color dark-theme?)}}
    label]
   (when suffix
     [icon/icon {:icon  :icon/chevron-right
                 :size  20
                 :color (style/secondary-color dark-theme? background)
                 :style (style/scaled-icon (get chevron-icon-scale size))}])
   (when suffix
     [text/text {:font            (get text-font size)
                 :number-of-lines 1
                 :style           {:color (style/title-color dark-theme?)}}
      suffix])])

(defn context-tag
  "Context tag component.

  API:
  - `props` map
    - `:type` one of `:default`, `:image`, `:account`, `:group`, `:icon`, `:audio`
    - `:size` one of `24` or `32` (default `24`)
    - `:state` one of `:default` or `:selected` (default `:default`)
    - `:background` one of `:solid` or `:blur` (default `:solid`)
    - `:label` main text label
    - `:image-source` image source for `:default`, `:image`, and optional `:account`
    - `:emoji` optional emoji fallback for `:account`
    - `:color` color family keyword for `:account`, `:group`, and `:audio`
      (defaults depend on type)
    - `:icon` icon keyword for `:group`, `:icon`, and `:audio`
    - `:suffix` optional trailing text rendered after the label; when present,
      a chevron is inserted automatically between label and suffix
    - `:style` optional caller style (map/vector/js style)
    - Any additional keys are forwarded to the root `:rn/view`."
  [{:keys [background color icon image-source emoji label size state style suffix type]
    :or   {background :solid
           size       24
           state      :default
           type       :default}
    :as   props}]
  (let [{:keys [dark-theme?]} (context/use-theme-color)
        color                 (or color
                                  (case type
                                    :account :color/sky
                                    :group   :color/purple
                                    :audio   :color/blue
                                    :color/blue))]
    [:rn/view (-> props
                  (dissoc :background :color :emoji :icon :image-source :label
                          :size :state :style :suffix :type)
                  (assoc :accessibility-label :context-tag
                         :style               (rec.xf/add-styles
                                                style/root-base
                                                (style/container size type dark-theme? background state)
                                                style)))
     [leading-view {:background          background
                    :color               color
                    :dark-theme?         dark-theme?
                    :emoji               emoji
                    :icon                icon
                    :image-source        image-source
                    :size                size
                    :type                type}]
     [label-view {:background     background
                  :dark-theme?    dark-theme?
                  :label          label
                  :size           size
                  :suffix         suffix}]]))
