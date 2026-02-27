(ns xquo.components.button.view
  (:require [reagent-extended-compiler.utils.transforms :as rec.xf]
            [xquo.components.button.style :as style]
            [xquo.components.text.view :as text]
            [xquo.context :as context]
            [xquo.react-native :as rn]))

(defn- button-text [{:keys [type size]} content]
  (let [theme (context/use-theme)]
    [text/text {:font  (get style/font-type size)
                :style (style/text-style theme type)}
     content]))

(defn- button-icon-placeholder [{:keys [size side]}]
  [:rn/view {:style [(style/icon-size-styles size)
                     (style/icon-gap-style side)]}])

(defn button [{:keys [type size icon disabled? on-press-in on-press-out]
               :or   {type :primary size 40}
               :as   props}
              content]
  (let [theme         (context/use-theme)
        [pressed? set-pressed!] (rn/use-state false)
        on-press-in!  (rn/use-callback (fn [event]
                                         (set-pressed! true)
                                         (when on-press-in
                                           (on-press-in event)))
                                       [on-press-in])
        on-press-out! (rn/use-callback (fn [event]
                                         (set-pressed! false)
                                         (when on-press-out
                                           (on-press-out event)))
                                       [on-press-out])]
    [:rn/pressable (-> props
                       (dissoc :type :size :icon :state :disabled? :style :on-press-in :on-press-out)
                       (assoc :disabled disabled?
                              :style (rec.xf/add-styles
                                      style/pressable-base-style
                                      (style/container-layout-style size icon)
                                      (style/pressable-type-style theme type disabled? pressed?)
                                      (:style props))
                              :on-press-in on-press-in!
                              :on-press-out on-press-out!))
     (case icon
       :right [:<>
               [button-text {:type type
                             :size size}
                content]
               [button-icon-placeholder {:size size
                                         :side :right}]]
       :left [:<>
              [button-icon-placeholder {:size size
                                        :side :left}]
              [button-text {:type type
                            :size size}
               content]]
       :left-right [:<>
                    [button-icon-placeholder {:size size
                                              :side :left}]
                    [button-text {:type type
                                  :size size}
                     content]
                    [button-icon-placeholder {:size size
                                              :side :right}]]
       :icon-only [button-icon-placeholder {:size size}]
       [button-text {:type type
                     :size size}
        content])]))
