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

(defn- image-view [{:keys [blur? image-source shape size type]}]
  [:rn/image {:source image-source
              :style  (style/media-image size type shape blur?)}])

(defn- squircle-image-view [{:keys [blur? color emoji image-source size]}]
  (if image-source
    [image-view {:blur?        blur?
                 :image-source image-source
                 :shape        :squircle
                 :size         size
                 :type         :image}]
    [:rn/view {:style (style/squircle-surface size color)}
     (when emoji
       [:rn/text {:style (style/emoji-style size)}
        emoji])]))

(defn- icon-node [{:keys [color icon scale size]}]
  [icon/icon {:icon  icon
              :size  size
              :color color
              :style (style/scaled-icon scale)}])

(defn- leading-view
  [{:keys [blur? color icon image-source emoji shape size type dark-theme?]}]
  (cond
    (and (= type :image) (= shape :squircle))
    [squircle-image-view {:blur?        blur?
                          :color        color
                          :emoji        emoji
                          :image-source image-source
                          :size         size}]

    (and (#{:default :image} type) image-source)
    [image-view {:blur?        blur?
                 :image-source image-source
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
    [icon-node {:color    (style/secondary-color dark-theme? blur?)
                :icon     icon
                :scale    1
                :size     (get inline-icon-size size)}]))

(defn- label-view [{:keys [blur? dark-theme? label size suffix]}]
  [:rn/view {:style style/label-row}
   [text/text {:font            (get text-font size)
               :number-of-lines 1
               :style           {:color (style/title-color dark-theme?)}}
    label]
   (when suffix
     [icon/icon {:icon  :icon/chevron-right
                 :size  20
                 :color (style/secondary-color dark-theme? blur?)
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
    - `:type` one of `:default`, `:image`, `:group`, `:icon`, `:audio`
    - `:size` one of `24` or `32` (default `24`)
    - `:state` one of `:default` or `:selected` (default `:default`)
    - `:blur?` optional boolean that uses the blur background treatment (default `false`)
    - `:shape` one of `:circle` or `:squircle` for `:image` (default `:circle`)
    - `:image-source` image source for `:default` and `:image`
    - `:emoji` optional emoji fallback for `:image` with `:shape :squircle`
    - `:icon` icon keyword for `:group`, `:icon`, and `:audio`
    - `:suffix` optional trailing text rendered after the label; when present,
      a chevron is inserted automatically between label and suffix
    - `:style` optional caller style (map/vector/js style)
    - Any additional keys are forwarded to the root `:rn/view`.
  - `label` child content rendered in the main text slot."
  [{:keys [blur? emoji icon image-source shape size state style suffix type]
    :or   {blur?      false
           shape      :circle
           size       24
           state      :default
           type       :default}
    :as   props}
   label]
  (let [{:keys [color dark-theme?]} (context/use-theme-color)]
    [:rn/view (-> props
                  (dissoc :blur? :emoji :icon :image-source :shape :size :state :style
                          :suffix :type)
                  (assoc :style (rec.xf/add-styles
                                 style/root-base
                                 (style/container size type shape dark-theme? blur? state color)
                                 style)))
     [leading-view {:blur?        blur?
                    :color        color
                    :dark-theme?  dark-theme?
                    :emoji        emoji
                    :icon         icon
                    :image-source image-source
                    :shape        shape
                    :size         size
                    :type         type}]
     [label-view {:blur?       blur?
                  :dark-theme? dark-theme?
                  :label       label
                  :size        size
                  :suffix      suffix}]]))
