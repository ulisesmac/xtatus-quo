(ns xquo.components.settings.item.view
  (:require [reagent-extended-compiler.utils.transforms :as rec.xf]
            [xquo.components.button.view :as button]
            [xquo.components.icon.view :as icon]
            [xquo.components.selectors.selector.view :as selector]
            [xquo.components.counter.step.view :as step]
            [xquo.components.settings.item.style :as style]
            [xquo.components.text.view :as text]
            [xquo.context :as context]))

(defn- title-view [{:keys [theme title]}]
  [text/text {:font            :font/medium-15
              :number-of-lines 1
              :style           {:color (style/title-color theme)}}
   title])

(defn- description-view [{:keys [theme background description]}]
  (let [{description-text :text
         description-icon :icon
         status-color     :status-color
         status-text      :status-text} description
        description-type (:type description)
        text-color       (style/secondary-text-color theme background)]
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
       [icon/icon {:icon  (or description-icon :icon/browser)
                   :size  16
                   :color (style/trailing-icon-color theme background)}]]

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

(defn- leading-view [{:keys [theme background image]}]
  (let [image-type (or (:type image) :icon)
        image-icon (:icon image)]
    (cond
      (= image-type :icon)
      [icon/icon {:icon  (or image-icon :icon/browser)
                  :size  20
                  :color (style/leading-icon-color theme background)}]

      (= image-type :image)
      [:rn/view {:style style/image-placeholder}]

      (= image-type :avatar)
      [:rn/view {:style style/avatar-placeholder}])))

(defn- label-view [{:keys [theme background label]}]
  (let [{label-text  :text
         label-color :color
         label-value :counter
         label-icon  :icon} label
        label-type (:type label)]
    (cond
      (= label-type :text)
      [text/text {:font            :font/regular-15
                  :number-of-lines 1
                  :style           [style/label-text
                                    {:color (style/secondary-text-color theme background)}]}
       (or label-text "Label")]

      (= label-type :color)
      [:rn/view {:style style/color-placeholder}]

      (= label-type :counter)
      [step/step {:type       :complete
                  :background background
                  :color      (or label-color :color/blue)}
       (or label-value 1)]

      (= label-type :icon)
      [icon/icon {:icon  (or label-icon :icon/placeholder)
                  :size  20
                  :color (style/trailing-icon-color theme background)}])))

(defn- action-view [{:keys [theme background action]}]
  (let [{action-type        :type
         action-on-press    :on-press
         action-on-select   :on-select
         action-button-text :button-text
         action-selected?   :selected?} action]
    (cond
      (= action-type :arrow)
      [icon/icon {:icon  :icon/chevron-right
                  :size  20
                  :color (style/trailing-icon-color theme background)}]

      (= action-type :selector)
      [selector/selector (cond-> {:type       :toggle
                                  :background background}
                           (contains? action :selected?) (assoc :selected? action-selected?)
                           action-on-select (assoc :on-select action-on-select)
                           action-on-press (assoc :on-press action-on-press))]

      (= action-type :button)
      [button/button (cond-> {:type       :outline
                              :size       24
                              :background background}
                       action-on-press (assoc :on-press action-on-press))
       (or action-button-text "Button")])))

(defn- right-view [{:keys [theme background right]}]
  (let [label-type  (get-in right [:label :type])
        label-node  (label-view {:theme      theme
                                 :background background
                                 :label      (:label right)})
        action-node (action-view {:theme      theme
                                  :background background
                                  :action     (:action right)})]
    (when (or label-node action-node)
      [:rn/view {:style [style/right-content-base
                         (if (= label-type :text)
                           style/right-gap-6
                           style/right-gap-4)]}
       label-node
       action-node])))

(defn- content-view [{:keys [theme background title description tag]}]
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
                           :background  background
                           :description description}]]
       [tag-view {:tag tag}]])))

(defn settings-item
  "Settings item component.

  API:
  - `props` map
    - `:title` item title (default `\"Account\"`)
    - `:background` one of `:none`, `:blur` (default `:none`)
    - `:image` map
      - `:type` one of `:icon`, `:image`, `:avatar`, `:none`
      - `:icon` icon keyword for `:icon` type (default `:icon/browser`)
    - `:description` map
      - `:type` one of `:none`, `:text`, `:text-icon`, `:status`
      - `:text` description text for `:text` / `:text-icon`
      - `:icon` icon keyword for `:text-icon` (default `:icon/browser`)
      - `:status-color` color family keyword for `:status`
      - `:status-text` status text for `:status`
    - `:tag` map
      - `:type` one of `:none`, `:positive`, `:context`
    - `:right` map
      - `:label` map
        - `:type` one of `:none`, `:text`, `:color`, `:counter`, `:icon`
        - `:text`, `:color`, `:counter`, `:icon`
      - `:action` map
        - `:type` one of `:none`, `:arrow`, `:selector`, `:button`
        - `:on-press` callback
        - `:on-select` callback for selector action
        - `:selected?` optional selector selected state
        - `:button-text` button label
    - `:style` optional caller style (map/vector/js style)
    - Any additional keys are forwarded to `:rn/pressable`."
  [{:keys [title background image description tag right]
    :or   {title      "Account"
           background :none}
    :as   props}]
  (let [theme                (context/use-theme)
        image-type           (or (:type image) :icon)
        description-type     (:type description)
        tag-type             (:type tag)
        description-visible? (or (= description-type :text)
                                 (= description-type :text-icon)
                                 (= description-type :status))
        tag-visible?         (or (= tag-type :positive)
                                 (= tag-type :context))]
    [:rn/pressable (-> props
                       (dissoc :title :background :image :description :tag :right :style)
                       (assoc :style (rec.xf/add-styles
                                      style/container-base
                                      (style/container-padding-style
                                       image-type
                                       description-visible?
                                       tag-visible?)
                                      (if (= image-type :none) style/gap-0 style/gap-12)
                                      (:style props))))
     [leading-view {:theme      theme
                    :background background
                    :image      image}]
     [content-view {:theme       theme
                    :background  background
                    :title       title
                    :description description
                    :tag         tag}]
     [right-view {:theme      theme
                  :background background
                  :right      right}]]))
