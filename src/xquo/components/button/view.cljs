(ns xquo.components.button.view
  (:require [react-native.utils :as rn.utils]
            [xquo.components.button.style :as style]
            [xquo.components.icon.view :as icon]
            [xquo.components.text.view :as text]
            [xquo.context :as context]
            [react-native.core :as rn]))

(defn- button-text [{:keys [type size background]} content]
  (let [theme (context/use-theme)]
    [text/text {:font            (get style/font-type size)
                :style           (style/text-style theme type background size)
                :number-of-lines 1
                :ellipsize-mode  :middle}
     content]))

(defn- button-content [{:keys [background size type]} content]
  (if (string? content)
    [button-text {:type       type
                  :size       size
                  :background background}
     content]
    content))

(defn- button-icon [{:keys [icon side type size background icon-only? disabled? pressed?]}]
  (let [theme     (context/use-theme)
        gap-style (case side
                    :left style/icon-left-gap
                    :right style/icon-right-gap
                    nil)]
    [icon/view (assoc icon
                      :size  (:size icon (style/icon-size size))
                      :color (:color icon (style/icon-color theme type background icon-only? disabled? pressed?))
                      :style (:style icon gap-style))]))

(defn- layout-type [icon-only? left-icon right-icon]
  (cond
    icon-only?                         :icon-only
    (and (:name left-icon)
         (:name right-icon))           :both
    (:name left-icon)                  :left
    (:name right-icon)                 :right
    :else                              nil))

(defn button [{:keys [color type size photo? blur? disabled? on-press-in on-press-out icon icons]
               :or   {type :primary size 40}
               :as   props}
              content]
  (let [theme                   (context/use-theme)
        context-color           (context/use-color)
        resolved-color          (or color context-color)
        background              (cond photo? :photo blur? :blur)
        icon-name               (:name icon)
        icon-side               (:side icon :right)
        left-icon               (or (:left icons)
                                    (when (= icon-side :left) icon))
        right-icon              (or (:right icons)
                                    (when (= icon-side :right) icon))
        icon-only?              (and (nil? content) icon-name)
        layout                  (layout-type icon-only? left-icon right-icon)
        [pressed? set-pressed!] (rn/use-state false)
        on-press-in!            (rn/use-callback (fn [event]
                                                   (set-pressed! true)
                                                   (when on-press-in
                                                     (on-press-in event)))
                                                 [on-press-in])
        on-press-out!           (rn/use-callback (fn [event]
                                                   (set-pressed! false)
                                                   (when on-press-out
                                                     (on-press-out event)))
                                                 [on-press-out])]
    (assert (not (and photo? blur?)) "Button :photo? and :blur? are alternative surface contexts")
    [(if photo? :animated/effect-pressable :animated/pressable)
     (cond-> (-> props
                 (dissoc :type :size :photo? :blur? :icon :icons :state :disabled?
                         :on-press-in :on-press-out :color
                         :background :glass? :blur-amount :blur-intensity :container-style)
                 (assoc :disabled     disabled?
                        :style        (rn.utils/add-styles (if pressed?
                                                             style/pressable-pressed-state-style
                                                             style/pressable-default-state-style)
                                                           style/pressable-base-style
                                                           (style/container-layout-style size layout type)
                                                           (style/icon-only-shape-style layout type)
                                                           (style/pressable-type-style theme type background resolved-color disabled? pressed?)
                                                           (:style props))
                        :on-press-in  on-press-in!
                        :on-press-out on-press-out!))
       photo? (assoc :effect      :blur
                     :blur-amount 40
                     :theme       theme))
     (cond
       icon-only?
       [button-icon {:icon       (dissoc icon :side)
                     :type       type
                     :size       size
                     :background background
                     :icon-only? true
                     :disabled?  disabled?
                     :pressed?   pressed?}]

       (= layout :both)
       [:<>
        [button-icon {:icon       (dissoc left-icon :side)
                      :side       :left
                      :type       type
                      :size       size
                      :background background
                      :disabled?  disabled?
                      :pressed?   pressed?}]
        [button-content {:type       type
                         :size       size
                         :background background}
         content]
        [button-icon {:icon       (dissoc right-icon :side)
                      :side       :right
                      :type       type
                      :size       size
                      :background background
                      :disabled?  disabled?
                      :pressed?   pressed?}]]

       (= layout :right)
       [:<>
        [button-content {:type       type
                         :size       size
                         :background background}
         content]
        [button-icon {:icon       (dissoc right-icon :side)
                      :side       :right
                      :type       type
                      :size       size
                      :background background
                      :disabled?  disabled?
                      :pressed?   pressed?}]]

       (= layout :left)
       [:<>
        [button-icon {:icon       (dissoc left-icon :side)
                      :side       :left
                      :type       type
                      :size       size
                      :background background
                      :disabled?  disabled?
                      :pressed?   pressed?}]
        [button-content {:type       type
                         :size       size
                         :background background}
         content]]

       :else
       [button-content {:type       type
                        :size       size
                        :background background}
        content])]))
