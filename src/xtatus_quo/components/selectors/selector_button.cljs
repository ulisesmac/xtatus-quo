(ns xtatus-quo.components.selectors.selector-button
  (:require [quo.foundations.colors :as quo.colors]
            [xtatus-quo.utils :as quo.utils]
            [xtatus-quo.components.selectors.selectors.view :as selectors]
            [xtatus-quo.components.markdown.text :as md]))

;; TODO: pending blur version

(defn view [{:keys [text] :as props}]
  (let [theme (quo.context/use-theme)]
    [:rn/view {:style {:flex-direction     :row
                       :align-self         :flex-start
                       :align-items        :center
                       :padding-vertical   5
                       :padding-horizontal 8
                       :column-gap         8
                       :border-radius      10
                       :background-color   (quo.utils/if-theme theme
                                             quo.colors/neutral-5
                                             quo.colors/neutral-90)}}
     [selectors/view props]
     [md/text {:font-style :font/medium-15}
      text]
     ]))
