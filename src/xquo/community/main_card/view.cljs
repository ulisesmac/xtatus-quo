(ns xquo.community.main-card.view
  (:require [react-native.react.core :as react]
            [react-native.utils :refer [add-styles]]
            [xquo.community.main-card.style :as style]
            [xquo.community.stat.view :as stat]
            [xquo.components.avatar.view :as avatar]
            [xquo.components.button.style :as button-style]
            [xquo.components.text.view :as text]
            [xquo.context :as context]
            [xquo.tag.permissions.view :as permissions]
            [xquo.tag.standard.view :as tag]))

(defn- loading-content []
  (let [theme (context/use-theme)]
    [:<>
     [:rn/view {:style style/skeleton-info}
      [:rn/view {:style (style/skeleton theme 84 16 5 nil)}]
      [:rn/view {:style (style/skeleton theme "100%" 16 5 nil)}]]
     [:rn/view {:style style/skeleton-stats}
      (for [index (range 2)]
        ^{:key index}
        [:rn/view {:style style/skeleton-stat}
         [:rn/view {:style (style/skeleton theme 14 14 7 nil)}]
         [:rn/view {:style (style/skeleton theme 50 12 5 true)}]])]
     [:rn/view {:style style/skeleton-tags}
      (for [index (range 3)]
        ^{:key index}
        [:rn/view {:style (style/skeleton theme 76 24 20 true)}])]]))

(defn- card-content [{:keys [title description stats tags]}]
  [:<>
   [:rn/view {:style style/info}
    [text/text {:font :font/semibold-19 :number-of-lines 1 :style style/title} title]
    [text/text {:font :font/regular-15 :number-of-lines 1 :style style/description} description]]
   [:rn/view {:style style/stats}
    (for [[index item] (map-indexed vector stats)]
      ^{:key (or (:id item) index)}
      [stat/stat (dissoc item :value) (:value item)])]
   [:rn/view {:style style/tags}
    (for [[index item] (map-indexed vector tags)]
      ^{:key (or (:id item) index)}
      [tag/tag (-> item
                   (dissoc :label)
                   (assoc :size (:size item 24)))
       (:label item)])]])

(defn main-card [{:keys [avatar cover permissions loading? on-press] :as props}]
  (let [theme                   (context/use-theme)
        color                   (context/use-color)
        [pressed? set-pressed!] (react/use-state false)
        on-press-in!            (react/use-callback #(set-pressed! true) [])
        on-press-out!           (react/use-callback #(set-pressed! false) [])]
    [:animated/pressable {:on-press     on-press
                          :disabled     loading?
                          :on-press-in  on-press-in!
                          :on-press-out on-press-out!
                          :style        (add-styles (style/container theme)
                                                    (if pressed?
                                                      button-style/pressable-pressed-state-style
                                                      button-style/pressable-default-state-style)
                                                    (:style props))}
     [:rn/view {:style style/clip}
      [:rn/view {:style (style/cover theme)}
       (if (and cover (not loading?))
         [:rn/image {:source      (or (:source cover) {:uri (:url cover)})
                     :resize-mode :cover
                     :style       (add-styles style/cover-image (:style cover))}]
         [:rn/view {:style (style/cover-fill theme color loading?)}])]
      [:rn/view {:style (style/body theme loading?)}
       (if loading?
         [loading-content]
         [card-content props])]
      (if loading?
        [:rn/view {:style (add-styles style/avatar (style/skeleton theme 48 48 24 nil))}]
        [avatar/avatar (assoc avatar
                              :size  (:size avatar 48)
                              :style (add-styles style/avatar (:style avatar)))])
      (cond
        loading?
        [:rn/view {:style (add-styles style/permissions (style/skeleton theme 48 24 20 true))}]

        permissions
        [permissions/permissions-tag (assoc permissions
                                            :size  (:size permissions 24)
                                            :style (add-styles style/permissions (:style permissions)))])]]))
