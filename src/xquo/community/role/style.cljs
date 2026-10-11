(ns xquo.community.role.style
  (:require [react-native.utils :refer [defstyle style]]
            [xquo.foundations.colors :as colors]))

(defn container [theme]
  (style {:width            335
          :max-width        "100%"
          :min-height       40
          :border-radius    12
          :border-width     1
          :border-color     (colors/themed theme :color/neutral-10 :color/neutral-80)
          :padding-left     11
          :padding-right    7
          :padding-vertical 7
          :flex-direction   :row
          :align-items      :center
          :flex-wrap        :wrap
          :gap              4}))

(defstyle label
  {:include-font-padding false})
