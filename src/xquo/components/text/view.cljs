(ns xquo.components.text.view
  (:require [xquo.context :as context]
            [xquo.foundations.colors :as colors]
            [xquo.foundations.typography :as typography]
            [react-native.utils :as rn.utils]))

(def light-text-style
  {:color (colors/get-color :color/neutral-100)})

(def dark-text-style
  {:color (colors/get-color :color/white-100)})

(defn text [{:keys [font style]
             :or   {font :font/regular-15}
             :as   props}
            & children]
  (let [theme (context/use-theme)]
    (into [:rn/text (-> props
                        (dissoc :font :style)
                        (assoc :style (rn.utils/add-styles
                                       (typography/get-style font)
                                       (if (= theme :theme/dark)
                                         dark-text-style
                                         light-text-style)
                                       style)))]
          children)))
