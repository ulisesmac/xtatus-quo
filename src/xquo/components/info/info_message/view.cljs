(ns xquo.components.info.info-message.view
  (:require [react-native.utils :as rn.utils]
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
    - `:color` optional message text and icon color override
    - `:icon` optional leading icon props map passed to `xquo/icon`
    - `:style` optional caller style (map/vector/js style)
    - Any additional keys are forwarded to `:rn/view`.
  - `content` message text."
  [{:keys [status size background color icon]
    :or   {status     :default
           size       :default
           background :none}
    :as   props}
   content]
  (let [theme         (context/use-theme)
        content-color (or color (:color (style/message-color theme background status)))
        tiny?         (= size :tiny)]
    [:rn/view (-> props
                  (dissoc :status :size :background :color :icon :style)
                  (assoc :style (rn.utils/add-styles
                                 style/container-base
                                 (if tiny?
                                   style/tiny-size-base
                                   style/default-size-base)
                                 (:style props))))
     [icon/view (merge {:name  :icon/info-outline
                        :size  (if tiny? 12 16)
                        :color content-color
                        :style (if tiny? style/icon-tiny style/icon-default)}
                       icon)]
     [text/text {:font  (if tiny? :font/regular-11 :font/regular-13)
                 :style [style/text-slot {:color content-color}]}
      content]]))
