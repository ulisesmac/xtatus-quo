(ns xquo.profile.tile.view
  (:require [react-native.react.core :as react]
            [react-native.utils :as rn.utils]
            [xquo.components.avatar.view :as avatar]
            [xquo.components.button.style :as button-style]
            [xquo.components.text.view :as text]
            [xquo.context :as context]
            [xquo.profile.tile.style :as style]))

(defn- cover-view [{:keys [cover color loading? theme]}]
  (cond
    loading?
    [:rn/view {:style [style/cover (style/skeleton theme "100%" 64 0)]}]

    (= (:type cover) :image)
    (let [{:keys [source url] :as props} (:image cover)]
      [:rn/image (-> props
                     (dissoc :url)
                     (assoc :source      (or source {:uri url})
                            :resize-mode (:resize-mode props :cover)
                            :style       (rn.utils/add-styles style/cover (:style props))))])

    :else
    [:rn/view {:style [style/cover (style/cover-color (:color cover color))]}]))

(defn- loading-footer [{:keys [theme cover]}]
  (if cover
    [:rn/view {:style (style/skeleton theme 46 24 8)}]
    (into [:rn/view {:style (style/footer-outline theme)}]
          (map (fn [index] [:rn/view {:style (style/skeleton-token theme index)}]))
          (range 3))))

(defn tile [{:keys [title description description-props avatar cover footer loading? disabled?
                    on-press on-long-press on-press-in on-press-out]
             :as   props}]
  (let [{:keys [theme color]}   (context/use-theme-color)
        [pressed? set-pressed!] (react/use-state false)
        pressable?              (or on-press on-long-press)
        disabled?               (or disabled? loading?)
        pressed?                (and pressable? pressed? (not disabled?))
        component               (if pressable? :rn/pressable :rn/view)
        avatar-size             (:size avatar 48)
        avatar-radius           (if (= (:shape avatar :circle) :circle) (/ avatar-size 2) 12)
        press-in!               (react/use-callback (fn [event]
                                                      (set-pressed! true)
                                                      (when on-press-in (on-press-in event)))
                                                    [on-press-in])
        press-out!              (react/use-callback (fn [event]
                                                      (set-pressed! false)
                                                      (when on-press-out (on-press-out event)))
                                                    [on-press-out])]
    [component (cond-> props
                 :always (dissoc :title :description :description-props :avatar :cover :footer :loading? :disabled?)
                 :always (assoc :pointer-events (if disabled? :none (:pointer-events props))
                                :style          (rn.utils/add-styles style/container (:style props)))
                 pressable? (assoc :disabled     disabled?
                                   :on-press-in  press-in!
                                   :on-press-out press-out!))
     [:animated/view {:style [(style/frame theme cover (and disabled? (not loading?)))
                              (when pressable?
                                (if pressed?
                                  button-style/pressable-pressed-state-style
                                  button-style/pressable-default-state-style))]}
      [:rn/view {:style style/clipped}
       (when cover
         [:<>
          [cover-view {:cover cover :color color :loading? loading? :theme theme}]
          [:rn/view {:style (style/surface theme)}]])
       [:rn/view {:style style/content}
        [:rn/view {:style (style/avatar-frame avatar-size)}
         (when cover
           [:rn/view {:style (style/avatar-clearance theme avatar-radius)}])
         (if loading?
           [:rn/view {:style (style/skeleton theme avatar-size avatar-size avatar-radius)}]
           [avatar/avatar (assoc avatar :size avatar-size)])]
        [:rn/view {:style style/info}
         (if loading?
           [:<>
            [:rn/view {:style (style/skeleton theme 101 16 6)}]
            [:rn/view {:style [style/skeleton-description (style/skeleton theme 64 8 6)]}]]
           [:<>
            [text/text {:font :font/semibold-15 :number-of-lines 1 :ellipsize-mode :tail :style (style/text theme nil)}
             title]
            (when description
              [text/text (assoc description-props
                                :font            (:font description-props :font/medium-13)
                                :number-of-lines (:number-of-lines description-props 1)
                                :ellipsize-mode  (:ellipsize-mode description-props :tail)
                                :style           (rn.utils/add-styles (style/text theme true) (:style description-props)))
               description])])]
        (when footer
          [:rn/view {:style style/footer}
           (if loading?
             [loading-footer {:theme theme :cover cover}]
             footer)])]]]]))
