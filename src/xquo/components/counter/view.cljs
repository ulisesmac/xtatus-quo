(ns xquo.components.counter.view
  (:require [reagent-extended-compiler.utils.transforms :as rec.xf]
            [xquo.components.counter.style :as counter-style]
            [xquo.components.text.view :as text]
            [xquo.context :as context]))

(def ^:private value-font
  {:default :font/medium-11
   :large   :font/medium-13})

(defn- value-layout-key [label]
  (cond
    (= label "99+")      :ninety-nine-plus
    (= label "-99")      :negative
    (<= (count label) 1) :one
    (= (count label) 2)  :two
    (= (count label) 3)  :three
    :else                :four))

(defn- value-view [{:keys [blur? color dark-theme? size text-style type]} label]
  [text/text {:font            (get value-font size)
              :ellipsize-mode  :clip
              :number-of-lines 1
              :style           [text-style
                                (counter-style/value-text-style color size type dark-theme? blur?)]}
   label])

(defn- default-counter-view [{:keys [blur? color dark-theme? root-props style type value]}]
  (let [label      (str value)
        layout-key (value-layout-key label)]
    [:rn/view (assoc root-props :style (rec.xf/add-styles
                                        counter-style/default-root-base
                                        (counter-style/default-root-style color layout-key type dark-theme? blur?)
                                        style))
     [:rn/view {:style [counter-style/default-surface-base
                        (counter-style/default-surface-style color layout-key type dark-theme? blur?)]}]
     [:rn/view {:style [counter-style/default-value-slot-base
                        (counter-style/default-value-slot-style layout-key)]}
      [value-view {:blur?       blur?
                   :color       color
                   :dark-theme? dark-theme?
                   :size        :default
                   :type        type}
       label]]]))

(defn- large-counter-view [{:keys [blur? color dark-theme? root-props style type value]}]
  (let [label      (str value)
        layout-key (value-layout-key label)]
    [:rn/view (assoc root-props :style (rec.xf/add-styles
                                        counter-style/large-root-base
                                        (counter-style/large-root-style color layout-key type dark-theme? blur?)
                                        style))
     [:rn/view {:style [counter-style/large-surface-base
                        (counter-style/large-surface-style color layout-key type dark-theme? blur?)]}]
     [:rn/view {:style counter-style/large-value-slot-base}
      [value-view {:blur?       blur?
                   :color       color
                   :dark-theme? dark-theme?
                   :size        :large
                   :type        type}
       label]]]))

(defn counter
  "Counter component.

  API:
  - `props` map
    - `:type` one of `:default`, `:secondary`, `:grey`, `:outline`,
      `:outline-color`, `:warning`, `:error` (default `:default`)
    - `:size` one of `:default` or `:large` (default `:default`)
    - `:blur?` optional boolean that uses the blur treatment (default `false`)
    - `:style` optional caller style (map/vector/js style)
    - Any additional keys are forwarded to the root `:rn/view`
      (for example `:accessibility-label`, `:testID`)
  - `value` label rendered inside the counter."
  [{:keys [blur? size style type]
    :or   {blur? false
           size  :default
           type  :default}
    :as   props}
   value]
  (let [{:keys [color dark-theme?]} (context/use-theme-color)
        root-props            (dissoc props :blur? :size :style :type)]
    (if (= size :large)
      [large-counter-view {:blur?       blur?
                           :color       color
                           :dark-theme? dark-theme?
                           :root-props  root-props
                           :style       style
                           :type        type
                           :value       value}]
      [default-counter-view {:blur?       blur?
                             :color       color
                             :dark-theme? dark-theme?
                             :root-props  root-props
                             :style       style
                             :type        type
                             :value       value}])))
