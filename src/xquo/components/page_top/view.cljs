(ns xquo.components.page-top.view
  (:require [xquo.components.button.view :as button]
            [xquo.components.page-top.style :as style]
            [xquo.components.text.view :as text]))

(defn page-top
  "Page heading. `:button` accepts Xquo button props and renders as a 32px icon-only action."
  [{button-props :button
    :keys [description leading-image title]}]
  [:rn/view {:style style/container}
   [:rn/view {:style style/title-row}
    (when leading-image
      [:rn/image {:source      leading-image
                  :resize-mode :contain
                  :style       style/leading-image}])
    [text/text {:font  :font/semibold-27
                :style style/title}
     title]
    (when button-props
      [:rn/view {:style style/button}
       [button/button (assoc button-props :size 32)]])]
   (when description
     [:rn/view {:style style/description}
      (if (string? description)
        [text/text {:font :font/regular-15}
         description]
        description)])])
