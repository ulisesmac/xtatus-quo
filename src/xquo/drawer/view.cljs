(ns xquo.drawer.view
  (:require [react-native.core :as rn]
            [reagent-extended-compiler.utils.transforms :as xf]
            [xquo.foundations.colors :as colors]))

(defn handle [theme]
  (xf/prop {:width      32
            :height     4
            :top-margin 8
            :color      (colors/themed theme :color/neutral-100-5 :color/white-10)
            :adaptive   false}))

(defn screen-options [{:keys [theme handle?]
                       :or   {handle? true}}]
  (xf/prop {:dimmed-background-color (colors/get-color :color/neutral-100-70)
            :background-color        (if rn/ios?
                                       (colors/themed theme :color/white-70 :color/neutral-80-70)
                                       (colors/themed theme :color/white :color/neutral-95))
            :corner-radius           20
            :grabber                 handle?
            :grabber-options         (when handle?
                                       (handle theme))}))
