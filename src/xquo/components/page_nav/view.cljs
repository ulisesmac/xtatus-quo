(ns xquo.components.page-nav.view
  (:require [react-native.utils :as rn.utils]
            [xquo.components.button.view :as button]
            [xquo.components.icon.view :as icon]
            [xquo.components.page-nav.style :as style]
            [xquo.components.text.view :as text]
            [xquo.context :as context]))

(defn- icon-button [{:keys [on-press theme background icon]}]
  (when (:name icon)
    [button/button (cond-> {:type       (style/action-button-type theme background)
                            :size       32
                            :background background
                            :glass?     true
                            :icon       (merge {:color (style/icon-color theme background)}
                                               icon)}
                     on-press (assoc :on-press on-press))]))

;; TODO: Replace this placeholder with the real avatar component.
(defn- account-switcher-placeholder [{:keys [on-press]}]
  [:rn/pressable (cond-> {:style style/placeholder-account-switcher}
                   on-press (assoc :on-press on-press))])

;; TODO: Replace this placeholder with the real channel avatar/emoji component.
(defn- channel-avatar-placeholder []
  [:rn/view {:style style/placeholder-channel-avatar}])

(defn- max-right-actions [center]
  (case (:type center)
    :dropdown               1
    :title-description      2
    :title-icon-description 2
    :wallet-networks        2
    :title                  (if (= (:text-align center) :center) 1 3)
    3))

(defn- account-switcher-supported? [center]
  (case (:type center)
    (:no-title :title :token :wallet-networks) true
    false))

(defn- visible-right-actions [center right]
  (->> (or right [])
       (remove (fn [{:keys [type]}]
                 (and (= type :account-switcher)
                      (not (account-switcher-supported? center)))))
       (take (max-right-actions center))))

(defn- centered-layout? [center]
  (case (:type center)
    :title           (= (:text-align center) :center)
    :dropdown        true
    :wallet-networks true
    :custom          true
    false))

(defn- right-content [{:keys [right center centered-content? theme background]}]
  (let [actions   (visible-right-actions center right)
        balanced? (and centered-content? (<= (count actions) 1))]
    [:rn/view {:style (if balanced?
                        style/right-slot-balanced
                        style/right-slot-unbalanced)}
     [:rn/view {:style style/right-actions-row}
      (map-indexed (fn [index action]
                     ^{:key (str "right-action-" index)}
                     [:rn/view {:style (when (pos? index) style/action-gap)}
                      (if (= (:type action) :account-switcher)
                        [account-switcher-placeholder {:on-press (:on-press action)}]
                        [icon-button {:icon       (:icon action)
                                      :on-press   (:on-press action)
                                      :theme      theme
                                      :background background}])])
                   actions)]]))

(defn- media-image [source image-style]
  (when source
    [:rn/image {:source source
                :style  image-style}]))

;; TODO: Replace this placeholder with the final center three-dots component.
(defn- center-dots-placeholder []
  [:rn/view {:style style/placeholder-dots}])

(defn- title-center [{:keys [title theme background]}]
  [text/text {:font            :font/medium-15
              :style           {:color (style/title-color theme background)}
              :number-of-lines 1}
   title])

(defn- dropdown-center [{:keys [dropdown-on-press dropdown-text theme background]}]
  [:rn/pressable {:on-press dropdown-on-press
                  :style    [style/dropdown-trigger
                             (style/dropdown-surface-style theme background)]}
   [text/text {:font            :font/medium-15
               :style           {:color (style/title-color theme background)}
               :number-of-lines 1}
    dropdown-text]
   [:rn/view {:style style/dropdown-chevron}
    [icon/view {:name  :icon/dropdown
                :size  20
                :color :no-color}]]])

(defn- token-center [{:keys [token-logo token-name theme background]}]
  [:rn/view {:style style/title-row}
   [media-image token-logo style/token-logo]
   [text/text {:font            :font/medium-15
               :style           {:color (style/title-color theme background)}
               :number-of-lines 1}
    token-name]])

(defn- channel-center [{:keys [channel-name icon theme background]}]
  [:rn/view {:style style/title-row}
   [:rn/view {:style style/placeholder-avatar-gap}
    [channel-avatar-placeholder]]
   [text/text {:font            :font/medium-15
               :style           {:color (style/title-color theme background)}
               :number-of-lines 1}
    channel-name]
   (when (:name icon)
     [:rn/view {:style {:margin-left 6}}
      [icon/view (merge {:size  12
                         :color (style/icon-color theme background)}
                        icon)]])])

(defn- title-description-center [{:keys [title description picture theme background]}]
  [:rn/view {:style style/title-row}
   [media-image picture style/leading-media]
   [:rn/view {:style style/title-description-column}
    [text/text {:font            :font/medium-15
                :style           {:color (style/title-color theme background)}
                :number-of-lines 1}
     title]
    [text/text {:font            :font/regular-11
                :style           {:color (style/description-color theme background)}
                :number-of-lines 1}
     description]]])

(defn- title-icon-description-center
  [{:keys [title description picture icon theme background]}]
  [:rn/view {:style style/title-row}
   [media-image picture style/leading-media]
   [:rn/view {:style style/title-description-column}
    [:rn/view {:style style/title-row}
     [text/text {:font            :font/semibold-15
                 :style           {:color (style/title-color theme background)}
                 :number-of-lines 1}
      title]
     (when (:name icon)
       [:rn/view {:style style/dropdown-chevron}
        [icon/view (merge {:size  12
                           :color (style/icon-color theme background)}
                          icon)]])]
    [text/text {:font            :font/medium-13
                :style           {:color (style/description-color theme background)}
                :number-of-lines 1}
     description]]])

(defn- wallet-network-logo [source index]
  ^{:key (str "wallet-network-" index)}
  [media-image source style/network-logo])

(defn- wallet-networks-center [{:keys [networks networks-on-press theme background]}]
  (let [network-list (take 2 networks)]
    [:rn/pressable {:on-press networks-on-press
                    :style    [style/wallet-networks-container
                               (style/wallet-networks-surface-style theme background)]}
     (map-indexed (fn [index source]
                    [wallet-network-logo source index])
                  network-list)
     [center-dots-placeholder]
     [:rn/view {:style style/dropdown-chevron}
      [icon/view {:name  :icon/dropdown
                  :size  12
                  :color :no-color}]]]))

(defn- community-network-center [{:keys [type community-name community-logo network-name network-logo
                                         theme background]}]
  (let [community? (= type :community)
        label      (if community? community-name network-name)
        logo       (if community? community-logo network-logo)]
    [:rn/view {:style style/title-row}
     [media-image logo style/leading-media]
     [text/text {:font            :font/medium-15
                 :style           {:color (style/title-color theme background)}
                 :number-of-lines 1}
      label]]))

(defn- center-content [{:keys [type custom-content] :as props}]
  (case type
    :title                  [title-center props]
    :dropdown               [dropdown-center props]
    :token                  [token-center props]
    :channel                [channel-center props]
    :title-description      [title-description-center props]
    :title-icon-description [title-icon-description-center props]
    :wallet-networks        [wallet-networks-center props]
    (:community :network) [community-network-center props]
    :custom                 custom-content
    nil))

(defn nav-left-action [{:keys [on-press background icon]
                        :or   {background :white}}]
  (let [theme (context/use-theme)]
    [icon-button {:icon       icon
                  :on-press   on-press
                  :theme      theme
                  :background background}]))

(defn nav-title [{:keys [title background]
                  :or   {background :white}}]
  (let [theme (context/use-theme)]
    [title-center {:title      title
                   :theme      theme
                   :background background}]))

(defn page-nav
  "Page nav component.

  Props:
  - `:background` one of `:white`, `:neutral-5`, `:neutral-90`, `:neutral-95`,
    `:neutral-100`, `:photo`, `:blur`
  - `:left` map: `{:icon {:name :icon/arrow-left} :on-press fn}`
  - `:right` nil or vector (up to 3 items)
    - action item: `{:icon {:name :icon/placeholder} :on-press fn}`
    - account switcher placeholder: `{:type :account-switcher :on-press fn}`
  - `:center` map describing center variant and its props
  - `:center-opacity` optional opacity for the center slot

  `:center` types:
  - `{:type :no-title}`
  - `{:type :title :title \"...\" :text-align :center|:left}`
  - `{:type :dropdown :dropdown-text \"...\" :dropdown-on-press fn}`
  - `{:type :token :token-name \"...\" :token-logo source}`
  - `{:type :channel :channel-name \"...\" :icon {:name :icon/...}}`
  - `{:type :title-description :title \"...\" :description \"...\" :picture source}`
  - `{:type :title-icon-description :title \"...\" :description \"...\" :picture source
     :icon {:name :icon/...}}`
  - `{:type :wallet-networks :networks [source ...] :networks-on-press fn}`
  - `{:type :community :community-name \"...\" :community-logo source}`
  - `{:type :network :network-name \"...\" :network-logo source}`
  - `{:type :custom :custom-content hiccup-node}`

  Right-side limits by center type:
  - `:dropdown` -> up to 1 action
  - `:title` centered -> up to 1 item; left -> up to 3 items
  - `:title-description` -> up to 2 actions
  - `:title-icon-description` -> up to 2 actions
  - `:wallet-networks` -> up to 2 items
  - `:no-title`, `:token`, `:channel`, `:community`, `:network`, `:custom` -> up to 3 items"
  [{:keys [background left right center center-opacity]
    :as   props}]
  (let [theme     (context/use-theme)
        centered? (centered-layout? center)]
    [:rn/view (-> props
                  (dissoc :background :left :right :center :center-opacity :style)
                  (assoc :style (rn.utils/add-styles
                                 style/container-base
                                 (style/nav-surface-style theme background)
                                 (:style props))))
     [:rn/view {:style style/side-slot}
      [icon-button {:icon       (:icon left)
                    :on-press   (:on-press left)
                    :theme      theme
                    :background background}]]
     [:rn/view {:style [style/center-slot
                        (if centered?
                          style/center-slot-centered
                          style/center-slot-left)
                        (style/center-opacity-style center-opacity)]}
      [center-content (assoc center
                        :theme      theme
                        :background background)]]
     [right-content {:right             right
                     :center            center
                     :centered-content? centered?
                     :theme             theme
                     :background        background}]]))
