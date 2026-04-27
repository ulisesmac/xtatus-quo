(ns xquo.components.color-picker.color.view
  (:require [reagent-extended-compiler.utils.transforms :as rec.xf]
            [xquo.components.color-picker.color.style :as style]
            [xquo.components.icon.view :as icon]
            [xquo.context :as context]
            [xquo.foundations.colors :as colors]))

(defn color
  "Color picker color.

  API:
  - `props` map
    - `:color` color family keyword (default `:color/primary`)
    - `:selected?` optional boolean
    - `:background` optional `:blur`
    - `:style` optional caller style (map/vector/js style)
    - Any additional keys are forwarded to `:rn/pressable`."
  [{:keys [color selected? background]
    :or   {color :color/primary}
    :as   props}]
  (let [theme (context/use-theme)]
    [:rn/pressable (-> props
                       (dissoc :color :selected? :background :style)
                       (assoc :style (rec.xf/add-styles
                                      style/container-base
                                      (:style props))))
     (when selected?
       [:rn/view {:style style/selection-ring-base}
        [:rn/view {:style [style/selection-ring-left
                           (style/selection-ring-left-style color)]}]
        [:rn/view {:style [style/selection-ring-right
                           (style/selection-ring-right-style color)]}]])
     [:rn/view {:style [style/swatch-base
                        (style/swatch-color-style theme background color)]}
      (when selected?
        [icon/view {:name  :icon/check
                    :size  20
                    :color (colors/get-color :color/white-100)}])]]))
