(ns xquo.components.text.page-top.view
  (:require [react-native.utils :as rn.utils]
            [xquo.components.avatar.view :as avatar]
            [xquo.components.icon.view :as icon]
            [xquo.components.text.combination.view :as combination]
            [xquo.components.text.page-top.style :as style]
            [xquo.components.text.view :as text]
            [xquo.context :as context]
            [xquo.input.search.view :as search]))

(defn- leading [{:keys [content blur?]}]
  (case (:type content)
    :avatar
    [avatar/avatar (assoc (:avatar content)
                          :size  (get-in content [:avatar :size] 32)
                          :blur? (get-in content [:avatar :blur?] blur?))]

    :icon
    [icon/view (assoc (:icon content) :size (get-in content [:icon :size] 32))]

    :image
    (let [{:keys [source url] :as props} (:image content)]
      [:rn/image (-> props
                     (dissoc :url)
                     (assoc :source      (or source {:uri url})
                            :resize-mode (:resize-mode props :contain)
                            :style       (rn.utils/add-styles style/image (:style props))))])

    nil))

(defn page-top [{:keys [title title-props content description description-props description-spacing
                        caption caption-props icons tag right type blur? search footer footer-style
                        button leading-image]
                :or   {description-spacing 8}
                :as   props}]
  (let [theme   (context/use-theme)
        content (or content (when leading-image {:type :image :image {:source leading-image}}))
        right   (or right (when button {:type :button :button button}))]
    [:rn/view (-> props
                  (dissoc :title :title-props :content :description :description-props :description-spacing
                          :caption :caption-props :icons :tag :right :type :blur? :search :footer :footer-style
                          :button :leading-image)
                  (assoc :style (rn.utils/add-styles style/root (:style props))))
     [:rn/view {:style (style/content description-spacing)}
      [:rn/view {:style style/title-row}
       (when content
         [leading {:content content :blur? blur?}])
       [combination/title {:title         title
                           :title-props   title-props
                           :caption       caption
                           :caption-props caption-props
                           :icons         icons
                           :tag           tag
                           :right         right
                           :type          type
                           :blur?         blur?
                           :style         style/title}]]
      (when description
        (if (string? description)
          [text/text (assoc description-props
                            :font  (:font description-props :font/regular-15)
                            :style (rn.utils/add-styles style/description (:style description-props)))
           description]
          description))]
     (when (or search footer)
       [:rn/view {:style (rn.utils/add-styles style/footer footer-style)}
        (if search
          [search/search-input (assoc search :blur? (:blur? search blur?))]
          footer)])
     (when (or search footer)
       [:rn/view {:pointer-events :none :style (style/border theme blur?)}])]))
