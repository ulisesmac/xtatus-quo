(ns xtatus-quo.components.drawers.drawer-action.view
  (:require
   [xtatus-quo.components.drawers.drawer-action.style :as style]
   [quo.context]
   [react-native.corex :as rn]
   [xtatus-quo.components.icon :as icon]
   [xtatus-quo.components.inputs.input.view :as input]
   [xtatus-quo.components.markdown.text :as text]
   [xtatus-quo.components.selectors.selectors.view :as selectors]))

(defn view
  [{:keys       [action action-icon icon icon-props description state title on-press blur? accessibility-label
                 input-props]
    action-type :type
    :or         {blur? false}}]
  (let [theme               (quo.context/use-theme)
        customization-color (quo.context/use-color)
        action-type         (or action-type :main)
        [pressed? set-pressed] (rn/use-state false)
        on-press-in         (rn/use-callback #(set-pressed true))
        on-press-out        (rn/use-callback #(set-pressed false))]

    [rn/pressable
     {:on-press            on-press
      :on-press-in         on-press-in
      :on-press-out        on-press-out
      :style               (style/container {:state               state
                                             :action              action
                                             :customization-color customization-color
                                             :theme               theme
                                             :pressed?            pressed?
                                             :description?        (not-empty description)
                                             :blur?               blur?})
      :accessibility-label accessibility-label}
     [rn/view {:style {:flex-direction :row :align-items :center}}
      (when icon
        [icon/icon icon
         (merge {:accessibility-label :left-icon
                 :container-style     style/left-icon
                 :color               (style/icon-color {:theme theme
                                                         :type  action-type
                                                         :blur? blur?})}
                icon-props)])

      [rn/view
       {:style style/text-container}
       [text/text
        (merge (style/text {:theme theme
                            :type  action-type
                            :blur? blur?})
               {:font :font/medium-15})
        title]

       (when (seq description)
         [text/text
          {:font  :font/regular-13
           :style (style/desc {:theme theme
                               :blur? blur?})}
          description])]

      (cond
        (= action :toggle)
        [selectors/view
         {:theme               theme
          :label-prefix        "toggle"
          :customization-color customization-color
          :type                :toggle
          :on-change           on-press
          :checked?            (= state :selected)}]

        (= action :arrow)
        [icon/icon (or action-icon :i/chevron-right)
         {:accessibility-label :arrow-icon
          :color               (style/icon-color {:theme theme
                                                  :type  action-type
                                                  :blur? blur?})}]

        (= state :selected)
        [icon/icon :i/check
         {:accessibility-label :check-icon
          :color               (style/check-color {:theme               theme
                                                   :blur?               blur?
                                                   :customization-color customization-color})}])]

     (when (and (= action :input) (= state :selected))
       [input/input
        (assoc input-props
               :blur?               blur?
               :accessibility-label :input)])]))
