(ns xquo.components.list.view
  (:require [reagent-extended-compiler.utils.transforms :as rec.xf]
            [xquo.components.button.view :as button]
            [xquo.components.counter.step.view :as counter-step]
            [xquo.components.icon.view :as icon]
            [xquo.components.list.style :as style]
            [xquo.components.text.view :as text]
            [xquo.context :as context]))

(defn- list-element
  [{:keys [type button icon icon-color selected? step-number title description theme]
    :or   {type :bullet}}]
  [:rn/view {:style style/element-container}
   (if (= type :step)
     [counter-step/step (if selected? {:type :active} {}) step-number]
     [icon/icon {:icon (or icon :icon/bullet)
                 :size 20
                 :color (or icon-color (style/bullet-color theme))}])
   [:rn/view {:style style/content-container}
    (when title
      [text/text {:font :font/semibold-13}
       title])
    [text/text {:font :font/regular-13}
     description]]
   (when button
     [:rn/view {:style style/button-container}
      [button/button (-> button
                         (dissoc :label)
                         (assoc :size 24))
       (:label button)]])])

(defn view
  "List component.

  - `props` map
    - `:items` collection of list item prop maps
      - `:type` one of `:bullet`, `:step` (default `:bullet`)
      - `:button` optional `xquo/button` props plus `:label`, rendered on the right
      - `:icon` optional icon keyword for `:bullet` items (default `:icon/bullet`)
      - `:icon-color` optional icon tint for `:bullet` items
      - `:selected?` marks `:step` items as active
      - `:title` optional title text
      - `:description` required description text
    - `:style` optional caller style (map/vector/js style)
    - Any additional keys are forwarded to `:rn/view`."
  [{:keys [items] :as props}]
  (let [theme (context/use-theme)]
    (into [:rn/view (-> props
                        (dissoc :items :style)
                        (assoc :style (rec.xf/add-styles style/container-base (:style props))))]
          (map-indexed (fn [index item]
                         [list-element (assoc item
                                              :step-number (inc index)
                                              :theme       theme)]))
          items)))
