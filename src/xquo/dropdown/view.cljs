(ns xquo.dropdown.view
  (:require [react-native.react.core :as react]
            [react-native.utils :as rn.utils]
            [xquo.components.button.view :as button]
            [xquo.components.icon.view :as icon]
            [xquo.components.text.view :as text]
            [xquo.context :as context]
            [xquo.dropdown.style :as style]))

(defn- use-press [{:keys [on-press-in on-press-out]}]
  (let [[pressed? set-pressed!] (react/use-state false)
        press-in!               (react/use-callback (fn [event]
                                                      (set-pressed! true)
                                                      (when on-press-in (on-press-in event)))
                                                    [on-press-in])
        press-out!              (react/use-callback (fn [event]
                                                      (set-pressed! false)
                                                      (when on-press-out (on-press-out event)))
                                                    [on-press-out])]
    [pressed? press-in! press-out!]))

(defn- leading [{:keys [content size width color]}]
  (let [icon-size (style/icon-size size)
        icon      (:icon content)
        image     (:image content)]
    [:rn/view {:style (style/leading icon-size width)}
     (case (:type content)
       :icon
       [icon/view (assoc icon
                         :size  (:size icon icon-size)
                         :color (:color icon color)
                         :style (rn.utils/add-styles (style/icon-scale icon-size width) (:style icon)))]

       :image
       [:rn/image (-> image
                      (dissoc :url)
                      (assoc :source      (or (:source image) {:uri (:url image)})
                             :resize-mode (:resize-mode image :contain)
                             :style       (rn.utils/add-styles (style/image size) (:style image))))]

       nil)]))

(defn- arrow [{:keys [theme type size active? blur? photo?]}]
  [icon/view {:name  (if active? :icon/pullup :icon/dropdown)
              :size  (style/icon-size size)
              :color (style/arrow-colors theme type blur? photo?)}])

(defn dropdown-button [{:keys [type size content active? disabled? color blur? photo?]
                        :or   {type :outline size 40}
                        :as   props}
                       label]
  (let [theme                           (context/use-theme-color)
        color                           (or color (:color theme))
        [pressed? press-in! press-out!] (use-press props)
        highlighted?                    (and (not disabled?) (or active? pressed?))
        content-only?                   (and content (nil? label))
        leading-width                   (if (= (:type content) :icon)
                                          (style/leading-width theme type size highlighted? disabled? blur? photo?)
                                          (style/icon-size size))]
    [button/button (-> props
                       (dissoc :content :active?)
                       (assoc :type         :ghost
                              :size         size
                              :on-press-in  press-in!
                              :on-press-out press-out!
                              :style        (rn.utils/add-styles (style/button theme
                                                                               color
                                                                               type
                                                                               size
                                                                               content
                                                                               content-only?
                                                                               highlighted?
                                                                               blur?
                                                                               photo?)
                                                                 (:style props))))
     [:rn/view {:pointer-events :none :style style/row}
      (when content
        [leading {:content content
                  :size    size
                  :width   leading-width
                  :color   (style/leading-color theme type blur? photo? false)}])
      (when label
        [text/text {:font            (style/font-type size)
                    :number-of-lines 1
                    :ellipsize-mode  :tail
                    :style           (style/text theme type size)}
         label])
      (when-not content-only?
        [arrow {:theme   theme
                :type    type
                :size    size
                :active? (and active? (not disabled?))
                :blur?   blur?
                :photo?  photo?}])]]))

(defn dropdown-input [{:keys [label content active? disabled? blur?] :as props} value]
  (let [theme                           (context/use-theme-color)
        [pressed? press-in! press-out!] (use-press props)
        highlighted?                    (and (not disabled?) (or active? pressed?))]
    [:rn/view {:style (rn.utils/add-styles style/input-container (:style props))}
     (when label
       [text/text {:font :font/medium-13 :style (style/input-label theme blur?)} label])
     [button/button (-> props
                        (dissoc :label :content :active?)
                        (assoc :type         :ghost
                               :size         40
                               :on-press-in  press-in!
                               :on-press-out press-out!
                               :style        (style/input theme content highlighted? disabled? blur?)))
      [:rn/view {:pointer-events :none :style style/input-row}
       (when content
         [leading {:content content
                   :size    40
                   :width   20
                   :color   (style/leading-color theme nil blur? nil true)}])
       [text/text {:font            :font/regular-15
                   :number-of-lines 1
                   :ellipsize-mode  :tail
                   :style           (rn.utils/add-styles (style/text theme nil 40) style/input-text)}
        value]
       [arrow {:theme   theme
               :size    40
               :active? (and active? (not disabled?))
               :blur?   blur?}]]]]))
