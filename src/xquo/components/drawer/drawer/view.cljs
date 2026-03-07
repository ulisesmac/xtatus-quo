(ns xquo.components.drawer.drawer.view
  (:require [reagent-extended-compiler.utils.transforms :as rec.xf]
            [xquo.components.button.view :as button]
            [xquo.components.drawer.bottom-actions.view :as bottom-actions-view]
            [xquo.components.drawer.drawer.style :as style]
            [xquo.components.drawer.drawer-action.view :as drawer-action]
            [xquo.components.drawer.top.view :as drawer-top]
            [xquo.components.settings.section-label.view :as section-label]
            [xquo.components.text.view :as text]
            [xquo.context :as context]
            [xquo.foundations.colors :as colors]))

(defn- action-item-view [{:keys [action theme background]}]
  (cond
    (= action :divider)
    [:rn/view {:style style/divider-section}
     [:rn/view {:style [style/divider-base
                        (style/divider-color-style theme background)]}]]

    :else
    [:rn/view {:style style/action-section}
     [drawer-action/drawer-action (cond-> action
                                    background (assoc :background background))]]))

(defn- actions-view [{:keys [actions theme background]}]
  (into [:<>]
        (map (fn [action]
               [action-item-view {:action     action
                                  :theme      theme
                                  :background background}]))
        actions))

(defn- body-view [{:keys [theme body background]
                   button-props :button}]
  (let [dark-solid? (and (= theme :theme/dark)
                         (not= background :blur))]
    [:<>
     (when body
       [:rn/view {:style (if button-props
                           style/body-section-with-button
                           style/body-section)}
        [text/text {:font  :font/regular-13
                    :style [style/body-text
                            (style/body-text-style theme)]}
         body]])
     (when button-props
       [:rn/view {:style style/button-section}
        [button/button
         (cond-> (-> button-props
                     (dissoc :label)
                     (assoc :type  :outline
                            :size  24
                            :icons {:right :icon/info}))
           background
           (assoc :background background)

           dark-solid?
           (assoc :style      (rec.xf/add-styles
                               style/cta-button-dark-solid-style
                               (:style button-props))
                  :icon-color (colors/get-color :color/white 100)))
         (:label button-props)]])]))

(defn drawer
  "Drawer component.

  API:
  - `props` map
    - `:title` top title (default `\"Title\"`)
    - `:body` optional documentation body text
    - `:button` optional CTA button props map with `:label`
    - `:actions-label` optional section-label text rendered inside the drawer
    - `:actions` vector of drawer-action prop maps or the keyword `:divider`
    - `:bottom-actions` vector of one or two button prop maps for bottom actions
    - `:background` optional `:blur`
    - `:style` optional caller style
    - Any additional keys are forwarded to `:rn/view`."
  [{:keys [title body actions-label actions bottom-actions background]
    button-props :button
    :or   {title "Title"}
    :as   props}]
  (let [theme (context/use-theme)]
    [:rn/view (-> props
                 (dissoc :title :body :button :actions-label :actions
                          :bottom-actions :background :style)
                  (assoc :style (rec.xf/add-styles
                                 style/container-base
                                 (style/container-color-style theme background)
                                 (:style props))))
     [drawer-top/drawer-top
      (cond-> {:title    title
               :compact? true}
        background (assoc :background background))]
     [body-view {:theme      theme
                 :body       body
                 :button     button-props
                 :background background}]
     (when actions-label
       [:rn/view {:style style/label-section}
        [section-label/section-label {:label      actions-label
                                      :background background}]])
     (when actions
       [:rn/view {:style style/content-section}
        [actions-view {:actions    actions
                       :theme      theme
                       :background background}]])
     (when bottom-actions
       [bottom-actions-view/bottom-actions
        (cond-> {:buttons bottom-actions}
          background (assoc :background background))])]))
