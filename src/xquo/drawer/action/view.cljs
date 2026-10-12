(ns xquo.drawer.action.view
  (:require [react-native.react.core :as react]
            [react-native.utils :as rn.utils :refer [style]]
            [xquo.components.button.style :as button-style]
            [xquo.components.icon.view :as icon]
            [xquo.input.text.view :as input]
            [xquo.components.selectors.selector.view :as selector]
            [xquo.components.text.view :as text]
            [xquo.context :as context]
            [xquo.drawer.action.style :as styles]
            [xquo.drawer.style :as drawer-style]
            [xquo.foundations.colors :as colors]))

(defn action [{:keys [title description content action input selected? on-select on-press
                      on-press-in on-press-out color danger? disabled? blur?]
               :as   props}]
  (let [theme-color             (context/use-theme-color)
        icon-color              (if danger? :color/danger color)
        color                   (or icon-color (:color theme-color))
        selected-color          (if blur?
                                  (colors/get-color :color/white-100)
                                  (colors/get-color color 50))
        [pressed? set-pressed!] (react/use-state false)
        press!                  (react/use-callback (fn [event]
                                                      (when on-select (on-select (not selected?)))
                                                      (when on-press (on-press event)))
                                                    [on-select selected? on-press])
        press-in!               (react/use-callback (fn [event]
                                                      (set-pressed! true)
                                                      (when on-press-in (on-press-in event)))
                                                    [on-press-in])
        press-out!              (react/use-callback (fn [event]
                                                      (set-pressed! false)
                                                      (when on-press-out (on-press-out event)))
                                                    [on-press-out])
        input?                  (and (= action :input) selected?)]
    [:rn/pressable (-> props
                       (dissoc :title
                               :description
                               :content
                               :action
                               :input
                               :selected?
                               :on-select
                               :color
                               :danger?
                               :disabled?
                               :blur?)
                       (assoc :disabled     disabled?
                              :on-press     press!
                              :on-press-in  press-in!
                              :on-press-out press-out!
                              :style        (rn.utils/add-styles (styles/container theme-color
                                                                                   color
                                                                                   blur?
                                                                                   (and selected? (not= action :toggle))
                                                                                   pressed?
                                                                                   disabled?
                                                                                   description
                                                                                   input?)
                                                                 (:style props))))
     [:animated/view {:style (if pressed?
                               button-style/pressable-pressed-state-style
                               button-style/pressable-default-state-style)}
      [:rn/view {:style styles/row}
       (case (:type content)
         :icon
         (let [icon-props (:icon content)]
           [icon/view (assoc icon-props
                             :size  (:size icon-props 20)
                             :color (:color icon-props (styles/icon-color theme-color icon-color blur?))
                             :style (rn.utils/add-styles styles/icon (:style icon-props)))])

         :image
         (let [image (:image content)]
           [:rn/image {:source      (or (:source image) {:uri (:url image)})
                       :resize-mode :contain
                       :style       (rn.utils/add-styles styles/image (:style image))}])

         nil)
       [:rn/view {:style styles/text}
        [text/text {:font            :font/medium-15
                    :number-of-lines 1
                    :style           (style {:color (styles/foreground theme-color color blur? danger?)})}
         title]
        (when description
          (if (string? description)
            [text/text {:font            :font/regular-13
                        :number-of-lines 1
                        :style           (style {:color (drawer-style/secondary (:theme theme-color) blur?)})}
             description]
            description))]
       (cond
         (= action :toggle)
         [:rn/view {:pointer-events :none}
          [selector/selector {:type       :toggle
                              :selected?  selected?
                              :blur?      blur?}]]

         selected?
         [icon/view {:name  :icon/check
                     :size  20
                     :color selected-color}]

         (= action :arrow)
         [icon/view {:name  :icon/chevron-right
                     :size  20
                     :color (styles/icon-color theme-color icon-color blur?)}])]
      (when input?
        [input/input (assoc input :style (rn.utils/add-styles styles/input (:style input)))])]]))
