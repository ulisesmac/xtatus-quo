(ns xquo.components.info.information-box.view
  (:require [react-native.utils :as rn.utils]
            [xquo.components.button.view :as button]
            [xquo.components.info.information-box.style :as style]
            [xquo.components.icon.view :as icon]
            [xquo.components.text.view :as text]
            [xquo.context :as context]))

(defn- leading-icon-view
  [{:keys [theme blur? status theme-color close-button title button icon use-15-font?]}]
  (let [provided-name (:name icon)
        icon-name (or provided-name
                      (cond
                        (= status :error)   :icon/alert-outline
                        (= status :warning) :icon/alert-octagon-outline
                        close-button        :icon/cross
                        :else               :icon/info-outline))
        icon-size (cond
                    provided-name                                       20
                    close-button                                        12
                    (and (= status :error) use-15-font?)                20
                    (= status :error)                                   16
                    (and (= status :warning) use-15-font?)              20
                    (= status :warning)                                 16
                    use-15-font?                                        20
                    :else                                               16)]
    [icon/view (merge {:name  icon-name
                       :size  icon-size
                       :color (style/leading-icon-color theme blur? status theme-color)
                       :style (style/leading-icon-style close-button title button use-15-font?)}
                      icon)]))

(defn- close-button-view
  [{:keys [theme status title button]
    {on-press :on-press} :close-button}]
  (if on-press
    [:rn/pressable {:on-press on-press
                    :style    (style/close-button-style title button)}
     [icon/view {:name  :icon/close
                 :size  12
                 :color (style/close-button-color theme status)}]]
    [:rn/view {:style (style/close-button-style title button)}
     [icon/view {:name  :icon/close
                 :size  12
                 :color (style/close-button-color theme status)}]]))

(defn- button-view [{{:keys [label] :as button} :button}]
  [button/button (-> button
                     (dissoc :label)
                     (assoc :size 32
                            :type (:type button :primary)))
   label])

(defn- description-view
  [{:keys [theme blur? status title compact? description use-15-font?]}]
  [text/text {:font  (if use-15-font?
                       :font/regular-15
                       :font/regular-13)
              :style (if compact?
                       [style/compact-body-base
                        (style/body-text-style theme blur? status title)]
                       (style/body-text-style theme blur? status title))}
   description])

(defn information-box
  "Information box component.

  - `props` map
    - `:status` one of `:default`, `:info`, `:success`, `:warning`, `:error` (default `:default`)
    - `:icon` optional leading icon props map
    - `:title` optional title text
    - `:description` optional body text. When present, it overrides the child content
    - `:use-15-font?` optional boolean; when true the description uses `:font/regular-15`
    - `:button` optional nested button map
      - accepts `xquo/button` props plus required `:label`
      - `:size 24` is enforced internally
      - `:type` defaults to `:primary`
    - `:close-button` optional close slot map presence renders the trailing close icon
      - `:on-press` optional close callback
    - `:blur?` optional boolean
    - `:style` optional caller style (map/vector/js style)
    - Any additional keys are forwarded to `:rn/view`.
  - `content` optional body node rendered as-is."
  [{:keys [status icon title description use-15-font? button close-button blur?]
    :or   {status :default}
    :as   props}
   content]
  (let [{theme :theme
         theme-color :color} (context/use-theme-color)]
    [:rn/view (-> props
                  (dissoc :status :icon :title :description :use-15-font? :button
                          :close-button :blur? :background :style)
                  (assoc :style (rn.utils/add-styles
                                 style/container-base
                                 (style/container-color-style theme blur? status theme-color)
                                 (if (or title button)
                                   style/rich-layout-base
                                   (style/compact-layout-style theme close-button))
                                 (:style props))))
     (if (or title button)
       [:rn/view {:style style/rich-row-base}
        [leading-icon-view {:theme         theme
                            :blur?         blur?
                            :status        status
                            :theme-color   theme-color
                            :icon          icon
                            :close-button  close-button
                            :title         title
                            :button        button
                            :use-15-font?  use-15-font?}]
        [:rn/view {:style style/rich-content-base}
         [:rn/view {:style style/text-column-base}
          (when title
            [text/text {:font  :font/medium-15
                        :style (style/title-text-style theme status)}
             title])
          (when description
            [description-view {:theme       theme
                               :blur?       blur?
                               :status      status
                               :title       title
                               :description description
                               :use-15-font? use-15-font?}])]
         (when (and (not description) content)
           [:rn/view {:style style/body-slot-base}
            content])
         (when button
           [button-view {:button button}])]
        (when close-button
          [close-button-view {:theme        theme
                              :status       status
                              :title        title
                              :button       button
                              :close-button close-button}])]
       [:<>
       [leading-icon-view {:theme         theme
                           :blur?         blur?
                           :status        status
                           :theme-color   theme-color
                           :icon          icon
                           :close-button  close-button
                           :title         title
                           :button        button
                           :use-15-font?  use-15-font?}]
        (when description
          [description-view {:theme       theme
                             :blur?       blur?
                             :status      status
                             :title       title
                             :compact?    true
                             :description description
                             :use-15-font? use-15-font?}])
        (when (and (not description) content)
          [:rn/view {:style style/compact-body-base}
           content])
        (when close-button
          [close-button-view {:theme        theme
                              :status       status
                              :title        title
                              :button       button
                              :close-button close-button}])])]))
