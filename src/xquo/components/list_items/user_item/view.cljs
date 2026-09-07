(ns xquo.components.list-items.user-item.view
  (:require [react-native.react.core :as react]
            [react-native.utils :as rn.utils]
            [xquo.components.icon.view :as icon]
            [xquo.components.list-items.user-item.style :as user-style]
            [xquo.components.list.style :as style]
            [xquo.components.text.view :as text]
            [xquo.context :as context]
            [xquo.foundations.colors :as colors]))

(defn user-item [{:keys                [blur? on-press user-description user-name]
                 {:keys [photo icon]} :user-photo
                 :as                  props}]
  (let [{:keys [color theme]}    (context/use-theme-color)
        [pressed? set-pressed!] (react/use-state false)
        on-press-in!            (react/use-callback #(set-pressed! true) [])
        on-press-out!           (react/use-callback #(set-pressed! false) [])]
    [(if on-press :rn/pressable :rn/view)
     (cond-> (-> props
                 (dissoc :blur? :on-press :style :user-description :user-name :user-photo)
                 (assoc :style (rn.utils/add-styles style/element-shell
                                                  style/pressable-element-spacing
                                                  style/pressable-element-padding
                                                  (:style props))))
       on-press
       (assoc :on-press     on-press
              :on-press-in  on-press-in!
              :on-press-out on-press-out!))
     [:animated/view {:pointer-events :none
                      :style          [style/overlay-base
                                       (style/pressed-color-style color blur?)
                                       (style/pressed-color-state-style (and on-press pressed?))]}]
     [:animated/view {:style [(if (and on-press pressed?)
                               style/row-pressed-state-style
                               style/row-default-state-style)
                             style/element-container
                             user-style/row]}
      (if photo
        [:rn/image {:resize-mode :cover
                    :source      {:uri photo}
                    :style       user-style/image-container}]
        [:rn/view {:style [user-style/image-container
                           (if (= theme :theme/dark)
                             user-style/image-placeholder-dark
                             user-style/image-placeholder-light)]}
         [icon/view (merge {:name  :icon/user-hex
                           :size  20
                           :color (colors/themed theme :color/neutral-80-70 :color/white-70)}
                          icon)]])
      (let [description? (seq user-description)]
        [:rn/view {:style [style/content-container
                           (when-not description? user-style/name-only)]}
         [text/text {:ellipsize-mode  :tail
                     :font            :font/semibold-15
                     :number-of-lines 1}
          user-name]
         (when description?
           [text/text {:ellipsize-mode  :tail
                       :font            :font/regular-13
                       :number-of-lines 1
                       :style           (if (= theme :theme/dark)
                                          user-style/description-dark
                                          user-style/description-light)}
            user-description])])]]))
