(ns xquo.components.selectors.options.view
  (:require [reagent-extended-compiler.react :as react]
            [reagent-extended-compiler.utils.transforms :as rec.xf]
            [xquo.components.selectors.options.style :as style]
            [xquo.context :as context]))

(defn- option-view [{:keys [component on-select option option-style]}]
  (let [theme       (context/use-theme)
        color       (context/use-color)
        selected?   (:selected? option)
        on-press!   (react/use-callback
                     (fn []
                       (when on-select
                         (on-select option)))
                     [on-select option])]
    [:rn/pressable {:on-press on-press!
                    :style    (rec.xf/add-styles
                               style/option-base
                               (style/option-border-style theme selected? color)
                               option-style)}
     (when component
       [component option])]))

(defn view
  "Options selector container.

  API:
  - `props` map
    - `:data` sequence of option maps
    - `:component` Reagent component used to render each option content
    - `:on-select` optional callback invoked with the selected option map
    - `:horizontal?` optional boolean; defaults to vertical layout
    - `:option-style` optional style applied to each option shell
    - `:key-fn` optional fn used to derive item keys
    - `:style` optional caller style for the outer container
    - Any additional keys are forwarded to `:rn/view`."
  [{:keys [component data horizontal? key-fn on-select option-style]
    :or   {data []}
    :as   props}]
  (into [:rn/view (-> props
                      (dissoc :component :data :horizontal? :key-fn :on-select :option-style :style)
                      (assoc :style (rec.xf/add-styles
                                     (style/container-style horizontal?)
                                     (:style props))))]
        (map-indexed
         (fn [index option]
           (let [option-key (if key-fn
                              (key-fn option)
                              (or (:key option)
                                  (:id option)
                                  index))]
             ^{:key option-key}
           [option-view {:component    component
                         :on-select    on-select
                         :option       option
                         :option-style option-style}])))
        data))
