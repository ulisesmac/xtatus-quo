(ns xquo.components.counter.step.view
  (:require [reagent-extended-compiler.utils.transforms :as rec.xf]
            [xquo.components.counter.step.style :as style]
            [xquo.components.text.view :as text]
            [xquo.context :as context]))

(defn- characters-count [label]
  (if (>= (count label) 3)
    3
    (count label)))

(defn step
  "Step counter component.

  Props:
  - `:type` one of `:neutral`, `:complete`, `:active` (default `:neutral`)
  - `:background` one of `:none`, `:blur` (default `:none`)
  - `:style` optional caller style (map/vector/js style)
  - Any additional keys are forwarded to `:rn/view` (for example
    `:accessibility-label`, `:testID`)

  Content:
  - `value` label shown inside the step.
  - Width adapts to label length:
    - 1 character -> 20px
    - 2 characters -> 20px
    - 3+ characters -> 28px."
  [{:keys [type background]
    :or   {type       :neutral
           background :none}
    :as   props}
   value]
  (let [theme      (context/use-theme)
        color      (context/use-color)
        label      (str value)
        characters (characters-count label)]
    [:rn/view (-> props
                  (dissoc :type :background :style)
                  (assoc :style (rec.xf/add-styles
                                 style/container-base
                                 (style/container-width-style characters)
                                 (:style props))))
     [:rn/view {:style [style/surface-base
                        (style/surface-inset-style characters)
                        (style/surface-color-style theme type background color)]}]
     [:rn/view {:style [style/value-slot-base
                        (style/value-slot-inset-style characters)]}
      [text/text {:font  :font/medium-11
                  :style {:color (style/value-color theme type)}}
       label]]]))
