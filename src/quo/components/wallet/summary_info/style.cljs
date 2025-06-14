(ns quo.components.wallet.summary-info.style
  (:require
    [quo.foundations.colors :as colors]))

(defn container
  [networks-to-show? theme]
  {:width         "100%"
   :height        (if networks-to-show? 90 56)
   :border-radius 16
   :border-width  1
   :border-color  (colors/theme-colors colors/neutral-10 colors/neutral-80 theme)
   :margin-bottom (if networks-to-show? 4 8)})

(def info-container
  {:flex-direction     :row
   :height             55
   :padding-horizontal 12
   :align-items        :center})

(defn dot-divider
  [theme]
  {:width             2
   :height            2
   :border-radius     2
   :margin-horizontal 8
   :background-color  (colors/theme-colors colors/neutral-50
                                           colors/neutral-60
                                           theme)})

(defn line-divider
  [theme]
  {:width            "100%"
   :height           1
   :background-color (colors/theme-colors colors/neutral-10 colors/neutral-80 theme)})

(def networks-container
  {:padding-horizontal 12
   :padding-vertical   4
   :height             32
   :flex-direction     :row
   :align-items        :center})

(def network-icon
  {:width  32
   :height 32})
