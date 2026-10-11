(ns xquo.components.info.info-message.view
  (:require [react-native.utils :as rn.utils]
            [xquo.components.info.info-message.style :as style]
            [xquo.components.icon.view :as icon]
            [xquo.components.text.view :as text]
            [xquo.context :as context]
            [xquo.foundations.colors :as colors]))

(defn info-message [{:keys [status size background color icon]
                     :or   {status :default size 13}
                     :as   props}
                    content]
  (let [theme         (context/use-theme-color)
        size          (get style/legacy-size size size)
        blur?         (:blur? props (= background :blur))
        content-color (if color
                        (colors/get-color color)
                        (style/message-color theme blur? status))]
    [:rn/view (-> props
                  (dissoc :status :size :background :blur? :color :icon)
                  (assoc :style (rn.utils/add-styles (style/container size) (:style props))))
     [:rn/view {:style (style/icon-slot size)}
      [icon/view (assoc icon
                        :name  (:name icon :icon/info-outline)
                        :size  (:size icon (style/icon-size size))
                        :color (:color icon content-color))]]
     [text/text {:font (style/font size) :style (style/text-style size content-color)}
      content]]))
