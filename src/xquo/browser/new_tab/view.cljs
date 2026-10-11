(ns xquo.browser.new-tab.view
  (:require [react-native.utils :as rn.utils]
            [xquo.browser.new-tab.style :as style]
            [xquo.components.button.view :as button]
            [xquo.components.text.view :as text]
            [xquo.context :as context]
            [xquo.foundations.colors :as colors]))

(defn new-tab [{:keys [title on-press] button-props :button :or {title "New tab"} :as props}]
  (let [color (context/use-color)]
    [:rn/view (-> props
                  (dissoc :title :on-press :button)
                  (assoc :style (rn.utils/add-styles style/container
                                                     (style/background color)
                                                     (:style props))))
     [:svg/svg {:width 161 :height 173 :style style/border :pointer-events :none}
      [:svg/rect {:x                0.5
                  :y                0.5
                  :width            160
                  :height           172
                  :rx               16
                  :fill             :none
                  :stroke           (colors/get-color color 50 40)
                  :stroke-width     1
                  :stroke-dasharray "2 5"
                  :stroke-linecap   :round}]]
     [button/button (assoc button-props
                           :size     (:size button-props 32)
                           :type     (:type button-props :primary)
                           :icon     (:icon button-props {:name :icon/add})
                           :on-press (:on-press button-props on-press))]
     [text/text {:font :font/medium-13 :style style/title} title]]))
