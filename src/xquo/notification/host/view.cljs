(ns xquo.notification.host.view
  (:require [applied-science.js-interop :as j]
            [react-native.react.core :as react]
            [react-native.safe-area-context.core :as safe-area]
            [xquo.context :as context]
            [xquo.notification.host.style :as style]))

(defonce ^:private notifications-context (react/create-context nil))
(def ^:private motion (js/require "xquo/notification/motion"))

(defn- item [{:keys [id content duration dismissing? on-dismiss top offset measure! remove!]}]
  (let [removed!                           (react/use-callback (fn [reason]
                                                                 (remove! id)
                                                                 (when on-dismiss (on-dismiss (keyword reason))))
                                                               [id remove! on-dismiss])
        measured!                          (react/use-callback #(measure! id %) [id measure!])
        [gesture animated-style on-layout] (j/call motion
                                                   :useNotificationMotion
                                                   duration
                                                   (+ top 8)
                                                   offset
                                                   dismissing?
                                                   removed!
                                                   measured!)]
    [:animated/view {:collapsable false
                     :on-layout   on-layout
                     :style       [style/item animated-style]}
     [:gh/gesture-detector {:gesture gesture}
      content]]))

(defn provider [& children]
  (let [[items set-items!] (react/use-state [])
        top                (safe-area/use-top)
        show!              (react/use-callback (fn [content options]
                                                 (let [id (random-uuid)]
                                                   (set-items! (fn [items]
                                                                 (into [(assoc options
                                                                               :id       id
                                                                               :content  content
                                                                               :duration (:duration options 5000))]
                                                                       items)))
                                                   id))
                                               [])
        dismiss!           (react/use-callback (fn [id]
                                                 (set-items! (fn [items]
                                                               (mapv (fn [entry]
                                                                       (cond-> entry
                                                                         (= id (:id entry)) (assoc :dismissing? true)))
                                                                     items))))
                                               [])
        remove!            (react/use-callback (fn [id]
                                                 (set-items! (fn [items]
                                                               (filterv #(not= id (:id %)) items))))
                                               [])
        measure!           (react/use-callback (fn [id height]
                                                 (set-items! (fn [items]
                                                               (mapv (fn [entry]
                                                                       (if (= id (:id entry))
                                                                         (assoc entry :height height)
                                                                         entry))
                                                                     items))))
                                               [])
        offsets            (reductions (fn [offset {:keys [height]}]
                                         (if height (+ offset height 8) offset))
                                       0
                                       items)
        value              (react/use-memo (fn [] #js{:show show! :dismiss dismiss!}) [show! dismiss!])]
    [:> (j/get notifications-context :Provider) {:value value}
     (into [:<>] children)
     (when (seq items)
       [:notification/overlay
        [:gh/gesture-handler-root-view {:pointer-events :box-none
                                        :style          style/root}
         [:rn/view {:pointer-events :box-none :style style/stack}
          (for [[entry offset] (map vector items offsets)]
            ^{:key (str (:id entry))}
            [item (assoc entry
                         :top      top
                         :offset   offset
                         :measure! measure!
                         :remove!  remove!)])]]])]))

(defn use-notifications []
  (let [controller            (react/use-context notifications-context)
        enqueue!              (j/get controller :show)
        dismiss!              (j/get controller :dismiss)
        {:keys [theme color]} (context/use-theme-color)
        show!                 (react/use-callback (fn show!
                                                    ([content]
                                                     (show! content nil))
                                                    ([content options]
                                                     (enqueue! [context/provider {:theme theme :color color} content] options)))
                                                  [enqueue! theme color])]
    {:show! show! :dismiss! dismiss!}))
