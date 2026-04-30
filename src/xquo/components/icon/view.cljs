(ns xquo.components.icon.view
  (:require [applied-science.js-interop :as j]
            [reagent.core :as r]
            [xquo.components.icon.svg :as svg]))

(def create-nano-icon-set
  (.-createNanoIconSet (js/require "../node_modules/react-native-nano-icons/lib/module/index.js")))

(defonce glyph-map-20 (js/require "../resources/icons/nanoicons/icons20.glyphmap.json"))
(defonce glyph-map-16 (js/require "../resources/icons/nanoicons/icons16.glyphmap.json"))
(defonce glyph-map-12 (js/require "../resources/icons/nanoicons/icons12.glyphmap.json"))
(def icon-20 (r/adapt-react-class (create-nano-icon-set glyph-map-20)))
(def icon-16 (r/adapt-react-class (create-nano-icon-set glyph-map-16)))
(def icon-12 (r/adapt-react-class (create-nano-icon-set glyph-map-12)))

(def ->icon-kw (partial keyword "icon"))
(defn build-icon-set [glyph-map]
  (->> (j/get glyph-map :i) (js-keys) (map ->icon-kw) (set)))

(def icons
  {20 (build-icon-set glyph-map-20)
   16 (build-icon-set glyph-map-16)
   12 (build-icon-set glyph-map-12)})

(def components
  {20 icon-20
   16 icon-16
   12 icon-12})

(defn sizes-for [icon-name]
  (->> icons
       (keep (fn [[size icon-set]]
               (when (icon-name icon-set)
                 size)))
       (set)))

(defn closer-to [requested-size available-sizes]
  (->> available-sizes
       (map (fn [size]
              [size (abs (- requested-size size))]))
       (sort-by second)
       (ffirst)))

(def family-component
  (memoize
   (fn [size icon-name]
     (if (icon-name (icons size))
       (get components size)
       (let [available-sizes (sizes-for icon-name)
             inferred-size   (closer-to size available-sizes)]
         (get components inferred-size))))))

(defn svg-sizes-for [icon-name]
  (->> svg/icons
       (keep (fn [[size icon-map]]
               (when (get icon-map icon-name)
                 size)))
       (set)))

(def svg-component
  (memoize
   (fn [size icon-name]
     (let [available-sizes (svg-sizes-for icon-name)
           icon-size       (if (icon-name (svg/icons size))
                             size
                             (closer-to size available-sizes))]
       (icon-name (svg/icons icon-size))))))

(defn- svg-icon [{:keys     [color size style]
                  icon-name :name
                  :or       {icon-name :icon/placeholder
                             size      20}}]
  [(svg-component size icon-name) {:color color
                                   :size  size
                                   ;:style style
                                   }])

(defn view
  "Icon component"
  [{:keys     [color size style]
    icon-name :name
    :or       {icon-name :icon/placeholder
               size      20}
    :as       props}]
  (cond
    (svg/iconset icon-name)
    [svg-icon props]

    (family-component size icon-name)
    [:rn/view {:style style} ;; TODO: remove the view for styles
     [(family-component size icon-name) {:name  icon-name
                                         :size  size
                                         :color color
                                         :allowFontScaling false}]]
    :else
    [:rn/view {:style {:background-color :red}}
     [:rn/text (str (name icon-name) size)]]))
