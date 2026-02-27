(ns xquo.components.text.view
  (:require [xquo.context :as context]
            [xquo.foundations.colors :as colors]
            [xquo.foundations.typography :as typography]
            [reagent-extended-compiler.utils.transforms :as rec.xf]))

(def light-text-style
  {:color (colors/get-color :color/neutral-100)})

(def dark-text-style
  {:color (colors/get-color :color/white-100)})

(defn text [{:keys [font style]
             :or   {font :font/regular-15}
             :as   props}
            & children]
  (into [:rn/text (-> props
                      (dissoc :font :style)
                      (assoc :style (rec.xf/add-styles
                                     (typography/get-style font)
                                     (if (= (context/use-theme) :theme/dark)
                                       dark-text-style
                                       light-text-style)
                                     style)))]
          children))
