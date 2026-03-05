(ns xquo.components.selectors.filter.view
  (:refer-clojure :exclude [filter])
  (:require [reagent-extended-compiler.utils.transforms :as rec.xf]
            [xquo.components.button.style :as button.style]
            [xquo.components.icon.view :as icon]
            [xquo.components.selectors.filter.style :as style]
            [xquo.components.text.view :as text]
            [xquo.context :as context]
            [xquo.react-native :as rn]))

(defn filter
  "Filter selector.

  API:
  - `props` map
    - `:selected?` optional boolean (default `false`)
    - `:size` one of `24` or `32` (default `32`)
    - `:background` one of `:none` or `:blur` (default `:none`)
    - `:icon` optional icon keyword (`:icon/...`)
    - `:style` optional caller style (map/vector/js style)
    - Any additional keys are forwarded to `:rn/pressable` (for example
      `:on-press`, `:accessibility-label`, `:testID`)
  - `content` optional label. When omitted, the component renders icon-only.

  State is externally controlled through `:selected?`."
  [{:keys [selected? size background icon on-press-in on-press-out]
    :or   {size       32
           background :none}
    :as   props}
   content]
  (let [theme                  (context/use-theme)
        text?                  (some? content)
        label-color            (style/text-color theme selected?)
        [pressed? set-pressed!] (rn/use-state false)
        on-press-in!           (rn/use-callback
                                (fn [event]
                                  (set-pressed! true)
                                  (when on-press-in
                                    (on-press-in event)))
                                [on-press-in])
        on-press-out!          (rn/use-callback
                                (fn [event]
                                  (set-pressed! false)
                                  (when on-press-out
                                    (on-press-out event)))
                                [on-press-out])]
    [:animated/view {:style (if pressed?
                              button.style/pressable-pressed-state-style
                              button.style/pressable-default-state-style)}
     [:rn/pressable (-> props
                        (dissoc :selected? :size :background :icon :style :hit-slop :on-press-in :on-press-out)
                        (assoc :hit-slop     6
                               :on-press-in  on-press-in!
                               :on-press-out on-press-out!
                               :style        (rec.xf/add-styles
                                              style/container-base
                                              (style/container-size-style size text?)
                                              (style/surface-color-style theme background selected?)
                                              (:style props))))
      (if (= size 24)
        [icon/icon {:icon  (or icon :icon/placeholder)
                    :size  12
                    :color (style/icon-color theme size selected?)}]
        [icon/icon {:icon  (or icon :icon/unread)
                    :size  20
                    :color (style/icon-color theme size selected?)}])
      (when text?
        [text/text {:font  (style/label-font size)
                    :style {:color label-color}}
         content])]]))
