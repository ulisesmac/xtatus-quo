(ns xtatus-quo.components.drawers.bottom-actions.view
  (:require
    [xtatus-quo.components.buttons.button.view :as button]
    [quo.components.drawers.bottom-actions.style :as style]
    [xtatus-quo.components.icon :as icon]
    [xtatus-quo.components.markdown.text :as text]
    [quo.components.tags.context-tag.schema :as context-tag.schema]
    [xtatus-quo.components.tags.context-tag.view :as context-tag]
    [quo.context :as quo.context]
    [quo.foundations.colors :as colors]
    [react-native.core :as rn]
    [utils.i18n :as i18n]))

(def ?schema
  [:=>
   [:catn
    [:props
     [:map {:closed true}
      [:actions [:maybe [:enum :one-action :two-actions :two-vertical-actions]]]
      [:description {:optional true} [:maybe [:enum :top :bottom :top-error]]]
      [:description-text {:optional true} [:maybe [:or :string :schema.common/hiccup]]]
      [:description-top-text {:optional true} [:maybe [:or :string :schema.common/hiccup]]]
      [:error-message {:optional true} [:maybe :string]]
      [:role {:optional true} [:maybe [:enum :admin :member :token-master :owner]]]
      [:context-tag-props {:optional true} [:maybe context-tag.schema/?schema]]
      [:button-one-label {:optional true} [:maybe :string]]
      [:button-two-label {:optional true} [:maybe :string]]
      [:button-one-props {:optional true} [:maybe :map]]
      [:button-two-props {:optional true} [:maybe :map]]
      [:scroll? {:optional true} [:maybe :boolean]]
      [:blur? {:optional true} [:maybe :boolean]]
      [:container-style {:optional true} [:maybe :map]]
      [:buttons-container-style {:optional true} [:maybe :map]]
      [:buttons-style {:optional true} [:maybe :map]]]]]
   :any])

(def ^:private role-icon
  {:admin        :i/gavel
   :member       :i/members
   :token-master :i/token-master
   :owner        :i/crown})

(defn- view
  [{:keys [actions description description-text description-top-text error-message role button-one-label
           button-two-label blur? button-one-props button-two-props scroll? container-style
           buttons-container-style buttons-style context-tag-props]}]
  (let [theme (quo.context/use-theme)]
    [rn/view {:style (merge (style/container scroll? blur? theme) container-style)}
     (when (= description :top-error)
       [rn/view {:style style/error-message}
        [icon/icon
         :i/alert
         {:color (colors/theme-colors colors/danger-50 colors/danger-60 theme)
          :size  16}]
        [text/text
         {:size  :paragraph-2
          :style {:color (colors/theme-colors colors/danger-50 colors/danger-60 theme)}}
         error-message]])

     (when (= description :top)
       (if (vector? description-top-text)
         description-top-text
         (when (or role context-tag-props)
           [rn/view {:style style/description-top}
            [text/text
             {:size  :paragraph-2
              :style (style/description-top-text scroll? blur? theme)}
             (or description-top-text (i18n/label :t/eligible-to-join-as))]
            [context-tag/view
             (if role
               {:type    :icon
                :size    24
                :icon    (role role-icon)
                :blur?   blur?
                :context (i18n/label (keyword "t" role))}
               context-tag-props)]])))

     [rn/view {:style (style/buttons-container actions buttons-container-style)}
      (when (or (= actions :two-actions)
                (= actions :two-vertical-actions))
        [button/button
         (merge
          {:size                40
           :container-style     (merge style/button-container buttons-style)
           :background          (when (or blur? scroll?) :blur)
           :theme               theme
           :accessibility-label :button-two}
          button-two-props)
         button-two-label])
      [button/button
       (merge
        {:size                40
         :container-style     (merge style/button-container buttons-style)
         :background          (when (or blur? scroll?) :blur)
         :theme               theme
         :accessibility-label :button-one}
        button-one-props)
       button-one-label]]
     (when (= description :bottom)
       (if (string? description-text)
         [text/text
          {:size  :paragraph-2
           :style (style/description-bottom scroll? blur? theme)} description-text]
         description-text))]))
