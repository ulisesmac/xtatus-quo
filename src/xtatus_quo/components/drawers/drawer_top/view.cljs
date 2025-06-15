(ns xtatus-quo.components.drawers.drawer-top.view
  (:require
   [quo.components.avatars.icon-avatar :as icon-avatar]
   [quo.components.avatars.user-avatar.view :as user-avatar]
   [quo.components.wallet.address-text.view :as address-text]
   [quo.context :as quo.context]
   [quo.foundations.colors :as colors]
   [react-native.core :as rn]
   [utils.i18n :as i18n]
   [xtatus-quo.components.avatars.account-avatar.view :as account-avatar]
   [xtatus-quo.components.buttons.button.view :as button]
   [xtatus-quo.components.drawers.drawer-top.style :as style]
   [xtatus-quo.components.icon :as icons]
   [xtatus-quo.components.markdown.text :as text]
   [xtatus-quo.components.tags.context-tag.view :as context-tag]))

;; TODO: improve the API of this component

(def ^:private left-image-supported-types #{:account :keypair :default-keypair})

(defn- left-image
  [{:keys [type customization-color account-avatar-emoji account-avatar-type icon-avatar
           profile-picture blur?]}]
  (case type
    :account         [account-avatar/view
                      {:customization-color customization-color
                       :size                32
                       :emoji               account-avatar-emoji
                       :type                (or account-avatar-type :default)}]
    :keypair         [icon-avatar/icon-avatar
                      {:icon    icon-avatar
                       :border? true
                       :blur?   blur?
                       :color   :neutral}]

    :default-keypair [user-avatar/user-avatar
                      {:size              :small
                       :status-indicator? false
                       :profile-picture   profile-picture}]
    nil))

(defn- keypair-subtitle
  [{:keys [theme blur? stored]}]
  [rn/view {:style style/row}
   [text/text
    {:size   :paragraph-2
     :weight :regular
     :style  (style/description theme blur?)}
    (case stored
      :on-device  (i18n/label :t/on-device)
      :on-keycard (i18n/label :t/on-keycard)
      :missing    (i18n/label :t/import-to-use-derived-accounts)
      (i18n/label :t/on-device))]
   (when (= stored :on-keycard)
     [icons/icon
      :i/keycard-card
      {:color           (colors/theme-colors colors/neutral-50 colors/neutral-40 theme)
       :size            16
       :container-style style/keycard-icon}])])

(defn- account-subtitle
  [{:keys [description]}]
  [address-text/view
   {:address description
    :format  :short}])

(defn- default-keypair-subtitle
  [{:keys [description theme blur?]}]
  [text/text
   {:accessibility-label :default-keypair-text
    :size                :paragraph-2
    :weight              :regular
    :style               (style/description theme blur?)}
   (str description " · " (i18n/label :t/on-device))])

(defn- context-tag-subtitle
  [{:keys [context-tag-type context icon community-logo community-name account-name emoji
           customization-color full-name profile-picture blur?]}]
  (let [tag-type (or context-tag-type :account)]
    [rn/view
     {:accessibility-label :context-tag-wrapper
      :style               {:flex-wrap  :wrap
                            :margin-top 4}}
     [context-tag/view
      {:type                tag-type
       :account-name        account-name
       :emoji               emoji
       :community-name      community-name
       :community-logo      community-logo
       :size                24
       :customization-color customization-color
       :profile-picture     profile-picture
       :full-name           full-name
       :context             context
       :icon                icon
       :blur?               blur?}]]))

(defn- description-subtitle
  [{:keys [theme blur? description]}]
  [text/text
   {:size   :paragraph-1
    :weight :regular
    :style  (style/description theme blur?)}
   description])

(defn- subtitle
  [{:keys [type theme blur? stored description community-name community-logo
           context-tag-type account-name emoji customization-color full-name profile-picture context
           icon]}]
  (cond
    (= :keypair type)
    [keypair-subtitle
     {:theme  theme
      :blur?  blur?
      :stored stored}]

    (= :account type)
    [account-subtitle
     {:theme       theme
      :blur?       blur?
      :description description}]

    (= :default-keypair type)
    [default-keypair-subtitle
     {:description description
      :theme       theme
      :blur?       blur?}]

    (= :context-tag type)
    [context-tag-subtitle
     {:context-tag-type    context-tag-type
      :community-logo      community-logo
      :community-name      community-name
      :account-name        account-name
      :emoji               emoji
      :customization-color customization-color
      :profile-picture     profile-picture
      :full-name           full-name
      :context             context
      :icon                icon
      :blur?               blur?}]

    (and (not= :label type) description)
    [description-subtitle
     {:theme       theme
      :blur?       blur?
      :description description}]))

(defn- right-icon
  [{:keys [theme type on-button-press on-button-long-press button-disabled? button-icon button-type]
    :or   {button-type :primary}}]
  (cond
    (= :info type)
    [icons/icon
     :i/info
     {:accessibility-label :info-icon
      :size                20
      :color               (colors/theme-colors colors/neutral-50
                                                colors/neutral-40
                                                theme)}]
    (and (= :context-tag type) button-icon)
    [button/button
     {:accessibility-label :button-icon
      :on-press            on-button-press
      :on-long-press       on-button-long-press
      :disabled?           button-disabled?
      :type                button-type
      :size                24
      :icon-only?          true}
     button-icon]))

(defn- left-title
  [{:keys [type label title title-icon theme blur?]}]
  (case type
    :label   [text/text
              {:weight :medium
               :size   :paragraph-2
               :style  (style/description theme blur?)}
              label]
    :address [address-text/view
              {:address       title
               :full-address? true
               :weight        :semi-bold
               :size          :heading-2}]
    [rn/view {:style style/title-container}
     [text/text
      {:size   :heading-2
       :weight :semi-bold
       :style  (when blur? {:color colors/white})}
      title]
     (when title-icon
       [icons/icon title-icon
        {:container-style style/title-icon
         :size            20
         :color           (if blur?
                            colors/white-opa-40
                            (colors/theme-colors colors/neutral-50 colors/neutral-40 theme))}])]))

(defn view
  [{:keys [title title-icon type description blur? community-name community-logo button-icon
           account-name emoji context-tag-type button-type container-style
           on-button-press on-button-long-press profile-picture stored label full-name
           button-disabled? account-avatar-emoji account-avatar-type customization-color icon-avatar
           context icon theme]}]
  (let [actual-theme (or theme (quo.context/use-theme))]
    [quo.context/provider {:theme actual-theme}
     [rn/view {:style [style/container container-style]}
      (when (left-image-supported-types type)
        [rn/view {:style style/left-container}
         [left-image
          {:type                 type
           :blur?                blur?
           :customization-color  customization-color
           :account-avatar-emoji account-avatar-emoji
           :account-avatar-type  account-avatar-type
           :icon-avatar          icon-avatar
           :profile-picture      profile-picture}]])
      [rn/view {:style style/body-container}
       [left-title
        {:type       type
         :label      label
         :title      title
         :title-icon title-icon
         :theme      actual-theme
         :blur?      blur?}]
       [subtitle
        {:type                type
         :theme               actual-theme
         :blur?               blur?
         :stored              stored
         :description         description
         :community-name      community-name
         :community-logo      community-logo
         :context-tag-type    context-tag-type
         :customization-color customization-color
         :account-name        account-name
         :emoji               emoji
         :full-name           full-name
         :profile-picture     profile-picture
         :context             context
         :icon                icon}]]
      [right-icon
       {:theme                actual-theme
        :type                 type
        :button-type          button-type
        :on-button-press      on-button-press
        :on-button-long-press on-button-long-press
        :button-disabled?     button-disabled?
        :button-icon          button-icon}]]]))
