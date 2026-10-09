(ns xquo.demo.divider-strength
  (:require [react-native.react.core :as react]
            [react-native.utils :refer [defstyle]]
            [xquo.core :as xquo]
            [xquo.demo.controls :as controls]
            [xquo.demo.screen :as screen]
            [xquo.foundations.colors :as colors]))

(def ^:private titles
  {:informative "Tips to improve your password"
   :alert       "Alert message"
   :very-weak   "Very weak"
   :weak        "Weak"
   :okay        "Okay"
   :strong      "Strong"
   :very-strong "Very strong"})

(defstyle ^:private background
  {:background-color (colors/get-color :color/neutral-95)})

(defn screen [_]
  (let [[status set-status!] (react/use-state :informative)
        [title set-title!]   (react/use-state (titles :informative))
        on-status-change!    (react/use-callback (fn [status]
                                                   (set-status! status)
                                                   (set-title! (titles status)))
                                                 [])]
    [screen/screen {:title   "Divider strength"
                    :preview [:rn/view {:style background}
                              [xquo/divider.strength {:status status :title title}]]}
     [controls/choice {:label     ":status"
                       :value     status
                       :on-change on-status-change!
                       :options   (map (fn [status]
                                         {:value status :label (str status)})
                                       [:informative :alert :very-weak :weak :okay :strong :very-strong])}]
     [xquo/input {:label ":title" :value title :on-change-text set-title!}]]))
