(ns xquo.notification.in-app.view
  (:require [react-native.utils :as rn.utils :refer [style]]
            [xquo.components.avatar.view :as avatar]
            [xquo.components.button.view :as button]
            [xquo.components.icon.view :as icon]
            [xquo.components.tags.context-tag.view :as context-tag]
            [xquo.components.text.view :as text]
            [xquo.context :as context]
            [xquo.notification.in-app.style :as styles]
            [xquo.notification.style :as notification-style]))

(defn- leading [{:keys [content theme]}]
  (case (:type content)
    :avatar (let [props (:avatar content)]
              [avatar/avatar (assoc props :size (:size props 32))])
    :icon [:rn/view {:style (styles/icon-background theme)}
           [icon/view (assoc (:icon content)
                             :size  (:size (:icon content) 20)
                             :color (:color (:icon content) (styles/icon-color theme)))]]
    nil))

(defn- attachment-view [{:keys [type image images]}]
  (into [:rn/view {:style (styles/attachment type)}]
        (map (fn [image]
               [:rn/image (-> image
                              (dissoc :url)
                              (assoc :source      (or (:source image) {:uri (:url image)})
                                     :resize-mode (if (= type :sticker) :contain :cover)
                                     :style       (rn.utils/add-styles (styles/image type) (:style image))))]))
        (if (= type :photos) images [image])))

(defn in-app [{:keys [title description content attachment context-tag buttons on-press]
               :as   props}]
  (let [{:keys [dark-theme?] :as theme} (context/use-theme-color)
        surface-theme                   (if dark-theme? :theme/light :theme/dark)
        footer                          (or context-tag (seq buttons))
        details                         (or description attachment footer)]
    [context/provider {:theme surface-theme}
     [(if on-press :rn/pressable :rn/view)
      (-> props
          (dissoc :title :description :content :attachment :context-tag :buttons)
          (assoc :style (rn.utils/add-styles (notification-style/frame 16 8) (:style props))))
      [:effect/view {:pointer-events :none
                     :effect         :blur
                     :blur-amount    40
                     :theme          surface-theme
                     :style          (notification-style/backdrop theme 16)}]
      [:rn/view {:style (styles/row description attachment footer)}
       (when content
         [:rn/view {:style (styles/leading details)}
          [leading {:content content :theme theme}]])
       [:rn/view {:style styles/content}
        [text/text {:font  (if details :font/semibold-15 :font/medium-15)
                    :style (style {:color (notification-style/foreground theme)})}
         title]
        (when description
          (if (string? description)
            [text/text {:font  :font/regular-13
                        :style (style {:color (notification-style/foreground theme)})}
             description]
            description))
        (when attachment
          [attachment-view attachment])
        (when context-tag
          [:rn/view {:style styles/footer}
           [context-tag/context-tag (-> context-tag
                                        (dissoc :label)
                                        (assoc :size  (:size context-tag 24)
                                               :blur? (:blur? context-tag true)))
            (:label context-tag)]])
        (when (seq buttons)
          (into [:rn/view {:style styles/footer}]
                (map (fn [action]
                       [button/button (-> action
                                          (dissoc :label)
                                          (assoc :size  (:size action 24)
                                                 :blur? (:blur? action true)))
                        (:label action)]))
                buttons))]]]]))
