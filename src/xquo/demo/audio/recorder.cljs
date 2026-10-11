(ns xquo.demo.audio.recorder
  (:require [react-native.react.core :as react]
            [xquo.context :as context]
            [xquo.core :as xquo]
            [xquo.demo.audio.controls :as audio-controls]
            [xquo.demo.controls :as controls]
            [xquo.demo.screen :as screen]))

(def ^:private state-options
  (map (fn [state]
         {:value state :label (str state)})
       [:default :locking :release-to-lock :locked :deleting :release-to-delete
        :sending :release-to-send :stopped :listening]))

(defn screen [_]
  (let [[state set-state!]       (react/use-state :default)
        [time set-time!]         (react/use-state "00:15")
        [progress set-progress!] (react/use-state (/ 20 164))
        [warning? set-warning!]  (react/use-state false)
        [color set-color!]       (react/use-state nil)
        stop!                    (react/use-callback (fn []
                                                       (js/alert "Stop recording")
                                                       (set-state! :stopped))
                                                     [])
        lock!                    (react/use-callback (fn []
                                                       (js/alert "Lock recording")
                                                       (set-state! :locked))
                                                     [])
        delete!                  (react/use-callback (fn []
                                                       (js/alert "Delete recording")
                                                       (set-state! :default)
                                                       (set-progress! 0)
                                                       (set-time! "00:00"))
                                                     [])
        send!                    (react/use-callback #(js/alert "Send recording") [])
        play!                    (react/use-callback (fn []
                                                       (js/alert "Play recording")
                                                       (set-state! :listening))
                                                     [])
        pause!                   (react/use-callback (fn []
                                                       (js/alert "Pause recording")
                                                       (set-state! :stopped))
                                                     [])]
    [context/provider {:color color}
     [screen/screen {:title   "Audio recorder"
                     :preview [:rn/view {:style {:padding-horizontal 12}}
                               [xquo/audio.recorder {:state     state
                                                     :time      time
                                                     :progress  progress
                                                     :warning?  warning?
                                                     :on-stop   stop!
                                                     :on-lock   lock!
                                                     :on-delete delete!
                                                     :on-send   send!
                                                     :on-play   play!
                                                     :on-pause  pause!
                                                     :on-seek   set-progress!
                                                     :style     {:align-self :center}}]]}
      [controls/choice {:label     ":state"
                        :value     state
                        :on-change set-state!
                        :options   state-options}]
      [xquo/input {:label ":time" :value time :on-change-text set-time!}]
      [audio-controls/progress {:value progress :on-change set-progress!}]
      [controls/checkbox {:label ":warning?" :value warning? :on-change set-warning!}]
      [audio-controls/color {:value color :on-change set-color!}]]]))
