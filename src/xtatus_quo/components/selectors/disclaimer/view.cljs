(ns xtatus-quo.components.selectors.disclaimer.view
  (:require
    [xtatus-quo.components.icon :as icons]
    [xtatus-quo.components.markdown.text :as text]
    [xtatus-quo.components.selectors.disclaimer.style :as style]
    [xtatus-quo.components.selectors.selectors.view :as selectors]
    [quo.context :as context]
    [quo.foundations.colors :as colors]
    [react-native.core :as rn]))

(defn view
  [{:keys [checked? blur? accessibility-label container-style on-change icon]} label]
  (let [theme (quo.context/use-theme)
        color (quo.context/use-color)]
    [rn/pressable
     {:on-press            (when on-change
                             #(on-change (not checked?)))
      :accessibility-label :disclaimer-touchable-opacity
      :style               [container-style (style/container blur? theme)]}
     [selectors/view
      {:type                :checkbox
       :accessibility-label accessibility-label
       :blur?               blur?
       :checked?            checked?
       :on-change           on-change
       :customization-color color}]
     [text/text
      {:size  :paragraph-2
       :style style/text}
      label]
     (when icon
       [rn/view {:style style/icon-container}
        [icons/icon icon
         {:accessibility-label :disclaimer-icon
          :color               (if blur?
                                 colors/white-opa-70
                                 (colors/theme-colors colors/neutral-50
                                                      colors/neutral-40
                                                      theme))}]])]))

(defn inner-disclaimer
  [{:keys [checked? blur? accessibility-label idx on-selector-press]}
   content]
  (let [color    (context/use-color)
        on-press (rn/use-callback
                  (fn [enabled?]
                    (on-selector-press idx enabled?))
                  [])]
    [:rn/view {:style {:flex-direction :row}}
     [selectors/view
      {:type                :checkbox
       :accessibility-label accessibility-label
       :blur?               blur?
       :checked?            checked?
       :on-change           on-press
       :customization-color color}]
     [text/text
      {:size  :paragraph-2
       :style style/text}
      content]]))

(defn multi-disclaimer
  [{:keys [blur? container-style on-all-accepted label] :as props}
   & disclaimers]
  (let [theme             (quo.context/use-theme)
        [checks set-checks!] (rn/use-state (vec (repeat (count disclaimers) false)))
        on-selector-press (rn/use-callback
                           (fn [idx checked?]
                             (set-checks! #(assoc % idx checked?)))
                           [])]
    (rn/use-effect
     (fn []
       (on-all-accepted (every? true? checks)))
     [checks])
    [:rn/view {:style [container-style (style/multi-container blur? theme)]}
     (when label
       [text/text
        {:weight :medium
         :size   :paragraph-2
         :style  (style/label theme)}
        label])
     (map-indexed (fn [idx content]
                    ^{:key (str "disclaimer-" idx)}
                    [inner-disclaimer (assoc props :idx idx
                                                   :on-selector-press on-selector-press
                                                   :checked? (get checks idx))
                     content])
                  disclaimers)]))
