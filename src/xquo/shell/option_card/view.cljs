(ns xquo.shell.option-card.view
  (:require [react-native.react.core :as react]
            [react-native.utils :as rn.utils]
            [xquo.components.button.view :as button]
            [xquo.components.icon.view :as icon]
            [xquo.components.text.view :as text]
            [xquo.foundations.colors :as colors]
            [xquo.shell.option-card.style :as style]))

(defn- small-card [{:keys        [title description icon image right-icon disabled? on-press]
                    caller-style :style}]
  (let [[pressed? set-pressed!] (react/use-state false)
        on-press-in!            (react/use-callback #(set-pressed! true) [])
        on-press-out!           (react/use-callback #(set-pressed! false) [])
        white                   (colors/get-color :color/white-100)]
    [:animated/pressable {:disabled     disabled?
                          :on-press     on-press
                          :on-press-in  on-press-in!
                          :on-press-out on-press-out!
                          :style        (rn.utils/add-styles style/small-container
                                                             caller-style
                                                             (style/state-style (and pressed? (not disabled?)) disabled?))}
     (cond
       image
       [:rn/image {:source      (or (:source image) {:uri (:url image)})
                   :resize-mode :contain
                   :style       (rn.utils/add-styles style/small-image (:style image))}]

       icon
       [icon/view (assoc icon :size (:size icon 32) :color (:color icon white))])
     [:rn/view {:style style/content}
      [text/text {:font :font/semibold-15 :style style/title}
       title]
      (when description
        [text/text {:font :font/regular-13 :style style/description}
         description])]
     (when right-icon
       [icon/view (assoc right-icon :size (:size right-icon 20) :color (:color right-icon white))])]))

(defn- big-card [{:keys [title description image button] caller-style :style}]
  [:rn/view {:style (rn.utils/add-styles style/big-container caller-style)}
   [:rn/view {:style style/top}
    [text/text {:font :font/semibold-15 :style style/title}
     title]
    [text/text {:font :font/regular-13 :style style/description}
     description]]
   [:rn/image {:source      (or (:source image) {:uri (:url image)})
               :resize-mode :contain
               :style       (rn.utils/add-styles style/big-image (:style image))}]
   [:rn/view {:style style/actions}
    [button/button (-> button
                       (dissoc :label :photo?)
                       (assoc :type       (:type button :grey)
                              :size       40
                              :blur?      true
                              :style      (rn.utils/add-styles style/button (:style button))))
     (:label button)]]])

(defn option-card [{:keys [variant] :or {variant :small} :as props}]
  (case variant
    :small [small-card props]
    :big [big-card props]))
