(ns xquo.components.segmented-tab.view
  (:require [react-native.utils :as rn.utils]
            [xquo.components.icon.view :as icon]
            [xquo.components.segmented-tab.style :as style]
            [xquo.components.text.view :as text]
            [xquo.context :as context]))

(defn segmented-tab [{:keys [type size selected? disabled? blur? symbol label-style]
                      :or   {type :grey size 28}
                      :as   props}
                     label]
  (let [{:keys [theme] :as theme-color} (context/use-theme-color)]
    [:rn/pressable (-> props
                       (dissoc :type :size :selected? :disabled? :blur? :symbol :label-style)
                       (assoc :disabled disabled?
                              :style    (rn.utils/add-styles (style/container theme-color type size selected? disabled? blur? symbol label)
                                                             (:style props))))
     (case (:type symbol)
       :icon
       (let [props (:icon symbol)]
         [icon/view (assoc props
                           :size  (:size props (if label 16 (style/icon-only-size size)))
                           :color (:color props (style/icon-color theme selected? blur?)))])

       :image
       (let [image (:image symbol)]
         [:rn/image {:source      (or (:source image) {:uri (:url image)})
                     :resize-mode :contain
                     :style       (rn.utils/add-styles (style/image size) (:style image))}])

       :emoji
       [:rn/text {:style (style/emoji size)} (:text symbol)]

       nil)
     (when label
       [text/text {:font            (style/font size)
                   :number-of-lines 1
                   :style           (rn.utils/add-styles (style/label theme selected? size) label-style)}
        label])]))
