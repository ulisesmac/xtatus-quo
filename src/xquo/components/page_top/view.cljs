(ns xquo.components.page-top.view
  (:require [xquo.components.page-top.style :as style]
            [xquo.components.text.view :as text]))

(defn page-top [{:keys [description title]}]
  [:rn/view {:style style/container}
   [text/text {:font :font/semibold-27}
    title]
   (when description
     [:rn/view {:style style/description}
      (if (string? description)
        [text/text {:font :font/regular-15}
         description]
        description)])])
