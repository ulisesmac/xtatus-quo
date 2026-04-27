(ns xquo.components.selectors.disclaimer.view
  (:require [reagent-extended-compiler.utils.transforms :as rec.xf]
            [xquo.components.icon.view :as icon]
            [xquo.components.selectors.disclaimer.style :as style]
            [xquo.components.selectors.selector.view :as selector]
            [xquo.components.text.view :as text]
            [xquo.context :as context]))

(defn disclaimer
  "Disclaimer selector row.

  API:
  - `props` map
    - `:selected?` optional boolean (`true`, `false`, or `nil`)
    - `:background` one of `:none`, `:blur` (default `:none`)
    - `:icon` optional right icon props map
    - `:on-select` optional callback invoked with next selected state boolean
    - `:style` optional caller style (map/vector/js style)
    - Any additional keys are forwarded to `:rn/view`.
  - `content` label shown in the row."
  [{:keys [selected? background on-select icon]
    :or   {background :none}
    :as   props}
   content]
  (let [theme (context/use-theme)]
    [:rn/view (-> props
                  (dissoc :selected? :background :icon :on-select :disabled? :style)
                  (assoc :style (rec.xf/add-styles
                                 style/container-base
                                 (style/container-color-style theme background)
                                 (:style props))))
     [selector/selector {:type       :checkbox
                         :selected?  selected?
                         :background background
                         :on-select  on-select
                         :style      style/selector-style}]
     [:rn/view {:style style/text-wrapper}
      [text/text {:font  :font/regular-13
                  :style {:color (style/text-color theme)}}
       content]]
     (when (:name icon)
       [icon/view (merge {:size  20
                          :color (style/icon-color theme background)
                          :style style/icon-style}
                         icon)])]))
