(ns xquo.components.icon.view
  (:require [xquo.components.icon.icons :as icons]))

(defn- image-style [size color]
  (cond-> {:width  size
           :height size}
    (not= color :no-color)
    (assoc :tint-color color)))

(defn icon [{:keys [icon size color style]
             :or   {icon  :icon/placeholder
                    size  20
                    color :no-color}}]
  [:rn/view {:style style}
   [:rn/image {:source (icons/icon-source (str (name icon) size))
               :style  (image-style size color)}]])
