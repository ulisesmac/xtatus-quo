(ns xquo.drawer.sheet
  (:require [react-native.utils :as rn.utils]
            [xquo.foundations.colors :as colors]))

(defn screen-options [{:keys [theme blur? handle?]
                       :or   {theme :theme/light handle? true}
                       :as   options}]
  (let [theme (if blur? :theme/dark theme)]
    (-> options
        (dissoc :theme :blur? :handle?)
        (assoc :detents (:detents options [:auto])
               :corner-radius (:corner-radius options 20)
               :background-color (if blur?
                                   "transparent"
                                   (colors/themed theme :color/white :color/neutral-95))
               :grabber handle?
               :grabber-options (when handle?
                                  {:width         32
                                   :height        4
                                   :top-margin    8
                                   :corner-radius 100
                                   :color         (colors/themed theme :color/neutral-100-5 :color/white-10)
                                   :adaptive      false})
               :dimmed-background-color (:dimmed-background-color options
                                         (colors/get-color :color/neutral-100-70))
               :inset-adjustment :automatic)
        (rn.utils/->js-prop-obj))))
