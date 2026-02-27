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

(defn- button-icon [{:keys [icon-name side type size]}]
  (let [theme (context/use-theme)]
    [icon/icon {:icon  icon-name
                :size  (style/icon-size size)
                :color (style/icon-color theme type)
                :style (style/icon-gap-style side)}]))

(defn button [{:keys [type size icons disabled? on-press-in on-press-out]
               :or   {type :primary size 40}
               :as   props}
              content]
  (let [theme         (context/use-theme)
        left-icon     (:left icons)
        right-icon    (:right icons)
        icon-only?    (and (nil? content)
                           (or left-icon right-icon))
        layout        (cond
                        icon-only? :icon-only
                        (and left-icon right-icon) :left-right
                        left-icon :left
                        right-icon :right
                        :else nil)
        [pressed? set-pressed!] (rn/use-state false)
        on-press-in!  (rn/use-callback
                       (fn [event]
                         (set-pressed! true)
                         (when on-press-in
                           (on-press-in event)))
                       [on-press-in])
        on-press-out! (rn/use-callback
                       (fn [event]
                         (set-pressed! false)
                         (when on-press-out
                           (on-press-out event)))
                       [on-press-out])]
    [:animated/view {:style [style/pressable-transition-style
                             (if pressed?
                               style/pressable-transition-in-duration
                               style/pressable-transition-out-duration)
                             (when pressed?
                               style/pressable-pressed-style)]}
     [:rn/pressable (-> props
                        (dissoc :type :size :icons :state :disabled? :style :on-press-in :on-press-out)
                        (assoc :disabled disabled?
                               :style (rec.xf/add-styles
                                       style/pressable-base-style
                                       (style/container-layout-style size layout)
                                       (style/pressable-type-style theme type disabled? pressed?)
                                       (:style props))
                               :on-press-in on-press-in!
                               :on-press-out on-press-out!))
      (if icon-only?
        [button-icon {:icon-name (or left-icon right-icon)
                      :type      type
                      :size      size}]
        (case layout
          :right [:<>
                  [button-text {:type type
                                :size size}
                   content]
                  [button-icon {:icon-name right-icon
                                :side      :right
                                :type      type
                                :size      size}]]
          :left [:<>
                 [button-icon {:icon-name left-icon
                               :side      :left
                               :type      type
                               :size      size}]
                 [button-text {:type type
                               :size size}
                  content]]
          :left-right [:<>
                       [button-icon {:icon-name left-icon
                                     :side      :left
                                     :type      type
                                     :size      size}]
                       [button-text {:type type
                                     :size size}
                        content]
                       [button-icon {:icon-name right-icon
                                     :side      :right
                                     :type      type
                                     :size      size}]]
          [button-text {:type type
                        :size size}
           content]))]]))
