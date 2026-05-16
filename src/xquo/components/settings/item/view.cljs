(ns xquo.components.settings.item.view
  (:require [reagent-extended-compiler.utils.transforms :as rec.xf]
            [xquo.components.button.view :as button]
            [xquo.components.icon.view :as icon]
            [xquo.components.selectors.selector.view :as selector]
            [xquo.components.counter.step.view :as step]
            [xquo.components.settings.item.style :as style]
            [xquo.components.text.view :as text]
            [xquo.context :as context]
            [react-native.core :as rn]))

(defn- title-view [{:keys [theme title]}]
  [text/text {:font            :font/medium-15
              :number-of-lines 1
              :style           {:color (style/title-color theme)}}
   title])

(defn- description-view [{:keys [theme blur? pressed? description]}]
  (let [{description-text :text
         description-name :name
         description-icon :icon
         status-color     :status-color
         status-text      :status-text} description
        description-type (:type description)
        text-color       (style/secondary-text-color theme blur? pressed?)]
    (cond
      (= description-type :text)
      [text/text {:font  :font/regular-13
                  :style {:color text-color}}
       (or description-text "This is a description")]

      (= description-type :text-icon)
      [:rn/view {:style style/description-row}
       [text/text {:font  :font/regular-13
                   :style {:color text-color}}
        (or description-text "This is a description")]
       [icon/view (merge {:name  (or description-name :icon/browser)
                          :size  16
                          :color (style/trailing-icon-color theme blur? pressed?)}
                         description-icon)]]

      (= description-type :status)
      [:rn/view {:style style/status-row}
       [:rn/view {:style [style/status-dot
                          {:background-color (style/status-dot-color
                                              theme
                                              (or status-color :color/success))}]}]
       [text/text {:font  :font/regular-13
                   :style {:color text-color}}
        (or status-text "Online now")]])))

(defn- tag-view [{:keys [tag]}]
  (let [tag-type (:type tag)]
    (cond
      (= tag-type :positive) [:rn/view {:style style/tag-placeholder}]
      (= tag-type :context) [:rn/view {:style style/tag-placeholder}])))

(defn- leading-view [{:keys [theme blur? pressed? image]}]
  (let [image-type (or (:type image) :icon)
        image-name (:name image)
        image-icon (:icon image)]
    (cond
      (= image-type :icon)
      [icon/view (merge {:name  (or image-name :icon/browser)
                         :size  20
                         :color (style/leading-icon-color theme blur? pressed?)}
                        image-icon)]

      (= image-type :image)
      [:rn/view {:style style/image-placeholder}]

      (= image-type :avatar)
      [:rn/view {:style style/avatar-placeholder}])))

(defn- label-view [{:keys [theme blur? pressed? label]}]
  (let [{label-text  :text
         label-color :color
         label-value :counter
         label-name  :name
         label-icon  :icon} label
        label-type (:type label)]
    (cond
      (= label-type :text)
      [text/text {:font            :font/regular-15
                  :number-of-lines 1
                  :style           [style/label-text
                                    {:color (style/secondary-text-color theme blur? pressed?)}]}
       (or label-text "Label")]

      (= label-type :color)
      [:rn/view {:style style/color-placeholder}]

      (= label-type :counter)
      [step/step {:type  :complete
                  :color (or label-color :color/primary)}
       (or label-value 1)]

      (= label-type :icon)
      [icon/view (merge {:name  (or label-name :icon/placeholder)
                         :size  20
                         :color (style/trailing-icon-color theme blur? pressed?)}
                        label-icon)])))

(defn- action-view [{:keys [theme blur? pressed? action selector-selected?]}]
  (let [{action-type        :type
         action-on-press    :on-press
         action-button-text :button-text
         action-disabled?   :disabled?
         selector-type      :selector-type} action]
    (cond
      (= action-type :arrow)
      [icon/view {:name  :icon/chevron-right
                  :size  20
                  :color (style/trailing-icon-color theme blur? pressed?)}]

      (= action-type :selector)
      [:rn/view {:pointer-events :none}
       [selector/selector (cond-> {:type (or selector-type :toggle)}
                            action-disabled? (assoc :disabled? true)
                            (or selector-selected?
                                (contains? action :selected?)) (assoc :selected? selector-selected?))]]

      (= action-type :button)
      [button/button (cond-> {:type :outline
                              :size 24}
                       action-on-press (assoc :on-press action-on-press))
       (or action-button-text "Button")])))

(defn- right-view [{:keys [theme blur? right pressed? selector-selected?]}]
  (let [label-type   (get-in right [:label :type])
        action-type  (get-in right [:action :type])
        label-node   (label-view {:theme theme
                                  :blur? blur?
                                  :pressed? pressed?
                                  :label (:label right)})
        action-node  (action-view {:theme              theme
                                   :blur?              blur?
                                   :pressed?           pressed?
                                   :action             (:action right)
                                   :selector-selected? selector-selected?})
        cluster-node (when (or label-node action-node)
                       [:rn/view {:style [style/right-content-base
                                          (if (= label-type :text)
                                            style/right-gap-6
                                            style/right-gap-4)]}
                        label-node
                        action-node])]
    (cond
      (= action-type :button)
      [:rn/view {:style style/button-right-slot}
       action-node]

      (= action-type :arrow)
      [:animated/view {:style (if pressed?
                                style/arrow-pressed-state-style
                                style/arrow-default-state-style)}
       cluster-node]

      cluster-node
      cluster-node)))

(defn- content-view [{:keys [theme blur? pressed? title description tag]}]
  (let [description-type     (:type description)
        tag-type             (:type tag)
        description-visible? (or (= description-type :text)
                                 (= description-type :text-icon)
                                 (= description-type :status))
        tag-visible?         (or (= tag-type :positive)
                                 (= tag-type :context))
        simple?              (and (not description-visible?)
                                  (not tag-visible?))]
    (if simple?
      [:rn/view {:style style/title-slot}
       [title-view {:theme theme
                    :title title}]]
      [:rn/view {:style [style/content-column-base
                         (when tag-visible?
                           style/content-column-gap-8)]}
       [:rn/view {:style style/info-column}
        [title-view {:theme theme
                     :title title}]
        [description-view {:theme       theme
                           :blur?       blur?
                           :pressed?    pressed?
                           :description description}]]
       [tag-view {:tag tag}]])))

(defn settings-item
  "Settings item component.

  API:
  - `props` map
    - `:title` item title (default `\"Account\"`)
    - `:blur?` optional boolean for blur styling
    - `:glass?` optional boolean; renders the item through a glass effect
      pressable
    - `:image` map
      - `:type` one of `:icon`, `:image`, `:avatar`, `:none`
      - `:name` icon keyword for `:icon` type
      - `:icon` optional icon props override map for `:icon` type
    - `:description` map
      - `:type` one of `:none`, `:text`, `:text-icon`, `:status`
      - `:text` description text for `:text` / `:text-icon`
      - `:name` icon keyword for `:text-icon`
      - `:icon` optional icon props override map for `:text-icon`
      - `:status-color` color family keyword for `:status`
      - `:status-text` status text for `:status`
    - `:tag` map
      - `:type` one of `:none`, `:positive`, `:context`
    - `:right` map
      - `:label` map
        - `:type` one of `:none`, `:text`, `:color`, `:counter`, `:icon`
        - `:text`, `:color`, `:counter`, `:name`, `:icon`
      - `:action` map
        - `:type` one of `:none`, `:arrow`, `:selector`, `:button`
        - `:selector-type` one of `:toggle`, `:radio`, `:checkbox`,
          `:filled-checkbox` for selector action
        - `:on-press` callback
        - `:on-select` callback for selector action
        - `:selected?` optional selector selected state
        - `:button-text` button label
    - `:style` optional caller style (map/vector/js style)
    - Any additional keys are forwarded to `:rn/pressable`."
  [{:keys [title blur? glass? image description tag right on-press on-press-in on-press-out]
    :or   {title "Account"}
    :as   props}]
  (let [theme                (context/use-theme)
        action               (:action right)
        action-type          (:type action)
        button-action?       (= action-type :button)
        selector-action?     (= action-type :selector)
        selector-disabled?   (:disabled? action)
        item-disabled?       (or button-action?
                                 selector-disabled?)
        selector-provided?   (contains? action :selected?)
        [internal-selector-selected?
         set-internal-selector-selected!] (rn/use-state false)
        selector-selected?   (if selector-provided?
                               (:selected? action)
                               internal-selector-selected?)
        [pressed? set-pressed!] (rn/use-state false)
        pressed-now?            (and pressed?
                                     (not item-disabled?))
        image-type           (or (:type image) :icon)
        description-type     (:type description)
        tag-type             (:type tag)
        description-visible? (or (= description-type :text)
                                 (= description-type :text-icon)
                                 (= description-type :status))
        tag-visible?         (or (= tag-type :positive)
                                 (= tag-type :context))
        on-press!            (rn/use-callback
                              (fn [event]
                                (when (and selector-action?
                                           (not selector-disabled?))
                                  (let [next-selected? (not selector-selected?)]
                                    (when-not selector-provided?
                                      (set-internal-selector-selected! next-selected?))
                                    (when-let [action-on-select (:on-select action)]
                                      (action-on-select next-selected?))
                                    (when-let [action-on-press (:on-press action)]
                                      (action-on-press event))))
                                (when on-press
                                  (on-press event)))
                              [selector-action?
                               selector-disabled?
                               selector-selected?
                               selector-provided?
                               action
                               on-press])
        on-press-in!         (rn/use-callback
                              (fn [event]
                                (set-pressed! true)
                                (when on-press-in
                                  (on-press-in event)))
                              [on-press-in])
        on-press-out!        (rn/use-callback
                              (fn [event]
                                (set-pressed! false)
                                (when on-press-out
                                  (on-press-out event)))
                              [on-press-out])]
    [(if glass? :effect/pressable :rn/pressable)
     (cond-> (-> props
                 (dissoc :title :blur? :glass? :image :description :tag :right :style :on-press
                         :on-press-in :on-press-out)
                 (assoc :disabled     item-disabled?
                        :on-press     on-press!
                        :on-press-in  (when-not item-disabled? on-press-in!)
                        :on-press-out (when-not item-disabled? on-press-out!)
                        :style        (rec.xf/add-styles
                                       style/container-base
                                       (style/container-padding-style
                                        image-type
                                        description-visible?
                                        tag-visible?)
                                       (:style props))))
       glass? (assoc :effect       :glass
                     :intensity    :clear
                     :theme        theme
                     :interactive? true))
     [:animated/view {:pointer-events :none
                      :style          [style/overlay-base
                                       (style/pressed-overlay-color-style theme blur?)
                                       (style/pressed-overlay-state-style pressed-now?)]}]
     [:rn/view {:style style/content-row-base}
      [:animated/view {:style [(when-not button-action?
                                 (if pressed-now?
                                   style/row-body-pressed-state-style
                                   style/row-body-default-state-style))
                               style/row-body-base
                               (if (= image-type :none) style/gap-0 style/gap-12)]}
       [leading-view {:theme theme
                      :blur? blur?
                      :pressed? pressed-now?
                      :image image}]
       [content-view {:theme       theme
                      :blur?       blur?
                      :pressed?    pressed-now?
                      :title       title
                      :description description
                      :tag         tag}]]
      [right-view {:theme              theme
                   :blur?              blur?
                   :right              right
                   :pressed?           pressed-now?
                   :selector-selected? selector-selected?}]]]))
