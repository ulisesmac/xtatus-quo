(ns xquo.components.selectors.selector.view
  (:require [reagent-extended-compiler.utils.transforms :as rec.xf]
            [xquo.components.icon.view :as icon]
            [xquo.components.selectors.selector.style :as style]
            [xquo.context :as context]
            [xquo.foundations.colors :as colors]
            [xquo.react-native :as rn]))

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
    - `:on-press-in` optional callback `(fn [event] ...)`
    - `:on-press-out` optional callback `(fn [event] ...)`
    - `:style` optional caller style (map/vector/js style)
    - Any additional keys are forwarded to `:rn/pressable` (for example
      `:on-press`, `:accessibility-label`, `:testID`)."
  [{:keys [type selected? disabled? background on-select on-press on-press-in on-press-out]
    :or   {type       :toggle
           background :none}
    :as   props}]
  (let [theme            (context/use-theme)
        color            (context/use-color)
        controlled?      (some? selected?)
        [internal-selected?
         set-internal-selected?] (rn/use-state false)
        [pressed? set-pressed?] (rn/use-state false)
        selected-now?    (if controlled?
                           selected?
                           internal-selected?)
        on-press-in!     (rn/use-callback (fn [event]
                                            (set-pressed? true)
                                            (when on-press-in
                                              (on-press-in event)))
                                          [on-press-in])
        on-press-out!    (rn/use-callback (fn [event]
                                            (set-pressed? false)
                                            (when on-press-out
                                              (on-press-out event)))
                                          [on-press-out])
        on-press-toggle! (rn/use-callback (fn [event]
                                            (let [next-selected? (not selected-now?)]
                                              (when-not controlled?
                                                (set-internal-selected? next-selected?))
                                              (when on-select
                                                (on-select next-selected?))
                                              (when on-press
                                                (on-press event))))
                                          [selected-now? controlled? on-select on-press])]
    [:animated/view {:style (if pressed?
                              style/pressable-pressed-state-style
                              style/pressable-default-state-style)}
     [:rn/pressable (-> props
                        (dissoc :type :selected? :disabled? :background :on-select :on-press-in :on-press-out :style :hit-slop)
                        (assoc :disabled disabled?
                               :hit-slop 6
                               :on-press on-press-toggle!
                               :on-press-in on-press-in!
                               :on-press-out on-press-out!
                               :style (rec.xf/add-styles
                                       (style/container-style type)
                                       (style/state-style theme type background selected-now? disabled? color)
                                       (:style props))))
      (cond
        (= type :toggle)
        [:animated/view {:style [style/toggle-handle-base
                                 (style/toggle-handle-state-style selected-now?)]}]

        (= type :radio)
        [:animated/view {:style [style/radio-dot-base
                                 (style/radio-dot-style theme background color)
                                 (style/radio-dot-state-style selected-now?)]}]

        (and (or (= type :checkbox)
                 (= type :filled-checkbox))
             selected-now?)
        [icon/view {:name  :icon/check-thick
                    :size  12
                    :color (if (and (= type :filled-checkbox) (= theme :theme/light))
                             (colors/get-color :color/neutral-100)
                             (colors/get-color :color/white-100))}])]]))
