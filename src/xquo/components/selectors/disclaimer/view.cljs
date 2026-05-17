(ns xquo.components.selectors.disclaimer.view
  (:require [react-native.utils :as rn.utils]
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
    - `:glass?` optional boolean; only the checkbox uses the glass effect
    - `:icon` optional right icon props map
    - `:on-select` optional callback invoked with next selected state boolean
    - `:style` optional caller style (map/vector/js style)
    - Any additional keys are forwarded to `:rn/view`.
  - `content` label shown in the row."
  [{:keys [selected? background glass? on-select icon]
    :or   {background :none}
    :as   props}
   content]
  (let [theme (context/use-theme)]
    [:rn/view (-> props
                  (dissoc :selected? :background :glass? :icon :on-select :disabled? :style)
                  (assoc :style (rn.utils/add-styles
                                 style/container-base
                                 (style/container-color-style theme background)
                                 (:style props))))
     [selector/selector {:type       :checkbox
                         :selected?  selected?
                         :background background
                         :glass?     glass?
                         :on-select  on-select
                         :style      style/selector-style}]
     [:rn/view {:style style/text-wrapper}
      (if (string? content)
        [text/text {:font  :font/regular-13
                    :style {:color (style/text-color theme)}}
         content]
        content)]
     (when (:name icon)
       [icon/view (merge {:size  20
                          :color (style/icon-color theme background)
                          :style style/icon-style}
                         icon)])]))
