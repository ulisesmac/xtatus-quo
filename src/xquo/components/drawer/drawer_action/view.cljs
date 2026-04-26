(ns xquo.components.drawer.drawer-action.view
  (:require [reagent-extended-compiler.utils.transforms :as rec.xf]
            [xquo.components.drawer.drawer-action.style :as style]
            [xquo.components.icon.view :as icon]
            [xquo.components.selectors.selector.view :as selector]
            [xquo.components.text.view :as text]
            [xquo.context :as context]
            [xquo.react-native :as rn]))

(defn- description-view [{:keys [theme background description]}]
  [text/text {:font  :font/regular-13
              :style (style/description-text-style theme background)}
   description])

(defn- trailing-view
  [{:keys [theme background selected? selected-provided? arrow? toggle? on-select danger?
           action-color pressed?]}]
  (cond
    toggle?
    [:rn/view {:pointer-events :none}
     [selector/selector (cond-> {:type       :toggle
                                 :background background}
                          selected-provided? (assoc :selected? selected?)
                          on-select (assoc :on-select on-select))]]

    selected?
    [icon/icon {:name  :icon/check
                :size  20
                :color (style/trailing-icon-color theme background true danger? action-color)}]

    arrow?
    [:animated/view {:style (if pressed?
                              style/arrow-slot-pressed-state-style
                              style/arrow-slot-default-state-style)}
     [icon/icon {:name  :icon/chevron-right
                 :size  20
                 :color (style/trailing-icon-color theme background false danger? action-color)}]]))

(defn drawer-action
  "Drawer action component.

  API:
  - `props` map
    - `:title` action title (default `\"Action\"`)
    - `:description` optional secondary text
    - `:name` optional leading icon keyword
    - `:color` optional color family keyword
    - `:danger?` optional boolean
    - `:selected?` optional boolean
    - `:arrow?` optional boolean
    - `:toggle?` optional boolean
    - `:on-select` optional callback used by the toggle variant
    - `:background` optional `:blur`
    - `:style` optional caller style
    - Any additional keys are forwarded to `:rn/pressable`."
  [{:keys [title description color danger? selected? arrow? toggle? background on-select on-press
           on-press-in on-press-out]
    icon-name :name
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
                       (dissoc :title :description :name :icon :color :danger? :selected? :arrow?
                               :toggle? :background :on-select :style :on-press
                               :on-press-in :on-press-out)
                       (assoc :on-press on-press!
                              :on-press-in on-press-in!
                              :on-press-out on-press-out!
                              :style (rec.xf/add-styles
                                      style/container-base
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
                              (when icon-name style/gap-12)]}
      (when icon-name
        [icon/icon {:name  icon-name
                    :size  20
                    :color (style/icon-color theme background danger? color)}])
      [:rn/view {:style (if description
                          [style/content-base style/content-gap-2]
                          style/content-base)}
       [:rn/view {:style style/title-row}
        [text/text {:font            :font/medium-15
                    :number-of-lines 1
                    :style           (style/title-text-style theme background danger? color)}
         title]]
       (when description
         [description-view {:theme       theme
                            :background  background
                            :description description}])]
      [trailing-view {:theme              theme
                      :background         background
                      :selected?          selected-now?
                      :selected-provided? (or selected-provided? toggle?)
                      :arrow?             arrow?
                      :toggle?            toggle?
                      :on-select          on-select
                      :danger?            danger?
                      :action-color       (or color accent-color)
                      :pressed?           pressed?}]]]))
