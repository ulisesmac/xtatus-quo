(ns xquo.community.stat.view
  (:require [react-native.utils :refer [add-styles]]
            [xquo.community.stat.style :as style]
            [xquo.components.icon.view :as icon]
            [xquo.components.text.view :as text]
            [xquo.context :as context]
            [xquo.foundations.colors :as colors]))

(defn stat [{:keys [icon] :as props} value]
  (let [theme (context/use-theme)]
    [:rn/view {:style (add-styles style/container (:style props))}
     [icon/view (assoc icon
                       :name  (:name icon :icon/users)
                       :size  (:size icon 16)
                       :color (:color icon (colors/themed theme :color/neutral-50 :color/neutral-40)))]
     [text/text {:font :font/regular-15 :style style/value} value]]))
