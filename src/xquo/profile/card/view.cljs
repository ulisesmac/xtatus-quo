(ns xquo.profile.card.view
  (:require [react-native.utils :as rn.utils]
            [xquo.components.avatar.view :as avatar]
            [xquo.components.button.view :as button]
            [xquo.components.icon.view :as icon]
            [xquo.components.text.view :as text]
            [xquo.context :as context]
            [xquo.foundations.colors :as colors]
            [xquo.profile.card.style :as style]
            [xquo.tag.standard.view :as tag]))

(defn card [{:keys [name description avatar name-icon tag button color] :as props}]
  (let [context-color (context/use-color)
        color         (or color context-color)]
    [context/provider {:theme :theme/dark :color color}
     [:rn/view (-> props
                   (dissoc :name :description :avatar :name-icon :tag :button :color)
                   (assoc :style (rn.utils/add-styles (style/container color) (:style props))))
      [:rn/view {:style style/header}
       [avatar/avatar (assoc avatar
                             :size  (:size avatar 48)
                             :blur? (:blur? avatar true))]
       (when (or tag button)
         [:rn/view {:style style/actions}
          (when tag
            [tag/tag (-> tag
                         (dissoc :label)
                         (assoc :size  (:size tag 32)
                                :blur? (:blur? tag true)))
             (:label tag)])
          (when button
            [button/button (-> button
                               (dissoc :label)
                               (assoc :size  (:size button 32)
                                      :type  (:type button :grey)
                                      :blur? (:blur? button (not (:photo? button)))))
             (:label button)])])]
      [:rn/view {:style style/text}
       [:rn/view {:style style/name-row}
        [text/text {:font            :font/semibold-19
                    :number-of-lines 1
                    :ellipsize-mode  :tail
                    :style           style/name-text}
         name]
        (when name-icon
          [:rn/view {:style style/name-icon}
           [icon/view (assoc name-icon
                             :size  (:size name-icon 20)
                             :color (:color name-icon (colors/get-color :color/white-40)))]])]
       (when (seq description)
         [text/text {:font            :font/monospace-15
                     :number-of-lines 1
                     :ellipsize-mode  :tail
                     :style           style/description}
          description])]]]))
