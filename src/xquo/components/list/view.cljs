(ns xquo.components.list.view
  (:require [applied-science.js-interop :as j]
            [reagent-extended-compiler.utils.transforms :as rec.xf]
            [xquo.components.button.view :as button]
            [xquo.components.counter.step.view :as counter-step]
            [xquo.components.divider.divider-label.view :as divider-label]
            [xquo.components.divider.divider-line.view :as divider-line]
            [xquo.components.icon.view :as icon]
            [xquo.components.list.style :as style]
            [xquo.components.text.view :as text]
            [xquo.context :as context]
            [xquo.react-native :as rn]))

(declare list-item)

(defn- description-view [{:keys [description]}]
  (if (string? description)
    [text/text {:font :font/regular-13}
     description]
    description))

(defn- list-element
  [{row-style :style
    :keys     [type button collapsable color description icon on-press
               selected? step-number theme title]
    :or       {type :bullet}}]
  (let [[pressed? set-pressed!] (rn/use-state false)
        on-press-in!           (rn/use-callback #(set-pressed! true) [])
        on-press-out!          (rn/use-callback #(set-pressed! false) [])
        root-component         (if on-press :rn/pressable :rn/view)
        root-props             (cond-> {:style (rec.xf/add-styles
                                                style/element-shell
                                                (when on-press style/pressable-element-spacing)
                                                (if on-press
                                                  style/pressable-element-padding
                                                  style/element-padding)
                                                row-style)}
                                 (some? collapsable)
                                 (assoc :collapsable collapsable)

                                 on-press
                                 (assoc :on-press     on-press
                                        :on-press-in  on-press-in!
                                        :on-press-out on-press-out!))]
    [root-component root-props
     (when on-press
       [:animated/view {:pointer-events :none
                        :style          [style/overlay-base
                                         (style/pressed-color-style color)
                                         (style/pressed-color-state-style pressed?)]}])
     [:rn/view {:style style/element-container}
      (if (= type :step)
        [counter-step/step (if selected? {:type :active} {}) step-number]
        [icon/view (assoc icon
                          :name  (:name icon :icon/bullet)
                          :size  (:size icon 20)
                          :color (:color icon (style/bullet-color theme)))])
      [:rn/view {:style style/content-container}
       (when title
         [text/text {:font :font/semibold-13}
          title])
       [description-view {:description description}]]
      (when button
        [:rn/view {:style style/button-container}
         [button/button (-> button
                            (dissoc :label)
                            (assoc :size 24))
          (:label button)]])]]))

(defn- section-content [{:keys [color items theme visible?]}]
  (let [content-items                        (keep identity items)
        [content-height set-content-height!] (rn/use-state nil)
        measuring?                           (and (nil? content-height) (not visible?))
        measure-content!                     (rn/use-callback
                                               (fn [event]
                                                 (let [height (j/get-in event [:nativeEvent :layout :height])]
                                                   (when (pos? height)
                                                     (set-content-height! (fn [current-height]
                                                                            (or current-height height))))))
                                               [])
        content-props                        (cond-> {:style [style/section-content
                                                              (when measuring?
                                                                style/section-content-measuring)
                                                              (style/section-content-opacity visible?)]}
                                               (nil? content-height)
                                               (assoc :on-layout measure-content!))]
    [:animated/view {:collapsable false
                     :style       (style/section-content-container visible? content-height)}
     (into [:animated/view content-props]
           (map-indexed (fn [index item]
                          [list-item {:color color
                                      :index index
                                      :item  item
                                      :theme theme}]))
           content-items)]))

(defn- section-view
  [{section-style :style
    label-props   :divider-label
    :keys         [color items theme]}]
  (let [initial-open?     (:initial-open? label-props)
        [open? set-open!] (rn/use-state initial-open?)
        on-press          (:on-press label-props)
        toggleable?       (or on-press (:chevron label-props) (:counter label-props))
        on-press!         (rn/use-callback
                            (fn [event]
                              (set-open! not)
                              (when on-press
                                (on-press event)))
                            [on-press])]
    [:rn/view
     {:style (rec.xf/add-styles style/section-shell section-style)}
     [divider-label/divider-label (cond-> label-props
                                    toggleable?
                                    (assoc :on-press        on-press!
                                           :open?           open?
                                           :toggle-duration style/section-content-transition-duration
                                           :toggle-timing-function style/section-content-transition-timing-function))]
     [section-content {:color color
                       :items items
                       :theme theme
                       :visible? open?}]]))

(defn- list-item
  [{:keys [color index item theme]}]
  (let [item (assoc item :collapsable false)]
    (case (:type item)
      :divider
      [divider-line/divider-line (dissoc item :type)]

      :divider-label
      [divider-label/divider-label (dissoc item :type)]

      :section
      [section-view (assoc item
                           :color color
                           :theme theme)]

      [list-element (assoc item
                           :color       color
                           :step-number (inc index)
                           :theme       theme)])))

(defn view
  "List component.

  - `props` map
    - `:items` collection of list item prop maps
      - `:type` one of `:bullet`, `:step` (default `:bullet`)
      - `{:type :divider}` renders an `xquo/divider-line` between rows
      - `{:type :divider-label}` renders an `xquo/divider-label` between rows
      - `{:type :section
          :divider-label {...}
          :items [...]}` renders a divider label plus child items in a shared
        clipped content container. The section owns its open state and keeps
        child items mounted while hiding/showing them.
      - `:button` optional `xquo/button` props plus `:label`, rendered on the right
      - `:icon` optional icon props map for `:bullet` items
      - `:on-press` optional row press callback
      - `:selected?` marks `:step` items as active
      - `:title` optional title text
      - `:description` required description text
      - `:style` optional item style owned by that row/item
    - `:style` optional caller style (map/vector/js style)
    - The list only owns its own full-width container style. Item padding and
      margin belong to each item/component.
    - Any additional keys are forwarded to the root container."
  [{:keys [items] :as props}]
  (let [{:keys [color theme]} (context/use-theme-color)]
    (into [:rn/view
           (-> props
               (dissoc :items :item-layout :layout :entering :exiting :collapsable :style)
               (assoc :collapsable false
                      :style       (rec.xf/add-styles style/container-base (:style props))))]
          (map-indexed (fn [index item]
                         [list-item {:color       color
                                     :index       index
                                     :item        item
                                     :theme       theme}]))
          items)))
