(ns xtatus-quo.components.dividers.divider-line.view
  (:require
   [xtatus-quo.context :as context]
   [xtatus-quo.components.dividers.divider-line.style :as style]))

(defn view [{:keys [blur?]}]
  (let [theme (context/use-theme)]
    [:rn/view {:style (style/divider-line blur? theme)}]))
