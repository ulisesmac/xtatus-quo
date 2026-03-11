(ns xquo.components.selectors.options.view
  (:require [reagent-extended-compiler.utils.transforms :as rec.xf]
            [xquo.components.selectors.options.style :as style]
            [xquo.components.selectors.selector.view :as selector]
            [xquo.context :as context]
            [xquo.react-native :as rn]))

(defn- option-view [{:keys [component on-select option option-id option-style selected-id]}]
  (let [{:keys [color dark-theme?]} (context/use-theme-color)
        option-id-value             (option-id option)
        selected?                   (= option-id-value selected-id)
        on-press!                   (rn/use-callback (fn []
                                                       (when on-select
                                                         (on-select option-id-value)))
                                                     [on-select option-id-value])]
    [:rn/pressable {:style    (rec.xf/add-styles
                               style/option-base
                               (style/option-border-style dark-theme? selected? color)
                               option-style)
                    :on-press on-press!}
     [:rn/view {:style          style/selector-slot
                :pointer-events :none}
      [selector/selector {:type      :radio
                          :selected? selected?}]]
     (when component
       [component option])]))

(defn view
  "Options selector container.

  API:
  - `props` map
    - `:data` sequence of option maps
      - each option map is forwarded to `:component`
    - `:container-component` component used to wrap the rendered options
      - defaults to `:rn/scroll-view`
    - `:component` Reagent component used to render each option content
    - `:option-id` function receiving each option map and returning its id
      - defaults to `:id`
    - `:initial-selected` optional id used to seed the internal selected state
    - `:on-select` optional callback invoked with the selected option id
    - `:horizontal?` optional boolean; defaults to vertical layout
    - `:content-container-style` optional style applied to the inner content container
    - `:option-style` optional style applied to each option shell
    - `:style` optional caller style for the outer container
    - Any additional keys are forwarded to `:rn/view`."
  [{:keys [component container-component content-container-style data horizontal? initial-selected
           on-select option-id option-style style]
    :or   {container-component :rn/scroll-view
           option-id           :id}
    :as   props}]
  (let [[selected-id
         set-selected-id!] (rn/use-state initial-selected)
        on-select!      (rn/use-callback (fn [option-id-value]
                                             (set-selected-id! option-id-value)
                                             (when on-select
                                               (on-select option-id-value)))
                                           [on-select])]
    (into [container-component (-> props
                                   (dissoc :component :container-component :data :horizontal?
                                           :content-container-style :initial-selected
                                           :on-select :option-id :option-style :style)
                                   (assoc :style (rec.xf/add-styles style/root-base style)
                                          :content-container-style (rec.xf/add-styles
                                                                    (style/container-style horizontal?)
                                                                    content-container-style)))]
          (map (fn [option]
                 [option-view {:component    component
                               :on-select    on-select!
                               :option       option
                               :option-id    option-id
                               :option-style option-style
                               :selected-id  selected-id}]))
          data)))
