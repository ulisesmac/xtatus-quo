(ns xquo.components.list.view
  (:require [applied-science.js-interop :as j]
            [react-native.core :as rn]
            [react-native.utils :as rn.utils]
            [xquo.components.button.view :as button]
            [xquo.components.counter.step.view :as counter-step]
            [xquo.components.divider.divider-label.view :as divider-label]
            [xquo.components.divider.divider-line.view :as divider-line]
            [xquo.components.icon.view :as icon]
            [xquo.components.list-items.simple-item.view :as simple-item]
            [xquo.components.list.style :as style]
            [xquo.components.text.view :as text]
            [xquo.context :as context]
            [xquo.worklets.list :as list-worklets]))

(declare list-item)

(defn- description-view [{:keys [description]}]
  (if (string? description)
    [text/text {:font :font/regular-13}
     description]
    description))

(defn- title-view [{:keys [title]}]
  (if (string? title)
    [text/text {:font :font/semibold-13}
     title]
    title))

(defn- element-content
  [{:keys [type button description icon pressable? pressed? right selected?
           step-number theme title]
    :or   {type :bullet}}]
  [(if pressable? :animated/view :rn/view)
   {:style (if pressable?
             [(if pressed?
                style/row-pressed-state-style
                style/row-default-state-style)
              style/element-container]
             style/element-container)}
   (if (= type :step)
     [counter-step/step (if selected? {:type :active} {}) step-number]
     [icon/view (assoc icon
                       :name  (:name icon :icon/bullet)
                       :size  (:size icon 20)
                       :color (:color icon (style/bullet-color theme)))])
   [:rn/view {:style style/content-container}
    (if description
      [:<>
       (when title
         [title-view {:title title}])
       [description-view {:description description}]]
      [description-view {:description title}])]
   (cond
     right
     [:rn/view {:style style/button-container}
      right]

     button
     [:rn/view {:style style/button-container}
      [button/button (-> button
                         (dissoc :label)
                         (assoc :size 24))
       (:label button)]])])

(defn- list-element
  [{row-style :style
    :keys     [collapsable]
    :as       props}]
  [:rn/view (cond-> {:style (rn.utils/add-styles
                             style/element-shell
                             style/element-padding
                             row-style)}
              (some? collapsable)
              (assoc :collapsable collapsable))
   (element-content props)])

(defn- pressable-list-element
  [{row-style :style
    :keys     [collapsable color on-press]
    :as       props}]
  (let [[pressed? set-pressed!] (rn/use-state false)
        on-press-in!           (rn/use-callback #(set-pressed! true) [])
        on-press-out!          (rn/use-callback #(set-pressed! false) [])]
    [:rn/pressable (cond-> {:on-press     on-press
                            :on-press-in  on-press-in!
                            :on-press-out on-press-out!
                            :style        (rn.utils/add-styles
                                           style/element-shell
                                           style/pressable-element-spacing
                                           style/pressable-element-padding
                                           row-style)}
                     (some? collapsable)
                     (assoc :collapsable collapsable))
     [:animated/view {:pointer-events :none
                      :style          [style/overlay-base
                                       (style/pressed-color-style color)
                                       (style/pressed-color-state-style pressed?)]}]
     (element-content (assoc props
                        :pressable? true
                        :pressed?   pressed?))]))

(defn- into-section-items [root {:keys [color items theme]}]
  (into root
        (map-indexed (fn [index item]
                       [list-item {:color color
                                   :index index
                                   :item  item
                                   :theme theme}]))
        (keep identity items)))

(defn- static-section-content [props]
  (into-section-items [:rn/view {:style style/section-content}]
                      props))

(defn- collapsible-section-content [{:keys [visible?] :as props}]
  (let [[height set-height!] (rn/use-state nil)
        animated-style       (list-worklets/use-collapsible-style
                              visible?
                              height
                              (style/section-content-transition-duration-ms visible?))
        measure-content!     (rn/use-callback
                              (fn [event]
                                (let [next-height (j/get-in event [:nativeEvent :layout :height])]
                                  (when (pos? next-height)
                                    (set-height! (fn [height]
                                                   (if (= height next-height)
                                                     height
                                                     next-height))))))
                              [])]
    [:animated/view {:collapsable    false
                     :pointer-events (if visible? :auto :none)
                     :style          [style/section-content-container animated-style]}
     (into-section-items [:rn/view {:collapsable false
                                    :on-layout   measure-content!
                                    :style       [style/section-content
                                                  style/section-content-absolute]}]
                         props)]))

(defn- section-content [{:keys [collapsible?] :as props}]
  (if collapsible?
    [collapsible-section-content props]
    [static-section-content props]))

(defn- section-view
  [{section-style :style
    label-props   :divider-label
    :keys         [color items theme]}]
  (let [collapsible?      (:collapsible? label-props true)
        initial-open?     (:initial-open? label-props)
        [open? set-open!] (rn/use-state initial-open?)
        on-press          (:on-press label-props)
        toggleable?       (and collapsible?
                                (or on-press (:chevron label-props) (:counter label-props)))
        on-press!         (rn/use-callback
                            (fn [event]
                              (set-open! not)
                              (when on-press
                                (on-press event)))
                            [on-press])]
    [:rn/view {:collapsable false
               :style       (rn.utils/add-styles style/section-shell section-style)}
     [divider-label/divider-label (cond-> label-props
                                    toggleable?
                                    (assoc :on-press        on-press!
                                           :open?           open?
                                           :toggle-duration (style/section-content-transition-duration open?)
                                           :toggle-timing-function style/section-content-transition-timing-function))]
     [section-content {:collapsible? collapsible?
                       :color        color
                       :items        items
                       :theme        theme
                       :visible?     (or (not collapsible?) open?)}]]))

(defn- list-item
  [{:keys [color index item theme]}]
  (let [item (assoc item :collapsable false)]
    (case (:type item)
      :divider
      [divider-line/divider-line (dissoc item :type)]

      :divider-label
      [divider-label/divider-label (dissoc item :type)]

      :simple
      [simple-item/simple-item (dissoc item :type)]

      :section
      [section-view (assoc item
                           :color color
                           :theme theme)]

      [(if (:on-press item)
         pressable-list-element
         list-element)
       (assoc item
              :color       color
              :step-number (inc index)
              :theme       theme)])))

(defn view
  "List component.

  - `props` map
    - `:items` collection of list item prop maps
      - `:type` one of `:bullet`, `:step` (default `:bullet`)
      - `{:type :simple ...}` renders an `xquo/simple-item`
      - `{:type :divider}` renders an `xquo/divider-line` between rows
      - `{:type :divider-label}` renders an `xquo/divider-label` between rows
      - `{:type :section
          :divider-label {...}
          :items [...]}` renders a divider label plus child items. Collapsible
        sections own their open state and animate their clipped content height.
        Set `:collapsible? false` inside `:divider-label` to render the section
        open and keep `:chevron` visual only.
      - `:button` optional `xquo/button` props plus `:label`, rendered on the right
      - `:right` optional custom trailing node, rendered on the right
      - `:icon` optional icon props map for `:bullet` items
      - `:on-press` optional row press callback
      - `:selected?` marks `:step` items as active
      - `:title` optional title text or renderable node
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
                      :style       (rn.utils/add-styles style/container-base (:style props))))]
          (map-indexed (fn [index item]
                         [list-item {:color       color
                                     :index       index
                                     :item        item
                                     :theme       theme}]))
          items)))
