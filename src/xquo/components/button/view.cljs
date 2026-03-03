(ns xquo.components.button.view
  (:require [reagent-extended-compiler.utils.transforms :as rec.xf]
            [xquo.components.button.style :as style]
            [xquo.components.icon.view :as icon]
            [xquo.components.text.view :as text]
            [xquo.context :as context]
            [xquo.react-native :as rn]))

(defn- button-text [{:keys [type size]} content]
  (let [theme (context/use-theme)]
    [text/text {:font  (get style/font-type size)
                :style (style/text-style theme type)}
     content]))

(defn- button-icon [{:keys [icon-name side type size icon-color]}]
  (let [theme (context/use-theme)]
    [icon/icon {:icon  icon-name
                :size  (style/icon-size size)
                :color (or icon-color
                           (style/icon-color theme type))
                :style (style/icon-gap-style side)}]))

(defn- layout-type [icon-only? left-icon right-icon]
  (cond
    icon-only?                 :icon-only
    (and left-icon right-icon) :left-right
    left-icon                  :left
    right-icon                 :right
    :else                      nil))

(defn button [{:keys               [type size disabled? on-press-in on-press-out icon-color]
               {left-icon  :left
                right-icon :right} :icons
               :or                 {type :primary
                                    size 40}
               :as                 props}
              content]
  (let [theme         (context/use-theme)
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
    [:animated/view {:style (if pressed?
                              style/pressable-pressed-state-style
                              style/pressable-default-state-style)}
     [:rn/pressable (-> props
                        (dissoc :type :size :icons :state :disabled? :style :on-press-in :on-press-out :icon-color)
                        (assoc :disabled disabled?
                               :style (rec.xf/add-styles
                                       style/pressable-base-style
                                       (style/container-layout-style size layout)
                                       (style/pressable-type-style theme type disabled? pressed?)
                                       (:style props))
                               :on-press-in on-press-in!
                               :on-press-out on-press-out!))
      (cond
        icon-only?
        [button-icon {:icon-name (or left-icon right-icon)
                      :type      type
                      :size      size
                      :icon-color icon-color}]

        (= layout :right)
        [:<>
         [button-text {:type type :size size}
          content]
         [button-icon {:icon-name right-icon
                       :side      :right
                       :type      type
                       :size      size
                       :icon-color icon-color}]]

        (= layout :left)
        [:<>
         [button-icon {:icon-name left-icon
                       :side      :left
                       :type      type
                       :size      size
                       :icon-color icon-color}]
         [button-text {:type type :size size}
          content]]

        (= layout :left-right)
        [:<>
         [button-icon {:icon-name left-icon
                       :side      :left
                       :type      type
                       :size      size
                       :icon-color icon-color}]
         [button-text {:type type :size size}
          content]
         [button-icon {:icon-name right-icon
                       :side      :right
                       :type      type
                       :size      size
                       :icon-color icon-color}]]

        :else
        [button-text {:type type :size size}
         content])]]))
