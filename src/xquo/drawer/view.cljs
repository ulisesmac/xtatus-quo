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

(defn- background-color [{:keys [theme dark-theme?]}]
  (if (and rn/ios? dark-theme?)
    (colors/get-color :color/neutral-95-40)
    (colors/themed theme :color/white :color/neutral-95)))

(defn screen-options [{:keys [theme dark-theme? handle?]
                       :or   {handle? true}
                       :as   theme-color}]
  (let [background-color (background-color theme-color)]
    (xf/->js-prop-obj (cond-> {:background-color background-color
                               :corner-radius    20
                               :grabber          handle?
                               :grabber-options  (when handle?
                                                   (handle theme))}
                        (and rn/ios? dark-theme?) (assoc :background-blur :system-ultra-thin-material-dark)
                        rn/android? (assoc :dimmed-background-color (colors/get-color :color/neutral-100-70))))))
