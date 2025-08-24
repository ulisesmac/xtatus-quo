(ns xtatus-quo.components.list-items.account.view
  (:require
   [xtatus-quo.components.avatars.account-avatar.view :as account-avatar]
   [xtatus-quo.components.icon :as icon]
   [xtatus-quo.components.list-items.account.style :as style]
   [xtatus-quo.components.markdown.text :as text]
   [quo.components.wallet.address-text.view :as address-text]
   [quo.context :as quo.context]
   [quo.foundations.colors :as colors]
   [react-native.core :as rn]))

(defn- account-view
  [{:keys [account-props title-icon blur? theme]}]
  [rn/view {:style style/left-container}
   [account-avatar/view (assoc account-props :size 32)]
   [rn/view {:style style/account-container}
    [rn/view {:style style/account-title-container}
     [text/text
      {:font :font/semibold-15}
      (:name account-props)]
     (when title-icon
       [icon/icon title-icon
        {:size                20
         :color               (if blur?
                                colors/white-opa-40
                                (colors/theme-colors colors/neutral-50 colors/neutral-40 theme))
         :container-style     style/title-icon-container
         :accessibility-label :title-icon}])]
    [address-text/view (assoc account-props :format :short)]]])

(defn- balance-view
  [{:keys [balance-props type theme]}]
  [rn/view
   {:style               style/balance-container
    :accessibility-label :balance-container}
   [text/text
    {:font :font/medium-13}
    (:fiat-value balance-props)]
   [rn/view
    {:style style/metrics-container}
    [text/text
     {:font  :font/regular-13
      :style (style/metric-text type theme)}
     (str (:percentage-change balance-props) "%")]
    [rn/view {:style (style/dot-divider type theme)}]
    [text/text
     {:font  :font/regular-13
      :style (style/metric-text type theme)}
     (:fiat-change balance-props)]
    (when (not= type :balance-neutral)
      [rn/view
       {:style               style/arrow-icon-container
        :accessibility-label :arrow-icon}
       [icon/icon (if (= type :balance-positive) :i/positive :i/negative)
        (assoc (style/arrow-icon type theme)
               :accessibility-label
               (if (= type :balance-positive) :icon-positive :icon-negative))]])]])

(defn- token-tag
  [{:keys [token-props blur? theme]}]
  ;; TODO: Use Tiny tag component when available (issue #17341)
  [rn/view
   {:style               (style/token-tag-text-container blur? theme)
    :accessibility-label :tag-container}
   [text/text
    {:font  :font/medium-11
     :style (style/token-tag-text blur? theme)}
    (str (:value token-props) " " (:symbol token-props))]])

(defn- options-button
  [{:keys [on-options-press blur? theme]}]
  [rn/pressable
   {:accessibility-label :options-button
    :on-press            #(when on-options-press
                            (on-options-press))}
   [icon/icon :i/options
    {:color (if blur?
              colors/white-opa-70
              (colors/theme-colors colors/neutral-50 colors/neutral-40 theme))}]])

(defn- check-icon
  [{:keys [blur? customization-color theme]}]
  [rn/view {:accessibility-label :check-icon}
   [icon/icon :i/check
    {:color (if blur?
              colors/white
              (colors/resolve-color customization-color theme))}]])

(defn view
  [{:keys [type state blur? customization-color on-press non-reactive?]
    :or   {customization-color :blue
           type                :default
           state               :default
           blur?               false}
    :as   props}]
  (let [theme                  (quo.context/use-theme)
        [pressed? set-pressed] (rn/use-state false)
        on-press-in            (rn/use-callback #(set-pressed true))
        on-press-out           (rn/use-callback #(set-pressed false))
        props                  (assoc props :theme theme)
        disabled?              (= state :disabled)]
    [rn/pressable
     (cond-> {:style               (style/container
                                    {:state               state
                                     :blur?               blur?
                                     :customization-color customization-color
                                     :pressed?            pressed?})
              :disabled            disabled?
              :accessibility-label :container}
       (not non-reactive?) (assoc :on-press-in (when-not disabled? on-press-in)
                                  :on-press (when-not disabled? on-press)
                                  :on-press-out (when-not disabled? on-press-out)))
     [account-view props]
     [rn/view {:style (when (= type :tag) style/token-tag-container)}
      (cond
        (#{:balance-neutral :balance-negative :balance-positive} type)
        [balance-view props]

        (= type :tag)
        [token-tag props]

        (= type :action)
        [options-button props]

        (and (= type :default) (= state :selected))
        [check-icon props])]]))
