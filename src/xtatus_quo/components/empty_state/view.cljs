(ns xtatus-quo.components.empty-state.view
  (:require
   [quo.context]
   [react-native.core :as rn]
   [xtatus-quo.components.buttons.button.view :as button]
   [xtatus-quo.components.icon :as icon]
   [xtatus-quo.components.markdown.text :as text]
   [xtatus-quo.foundations.colors :as colors]))

(defn- -circle-icon-container [icon-name icon-color icon-bg-color icon-no-color?]
  (let [theme            (quo.context/use-theme)
        icon-tint-color  (or icon-color
                             (colors/theme-colors colors/neutral-50 colors/neutral-40 theme))
        container-style  (cond-> {:width           80
                                  :height          80
                                  :align-items     :center
                                  :justify-content :center}
                           icon-bg-color (assoc :border-radius 40
                                                :background-color icon-bg-color))]
    [rn/view {:style container-style}
     [icon/icon icon-name {:size  20
                           :color icon-tint-color
                           :no-color icon-no-color?}]]))

(defn empty-state
  [{:keys        [image icon title description container-style icon-color icon-bg-color icon-no-color?]
    upper-button :upper-button
    lower-button :lower-button
    :or          {icon :i/warning}}]
  (let [theme             (quo.context/use-theme)
        title-color       (colors/theme-colors colors/neutral-100 colors/white theme)
        description-color (colors/theme-colors colors/neutral-50 colors/neutral-40 theme)]
    [rn/view {:style (merge {:padding          12
                             :align-items      :center
                             :justify-content  :center}
                            container-style)}
     (if image
       [rn/view {:style {:width           80
                         :height          80
                         :align-items     :center
                         :justify-content :center}}
        [rn/image {:source image
                   :style  {:width  72
                            :height 72}}]]
       [-circle-icon-container icon icon-color icon-bg-color icon-no-color?])
     [rn/view {:style {:margin-top   12
                       :align-items  :center}}
      [text/text {:font  :font/semibold-15
                  :style {:color      title-color
                          :text-align :center}}
       title]
      [text/text {:font  :font/regular-13
                  :style {:color      description-color
                          :text-align :center}}
       description]]
     (when-let [{upper-button-text     :text
                 upper-button-on-press :on-press} upper-button]
       [rn/view {:style {:margin-top 20}}
        [button/button {:type     :primary
                        :size     32
                        :on-press upper-button-on-press}
         upper-button-text]
        (when-let [{lower-button-text     :text
                    lower-button-on-press :on-press} lower-button]
          [button/button {:container-style {:margin-top 12}
                          :size            32
                          :type            :grey
                          :background      :blur
                          :on-press        lower-button-on-press}
           lower-button-text])])]))
