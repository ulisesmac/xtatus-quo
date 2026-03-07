(ns xquo.components.drawer.bottom-actions.view
  (:require [reagent-extended-compiler.utils.transforms :as rec.xf]
            [xquo.components.button.view :as button]
            [xquo.components.drawer.bottom-actions.style :as style]
            [xquo.components.icon.view :as icon]
            [xquo.components.text.view :as text]
            [xquo.context :as context]))

(defn- description-view [{:keys [theme background scroll? description context-tag?]}]
  (cond
    (and (= (:position description) :top)
         (= (:status description) :error))
    [:rn/view {:style style/top-error-row}
     [:rn/view {:style style/top-error-content}
      [icon/icon {:icon  :icon/alert
                  :size  16
                  :color (:color (style/description-text-style theme
                                                               background
                                                               scroll?
                                                               :top
                                                               :error))
                  :style style/top-error-icon}]
      [text/text {:font  :font/regular-13
                  :style (style/description-text-style theme
                                                      background
                                                      scroll?
                                                      :top
                                                      :error)}
       (:text description)]]]

    (= (:position description) :top)
    [:rn/view {:style style/top-description-row}
     [text/text {:font  :font/regular-13
                 :style (style/description-text-style theme
                                                     background
                                                     scroll?
                                                     :top
                                                     :default)}
      (:text description)]
     (when context-tag?
       ;; TODO: replace red placeholder with the real context tag component.
       [:rn/view {:style style/context-tag-placeholder}])]

    (= (:position description) :bottom)
    [:rn/view {:style style/bottom-description-row}
     [text/text {:font  :font/regular-13
                 :style [style/bottom-description-text
                         (style/description-text-style theme
                                                       background
                                                       scroll?
                                                       :bottom
                                                       :default)]}
      (:text description)]]))

(defn- action-button-view [{:keys [theme background scroll? description-position primary? button-props]}]
  (let [color (or (:color button-props) :color/blue)]
    [button/button (cond-> (-> button-props
                               (dissoc :label)
                               (assoc :size 40
                                      :container-style style/action-slot
                                      :style (rec.xf/add-styles
                                              style/action-fill
                                              (if primary?
                                                (style/primary-button-style theme color)
                                                (style/secondary-button-style theme background scroll? description-position))
                                              (:style button-props))))
                     primary? (assoc :color color))
     (:label button-props)]))

(defn- actions-view [{:keys [theme background scroll? description-position buttons]}]
  (let [two-actions? (= (count buttons) 2)]
    (into [:rn/view {:style style/actions-row}]
          (map-indexed
           (fn [index button-props]
             ^{:key (str "bottom-action-" index "-" (:label button-props))}
             [action-button-view {:theme        theme
                                  :background   background
                                  :scroll?      scroll?
                                  :description-position description-position
                                  :primary?     (or (not two-actions?) (= index 1))
                                  :button-props button-props}])
           buttons))))

(defn bottom-actions
  "Bottom actions component.

  API:
  - `props` map
    - `:buttons` vector of one or two button prop maps
      - each map accepts the `xquo/button` props plus `:label`
      - `:color` is forwarded to the nested button
    - `:description` optional map
      - `:position` one of `:top`, `:bottom`
      - `:status` one of `:default`, `:error` (default `:default`)
      - `:text` description text
    - `:context-tag?` optional boolean for the top/default variant
    - `:background` one of `:none`, `:blur` (default `:none`)
    - `:scroll?` optional boolean
    - `:style` optional caller style (map/vector/js style)
    - Any additional keys are forwarded to `:rn/view`."
  [{:keys [buttons description context-tag? background scroll?]
    :or   {background :none}
    :as   props}]
  (let [theme (context/use-theme)]
    [:rn/view (-> props
                  (dissoc :buttons :description :context-tag? :background :scroll? :style)
                  (assoc :style (rec.xf/add-styles
                                 style/container-base
                                 (:style props))))
     (when (= (:position description) :top)
       [description-view {:theme        theme
                          :background   background
                          :scroll?      scroll?
                          :description  description
                          :context-tag? context-tag?}])
     [actions-view {:theme      theme
                    :background background
                    :scroll?    scroll?
                    :description-position (:position description)
                    :buttons    buttons}]
     (when (= (:position description) :bottom)
       [description-view {:theme        theme
                          :background   background
                          :scroll?      scroll?
                          :description  description
                          :context-tag? context-tag?}])]))
