(ns xquo.components.empty-state.view
  (:require [react-native.utils :as rn.utils]
            [xquo.components.button.view :as button]
            [xquo.components.empty-state.style :as style]
            [xquo.components.text.view :as text]
            [xquo.context :as context]))

(defn- illustration-view [{:keys [image image-tint]}]
  [:rn/image {:source      image
              :resize-mode :contain
              :style       (rn.utils/add-styles style/illustration
                                                (style/illustration-tint-style image-tint))}])

(defn- content-copy-view [{:keys [description image-spacing title]}]
  [:rn/view {:style [style/text-combination
                     (style/text-combination-top-spacing image-spacing)]}
   [text/text {:font  :font/semibold-15
               :style style/title-text}
    title]
   (if (string? description)
     [text/text {:font  :font/regular-13
                 :style style/description-text}
      description]
     description)])

(defn- action-button-view [{:keys [blur? button-props primary?]}]
  (let [{:keys [dark-theme?]} (context/use-theme-color)
        type                  (cond
                                primary?                     :primary
                                (and dark-theme? (not blur?)) :dark-grey
                                :else                        :grey)]
    [button/button (-> button-props
                       (dissoc :label)
                       (assoc :size  (:size button-props 32)
                              :blur? (:blur? button-props (and (not primary?) blur? (not (:photo? button-props))))
                              :type  (:type button-props type)))
     (:label button-props)]))

(defn- actions-view [{:keys [blur? primary-button secondary-button]}]
  [:rn/view {:style style/actions-base}
   [:rn/view {:style style/actions-stack}
    [action-button-view {:blur?        blur?
                         :button-props primary-button
                         :primary?     true}]
    (when secondary-button
      [action-button-view {:blur?        blur?
                           :button-props secondary-button}])]])

(defn empty-state
  [{:keys                             [blur? description image image-spacing image-tint title]
    [primary-button secondary-button] :buttons
    :as                               props}]
  [:rn/view (-> props
                (dissoc :blur? :buttons :description :image :image-spacing :image-tint :style :title)
                (assoc :style (rn.utils/add-styles style/root-base (:style props))))
   [:rn/view {:style style/content-base}
    [:rn/view {:style style/top-base}
     (when image
       [illustration-view {:image      image
                           :image-tint image-tint}])
     [content-copy-view {:description   description
                         :image-spacing (if image (or image-spacing 12) 0)
                         :title         title}]]
    (when primary-button
      [actions-view {:blur?            blur?
                     :primary-button   primary-button
                     :secondary-button secondary-button}])]])
