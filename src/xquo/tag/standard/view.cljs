(ns xquo.tag.standard.view
  (:require [react-native.react.core :as react]
            [react-native.utils :refer [add-styles]]
            [xquo.components.button.style :as button-style]
            [xquo.components.icon.view :as icon]
            [xquo.components.text.view :as text]
            [xquo.context :as context]
            [xquo.tag.standard.style :as style]))

(defn- content-view [{:keys [content size blur?]}]
  (let [theme (context/use-theme)]
    (case (:type content)
      :icon
      [icon/view (assoc (:icon content)
                        :size  (:size (:icon content) (style/media-size size))
                        :color (:color (:icon content) (style/icon-color theme blur?)))]

      :image
      [:rn/image {:source      (or (get-in content [:image :source]) {:uri (get-in content [:image :url])})
                  :resize-mode :contain
                  :style       (add-styles (style/media size) (get-in content [:image :style]))}]

      :emoji
      [:rn/text {:style (style/emoji size)} (:text content)]

      nil)))

(defn tag
  ([props] (tag props nil))
  ([{:keys [size content selected? disabled? blur? on-press]
     :or   {size 32}
     :as   props} label]
   (let [{:keys [color] :as theme-color} (context/use-theme-color)
         [pressed? set-pressed!]         (react/use-state false)
         on-press-in!                    (react/use-callback #(set-pressed! true) [])
         on-press-out!                   (react/use-callback #(set-pressed! false) [])]
     [(if on-press :animated/pressable :rn/view)
      (cond-> {:style (add-styles (style/container size (:type content) label)
                                  (style/state-style theme-color color blur? selected? pressed? disabled?)
                                  (when on-press
                                    (if pressed?
                                      button-style/pressable-pressed-state-style
                                      button-style/pressable-default-state-style))
                                  (:style props))}
        on-press (assoc :on-press     on-press
                        :disabled     disabled?
                        :on-press-in  on-press-in!
                        :on-press-out on-press-out!))
      (when content
        [content-view {:content content :size size :blur? blur?}])
      (when label
        [text/text {:font (style/font size) :number-of-lines 1 :style style/label} label])])))
