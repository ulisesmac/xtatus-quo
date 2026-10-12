(ns xquo.components.selectors.disclaimer.view
  (:require [react-native.utils :as rn.utils]
            [xquo.components.icon.view :as icon]
            [xquo.components.selectors.disclaimer.style :as style]
            [xquo.components.selectors.selector.view :as selector]
            [xquo.components.text.view :as text]
            [xquo.context :as context]))

(defn disclaimer [{:keys [selected? blur? disabled? on-select icon] :as props} content]
  (let [theme-color (context/use-theme-color)]
    [:rn/view (-> props
                  (dissoc :selected? :blur? :disabled? :icon :on-select)
                  (assoc :style (rn.utils/add-styles style/container (:style props))))
     [:rn/view {:style (style/surface theme-color blur? disabled?)}
      [selector/selector (cond-> {:type      :checkbox
                                  :blur?     blur?
                                  :disabled? disabled?
                                  :on-select on-select
                                  :style     style/selector}
                           (contains? props :selected?) (assoc :selected? selected?))]
      [:rn/view {:style style/text-wrapper}
       (if (string? content)
         [text/text {:font :font/regular-13 :style style/text} content]
         content)]
      (when icon
        [icon/view (assoc icon
                          :size  (:size icon 20)
                          :color (:color icon (style/icon-color theme-color blur?)))])]]))
