(ns xquo.components.settings.category.view
  (:require [reagent-extended-compiler.utils.transforms :as rec.xf]
            [xquo.components.settings.category.style :as style]
            [xquo.components.settings.item.view :as settings-item]
            [xquo.components.settings.section-label.view :as section-label]
            [xquo.context :as context]))

(defn category
  "Settings category component.

  API:
  - `props` map
    - `:label` section label text (default `\"Label\"`)
    - `:items` vector of `settings-item` prop maps
    - `:background` one of `:none`, `:blur` (default `:none`)
    - `:style` optional caller style (map/vector/js style)
    - Any additional keys are forwarded to `:rn/view`.

  This component currently implements only the Figma `List type=Settings`
  variant."
  [{:keys [label items background]
    :or   {label      "Label"
           background :none}
    :as   props}]
  (let [theme (context/use-theme)]
    [:rn/view (-> props
                  (dissoc :label :items :background :style)
                  (assoc :style (rec.xf/add-styles
                                 style/container-base
                                 (:style props))))
     [section-label/section-label {:label      label
                                   :background background}]
     (when (seq items)
       (into [:rn/view {:style [style/surface-base
                                (style/surface-color-style theme background)]}]
             (map-indexed
              (fn [index item]
                [:<>
                 (when (pos? index)
                   [:rn/view {:style [style/divider-base
                                      (style/divider-color-style theme background)]}])
                 [settings-item/settings-item (assoc item :background background)]]))
             items))]))
