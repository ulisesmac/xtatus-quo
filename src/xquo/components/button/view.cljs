(ns xquo.components.button.view
  (:require [react-native.utils :as rn.utils]
            [xquo.components.button.style :as style]
            [xquo.components.icon.view :as icon]
            [xquo.components.text.view :as text]
            [xquo.context :as context]
            [react-native.core :as rn]))

(defn- button-text [{:keys [type size background]} content]
  (let [theme (context/use-theme)]
    [text/text {:font            (get style/font-type size)
                :style           (style/text-style theme type background size)
                :number-of-lines 1
                :ellipsize-mode  :middle}
     content]))

(defn- button-content [{:keys [background size type]} content]
  (if (string? content)
    [button-text {:type       type
                  :size       size
                  :background background}
     content]
    content))

(defn- button-icon [{:keys [icon side type size background icon-only? disabled? pressed?]}]
  (let [theme (context/use-theme)]
    [icon/view (merge {:size  (style/icon-size size)
                       :color (style/icon-color theme type background icon-only? disabled? pressed?)
                       :style (case side
                                :left style/icon-left-gap
                                :right style/icon-right-gap
                                nil)}
                      icon)]))

(defn- layout-type [icon-only? left-icon right-icon]
  (cond
    icon-only?                         :icon-only
    (and (:name left-icon)
         (:name right-icon))           :both
    (:name left-icon)                  :left
    (:name right-icon)                 :right
    :else                              nil))

(defn button
  "Button component.

  API:
  - `props` map
    - `:type` one of `:primary`, `:positive`, `:grey`, `:dark-grey`, `:outline`,
      `:ghost`, `:danger` (default `:primary`)
    - `:color` optional color token used by `:primary`; falls back to context color
    - `:size` one of `40`, `32`, `24` (default `40`)
    - `:background` one of `:none`, `:photo`, `:blur` (default `:none`)
    - `:glass?` optional boolean; renders the button through glass effect
    - `:icon` optional icon props map passed to `xquo/icon`
      - `:name` icon keyword
      - `:side` one of `:left` or `:right` when content is present
    - `:icons` optional map of icon props passed to `xquo/icon`
      - `:left` optional left icon map
      - `:right` optional right icon map
    - `:container-style` optional animated pressable layout style
    - `:disabled?` optional boolean
    - `:on-press-in` optional callback `(fn [event] ...)`
    - `:on-press-out` optional callback `(fn [event] ...)`
    - `:style` optional caller style (map/vector/js style)
    - Any additional keys are forwarded to the underlying pressable (for
      example `:on-press`, `:on-long-press`
  - `content` optional label string or arbitrary content node.

  Layout behavior:
  - If `content` is nil and `:icon` has `:name`, it renders icon-only.
  - With `content`, `:icon :side` controls whether the icon is rendered left
    or right of the content.
  - With `content`, `:icons` can render left and right icons at the same time."
  [{:keys               [color type size background disabled? glass? on-press-in on-press-out icon icons container-style] ;; TODO: horrendous API: container-style shouldn't be used
    :or                 {type       :primary
                         background :none
                         size       40}
    :as                 props}
  content]
  (let [theme          (context/use-theme)
        context-color  (context/use-color)
        resolved-color (or color context-color)
        icon-name      (:name icon)
        icon-side      (:side icon :right)
        left-icon      (or (:left icons)
                           (when (= icon-side :left) icon))
        right-icon     (or (:right icons)
                           (when (= icon-side :right) icon))
        icon-only?     (and (nil? content) icon-name)
        layout         (layout-type icon-only? left-icon right-icon)
        [pressed?
         set-pressed!] (rn/use-state false)
        on-press-in!   (rn/use-callback (fn [event]
                                          (set-pressed! true)
                                          (when on-press-in
                                            (on-press-in event)))
                                        [on-press-in])
        on-press-out!  (rn/use-callback (fn [event]
                                          (set-pressed! false)
                                          (when on-press-out
                                            (on-press-out event)))
                                        [on-press-out])]
    [(if glass? :effect/pressable :animated/pressable)
     (cond-> (-> props
                 (dissoc :type :size :background :icon :icons :state :disabled?
                         :glass? :on-press-in :on-press-out :container-style :color)
                 (assoc :disabled (boolean disabled?)
                        :style (rn.utils/add-styles
                                (if pressed?
                                  style/pressable-pressed-state-style
                                  style/pressable-default-state-style)
                                container-style
                                style/pressable-base-style
                                (style/container-layout-style size layout type)
                                (style/icon-only-shape-style layout type)
                                (style/pressable-type-style theme type background resolved-color disabled? pressed?)
                                (:style props))
                        :on-press-in on-press-in!
                        :on-press-out on-press-out!))
       glass? (assoc :effect       :glass
                     :intensity    (if (= type :outline) :regular :clear)
                     :theme        theme
                     :interactive? true))
     (cond
       icon-only?
       [button-icon {:icon       (dissoc icon :side)
                     :type       type
                     :size       size
                     :background background
                     :icon-only? true
                     :disabled?  disabled?
                     :pressed?   pressed?}]

       (= layout :both)
       [:<>
        [button-icon {:icon       (dissoc left-icon :side)
                      :side       :left
                      :type       type
                      :size       size
                      :background background
                      :disabled?  disabled?
                      :pressed?   pressed?}]
        [button-content {:type       type
                         :size       size
                         :background background}
         content]
        [button-icon {:icon       (dissoc right-icon :side)
                      :side       :right
                      :type       type
                      :size       size
                      :background background
                      :disabled?  disabled?
                      :pressed?   pressed?}]]

       (= layout :right)
       [:<>
        [button-content {:type       type
                         :size       size
                         :background background}
         content]
        [button-icon {:icon       (dissoc right-icon :side)
                      :side       :right
                      :type       type
                      :size       size
                      :background background
                      :disabled?  disabled?
                      :pressed?   pressed?}]]

       (= layout :left)
       [:<>
        [button-icon {:icon       (dissoc left-icon :side)
                      :side       :left
                      :type       type
                      :size       size
                      :background background
                      :disabled?  disabled?
                      :pressed?   pressed?}]
        [button-content {:type       type
                         :size       size
                         :background background}
         content]]

       :else
       [button-content {:type       type
                        :size       size
                        :background background}
        content])]))
