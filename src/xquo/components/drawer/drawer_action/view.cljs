(ns xquo.components.drawer.drawer-action.view
  (:require [react-native.utils :as rn.utils]
            [xquo.components.drawer.drawer-action.style :as style]
            [xquo.components.icon.view :as icon]
            [xquo.components.selectors.selector.view :as selector]
            [xquo.components.text.view :as text]
            [xquo.context :as context]
            [react-native.core :as rn]))

(defn- description-view [{:keys [theme background description]}]
  [:rn/view {:style style/description-row}
   (if (vector? description)
     description
     [text/text {:ellipsize-mode  :tail
                 :font            :font/regular-13
                 :number-of-lines 1
                 :style           (style/description-text-style theme background)}
      description])])

(defn- leading-view [{:keys [theme background danger? color icon image]}]
  (cond
    (:name icon)
    [icon/view (assoc icon
                 :size  (:size icon 20)
                 :color (:color icon (style/icon-color theme background danger? color)))]

    (:source image)
    [:rn/image {:style       (style/leading-image (:size image 32))
                :source      (:source image)
                :resize-mode :contain}]))

(defn- trailing-view
  [{:keys [theme background selected? selected-provided? arrow? toggle? on-select danger?
           arrow-icon color pressed?]}]
  (cond
    toggle?
    [:rn/view {:pointer-events :none
               :style          style/trailing-slot}
     [selector/selector (cond-> {:type       :toggle
                                 :background background}
                          selected-provided? (assoc :selected? selected?)
                          on-select (assoc :on-select on-select))]]

    selected?
    [:rn/view {:style style/trailing-slot}
     [icon/view {:name  :icon/check
                 :size  20
                 :color (style/trailing-icon-color theme background danger? color)}]]

    arrow?
    [:animated/view {:style [style/trailing-slot
                             (if pressed?
                               style/arrow-slot-pressed-state-style
                               style/arrow-slot-default-state-style)]}
     [icon/view {:name  (or arrow-icon :icon/chevron-right)
                 :size  20
                 :color (style/trailing-icon-color theme background danger? color)}]]))

(defn drawer-action
  "Drawer action component.

  API:
  - `props` map
    - `:title` action title (default `\"Action\"`)
    - `:description` optional secondary text or hiccup rendered in the 18px description slot
    - `:content` optional custom hiccup rendered in the text area instead of title and description
    - `:icon` optional leading icon props map
    - `:image` optional leading image props map with `:source` and optional `:size` (default `32`)
    - `:color` optional color family keyword
    - `:danger?` optional boolean
    - `:disabled?` optional boolean
    - `:selected?` optional boolean
    - `:arrow?` optional boolean
    - `:arrow-icon` optional trailing icon for the arrow slot
    - `:toggle?` optional boolean
    - `:on-select` optional callback used by the toggle variant
    - `:background` optional `:blur`
    - `:style` optional caller style
    - Any additional keys are forwarded to `:rn/pressable`."
  [{:keys [title description content color danger? selected? arrow? toggle? background on-select on-press
           on-press-in on-press-out icon image arrow-icon disabled?]
    :or   {title "Action"}
    :as   props}]
  (let [theme                   (context/use-theme)
        accent-color            (context/use-color)
        selected-provided?      (contains? props :selected?)
        [internal-selected?
         set-internal-selected?] (rn/use-state false)
        selected-now?           (if (and toggle? (not selected-provided?))
                                  internal-selected?
                                  selected?)
        [pressed? set-pressed!] (rn/use-state false)
        on-press!               (rn/use-callback
                                 (fn [event]
                                   (when toggle?
                                     (let [next-selected? (not selected-now?)]
                                       (when-not selected-provided?
                                         (set-internal-selected? next-selected?))
                                       (when on-select
                                         (on-select next-selected?))))
                                   (when on-press
                                     (on-press event)))
                                 [toggle? selected-now? selected-provided? on-select on-press])
        on-press-in!            (rn/use-callback
                                 (fn [event]
                                   (set-pressed! true)
                                   (when on-press-in
                                     (on-press-in event)))
                                 [on-press-in])
        on-press-out!           (rn/use-callback
                                 (fn [event]
                                   (set-pressed! false)
                                   (when on-press-out
                                     (on-press-out event)))
                                 [on-press-out])]
    [:rn/pressable (-> props
                       (dissoc :title :description :content :icon :image :color :danger? :selected? :arrow?
                               :arrow-icon :toggle? :background :on-select :style :on-press
                               :on-press-in :on-press-out :disabled?)
                       (assoc :disabled (boolean disabled?)
                              :on-press on-press!
                              :on-press-in on-press-in!
                              :on-press-out on-press-out!
                              :style (rn.utils/add-styles
                                      style/container-base
                                      (when disabled? style/disabled)
                                      (if description
                                        style/padding-description
                                        style/padding-default)
                                      (style/container-color-style background
                                                                   pressed?
                                                                   selected-now?
                                                                   danger?
                                                                   (or color accent-color))
                                      (:style props))))
     [:animated/view {:style [(if pressed?
                                style/row-pressed-state-style
                                style/row-default-state-style)
                              style/row-base
                              (cond
                                (:name icon)
                                style/gap-12

                                (:source image)
                                style/gap-8)]}
      (when (or (:name icon) (:source image))
        [leading-view {:theme      theme
                       :background background
                       :danger?    danger?
                       :color      color
                       :icon       icon
                       :image      image}])
      [:rn/view {:style (if description
                          [style/content-base style/content-gap-0]
                          style/content-base)}
       (if content
         content
         [:<>
          [:rn/view {:style style/title-row}
           [text/text {:ellipsize-mode  :tail
                       :font            :font/medium-15
                       :number-of-lines 1
                       :style           (style/title-text-style theme background danger? color)}
            title]]
          (when description
            [description-view {:theme       theme
                               :background  background
                               :description description}])])]
      [trailing-view {:theme              theme
                      :background         background
                      :selected?          selected-now?
                      :selected-provided? (or selected-provided? toggle?)
                      :arrow?             arrow?
                      :toggle?            toggle?
                      :on-select          on-select
                      :danger?            danger?
                      :arrow-icon         arrow-icon
                      :color              color
                      :pressed?           pressed?}]]]))
