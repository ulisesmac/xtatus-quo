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
    - `:label` optional section label text
    - `:items` vector of `settings-item` prop maps
    - `:blur?` optional boolean for blur styling
    - `:style` optional caller style (map/vector/js style)
    - Any additional keys are forwarded to `:rn/view`.

  This component currently implements only the Figma `List type=Settings`
  variant."
  [{:keys [label items blur?]
    :as   props}]
  (let [theme (context/use-theme)]
    [:rn/view (-> props
                  (dissoc :label :items :blur? :style)
                  (assoc :style (rec.xf/add-styles
                                 style/container-base
                                 (:style props))))
     (when label
       [section-label/section-label {:label label
                                     :blur? blur?}])
     (when (seq items)
       (into [:rn/view {:style [style/surface-base
                                (style/surface-color-style theme blur?)]}]
             (map-indexed
              (fn [index item]
                [:<>
                 (when (pos? index)
                   [:rn/view {:style [style/divider-base
                                      (style/divider-color-style theme blur?)]}])
                 [settings-item/settings-item (-> item
                                                  (assoc :blur? blur?)
                                                  (update :style rec.xf/add-styles
                                                          style/item-container-in-surface))]]))
             items))]))
