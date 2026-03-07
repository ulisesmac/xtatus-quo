(ns xquo.components.info.information-box.view
  (:require [reagent-extended-compiler.utils.transforms :as rec.xf]
            [xquo.components.button.view :as button]
            [xquo.components.info.information-box.style :as style]
            [xquo.components.icon.view :as icon]
            [xquo.components.text.view :as text]
            [xquo.context :as context]))

(defn- button-type [status]
  (cond
    (= status :error) :danger
    :else             :primary))

(defn- leading-icon-view [{:keys [theme background status color close-icon? title button-label]}]
  (cond
    close-icon?
    [icon/icon {:icon  :icon/placeholder
                :size  12
                :color (style/leading-icon-color theme background status color)
                :style (style/leading-icon-style close-icon? title button-label)}]

    :else
    [icon/icon {:icon  :icon/info
                :size  16
                :color (style/leading-icon-color theme background status color)
                :style (style/leading-icon-style close-icon? title button-label)}]))

(defn- close-icon-view [{:keys [theme status title button-label on-close]}]
  (cond
    on-close
    [:rn/pressable {:on-press on-close
                    :style    (style/close-icon-style title button-label)}
     [icon/icon {:icon  :icon/close
                 :size  12
                 :color (style/close-icon-color theme status)}]]

    :else
    [:rn/view {:style (style/close-icon-style title button-label)}
     [icon/icon {:icon  :icon/close
                 :size  12
                 :color (style/close-icon-color theme status)}]]))

(defn- button-view [{:keys [status color on-button-press button-label]}]
  [button/button (cond-> {:type  (button-type status)
                          :size  24
                          :color color}
                   on-button-press (assoc :on-press on-button-press))
   button-label])

(defn information-box
  "Information box component.

  API:
  - `props` map
    - `:status` one of `:default`, `:informative`, `:error`
      (default `:default`)
    - `:title` optional title text
    - `:button-label` optional 24px button label
    - `:on-button-press` optional button callback
    - `:close-icon?` optional boolean
    - `:on-close` optional close callback
    - `:background` one of `:none`, `:blur` (default `:none`)
    - `:color` customization color family keyword (default `:color/blue`)
    - `:style` optional caller style (map/vector/js style)
    - Any additional keys are forwarded to `:rn/view`.
  - `content` body text."
  [{:keys [status title button-label on-button-press close-icon? on-close background color]
    :or   {status     :default
           background :none
           color      :color/blue}
    :as   props}
   content]
  (let [theme (context/use-theme)
        rich? (or title button-label)]
    [:rn/view (-> props
                  (dissoc :status :title :button-label :on-button-press :close-icon?
                          :on-close :background :color :style)
                  (assoc :style (rec.xf/add-styles
                                 style/container-base
                                 (style/container-color-style theme background status color)
                                 (if rich?
                                   style/rich-layout-base
                                   (style/compact-layout-style theme close-icon?))
                                 (:style props))))
     (if rich?
       [:rn/view {:style style/rich-row-base}
        [leading-icon-view {:theme       theme
                            :background  background
                            :status      status
                            :color       color
                            :close-icon? close-icon?
                            :title       title
                            :button-label button-label}]
        [:rn/view {:style style/rich-content-base}
         [:rn/view {:style style/text-column-base}
          (when title
            [text/text {:font  :font/medium-15
                        :style (style/title-text-style theme status)}
             title])
          (when content
            [text/text {:font  :font/regular-13
                        :style (style/body-text-style theme background status title)}
             content])]
         (when button-label
           [button-view {:status          status
                         :color           color
                         :on-button-press on-button-press
                         :button-label    button-label}])]
        (when close-icon?
          [close-icon-view {:theme        theme
                            :status       status
                            :title        title
                            :button-label button-label
                            :on-close     on-close}])]
       [:<>
        [leading-icon-view {:theme       theme
                            :background  background
                            :status      status
                            :color       color
                            :close-icon? close-icon?
                            :title       title
                            :button-label button-label}]
        [text/text {:font  :font/regular-13
                    :style [style/compact-body-base
                            (style/body-text-style theme background status title)]}
         content]
        (when close-icon?
          [close-icon-view {:theme        theme
                            :status       status
                            :title        title
                            :button-label button-label
                            :on-close     on-close}])])]))
