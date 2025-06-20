(ns quo.components.tags.base-tag
  (:require
    [react-native.core :as rn]))

(defn style-container
  [size disabled border-color border-width background-color labelled? type]
  (merge {:height           size
          :border-color     border-color
          :background-color background-color
          :border-width     border-width
          :border-radius    size
          :align-items      :center
          :justify-content  :center}
         (when disabled
           {:opacity 0.3})
         (when (and (or (= type :icon) (= type :emoji)) (not labelled?))
           {:width size})))

(defn base-tag
  [{:keys [id size disabled? border-color border-width background-color on-press
           accessibility-label type labelled?]
    :or   {size 32}}
   children]
  [rn/pressable
   (merge {:disabled            disabled?
           :accessibility-label accessibility-label
           :style               (style-container size
                                                 disabled?
                                                 border-color
                                                 border-width
                                                 background-color
                                                 labelled?
                                                 type)}
          (when on-press
            {:on-press #(on-press id)}))
   children])
