(ns xquo.audio.player.style
  (:require [react-native.utils :refer [defstyle style]]
            [xquo.foundations.colors :as colors]))

(defstyle container
  {:width            295
   :padding-vertical 4})

(defn surface [{:keys [theme light-theme?]} state]
  (style {:height           56
          :border-radius    12
          :border-width     1
          :border-color     (colors/themed theme :color/neutral-20 :color/neutral-80)
          :background-color (colors/themed theme :color/neutral-5 :color/neutral-80-40)
          :padding-left     11
          :padding-right    (if (and light-theme? (not= state :default)) 15 11)
          :flex-direction   :row
          :align-items      :center
          :gap              16}))

(defstyle time-label
  {:width                39
   :text-align           :right
   :include-font-padding false})
