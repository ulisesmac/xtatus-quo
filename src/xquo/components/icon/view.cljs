(ns xquo.components.icon.view
  (:require [xquo.components.icon.icons :as icons]
            [xquo.context :as context]
            [xquo.foundations.colors :as colors]))

(defn- image-style [size color]
  (cond-> {:width  size
           :height size}
    (not= color :no-color)
    (assoc :tint-color color)))

(def svg-shapes
  {:icon/clear        {:circle "M3 10C3 6.13401 6.13401 3 10 3C13.866 3 17 6.13401 17 10C17 13.866 13.866 17 10 17C6.13401 17 3 13.866 3 10Z"
                       :x      "M9.15143 9.99998L7.07568 12.0757L7.92421 12.9243L9.99996 10.8485L12.0757 12.9242L12.9242 12.0757L10.8485 9.99998L12.9242 7.92421L12.0757 7.07568L9.99996 9.15145L7.92423 7.07572L7.0757 7.92425L9.15143 9.99998Z"}
   :icon/check-circle {:check "M3.75 6.25L5.25 7.75L8.25 4.25"}
   :icon/check-circle-outline
   {:path "M1.54995 5.99995C1.54995 3.54228 3.54228 1.54995 5.99995 1.54995C8.45762 1.54995 10.45 3.54228 10.45 5.99995C10.45 8.45762 8.45762 10.45 5.99995 10.45C3.54228 10.45 1.54995 8.45762 1.54995 5.99995ZM5.99995 0.449951C2.93477 0.449951 0.449951 2.93477 0.449951 5.99995C0.449951 9.06513 2.93477 11.55 5.99995 11.55C9.06513 11.55 11.55 9.06513 11.55 5.99995C11.55 2.93477 9.06513 0.449951 5.99995 0.449951ZM5.66754 8.10789L8.66754 4.60789L7.83236 3.89202L5.21889 6.94107L4.13886 5.86104L3.36104 6.63886L4.86104 8.13886L5.28102 8.55883L5.66754 8.10789Z"}})

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

(defn- circle-check-svg [{:keys [size]}]
  (let [theme        (context/use-theme)
        check-color  (colors/get-color :color/white)
        circle-color (colors/themed theme :color/success)
        {check :check} (get svg-shapes :icon/check-circle)]
    [:svg/svg {:width    size
               :height   size
               :view-box "0 0 12 12"
               :fill     :none}
     [:svg/circle {:cx   6
                   :cy   6
                   :r    5.5
                   :fill circle-color}]
     [:svg/path {:d            check
                 :stroke       check-color
                 :stroke-width 1.1}]]))

(defn- check-circle-outline-svg [{:keys [color size]}]
  (let [{path :path} (get svg-shapes :icon/check-circle-outline)]
    [:svg/svg {:width    size
               :height   size
               :view-box "0 0 12 12"
               :fill     :none}
     [:svg/path {:fill-rule :evenodd
                 :clip-rule :evenodd
                 :d         path
                 :fill      color}]]))

(def svg-icons
  {:icon/clear        clear-svg
   :icon/check-circle circle-check-svg
   :icon/check-circle-outline check-circle-outline-svg})

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
