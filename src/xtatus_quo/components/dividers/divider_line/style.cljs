(ns xtatus-quo.components.dividers.divider-line.style
  (:require [quo.foundations.colors :as colors]
            [reagent-extended-compiler.utils.transforms :refer [style]]))

(defn divider-line
  [blur? theme]
  (style {:border-color        (if blur?
                                 (colors/theme-colors colors/neutral-80-opa-5 colors/white-opa-5 theme)
                                 (colors/theme-colors colors/neutral-10 colors/neutral-90 theme))
          :border-bottom-width 1}))
