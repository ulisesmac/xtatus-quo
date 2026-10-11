(ns xquo.audio.player.view
  (:require [react-native.utils :refer [add-styles]]
            [xquo.audio.player.style :as style]
            [xquo.audio.soundtrack.view :as soundtrack]
            [xquo.components.button.view :as button]
            [xquo.components.text.view :as text]
            [xquo.context :as context]
            [xquo.foundations.colors :as colors]))

(def ^:private state-icons
  {:default :icon/play
   :playing :icon/pause
   :loading :icon/loading})

(defn player [{:keys [state time progress on-press on-seek]
               :or   {state :default progress 0}
               :as   props}]
  (let [{:keys [theme] :as theme-color} (context/use-theme-color)]
    [:rn/view {:style (add-styles style/container (:style props))}
     [:rn/view {:style (style/surface theme-color state)}
      [button/button {:size     32
                      :icon     {:name (state-icons state) :color (colors/get-color :color/white-100)}
                      :on-press (when (not= state :loading) on-press)}]
      [soundtrack/track {:state       (if (= state :loading) :loading :listening)
                         :progress    (if (= state :loading) 0 progress)
                         :on-seek     (when (not= state :loading) on-seek)
                         :track-color (colors/themed theme :color/neutral-30 :color/neutral-80)}]
      [text/text {:font :font/medium-13 :style style/time-label} time]]]))
