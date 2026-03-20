(ns xquo.components.button.view
  (:require [reagent-extended-compiler.utils.transforms :as rec.xf]
            [xquo.components.button.style :as style]
            [xquo.components.icon.view :as icon]
            [xquo.components.text.view :as text]
            [xquo.context :as context]
            [xquo.react-native :as rn]))

(defn- button-text [{:keys [type size background]} content]
  (let [theme (context/use-theme)]
    [text/text {:font  (get style/font-type size)
                :style (style/text-style theme type background)}
     content]))

(defn- button-icon [{:keys [icon-name side type size icon-color background icon-only?]}]
  (let [theme (context/use-theme)]
    [icon/icon {:icon  icon-name
                :size  (style/icon-size size)
                :color (or icon-color
                           (style/icon-color theme type background icon-only?))
                :style (style/icon-gap-style side)}]))

(defn- layout-type [icon-only? left-icon right-icon]
  (cond
    icon-only?                 :icon-only
    (and left-icon right-icon) :left-right
    left-icon                  :left
    right-icon                 :right
    :else                      nil))

(defn button
  "Button component.

  API:
  - `props` map
    - `:type` one of `:primary`, `:positive`, `:grey`, `:dark-grey`, `:outline`,
      `:ghost`, `:danger` (default `:primary`)
    - `:size` one of `40`, `32`, `24` (default `40`)
    - `:background` one of `:none`, `:photo`, `:blur` (default `:none`)
    - `:icons` optional map `{:left :icon/... :right :icon/...}`
    - `:icon-color` optional icon color override
    - `:container-style` optional outer animated wrapper style
    - `:disabled?` optional boolean
    - `:on-press-in` optional callback `(fn [event] ...)`
    - `:on-press-out` optional callback `(fn [event] ...)`
    - `:style` optional caller style (map/vector/js style)
    - Any additional keys are forwarded to `:rn/pressable` (for example
      `:on-press`, `:on-long-press`, `:accessibility-label`, `:testID`)
  - `content` optional label node.

  Layout behavior:
  - If `content` is nil and `:icons` has `:left` or `:right`, it renders icon-only.
  - With `content`, icon layout is derived from `:icons`:
    - `{:left ...}` left icon
    - `{:right ...}` right icon
    - `{:left ... :right ...}` both sides
    - no icons -> text-only."
  [{:keys               [type size background disabled? on-press-in on-press-out icon-color container-style] ;; TODO: horrendous API: container-=style shouldn't be used, as well as icon-color
    {left-icon  :left
     right-icon :right} :icons
    :or                 {type       :primary
                         background :none
                         size       40}
    :as                 props}
   content]
  (let [theme         (context/use-theme)
        color         (context/use-color)
        icon-only?    (and (nil? content) (or left-icon right-icon))
        layout        (layout-type icon-only? left-icon right-icon)
        [pressed? set-pressed!] (rn/use-state false)
        on-press-in!  (rn/use-callback (fn [event]
                                         (set-pressed! true)
                                         (when on-press-in
                                           (on-press-in event)))
                                       [on-press-in])
        on-press-out! (rn/use-callback (fn [event]
                                         (set-pressed! false)
                                         (when on-press-out
                                           (on-press-out event)))
                                       [on-press-out])]
    [:animated/view {:style (rec.xf/add-styles
                             (if pressed?
                               style/pressable-pressed-state-style
                               style/pressable-default-state-style)
                             container-style)}
     [:rn/pressable (-> props
                        (dissoc :type :size :background :icons :state :disabled? :style :on-press-in :on-press-out :icon-color :container-style :color)
                        (assoc :disabled disabled?
                               :style (rec.xf/add-styles
                                       style/pressable-base-style
                                       (style/container-layout-style size layout)
                                       (style/icon-only-shape-style layout type)
                                       (style/pressable-type-style theme type background color disabled? pressed?)
                                       (:style props))
                               :on-press-in on-press-in!
                               :on-press-out on-press-out!))
      (cond
        icon-only?
        [button-icon {:icon-name (or left-icon right-icon)
                      :type       type
                      :size       size
                      :background background
                      :icon-only? true
                      :icon-color icon-color}]

        (= layout :right)
        [:<>
         [button-text {:type type :size size :background background}
          content]
         [button-icon {:icon-name right-icon
                       :side      :right
                       :type      type
                       :size      size
                       :background background
                       :icon-color icon-color}]]

        (= layout :left)
        [:<>
         [button-icon {:icon-name left-icon
                       :side      :left
                       :type      type
                       :size      size
                       :background background
                       :icon-color icon-color}]
         [button-text {:type type :size size :background background}
          content]]

        (= layout :left-right)
        [:<>
         [button-icon {:icon-name left-icon
                       :side      :left
                       :type      type
                       :size      size
                       :background background
                       :icon-color icon-color}]
         [button-text {:type type :size size :background background}
          content]
         [button-icon {:icon-name right-icon
                       :side      :right
                       :type      type
                       :size      size
                       :background background
                       :icon-color icon-color}]]

        :else
        [button-text {:type type :size size :background background}
         content])]]))
