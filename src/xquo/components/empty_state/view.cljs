(ns xquo.components.empty-state.view
  (:require [reagent-extended-compiler.utils.transforms :as rec.xf]
            [xquo.components.button.view :as button]
            [xquo.components.empty-state.style :as style]
            [xquo.components.text.view :as text]
            [xquo.context :as context]))

(defn- illustration-view [{:keys [image image-tint]}]
  (if image
    [:rn/image {:style       [style/illustration
                              (style/illustration-tint-style image-tint)]
                :source      image
                :resize-mode :contain}]
    [:rn/view {:style style/illustration-placeholder}]))

(defn- content-copy-view [{:keys [description light-theme? title]}]
  [:rn/view {:style style/text-combination}
   [:rn/view {:style style/title-slot}
    [text/text {:font  :font/semibold-15
                :style [style/title-text (style/text-color-style light-theme?)]}
     title]]
   [text/text {:font  :font/regular-13
               :style [style/description-text (style/text-color-style light-theme?)]}
    description]])

(defn- action-button-view [{:keys [blur? button-props color light-theme? primary?]}]
  (let [{:keys [label]} button-props]
    [button/button (-> button-props
                       (dissoc :label)
                       (assoc :size       32
                              :background (when blur? :blur)
                              :style      (if primary?
                                            (style/primary-button-style light-theme? color)
                                            (style/secondary-button-style light-theme? blur?))
                              :type       (if primary? :primary :grey)))
     label]))

(defn- actions-view [{:keys [blur? color light-theme? primary-button secondary-button]}]
  [:rn/view {:style style/actions-base}
   [action-button-view {:blur?        blur?
                        :button-props primary-button
                        :color        color
                        :light-theme? light-theme?
                        :primary?     true}]
   (when secondary-button
     [:rn/view {:style style/secondary-action-spacing}
      [action-button-view {:blur?        blur?
                           :button-props secondary-button
                           :color        color
                           :light-theme? light-theme?
                           :primary?     false}]])])

(defn empty-state
  "Empty state component.

  API:
  - `props` map
    - `:title` title text
    - `:description` description text
    - `:buttons` optional vector `[primary-button secondary-button]`
      - each button map accepts `:label`, optional `:on-press`, and optional
        `:disabled?`
    - `:blur?` optional boolean
    - `:image` optional `:rn/image` source
    - `:image-tint` optional color keyword passed to `colors/get-color` and
      applied to `:image`
    - `:style` optional caller style (map/vector/js style)
    - Any additional keys are forwarded to the root `:rn/view`."
  [{:keys                             [blur? description image image-tint title]
    [primary-button secondary-button] :buttons
    :as                               props}]
  (let [{:keys [color light-theme?]} (context/use-theme-color)]
    [:rn/view (-> props
                  (dissoc :blur? :buttons :description :image :image-tint :style :title)
                  (assoc :style (rec.xf/add-styles style/root-base (:style props))))
     [:rn/view {:style style/content-base}
      [:rn/view {:style style/top-base}
       [illustration-view {:image      image
                           :image-tint image-tint}]
       [content-copy-view {:description  description
                           :light-theme? light-theme?
                           :title        title}]]
      (when primary-button
        [actions-view {:blur?            blur?
                       :color            color
                       :light-theme?     light-theme?
                       :primary-button   primary-button
                       :secondary-button secondary-button}])]]))
