(ns xquo.components.icon.view
  (:require [xquo.components.icon.icons :as icons]))

(defn- image-style [size color]
  (cond-> {:width  size
           :height size}
    (not= color :no-color)
    (assoc :tint-color color)))

(def svg-shapes
  {:icon/clear {:circle "M3 10C3 6.13401 6.13401 3 10 3C13.866 3 17 6.13401 17 10C17 13.866 13.866 17 10 17C6.13401 17 3 13.866 3 10Z"
                :x      "M9.15143 9.99998L7.07568 12.0757L7.92421 12.9243L9.99996 10.8485L12.0757 12.9242L12.9242 12.0757L10.8485 9.99998L12.9242 7.92421L12.0757 7.07568L9.99996 9.15145L7.92423 7.07572L7.0757 7.92425L9.15143 9.99998Z"}})

(defn- clear-svg [{:keys [color color-2 size]}]
  (let [{:keys [circle x]} (get svg-shapes :icon/clear)]
    [:svg/svg {:width    size
               :height   size
               :view-box "0 0 20 20"
               :fill     :none}
     [:svg/path {:fill-rule :evenodd
                 :clip-rule :evenodd
                 :d         circle
                 :fill      color}]
     [:svg/path {:fill-rule :evenodd
                 :clip-rule :evenodd
                 :d         x
                 :fill      color-2}]]))

(def svg-icons
  {:icon/clear clear-svg})

(defn icon
  "Icon component.

  API:
  - `props` map
    - `:icon` icon keyword (default `:icon/placeholder`)
    - `:size` icon size in points. This selects the bitmap asset size for
      bundled icons and scales SVG-backed icons (default `20`)
    - `:color` tint color for bitmap icons and primary fill for SVG-backed
      icons
    - `:color-2` secondary fill for SVG-backed icons
    - `:style` optional wrapper style applied to the root `:rn/view`

  Notes:
  - Bitmap icons use `:color`
  - SVG-backed icons use `:color` and `:color-2`
  - Any icon keyword present in `svg-icons` is rendered as SVG; all others use
    the bundled image source path."
  [{:keys [color color-2 icon size style]
    :or   {icon  :icon/placeholder
           size  20
           color :no-color}}]
  [:rn/view {:style style}
   (if-let [svg-icon (get svg-icons icon)]
     [svg-icon {:size    size
                :color   color
                :color-2 color-2}]
     [:rn/image {:source (icons/icon-source (str (name icon) size))
                 :style  (image-style size color)}])])
