(ns xquo.components.favicon.view
  (:require [react-native.utils :as rn.utils]
            [xquo.components.favicon.style :as style]
            [xquo.components.text.view :as text]
            [xquo.context :as context]))

(defn favicon [{:keys [content blur?] :as props}]
  (let [theme  (context/use-theme)
        image  (:image content)
        image? (= (:type content) :image)]
    [:rn/view (-> props
                  (dissoc :content :blur?)
                  (assoc :style (rn.utils/add-styles style/container
                                                     (when-not image? (style/background theme blur?))
                                                     (:style props))))
     (if image?
       [:rn/image {:source      (or (:source image) {:uri (:url image)})
                   :resize-mode :cover
                   :style       (rn.utils/add-styles style/image (:style image))}]
       [text/text {:font            :font/medium-11
                   :number-of-lines 1
                   :style           [style/initials (style/foreground theme blur?)]}
        (:text content)])
     [:rn/view {:pointer-events :none :style (style/border theme image?)}]]))
