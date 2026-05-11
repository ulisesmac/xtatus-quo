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
            [react-native.core :as rn]))

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
       (if description
         [:<>
          (when title
            [title-view {:title title}])
          [description-view {:description description}]]
         [description-view {:description title}])]
      (when button
        [:rn/view {:style style/button-container}
         [button/button (-> button
                            (dissoc :label)
                            (assoc :size 24))
          (:label button)]])]]))

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

(defn- collapsible-section-content [{:keys [content-key visible?] :as props}]
  (let [[measurement set-measurement!]             (rn/use-state {:content-key content-key})
        content-height                             (:height measurement)
        content-key-changed?                       (not= (:content-key measurement) content-key)
        measured-height                            (when-not content-key-changed? content-height)
        needs-measure?                             (or content-key-changed? (nil? content-height))
        measure-visible?                           (and needs-measure? visible?)
        render-content?                            (or visible? measured-height)
        render-measuring?                          (and needs-measure? (not visible?))
        measure-content!                           (rn/use-callback
                                                    (fn [event]
                                                      (let [height (j/get-in event [:nativeEvent :layout :height])]
                                                        (when (pos? height)
                                                          (set-measurement!
                                                           (fn [current-measurement]
                                                             (if (and (= (:content-key current-measurement) content-key)
                                                                      (= (:height current-measurement) height))
                                                               current-measurement
                                                               {:content-key content-key
                                                                :height      height}))))))
                                                    [content-key])
        content-props                              (cond-> {:style [style/section-content
                                                                    (style/section-content-opacity visible?)]}
                                                     measure-visible?
                                                     (assoc :on-layout measure-content!))]
    [:animated/view {:collapsable false
                     :style       (style/section-content-container visible? measured-height)}
     (when render-content?
       (into-section-items [:animated/view content-props]
                           props))
     (when render-measuring?
       (into-section-items [:rn/view {:collapsable    false
                                      :on-layout      measure-content!
                                      :pointer-events :none
                                      :style          [style/section-content
                                                       style/section-content-measuring]}]
                           props))]))

(defn- section-content [{:keys [collapsible?] :as props}]
  (if collapsible?
    [collapsible-section-content props]
    [static-section-content props]))

(defn- section-view
  [{section-style :style
    label-props   :divider-label
    :keys         [color content-key items theme]}]
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
    [:rn/view
     {:style (rec.xf/add-styles style/section-shell section-style)}
     [divider-label/divider-label (cond-> label-props
                                    toggleable?
                                    (assoc :on-press        on-press!
                                           :open?           open?
                                           :toggle-duration style/section-content-transition-duration
                                           :toggle-timing-function style/section-content-transition-timing-function))]
     [section-content {:collapsible? collapsible?
                       :color        color
                       :content-key  content-key
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
          :items [...]}` renders a divider label plus child items. Collapsible
        sections use a clipped content container, own their open state, and keep
        child items mounted while hiding/showing them.
        Set `:collapsible? false` inside `:divider-label` to render the section
        open and keep `:chevron` visual only.
        Set `:content-key` when a collapsible section's child content changes
        and the cached height must be measured again.
      - `:button` optional `xquo/button` props plus `:label`, rendered on the right
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
                      :style       (rec.xf/add-styles style/container-base (:style props))))]
          (map-indexed (fn [index item]
                         [list-item {:color       color
                                     :index       index
                                     :item        item
                                     :theme       theme}]))
          items)))
