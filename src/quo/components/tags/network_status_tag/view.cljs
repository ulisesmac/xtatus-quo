(ns quo.components.tags.network-status-tag.view
  (:require
    [quo.components.markdown.text :as text]
    [quo.components.tags.network-status-tag.style :as style]
    [quo.context :as quo.context]
    [react-native.core :as rn]))

(defn view
  [{:keys [label]}]
  (let [theme (quo.context/use-theme)]
    [rn/view {:style style/main}
     [rn/view {:style (style/inner theme)}
      [rn/view {:style style/dot}]
      [text/text
       {:style  style/label
        :weight :medium
        :size   :label
        :align  :center}
       label]]]))
