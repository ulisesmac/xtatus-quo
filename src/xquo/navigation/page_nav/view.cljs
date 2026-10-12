(ns xquo.navigation.page-nav.view
  (:require [applied-science.js-interop :as j]
            [react-native.react.core :as react]
            [react-native.utils :as rn.utils]
            [xquo.components.avatar.view :as avatar]
            [xquo.components.button.view :as button]
            [xquo.components.icon.view :as icon]
            [xquo.components.text.view :as text]
            [xquo.context :as context]
            [xquo.foundations.typography :as typography]
            [xquo.navigation.page-nav.style :as style]))

(defn- action [{:keys [entry blur? photo?]}]
  (let [{:keys [dark-theme?]} (context/use-theme-color)
        default-type          (if (and dark-theme? (not blur?) (not photo?)) :dark-grey :grey)]
    (if (map? entry)
      (let [{:keys [label]} entry
            photo?         (:photo? entry photo?)
            blur?          (:blur? entry (and blur? (not photo?)))]
        [button/button (-> entry
                           (dissoc :label)
                           (assoc :type   (:type entry default-type)
                                  :size   (:size entry 32)
                                  :blur?  blur?
                                  :photo? photo?))
         label])
      entry)))

(defn- leading [{:keys [content size blur?]}]
  (let [theme (context/use-theme)]
    (case (:type content)
      :avatar
      [avatar/avatar (assoc (:avatar content)
                            :size  size
                            :blur? (:blur? (:avatar content) blur?))]

      :image
      (let [{:keys [source url] :as props} (:image content)]
        [:rn/image (-> props
                       (dissoc :url :size)
                       (assoc :source      (or source {:uri url})
                              :resize-mode (:resize-mode props :contain)
                              :style       (rn.utils/add-styles (style/image size) (:style props))))])

      :icon
      [icon/view (assoc (:icon content)
                        :size  size
                        :color (:color (:icon content) (style/title-color theme nil)))]

      nil)))

(defn- identity-view [{:keys [blur? photo?]
                       {:keys [title description caption content icons on-press on-press-in
                               on-press-out disabled?]
                        center-style :style} :center}]
  (let [theme                   (context/use-theme)
        [pressed? set-pressed!] (react/use-state false)
        pressed?                (and on-press pressed? (not disabled?))
        component               (if on-press :rn/pressable :rn/view)
        size                    (case (:type content)
                                  :avatar (get-in content [:avatar :size] 32)
                                  :image  (get-in content [:image :size] 24)
                                  :icon   (get-in content [:icon :size] 20)
                                  0)
        font                    (if (or description caption content (seq icons)) :font/semibold-15 :font/medium-15)
        press-in!               (react/use-callback (fn [event]
                                                      (set-pressed! true)
                                                      (when on-press-in (on-press-in event)))
                                                    [on-press-in])
        press-out!              (react/use-callback (fn [event]
                                                      (set-pressed! false)
                                                      (when on-press-out (on-press-out event)))
                                                    [on-press-out])]
    [component (cond-> {:style (rn.utils/add-styles (style/content size disabled?) center-style)}
                 on-press (assoc :on-press     on-press
                                 :on-press-in  press-in!
                                 :on-press-out press-out!
                                 :disabled     disabled?))
     (when content
       [leading {:content content :size size :blur? blur?}])
     [:rn/view {:style style/text-column}
      [:rn/view {:style style/title-row}
       (if (string? title)
         [:animated/text {:number-of-lines 1
                          :ellipsize-mode  :tail
                          :style           [(typography/get-style font)
                                            (style/text (style/title-color theme pressed?) 22 pressed?)]}
          title]
         title)
       (when caption
         [text/text {:font            :font/medium-13
                     :number-of-lines 1
                     :style           (style/text (style/secondary theme blur? photo?) 18 nil)}
          caption])
       (when (seq icons)
         (into [:rn/view {:style style/icons}]
               (map (fn [props]
                      [icon/view (assoc props
                                        :size  (:size props 16)
                                        :color (:color props (style/icon-color theme blur?)))]))
               icons))]
      (when description
        (if (string? description)
          [text/text {:font            :font/medium-13
                      :number-of-lines 1
                      :ellipsize-mode  :tail
                      :style           (style/text (style/secondary theme blur? photo?) 18 nil)}
           description]
          description))]]))

(defn page-nav [{:keys [left center right blur? photo?] :as props} & children]
  (let [centered?                      (or (vector? center) (= (:text-align center) :center))
        [left-width set-left-width!]   (react/use-state 32)
        [right-width set-right-width!] (react/use-state 32)
        side-width                     (when centered? (max (if left left-width 0)
                                                            (if (seq right) right-width 0)))
        measure-left!                  (react/use-callback #(set-left-width! (j/get-in % [:nativeEvent :layout :width])) [])
        measure-right!                 (react/use-callback #(set-right-width! (j/get-in % [:nativeEvent :layout :width])) [])]
    (into [:rn/view (-> props
                        (dissoc :left :center :right :blur? :photo?)
                        (assoc :style (rn.utils/add-styles style/container (:style props))))
           [:rn/view {:style style/row}
            (when (or left (and centered? (seq right)))
              [:rn/view {:style (style/side side-width nil)}
               [:rn/view {:on-layout (when centered? measure-left!) :style style/actions}
                (when left
                  [action {:entry left :blur? blur? :photo? photo?}])]])
            [:rn/view {:style (style/center centered?)}
             (if (map? center)
               [identity-view {:center center :blur? blur? :photo? photo?}]
               center)]
            (when (or (seq right) (and centered? left))
              [:rn/view {:style (style/side side-width true)}
               (into [:rn/view {:on-layout (when centered? measure-right!) :style style/actions}]
                     (map (fn [entry]
                            [action {:entry entry :blur? blur? :photo? photo?}]))
                     right)])]]
          children)))
