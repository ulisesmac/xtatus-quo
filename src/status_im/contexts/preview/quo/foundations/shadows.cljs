(ns status-im.contexts.preview.quo.foundations.shadows
  (:require
    [quo.context :as quo.context]
    [quo.core :as quo]
    [quo.foundations.colors :as colors]
    [quo.foundations.shadows :as shadows]
    [react-native.core :as rn]
    [reagent.core :as reagent]
    [status-im.contexts.preview.quo.preview :as preview]))

(defn demo-box
  [shadow? description shadow-style theme]
  [rn/view
   {:style {:margin-left   :auto
            :margin-right  :auto
            :margin-top    8
            :margin-bottom 8
            :align-items   :center}}
   [quo/text {} description]
   [rn/view
    {:style (merge {:width            60
                    :height           60
                    :border-radius    16
                    :background-color (colors/theme-colors colors/white colors/neutral-90 theme)}
                   (when shadow? shadow-style))}]])

(def descriptor
  [{:key  :shadow?
    :type :boolean}])

(defn view
  []
  (let [state   (reagent/atom {:shadow? true})
        shadow? (reagent/cursor state [:shadow?])
        theme   (quo.context/use-theme)]
    (fn []
      [preview/preview-container {:state state :descriptor descriptor}
       [quo/text
        {:style {:margin-left  :auto
                 :margin-right :auto
                 :align-items  :center}}
        "Normal Scales"]
       [demo-box @shadow? "Shadow 1" (shadows/get 1 theme) theme]
       [demo-box @shadow? "Shadow 2" (shadows/get 2 theme) theme]
       [demo-box @shadow? "Shadow 3" (shadows/get 3 theme) theme]
       [demo-box @shadow? "Shadow 4" (shadows/get 4 theme) theme]
       [quo/text
        {:style {:margin-left  :auto
                 :margin-right :auto
                 :align-items  :center}}
        "Inverted Scales"]
       [demo-box @shadow? "Shadow 1" (shadows/get 1 theme :inverted) theme]
       [demo-box @shadow? "Shadow 2" (shadows/get 2 theme :inverted) theme]
       [demo-box @shadow? "Shadow 3" (shadows/get 3 theme :inverted) theme]
       [demo-box @shadow? "Shadow 4" (shadows/get 4 theme :inverted) theme]
       [quo/text
        {:style {:margin-left  :auto
                 :margin-right :auto
                 :align-items  :center}}
        "Inverted Scales"]
       [demo-box @shadow? "Inner Shadow" shadows/inner-shadow]])))
