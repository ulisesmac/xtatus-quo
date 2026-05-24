(ns xquo.components.selectors.options.view
  (:require [react-native.utils :as rn.utils]
            [xquo.components.button.style :as button.style]
            [xquo.components.selectors.options.style :as style]
            [xquo.components.selectors.selector.view :as selector]
            [xquo.context :as context]
            [react-native.core :as rn]))

(defn- option-view
  [{:keys [component on-select option option-id option-layout option-style selected-id]}]
  (let [{:keys [color dark-theme?]} (context/use-theme-color)
        option-id-value (option-id option)
        disabled?       (:disabled? option)
        selected?       (= option-id-value selected-id)
        [pressed? set-pressed!] (rn/use-state false)
        on-press!       (rn/use-callback (fn []
                                           (when on-select
                                             (on-select option-id-value)))
                                         [on-select option-id-value])
        on-press-in!    (rn/use-callback #(set-pressed! true) [])
        on-press-out!   (rn/use-callback #(set-pressed! false) [])]
    [(if disabled? :rn/view :animated/pressable)
     (cond-> {:style (rn.utils/add-styles
                      (when-not disabled?
                        (if pressed?
                          button.style/pressable-pressed-state-style
                          button.style/pressable-default-state-style))
                      style/option-base
                      (style/option-border-style dark-theme? selected? color)
                      option-style)}
       (not disabled?)
       (assoc :on-press on-press!
              :on-press-in on-press-in!
              :on-press-out on-press-out!)

       (and (not disabled?) option-layout)
       (assoc :layout option-layout))
     [:rn/view {:style          style/selector-slot
                :pointer-events :none}
      [selector/selector {:type      :radio
                          :selected? selected?
                          :disabled? disabled?}]]
     (when component
       [component option])
     (when disabled?
       [:rn/view {:style style/option-disabled-overlay}])]))

(defn view
  "Options selector container.

  API:
  - `props` map
    - `:data` sequence of option maps
      - each option map is forwarded to `:component`
      - `:disabled?` optional boolean; disables selection and overlays the option
    - `:container-component` component used to wrap the rendered options
      - defaults to `:rn/scroll-view`
    - `:component` Reagent component used to render each option content
    - `:option-id` function receiving each option map and returning its id
      - defaults to `:id`
    - `:selected-id` optional controlled selected option id
    - `:initial-selected` optional id used to seed the internal selected state
    - `:on-select` optional callback invoked with the selected option id
    - `:option-layout` optional layout transition applied to each option shell
    - `:horizontal?` optional boolean; defaults to vertical layout
    - `:content-container-style` optional style applied to the inner content container
    - `:option-style` optional style applied to each option shell
    - `:style` optional caller style for the outer container
    - Any additional keys are forwarded to `:rn/view`."
  [{:keys [component container-component content-container-style data horizontal? initial-selected
           on-select option-id option-layout option-style selected-id style]
    :or   {container-component :rn/scroll-view
           option-id           :id}
    :as   props}]
  (let [selected-id-provided?      (contains? props :selected-id)
        [internal-selected-id
         set-internal-selected-id!] (rn/use-state initial-selected)
        selected-id-now            (if selected-id-provided?
                                     selected-id
                                     internal-selected-id)
        on-select!                 (rn/use-callback
                                    (fn [option-id-value]
                                      (when-not selected-id-provided?
                                        (set-internal-selected-id! option-id-value))
                                      (when on-select
                                        (on-select option-id-value)))
                                    [on-select selected-id-provided?])]
    (into [container-component (-> props
                                   (dissoc :component :container-component :data :horizontal?
                                           :content-container-style :initial-selected
                                           :selected-id :option-layout
                                           :on-select :option-id :option-style :style)
                                   (assoc :style (rn.utils/add-styles style/root-base style)
                                          :content-container-style (rn.utils/add-styles
                                                                    (style/container-style horizontal?)
                                                                    content-container-style)))]
          (map (fn [option]
                 [option-view {:component    component
                               :on-select    on-select!
                               :option       option
                               :option-id    option-id
                               :option-layout option-layout
                               :option-style option-style
                               :selected-id  selected-id-now}]))
          data)))
