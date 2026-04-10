(ns xquo.components.divider.divider-line.view
  (:require [reagent-extended-compiler.utils.transforms :as rec.xf]
            [xquo.components.divider.divider-line.style :as style]
            [xquo.context :as context]))

(defn divider-line
  "Divider line component.
  - `props` map
    - `:style` optional caller style (map/vector/js style)
    - Any additional keys are forwarded to `:rn/view`."
  [{:as props}]
  (let [theme (context/use-theme)]
    [:rn/view (update props :style rec.xf/add-styles style/container-base)
     [:rn/view {:style [style/line-base (style/line-color-style theme)]}]]))
