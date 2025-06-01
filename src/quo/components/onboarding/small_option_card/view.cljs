(ns quo.components.onboarding.small-option-card.view
  (:require
    [quo.components.buttons.button.view :as button]
    [quo.components.markdown.text :as text]
    [quo.components.onboarding.small-option-card.style :as style]
    [quo.foundations.colors :as colors]
    [react-native.core :as rn]
    [react-native.fast-image :as fast-image]))

(defn- icon-variant
  [{:keys [title subtitle image accessibility-label on-press]}]
  [rn/pressable
   {:accessibility-label accessibility-label
    :style               style/icon-variant
    :underlay-color      colors/white-opa-5
    :on-press            on-press}
   [rn/view {:style style/icon-variant-image-container}
    [rn/image
     {:accessibility-label :small-option-card-icon-image
      :style               style/icon-variant-image
      :resize-mode         :contain
      :source              image}]]
   [rn/view {:style style/text-container}
    [text/text
     {:style           style/icon-title
      :size            :paragraph-1
      :weight          :semi-bold
      :number-of-lines 1}
     title]
    [text/text
     {:style           style/subtitle
      :size            :paragraph-2
      :weight          :regular
      :number-of-lines 1}
     subtitle]]])

(defn- main-variant
  [{:keys [title subtitle button-label image max-height accessibility-label on-press button-props]}]
  [rn/view {:style style/main-variant}
   [rn/view {:style style/main-variant-text-container}
    [text/text
     {:style           style/main-title
      :size            :heading-2
      :weight          :semi-bold
      :number-of-lines 1}
     title]
    [text/text
     {:style           style/subtitle
      :size            :paragraph-2
      :weight          :regular
      :number-of-lines 1}
     subtitle]]
   [rn/image
    {:accessibility-label :small-option-card-main-image
     :style               (style/main-variant-image max-height)
     :resize-mode         :contain
     :source              image}]
   [button/button
    (merge
     {:on-press            on-press
      :accessibility-label accessibility-label
      :type                :grey
      :size                40
      :container-style     style/main-button
      :theme               :dark
      :background          :blur}
     button-props)
    button-label]])

(defn small-option-card
  [{:keys [variant title subtitle button-label image max-height on-press accessibility-label
           button-props container-style]
    :or   {variant :main accessibility-label :small-option-card}}]
  (let [main-variant?  (= variant :main)
        card-component (if main-variant? main-variant icon-variant)
        card-height    (cond
                         (not main-variant?) style/icon-variant-height
                         max-height          (min max-height style/main-variant-height)
                         :else               style/main-variant-height)]
    [rn/view {:style (merge (style/card card-height) container-style)}
     [card-component
      {:title               title
       :subtitle            subtitle
       :button-label        button-label
       :on-press            on-press
       :accessibility-label accessibility-label
       :image               image
       :max-height          max-height
       :button-props        button-props}]]))
