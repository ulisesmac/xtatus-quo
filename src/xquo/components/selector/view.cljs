(ns xquo.components.selector.view
  (:require [reagent-extended-compiler.utils.transforms :as rec.xf]
            [xquo.components.selector.style :as style]
            [xquo.context :as context]
            [xquo.foundations.colors :as colors]
            [xquo.react-native :as rn]))

(def checkbox-check-image
  (js/require "../xtatus-quo/resources/icons/12/checkbox-check.png"))

(defn- checkmark-tint-color [type theme]
  (cond
    (and (= type :filled-checkbox) (= theme :theme/light))
    (colors/get-color :color/neutral-100)

    :else
    (colors/get-color :color/white-100)))

(defn selector
  "Selectors component.

  API:
  - `props` map
    - `:type` one of `:toggle`, `:radio`, `:checkbox`, `:filled-checkbox`
      (default `:toggle`)
    - `:selected?` optional boolean (`true`, `false`, or `nil`)
    - `:disabled?` optional boolean (default `false`)
    - `:background` one of `:none`, `:blur` (default `:none`)
    - `:on-select` optional callback invoked with next selected state boolean
    - `:style` optional caller style (map/vector/js style)
    - Any additional keys are forwarded to `:rn/pressable` (for example
      `:on-press`, `:testID`, `:accessibility-label`)."
  [{:keys [type selected? disabled? background on-select on-press]
    :or   {type       :toggle
           background :none}
    :as   props}]
  (let [theme             (context/use-theme)
        controlled?       (some? selected?)
        [internal-selected?
         set-internal-selected?] (rn/use-state false)
        selected-now?     (if controlled?
                            selected?
                            internal-selected?)
        on-press-toggle!  (rn/use-callback (fn [event]
                                             (let [next-selected? (not selected-now?)]
                                               (when-not controlled?
                                                 (set-internal-selected? next-selected?))
                                               (when on-select
                                                 (on-select next-selected?))
                                               (when on-press
                                                 (on-press event))))
                                           [selected-now? controlled? on-select on-press])]
    [:rn/pressable (-> props
                       (dissoc :type :selected? :disabled? :background :on-select :style)
                       (assoc :disabled disabled?
                              :on-press on-press-toggle!
                              :style (rec.xf/add-styles
                                      (style/container-style type)
                                      (style/state-style theme type background selected-now? disabled?)
                                      (:style props))))
     (cond
       (= type :toggle)
       [:rn/view {:style style/toggle-handle-style}]

       (and (= type :radio) selected-now?)
       [:rn/view {:style [style/radio-dot-base
                          (style/radio-dot-style theme background)]}]

       (and (or (= type :checkbox)
                (= type :filled-checkbox))
            selected-now?)
       [:rn/image {:source checkbox-check-image
                   :style  [style/checkmark-image-base
                            {:tint-color (checkmark-tint-color type theme)}]}]

       :else
       nil)]))
