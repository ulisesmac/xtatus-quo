(ns xquo.components.title-input.view
  (:require [xquo.components.icon.view :as icon]
            [xquo.components.text.view :as text]
            [xquo.components.title-input.style :as style]
            [xquo.context :as context]
            [xquo.foundations.colors :as colors]
            [react-native.core :as rn]))

(defn title-input [{:keys [blur? counter-value default-value disabled? max-chars
                           on-blur on-change-text on-focus value]
                    :as   props}]
  (let [{:keys [color dark-theme?]} (context/use-theme-color)
        counter-value-fn            (or counter-value identity)
        controlled?                 (contains? props :value)
        [focused? set-focused!]     (rn/use-state false)
        [internal-value
         set-internal-value!] (rn/use-state (or default-value ""))
        current-value   (if controlled? (or value "") internal-value)
        current-count   (count (counter-value-fn current-value))
        selection-color (colors/get-color color)
        on-change-text! (rn/use-callback (fn [next-value]
                                           (when-not controlled?
                                             (set-internal-value! next-value))
                                           (when on-change-text
                                             (on-change-text next-value)))
                                         [controlled? on-change-text])
        on-focus!       (rn/use-callback (fn [event]
                                           (set-focused! true)
                                           (when on-focus
                                             (on-focus event)))
                                         [on-focus])
        on-blur!        (rn/use-callback (fn [event]
                                           (set-focused! false)
                                           (when on-blur
                                             (on-blur event)))
                                         [on-blur])]
    [:rn/view {:style [style/container
                       (when disabled? style/container-disabled)]}
     [:rn/text-input (cond-> (assoc (dissoc props :blur? :default-value :disabled?
                                             :counter-value :max-chars :on-blur
                                             :on-change-text :on-focus :value)
                                     :cursor-color selection-color
                                     :on-blur on-blur!
                                     :on-change-text on-change-text!
                                     :on-focus on-focus!
                                     :placeholder-text-color (style/placeholder-color dark-theme? blur?)
                                     :selection-color selection-color
                                     :underline-color-android :transparent
                                     :value current-value
                                     :style [style/text-input
                                             (style/text-input-color-style dark-theme?)])
                       (not counter-value) (assoc :max-length max-chars))]
     (if focused?
       [:rn/view {:style style/counter-container}
        [text/text {:font  :font/regular-13
                    :style (style/counter-text-style dark-theme? blur?)}
         (str current-count "/" max-chars)]]
       [:rn/view {:style style/edit-icon-container}
        [icon/view {:name  :icon/edit
                    :size  20
                    :color (style/edit-icon-color dark-theme? blur?)}]])]))
