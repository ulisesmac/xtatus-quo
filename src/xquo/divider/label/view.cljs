(ns xquo.divider.label.view
  (:require [react-native.utils :as rn.utils]
            [xquo.components.counter.view :as counter]
            [xquo.divider.label.style :as style]
            [xquo.components.icon.view :as icon]
            [xquo.components.text.view :as text]
            [xquo.context :as context]
            [xquo.foundations.animations :as animations]
            [react-native.core :as rn]))

(defn- chevron-view [{:keys [theme blur? chevron-icon open? toggle-duration toggle-timing-function]}]
  [:animated/view {:style (style/chevron-state open? toggle-duration toggle-timing-function)}
   [icon/view (assoc chevron-icon
                     :name  (:name chevron-icon :icon/chevron-down)
                     :size  (:size chevron-icon 20)
                     :color (:color chevron-icon (style/icon-color theme blur?)))]])

(defn- right-view [{:keys [blur? counter right]}]
  [:rn/view {:style (if right style/right-slot style/counter-slot)}
   (if right
     right
     [counter/counter {:type  :grey
                       :blur? blur?}
      counter])])

(defn- content-view [{:keys [theme blur? chevron-icon chevron-side counter open? right title
                             toggle-duration toggle-timing-function]}]
  [:<>
   (when (= chevron-side :left)
     [:rn/view {:style style/left-chevron-slot}
      [chevron-view {:theme                  theme
                     :blur?                  blur?
                     :chevron-icon           chevron-icon
                     :open?                  open?
                     :toggle-duration        toggle-duration
                     :toggle-timing-function toggle-timing-function}]])
   [:rn/view {:style style/title-slot}
    [text/text {:font            :font/medium-13
                :number-of-lines 1
                :style           [(style/title-color theme blur?)
                                  (when (string? title) style/title-text)]}
     title]]
   (when (= chevron-side :right)
     [:rn/view {:style style/right-chevron-slot}
      [chevron-view {:theme                  theme
                     :blur?                  blur?
                     :chevron-icon           chevron-icon
                     :open?                  open?
                     :toggle-duration        toggle-duration
                     :toggle-timing-function toggle-timing-function}]])
   (when (or right counter)
     [right-view {:blur?   blur?
                  :counter counter
                  :right   right}])])

(defn divider-label
  [{controlled-open? :open?
    :keys            [title initial-open? compact? collapsible? divider-line? blur? chevron
                      chevron-icon counter right on-press on-press-in on-press-out disabled?
                      toggle-duration toggle-timing-function]
    :or              {compact?               true
                      collapsible?           true
                      divider-line?          true
                      toggle-duration        (:toggle-duration animations/state-change)
                      toggle-timing-function (:transition-timing-function animations/state-change)}
    :as              props}]
  (let [theme                   (context/use-theme)
        chevron-side            (if counter :left chevron)
        pressable               (or on-press (and collapsible? chevron-side))
        [local-open? set-open!] (rn/use-state initial-open?)
        [pressed? set-pressed!] (rn/use-state false)
        controlled?             (some? controlled-open?)
        open?                   (if controlled? controlled-open? local-open?)
        on-press!               (rn/use-callback (fn [event]
                                                   (when (and collapsible? (not controlled?))
                                                     (set-open! not))
                                                   (when on-press
                                                     (on-press event)))
                                                 [collapsible? controlled? on-press])
        on-press-in!            (rn/use-callback (fn [event]
                                                   (set-pressed! true)
                                                   (when on-press-in
                                                     (on-press-in event)))
                                                 [on-press-in])
        on-press-out!           (rn/use-callback (fn [event]
                                                   (set-pressed! false)
                                                   (when on-press-out
                                                     (on-press-out event)))
                                                 [on-press-out])
        root-component          (if pressable :rn/pressable :rn/view)]
    [root-component (-> props
                        (dissoc :title :initial-open? :compact? :collapsible? :divider-line? :blur?
                                :chevron :chevron-icon :counter :right :style :on-press :on-press-in
                                :on-press-out :disabled? :open? :toggle-duration :toggle-timing-function
                                :layout :entering :exiting)
                        (assoc :style (rn.utils/add-styles style/container-base
                                                           (when compact? style/compact-container)
                                                           (when (= chevron-side :left) style/left-chevron-container)
                                                           (:style props)))
                        (cond-> pressable (assoc :disabled     disabled?
                                                 :on-press     on-press!
                                                 :on-press-in  on-press-in!
                                                 :on-press-out on-press-out!)))
     (when divider-line?
       [:rn/view {:style [style/top-line (style/border-color theme blur?)]}])
     (when pressable
       [:animated/view {:pointer-events :none
                        :style          [style/overlay-base
                                         (style/overlay-color theme blur?)
                                         (style/overlay-state pressed?)]}])
     [content-view {:theme                  theme
                    :blur?                  blur?
                    :chevron-icon           chevron-icon
                    :chevron-side           chevron-side
                    :counter                counter
                    :open?                  open?
                    :right                  right
                    :title                  title
                    :toggle-duration        toggle-duration
                    :toggle-timing-function toggle-timing-function}]]))
