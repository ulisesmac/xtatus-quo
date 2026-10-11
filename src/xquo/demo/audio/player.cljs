(ns xquo.demo.audio.player
  (:require [react-native.react.core :as react]
            [xquo.context :as context]
            [xquo.core :as xquo]
            [xquo.demo.audio.controls :as audio-controls]
            [xquo.demo.controls :as controls]
            [xquo.demo.screen :as screen]))

(defn screen [_]
  (let [[state set-state!]       (react/use-state :default)
        [time set-time!]         (react/use-state "00:28")
        [progress set-progress!] (react/use-state 0.25)
        [color set-color!]       (react/use-state nil)
        on-press!                (react/use-callback (fn []
                                                       (js/alert (if (= state :playing) "Pause audio" "Play audio"))
                                                       (set-state! (if (= state :playing) :default :playing)))
                                                     [state])]
    [context/provider {:color color}
     [screen/screen {:title   "Audio player"
                     :preview [xquo/audio.player {:state    state
                                                  :time     time
                                                  :progress progress
                                                  :on-press on-press!
                                                  :on-seek  set-progress!
                                                  :style    {:align-self :center}}]}
      [controls/choice {:label     ":state"
                        :value     state
                        :on-change set-state!
                        :options   [{:value :default :label ":default"}
                                    {:value :playing :label ":playing"}
                                    {:value :loading :label ":loading"}]}]
      [xquo/input {:label ":time" :value time :on-change-text set-time!}]
      [audio-controls/progress {:value progress :on-change set-progress!}]
      [audio-controls/color {:value color :on-change set-color!}]]]))
