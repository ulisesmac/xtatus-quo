(ns xquo.demo.audio.soundtrack
  (:require [react-native.react.core :as react]
            [xquo.context :as context]
            [xquo.core :as xquo]
            [xquo.demo.audio.controls :as audio-controls]
            [xquo.demo.controls :as controls]
            [xquo.demo.screen :as screen]))

(defn screen [_]
  (let [[state set-state!]       (react/use-state :default)
        [time set-time!]         (react/use-state "00:30")
        [timer? set-timer!]      (react/use-state true)
        [progress set-progress!] (react/use-state 0.25)
        [warning? set-warning!]  (react/use-state false)
        [color set-color!]       (react/use-state nil)]
    [context/provider {:color color}
     [screen/screen {:title   "Audio soundtrack"
                     :preview [xquo/audio.soundtrack {:state    state
                                                      :time     (when timer? time)
                                                      :progress progress
                                                      :warning? warning?
                                                      :on-seek  (when (= state :listening) set-progress!)
                                                      :style    {:align-self :center}}]}
      [controls/choice {:label     ":state"
                        :value     state
                        :on-change set-state!
                        :options   [{:value :default :label ":default"}
                                    {:value :deleting :label ":deleting"}
                                    {:value :listening :label ":listening"}]}]
      [xquo/input {:label ":time" :value time :on-change-text set-time!}]
      [controls/checkbox {:label ":time present" :value timer? :on-change set-timer!}]
      [audio-controls/progress {:value progress :on-change set-progress!}]
      [controls/checkbox {:label ":warning?" :value warning? :on-change set-warning!}]
      [audio-controls/color {:value color :on-change set-color!}]]]))
