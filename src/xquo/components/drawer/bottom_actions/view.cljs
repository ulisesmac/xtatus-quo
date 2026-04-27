(ns xquo.components.drawer.bottom-actions.view
  (:require [reagent-extended-compiler.utils.transforms :as rec.xf]
            [xquo.components.button.view :as button]
            [xquo.components.drawer.bottom-actions.style :as style]
            [xquo.components.icon.view :as icon]
            [xquo.components.text.view :as text]
            [xquo.context :as context]))

(defn- description-view [{:keys [theme blur? scroll? description context-tag?]}]
  (cond
    (and (= (:position description) :top)
         (= (:status description) :error))
    [:rn/view {:style style/top-error-row}
     [:rn/view {:style style/top-error-content}
      [icon/view {:name  :icon/alert-outline
                  :size  16
                  :color (:color (style/description-text-style theme blur? scroll? :top :error))
                  :style style/top-error-icon}]
      [text/text {:font  :font/regular-13
                  :style (style/description-text-style theme blur? scroll? :top :error)}
       (:text description)]]]

    (= (:position description) :top)
    [:rn/view {:style style/top-description-row}
     [text/text {:font  :font/regular-13
                 :style (style/description-text-style theme blur? scroll? :top :default)}
      (:text description)]
     (when context-tag?
       ;; TODO: replace red placeholder with the real context tag component.
       [:rn/view {:style style/context-tag-placeholder}])]

    (= (:position description) :bottom)
    [:rn/view {:style style/bottom-description-row}
     [text/text {:font  :font/regular-13
                 :style [style/bottom-description-text
                         (style/description-text-style theme blur? scroll? :bottom :default)]}
      (:text description)]]))

(defn- secondary-button-type [theme blur? scroll? description-position]
  (if (and (= theme :theme/dark)
           (not scroll?)
           (not blur?)
           (not= description-position :top))
    :dark-grey
    :grey))

(defn- secondary-button-background [theme blur? scroll?]
  (cond
    (and (= theme :theme/light) scroll?)
    :blur

    (and (= theme :theme/dark)
         (or scroll? blur?))
    :blur))

(defn- action-button-view [{:keys [theme blur? scroll? description-position primary? button-props]}]
  (let [button-type       (or (:type button-props)
                              (if primary?
                                :primary
                                (secondary-button-type theme blur? scroll? description-position)))
        button-background (or (:background button-props)
                              (when-not primary?
                                (secondary-button-background theme blur? scroll?)))]
    [button/button (cond-> (-> button-props
                               (dissoc :label)
                               (assoc :size            40
                                      :container-style (rec.xf/add-styles
                                                        style/action-slot
                                                        (:container-style button-props))))
                     button-type (assoc :type button-type)
                     button-background (assoc :background button-background))
     (:label button-props)]))

(defn- actions-view [{:keys [theme blur? scroll? description-position buttons]}]
  (let [two-actions? (= (count buttons) 2)]
    (into [:rn/view {:style style/actions-row}]
          (map-indexed
           (fn [index button-props]
             [action-button-view {:theme                theme
                                  :blur?                blur?
                                  :scroll?              scroll?
                                  :description-position description-position
                                  :primary?             (or (not two-actions?) (= index 1))
                                  :button-props         button-props}]))
          buttons)))

(defn bottom-actions
  "Bottom actions component.

  - `props` map
    - `:buttons` vector of one or two button prop maps
      - each map accepts the `xquo/button` props plus `:label`
    - `:description` optional map
      - `:position` one of `:top`, `:bottom`
      - `:status` one of `:default`, `:error` (default `:default`)
      - `:text` description text
    - `:context-tag?` optional boolean for the top/default variant
    - `:blur?` optional boolean (default `false`)
    - `:scroll?` optional boolean
    - `:style` optional caller style (map/vector/js style)
    - Any additional keys are forwarded to `:rn/view`."
  [{:keys [blur? buttons description context-tag? scroll?]
    :or   {blur? false}
    :as   props}]
  (let [{:keys [theme]} (context/use-theme-color)]
    [:rn/view (-> props
                  (dissoc :blur? :buttons :description :context-tag? :scroll? :style)
                  (assoc :style (rec.xf/add-styles style/container-base (:style props))))
     (when (= (:position description) :top)
       [description-view {:theme        theme
                          :blur?        blur?
                          :scroll?      scroll?
                          :description  description
                          :context-tag? context-tag?}])
     [actions-view {:theme                theme
                    :blur?                blur?
                    :scroll?              scroll?
                    :description-position (:position description)
                    :buttons              buttons}]
     (when (= (:position description) :bottom)
       [description-view {:theme        theme
                          :blur?        blur?
                          :scroll?      scroll?
                          :description  description
                          :context-tag? context-tag?}])]))
