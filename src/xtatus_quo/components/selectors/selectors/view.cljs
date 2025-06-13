(ns xtatus-quo.components.selectors.selectors.view
  (:require
    [xtatus-quo.components.icon :as icons]
    [xtatus-quo.components.selectors.selectors.style :as style]
    [quo.context :as quo.context]
    [react-native.core :as rn]))

(defn- base-selector
  [{:keys [default-checked? checked? disabled? blur? customization-color on-change container-style
           label-prefix outer-style-fn inner-style-fn icon-style-fn]}]
  (let [theme                 (quo.context/use-theme)
        customization-color   (if customization-color customization-color :blue)
        controlled-component? (some? checked?)
        [internal-checked?
         set-internal-checked?] (rn/use-state (when-not controlled-component?
                                                (or default-checked? false)))
        actual-checked?       (if controlled-component? checked? internal-checked?)
        accessibility-label   (str label-prefix "-" (if actual-checked? "on" "off"))
        outer-styles          (outer-style-fn {:checked?            actual-checked?
                                               :disabled?           disabled?
                                               :blur?               blur?
                                               :container-style     container-style
                                               :customization-color customization-color
                                               :theme               theme})
        on-press              (rn/use-callback
                               (fn []
                                 (when-not (nil? internal-checked?)
                                   (set-internal-checked? (not internal-checked?)))
                                 (when on-change (on-change (not actual-checked?))))
                               [internal-checked? actual-checked? on-change])]
    [rn/pressable
     (when-not disabled?
       {:on-press                on-press
        :allow-multiple-presses? true
        :hit-slop                12})
     [rn/view
      {:style               outer-styles
       :accessibility-label accessibility-label}
      [rn/view {:style       (inner-style-fn {:theme               theme
                                              :checked?            actual-checked?
                                              :blur?               blur?
                                              :customization-color customization-color})
                :collapsable false}
       (when (and icon-style-fn actual-checked?)
         [icons/icon :i/check-small (icon-style-fn actual-checked? blur? theme)])]]]))

(defn- toggle
  [props]
  [base-selector
   (assoc props
          :label-prefix   "toggle"
          :outer-style-fn style/toggle
          :inner-style-fn style/toggle-inner)])

(defn- radio
  [props]
  [base-selector
   (assoc props
          :label-prefix   "radio"
          :outer-style-fn style/radio
          :inner-style-fn style/radio-inner)])

(defn- checkbox
  [props]
  [base-selector
   (assoc props
          :label-prefix   "checkbox"
          :outer-style-fn style/checkbox
          :inner-style-fn style/common-checkbox-inner
          :icon-style-fn  style/checkbox-check)])

(defn- filled-checkbox
  [props]
  [base-selector
   (assoc props
          :label-prefix   "filled-checkbox"
          :outer-style-fn style/filled-checkbox
          :inner-style-fn style/common-checkbox-inner
          :icon-style-fn  style/filled-checkbox-check)])

(defn view
  [{:keys [type]
    :or   {type :toggle}
    :as   props}]
  (case type
    :toggle          [toggle props]
    :radio           [radio props]
    :checkbox        [checkbox props]
    :filled-checkbox [filled-checkbox props]
    nil))
