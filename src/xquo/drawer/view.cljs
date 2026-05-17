(ns xquo.drawer.view
  (:require [react-native.core :as rn]
            [react-native.utils :as rn.utils]
            [xquo.foundations.colors :as colors]))

(defn handle [theme]
  (rn.utils/prop {:width      32
            :height     4
            :top-margin 8
            :color      (colors/themed theme :color/neutral-100-5 :color/white-10)
            :adaptive   false}))

(defn- background-color [{:keys [theme dark-theme?]}]
  (cond
    (and rn/ios? dark-theme?)
    (colors/get-color :color/neutral-95-40)

    rn/ios?
    (colors/get-color :color/white-70)

    :else
    (colors/themed theme :color/white :color/neutral-95)))

(defn- background-blur [{:keys [dark-theme?]}]
  (when rn/ios?
    (if dark-theme?
      :system-ultra-thin-material-dark
      :system-ultra-thin-material-light)))

(defn screen-options [{:keys [theme dark-theme? handle?]
                       :or   {handle? true}
                       :as   theme-color}]
  (let [background-color (background-color theme-color)
        background-blur  (background-blur theme-color)]
    (rn.utils/->js-prop-obj (cond-> {:background-color background-color
                               :corner-radius    20
                               :grabber          handle?
                               :grabber-options  (when handle?
                                                   (handle theme))}
                        background-blur (assoc :background-blur background-blur)
                        rn/android?     (assoc :dimmed-background-color (colors/get-color :color/neutral-100-70))))))
