(ns xquo.components.notification.view
  (:require [reagent-extended-compiler.utils.transforms :as rec.xf]
            [xquo.components.icon.view :as icon]
            [xquo.components.notification.style :as style]
            [xquo.components.text.view :as text]
            [xquo.context :as context]))

(defn- notification-type [type]
  (case type
    :notification/positive :notification/positive
    :notification/negative :notification/negative
    :notification/neutral))

(defn- default-icon-name [type]
  (case type
    :notification/positive :icon/check-circle-outline
    :notification/negative :icon/alert-outline
    :icon/placeholder))

(defn- notification-icon [{:keys [icon theme type]}]
  [icon/view (assoc icon
               :name  (or (:name icon) (default-icon-name type))
               :size  20
               :color (or (:color icon) (style/icon-color theme type)))])

(defn notification
  "Notification component.

  API:
  - `props` map
    - `:type` one of `:notification/positive`, `:notification/negative`,
      `:notification/neutral` (default `:notification/neutral`)
    - `:icon` optional leading icon props map
    - `:container-style` optional outer container style
    - `:style` optional notification row style
    - Any additional keys are forwarded to the outer `:rn/view`.
  - `content` notification text."
  [{:keys [container-style icon type]
    caller-style :style
    :or   {type :notification/neutral}
    :as   props}
   content]
  (let [theme (context/use-theme)
        type  (notification-type type)]
    [:rn/view (-> props
                  (dissoc :container-style :icon :type)
                  (assoc :style (rec.xf/add-styles
                                 style/container-base
                                 container-style)))
     [:rn/view {:style (rec.xf/add-styles
                        [style/root-base
                         (style/root-color-style theme)]
                        caller-style)}
      [notification-icon {:icon  icon
                          :theme theme
                          :type  type}]
      [text/text {:font  :font/medium-13
                  :style [style/text-slot
                          (style/text-color-style theme)]}
       content]]]))
