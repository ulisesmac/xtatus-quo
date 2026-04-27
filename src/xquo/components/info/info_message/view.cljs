(ns xquo.components.info.info-message.view
  (:require [reagent-extended-compiler.utils.transforms :as rec.xf]
            [xquo.components.info.info-message.style :as style]
            [xquo.components.icon.view :as icon]
            [xquo.components.text.view :as text]
            [xquo.context :as context]))

(defn info-message
  "Feedback info message.

  API:
  - `props` map
    - `:status` one of `:default`, `:success`, `:error`, `:warning`
      (default `:default`)
    - `:size` one of `:default`, `:tiny` (default `:default`)
    - `:background` one of `:none`, `:blur` (default `:none`)
    - `:style` optional caller style (map/vector/js style)
    - Any additional keys are forwarded to `:rn/view`.
  - `content` message text."
  [{:keys [status size background]
    :or   {status     :default
           size       :default
           background :none}
    :as   props}
   content]
  (let [theme               (context/use-theme)
        message-color-style (style/message-color theme background status)
        tiny?               (= size :tiny)]
    [:rn/view (-> props
                  (dissoc :status :size :background :style)
                  (assoc :style (rec.xf/add-styles
                                 style/container-base
                                 (if tiny?
                                   style/tiny-size-base
                                   style/default-size-base)
                                 (:style props))))
     (if tiny?
       [icon/icon {:name  :icon/info
                   :size  12
                   :color (:color message-color-style)
                   :style style/icon-tiny}]
       [icon/icon {:name  :icon/info
                   :size  16
                   :color (:color message-color-style)
                   :style style/icon-default}])
     [text/text {:font  (if tiny? :font/regular-11 :font/regular-13)
                 :style [style/text-slot message-color-style]}
      content]]))
