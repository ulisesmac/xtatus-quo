(ns xquo.dapp.activity.view
  (:require [react-native.utils :as rn.utils]
            [xquo.components.avatar.view :as avatar]
            [xquo.components.counter.view :as counter]
            [xquo.components.text.view :as text]
            [xquo.context :as context]
            [xquo.dapp.activity.style :as style]))

(defn activity [{:keys [title timestamp icon status blur? counter unread? on-press] :as props} & children]
  (let [{:keys [theme color]} (context/use-theme-color)]
    [(if on-press :rn/pressable :rn/view)
     (-> props
         (dissoc :title :timestamp :icon :status :blur? :counter :unread?)
         (assoc :style (rn.utils/add-styles style/container (:style props))))
     [avatar/avatar {:size    32
                     :color   :color/neutral
                     :border  :solid
                     :content {:type :icon :icon icon}
                     :blur?   blur?
                     :badge   (when status {:status status})
                     :style   style/avatar}]
     [:rn/view {:style style/content}
      [:rn/view {:style style/header}
       [:rn/view {:style style/title-group}
        [text/text {:font :font/semibold-15 :number-of-lines 1 :style style/title} title]
        (when counter
          [counter/counter {:type :outline :blur? blur?} counter])]
       [text/text {:font :font/regular-11 :number-of-lines 1 :style (style/timestamp theme blur?)} timestamp]
       (when unread?
         [:rn/view {:style style/unread-slot}
          [:rn/view {:style (style/unread theme color)}]])]
      (into [:rn/view {:style style/flow}]
            (map (fn [child]
                   (if (string? child)
                     [text/text {:font :font/regular-13 :style style/text} child]
                     child)))
            children)]]))
