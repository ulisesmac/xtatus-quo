(ns xquo.link-preview.view
  (:require [react-native.react.core :as react]
            [react-native.utils :as rn.utils]
            [xquo.components.button.style :as button-style]
            [xquo.components.favicon.view :as favicon]
            [xquo.components.text.view :as text]
            [xquo.context :as context]
            [xquo.link-preview.style :as style]))

(defn link-preview [{:keys [type title description url favicon image on-press on-press-in on-press-out]
                     :or   {type :default}
                     :as   props}]
  (let [{:keys [theme color dark-theme?]} (context/use-theme-color)
        [pressed? set-pressed!]           (react/use-state false)
        component                         (if on-press :animated/pressable :rn/view)
        press-in!                         (react/use-callback (fn [event]
                                                                (set-pressed! true)
                                                                (when on-press-in (on-press-in event)))
                                                              [on-press-in])
        press-out!                        (react/use-callback (fn [event]
                                                                (set-pressed! false)
                                                                (when on-press-out (on-press-out event)))
                                                              [on-press-out])]
    [component (cond-> props
                 :always (dissoc :type :title :description :url :favicon :image)
                 :always (assoc :style (rn.utils/add-styles (style/container theme (= type :url))
                                                            (when on-press
                                                              (if pressed?
                                                                button-style/pressable-pressed-state-style
                                                                button-style/pressable-default-state-style))
                                                            (:style props)))
                 on-press (assoc :on-press-in  press-in!
                                 :on-press-out press-out!))
     [:rn/view {:pointer-events :none :style (style/border theme)}]
     (if (= type :url)
       [text/text {:font :font/regular-15 :style (style/bare-url color dark-theme?)} url]
       [:rn/view {:style style/content}
        [:rn/view {:style style/main}
         [:rn/view {:style style/title-row}
          (when favicon [favicon/favicon favicon])
          [text/text {:font :font/semibold-15 :number-of-lines 1 :style style/title} title]]
         (when description
           [text/text {:font :font/regular-13 :style style/description} description])]
        [text/text {:font :font/medium-13 :number-of-lines 1 :style (style/url theme)} url]])
     (when (and image (not= type :url))
       [:rn/view {:style (style/media (= type :detail))}
        [:rn/image (-> image
                       (dissoc :url)
                       (assoc :source      (or (:source image) {:uri (:url image)})
                              :resize-mode (:resize-mode image :cover)
                              :style       (rn.utils/add-styles style/image (:style image))))]
        [:rn/view {:pointer-events :none :style style/image-border}]])]))
