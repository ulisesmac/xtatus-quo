(ns xquo.components.empty-state.view
  (:require [react-native.utils :as rn.utils]
            [xquo.components.button.view :as button]
            [xquo.components.empty-state.style :as style]
            [xquo.components.text.view :as text]))

(defn- illustration-view [{:keys [image image-tint]}]
  (if image
    [:rn/image {:style       [style/illustration
                              (style/illustration-tint-style image-tint)]
                :source      image
                :resize-mode :contain}]
    [:rn/view {:style style/illustration-placeholder}]))

(defn- content-copy-view [{:keys [description image-spacing title]}]
  [:rn/view {:style [style/text-combination
                     (style/text-combination-top-spacing image-spacing)]}
   [:rn/view {:style style/title-slot}
    [text/text {:font  :font/semibold-15
                :style style/title-text}
     title]]
   (if (string? description)
     [text/text {:font  :font/regular-13
                 :style style/description-text}
      description]
     description)])

(defn- action-button-view [{:keys [blur? button-props primary?]}]
  (let [{:keys [label]} button-props]
    [button/button (-> button-props
                       (dissoc :label)
                       (assoc :size       32
                              :background (when blur? :blur)
                              :type       (if primary? :primary :grey)))
     label]))

(defn- actions-view [{:keys [blur? primary-button secondary-button]}]
  [:rn/view {:style style/actions-base}
   [action-button-view {:blur?        blur?
                        :button-props primary-button
                        :primary?     true}]
   (when secondary-button
     [:rn/view {:style style/secondary-action-spacing}
      [action-button-view {:blur?        blur?
                           :button-props secondary-button
                           :primary?     false}]])])

(defn empty-state
  "Empty state component.
  - `props` map
    - `:title` title text
    - `:description` description text or arbitrary content
    - `:image-spacing` optional spacing between image and text (default `12`)
    - `:buttons` optional vector `[primary-button secondary-button]`
      - each button map accepts `:label`, optional `:on-press`, and optional
        `:disabled?`
    - `:blur?` optional boolean
    - `:image` optional `:rn/image` source
    - `:image-tint` optional color keyword passed to `colors/get-color` and
      applied to `:image`
    - `:style` optional caller style (map/vector/js style)
    - Any additional keys are forwarded to the root `:rn/view`."
  [{:keys                             [blur? description image image-spacing image-tint title]
    [primary-button secondary-button] :buttons
    :as                               props}]
  [:rn/view (-> props
                (dissoc :blur? :buttons :description :image :image-spacing :image-tint :style :title)
                (assoc :style (rn.utils/add-styles style/root-base (:style props))))
   [:rn/view {:style style/content-base}
    [:rn/view {:style style/top-base}
     [illustration-view {:image      image
                         :image-tint image-tint}]
     [content-copy-view {:description   description
                         :image-spacing image-spacing
                         :title         title}]]
    (when primary-button
      [actions-view {:blur?            blur?
                     :primary-button   primary-button
                     :secondary-button secondary-button}])]])
