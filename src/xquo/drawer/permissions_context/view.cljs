(ns xquo.drawer.permissions-context.view
  (:require [react-native.utils :as rn.utils :refer [defstyle style]]
            [xquo.components.button.view :as button]
            [xquo.components.text.view :as text]
            [xquo.context :as context]
            [xquo.drawer.style :as drawer-style]))

(defstyle ^:private container
  {:padding-horizontal 20
   :padding-vertical   12
   :flex-direction     :row
   :flex-wrap          :wrap
   :justify-content    :center
   :align-items        :center
   :gap                4})

(defn permissions-context [{:keys [button blur?] :as props} & children]
  (let [theme (context/use-theme)]
    (into [:rn/view (-> props
                        (dissoc :button :blur?)
                        (assoc :style (rn.utils/add-styles container (:style props))))
           (when button
             [button/button (-> button
                                (dissoc :label)
                                (assoc :type  (:type button :ghost)
                                       :size  (:size button 24)
                                       :blur? (:blur? button (and blur? (not (:photo? button))))
                                       :style (rn.utils/add-styles (style {:height 22}) (:style button))))
              (:label button)])]
          (map (fn [child]
                 (if (string? child)
                   [text/text {:font  :font/regular-13
                               :style (style {:color (drawer-style/secondary theme blur?)})} child]
                   child)))
          children)))
