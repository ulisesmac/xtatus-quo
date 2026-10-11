(ns xquo.tag.permissions.view
  (:require [react-native.utils :as rn.utils]
            [xquo.components.icon.view :as icon]
            [xquo.components.text.view :as text]
            [xquo.context :as context]
            [xquo.tag.permissions.style :as style]))

(def ^:private clipped-token-path
  {24 "M17.34375 3.21256A10 10 0 1 0 17.34375 16.78744A11 11 0 0 1 17.34375 3.21256Z"
   32 "M22.5 2.87570A14 14 0 1 0 22.5 25.12430A16 16 0 0 1 22.5 2.87570Z"})

(def ^:private more-icon-size
  {24 12
   32 20})

(defn- token-image [{:keys [image size last?]}]
  (let [diameter (style/token-size size)
        radius   (/ diameter 2)]
    [:rn/view {:style (style/slot size last?)}
     [:svg/svg {:width diameter :height diameter :view-box (str "0 0 " diameter " " diameter)}
      [:svg/defs
       [:svg/clip-path {:id "permissions-token"}
        (if last?
          [:svg/circle {:cx radius :cy radius :r radius}]
          [:svg/path {:d (clipped-token-path size)}])]]
      [:svg/image {:href                  (or (:source image) {:uri (:url image)})
                   :width                 diameter
                   :height                diameter
                   :preserve-aspect-ratio "xMidYMid slice"
                   :style                 (:style image)
                   :clip-path             "url(#permissions-token)"}]]]))

(defn- token-group [{:keys [group size foreground background]}]
  (let [{:keys [tokens total]} group
        total                  (or total (count tokens))
        overflow?              (> total 3)
        displayed              (vec (take (if overflow? 2 3) tokens))]
    [:rn/view {:style style/tokens}
     (for [[index image] (map-indexed vector displayed)]
       ^{:key index}
       [token-image {:image image
                     :size  size
                     :last? (and (not overflow?) (= index (dec (count displayed))))}])
     (when overflow?
       [:rn/view {:style (style/count-slot size background)}
        [text/text {:font (style/label-font size) :style (style/count-text size foreground)}
         (str "+" (- total (count displayed)))]])]))

(defn permissions-tag [{:keys [groups size locked? blur? more-groups? separator-label background-color]
                        :or   {size 24 separator-label "or"}
                        :as   props}]
  (let [dark-theme?      (:dark-theme? (context/use-theme-color))
        background       (style/background-color dark-theme? blur? background-color)
        foreground       (style/foreground dark-theme? blur?)
        count-color      (style/count-color size dark-theme? blur?)
        count-background (style/count-background dark-theme? blur? background-color)]
    [:rn/view (-> props
                  (dissoc :groups :size :locked? :blur? :more-groups? :separator-label :background-color :style)
                  (assoc :style (rn.utils/add-styles style/container
                                                     (style/container-style size background)
                                                     (:style props))))
     [icon/view {:name  (if locked? :icon/lock :icon/lock-open)
                 :size  (style/icon-size size)
                 :color foreground}]
     (for [[index group] (map-indexed vector groups)]
       ^{:key index}
       [:<>
        (when (pos? index)
          [text/text {:font (style/label-font size) :style (style/separator size foreground)}
           separator-label])
        [token-group {:group      group
                      :size       size
                      :foreground count-color
                      :background count-background}]])
     (when more-groups?
       [:<>
        [text/text {:font (style/label-font size) :style (style/separator size foreground)}
         separator-label]
        [:rn/view {:style (style/count-slot size count-background)}
         [icon/view {:name :icon/options :size (more-icon-size size) :color foreground}]]])]))
