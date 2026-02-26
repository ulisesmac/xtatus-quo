(ns xquo.components.text.view
  (:require [xquo.context :as context]
            [xquo.foundations.colors :as colors]
            [xquo.foundations.typography :as typography]
            [reagent-extended-compiler.utils.transforms :as rec.xf]))

(def light-text-style
  {:color (colors/get-color :color/neutral-100)})

(def dark-text-style
  {:color (colors/get-color :color/white-100)})

(defn text [{:keys [font style] :as props} & children]
  (let [current-theme (context/use-theme)
        font-style    (typography/get-style font)
        theme-style   (if (= current-theme :theme/dark) dark-text-style light-text-style)]
    (into [:rn/text (-> props
                        (dissoc :font :theme :style)
                        (assoc :style (rec.xf/add-styles [font-style theme-style style])))]
          children)))
