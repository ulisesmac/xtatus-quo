(ns quo.components.drawers.documentation-drawers.style
  (:require
    [quo.foundations.colors :as colors]
    [react-native.safe-area :as safe-area]))

(def outer-container
  ;; Margin negative to fill the bottom-sheet container where this component is used
  {:margin-bottom (- (+ safe-area/bottom 8))})

(def container
  {:align-items        :flex-start
   :padding-horizontal 20})

(def content
  {:align-items   :flex-start
   :margin-bottom (+ safe-area/bottom 12)})

(defn title
  [theme]
  {:color         (colors/theme-colors colors/neutral-100
                                       colors/white
                                       theme)
   :margin-bottom 8})
