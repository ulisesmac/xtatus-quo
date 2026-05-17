(ns xquo.components.settings.sort-item.view
  (:require [react-native.utils :as rn.utils]
            [xquo.components.selectors.filter.view :as filter]
            [xquo.components.settings.sort-item.style :as style]
            [xquo.components.text.view :as text]
            [xquo.context :as context]))

(defn sort-item
  "Settings sort item.

  API:
  - `props` map
    - `:label` item label (default `\"Text\"`)
    - `:selected` one of `:ascending`, `:descending`, or `nil`
    - `:on-select` optional callback called with `:ascending` or `:descending`
    - `:style` optional caller style (map/vector/js style)
    - Any additional keys are forwarded to `:rn/view`."
  [{:keys [label selected on-select]
    :or   {label "Text"}
    :as   props}]
  (let [theme (context/use-theme)]
    [:rn/view (-> props
                  (dissoc :label :selected :on-select :style)
                  (assoc :style (rn.utils/add-styles
                                 style/container-base
                                 (:style props))))
     [:rn/view {:style style/label-wrapper}
      [text/text {:font  :font/medium-15
                  :style {:color (style/label-color theme)}}
       label]]
     [:rn/view {:style style/actions-row}
     [filter/filter {:size       24
                      :icon       {:name :icon/arrow-top}
                      :selected?  (= selected :ascending)
                      :on-press   (when on-select
                                    (fn []
                                      (on-select :ascending)))}]
      [:rn/view {:style style/action-gap}
       [filter/filter {:size       24
                       :icon       {:name :icon/arrow-down}
                       :selected?  (= selected :descending)
                       :on-press   (when on-select
                                     (fn []
                                       (on-select :descending)))}]]]]))
