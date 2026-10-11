(ns xquo.drawer.top.view
  (:require [react-native.utils :as rn.utils :refer [style]]
            [xquo.components.avatar.view :as avatar]
            [xquo.components.button.view :as button]
            [xquo.components.counter.view :as counter]
            [xquo.components.icon.view :as icon]
            [xquo.components.tags.context-tag.view :as context-tag]
            [xquo.components.text.view :as text]
            [xquo.context :as context]
            [xquo.drawer.style :as drawer-style]
            [xquo.drawer.top.style :as styles]))

(defn- leading [{:keys [content blur?]}]
  (case (:type content)
    :icon [avatar/avatar {:size    32
                          :color   :color/neutral
                          :border  :solid
                          :blur?   blur?
                          :content content}]
    :avatar [avatar/avatar (assoc (:avatar content)
                                  :size  (:size (:avatar content) 32)
                                  :blur? (:blur? (:avatar content) blur?))]
    :image (let [image (:image content)]
             [:rn/image {:source      (or (:source image) {:uri (:url image)})
                         :resize-mode :contain
                         :style       (rn.utils/add-styles styles/image (:style image))}])
    nil))

(defn- tag [{:keys [item blur?]}]
  (if (string? item)
    [text/text {:font :font/regular-13} item]
    [context-tag/context-tag (-> item
                                 (dissoc :label)
                                 (assoc :blur? (:blur? item blur?)))
     (:label item)]))

(defn top [{:keys [title description content title-icon description-icon trailing-icon
                   button counter context-tags compact? blur?]
            :as   props}]
  (let [theme (context/use-theme)]
    [:rn/view (-> props
                  (dissoc :title
                          :description
                          :content
                          :title-icon
                          :description-icon
                          :trailing-icon
                          :button
                          :counter
                          :context-tags
                          :compact?
                          :blur?)
                  (assoc :style (rn.utils/add-styles (styles/container compact?) (:style props))))
     [:rn/view {:style styles/leading-row}
      (when content
        [leading {:content content :blur? blur?}])
      [:rn/view {:style styles/column}
       [:rn/view {:style styles/title-row}
        [:rn/view {:style styles/title}
         (if (string? title)
           [text/text {:font :font/semibold-19 :number-of-lines 1} title]
           title)
         (when title-icon
           [icon/view (assoc title-icon :size (:size title-icon 20))])]
        (cond
          button
          [button/button (-> button
                             (dissoc :label)
                             (assoc :size  (:size button 24)
                                    :blur? (:blur? button (and blur? (not (:photo? button))))))
           (:label button)]

          trailing-icon
          [icon/view (assoc trailing-icon :size (:size trailing-icon 20))]

          (map? counter)
          [counter/counter (-> counter
                               (dissoc :value)
                               (assoc :blur? (:blur? counter blur?)))
           (:value counter)]

          counter
          [text/text {:font :font/regular-13 :style (styles/counter theme blur?)} counter])]
       (when (seq context-tags)
         (into [:rn/view {:style styles/tags}]
               (map (fn [item]
                      [tag {:item item :blur? blur?}]))
               context-tags))
       (when description
         [:rn/view {:style (rn.utils/add-styles styles/description-row
                                                (styles/description content (seq context-tags)))}
          (if (string? description)
            [text/text {:font  (if content :font/monospace-13 :font/regular-15)
                        :style (style {:color (drawer-style/secondary theme blur?)})}
             description]
            description)
          (when description-icon
            [icon/view (assoc description-icon
                              :size  (:size description-icon 16)
                              :color (:color description-icon (drawer-style/secondary theme blur?)))])])]]]))
