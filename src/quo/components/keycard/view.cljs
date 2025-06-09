(ns quo.components.keycard.view
  (:require
    [clojure.string :as string]
    [quo.components.keycard.style :as style]
    [quo.components.tags.tag :as tag]
    [quo.context :as quo.context]
    [quo.foundations.colors :as colors]
    [quo.foundations.resources :as resources]
    [react-native.core :as rn]
    [utils.i18n :as i18n]))

(defn keycard
  []
  (let [width (- (:width (rn/get-window)) 55)]
    [rn/image
     {:resize-mode :contain
      :style       {:margin-top     8
                    :margin-bottom  20
                    :width          width
                    :max-height     (/ width 1.6)
                    :flex           1
                    :flex-direction :row
                    :align-self     :center}
      :source      (resources/get-image :keycard-orange)}]))

(defn keycard-component
  "This component based on the following properties:
  - :holder-name - Can be owner's name. Default is Empty
  - :locked? - Boolean to specify whether the keycard is locked or not
  - :theme :theme/light/:theme/dark
  "
  [{:keys [holder-name locked? blur?]}]
  (let [theme (quo.context/use-theme)]
    [rn/view {:style (style/card-container locked? theme blur?)}
     [rn/image
      {:source (resources/get-image :keycard-logo)
       :style  (style/keycard-logo locked? theme)}]
     [rn/image
      {:source (resources/get-image
                (if (or locked? (= :theme/dark theme)) :keycard-chip-dark :keycard-chip-light))
       :style  style/keycard-chip}]
     (when-not (string/blank? holder-name)
       [tag/tag
        {:size                32
         :type                (when locked? :icon)
         :label               (i18n/label :t/user-keycard {:name holder-name})
         :labelled?           true
         :blurred?            true
         :resource            (when locked? :i/locked)
         :accessibility-label :holder-name
         :icon-color          colors/white-70-blur
         :override-theme      (when locked? :theme/dark)}])]))
