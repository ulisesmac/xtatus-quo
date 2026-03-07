(ns xquo.components.color-picker.picker.view
  (:require [applied-science.js-interop :as j]
            [reagent-extended-compiler.utils.transforms :as rec.xf]
            [xquo.components.color-picker.color.view :as color]
            [xquo.components.color-picker.picker.style :as style]
            [xquo.react-native :as rn]))

(def ^:private picker-colors
  [:color/blue
   :color/yellow
   :color/purple
   :color/turquoise
   :color/magenta
   :color/sky
   :color/orange
   :color/army
   :color/flamingo
   :color/camel
   :color/copper])

(def ^:private item-size 48)
(def ^:private item-gap 8)
(def ^:private base-content-width
  (+ (* (count picker-colors) item-size)
     (* (dec (count picker-colors)) item-gap)))

(defn- picker-item [{:keys [color selected? background select-color!]}]
  (let [on-press (fn []
                   (select-color! color))]
    [color/color {:color     color
                  :selected? selected?
                  :background background
                  :on-press  on-press}]))

(defn picker
  "Color picker.

  API:
  - `props` map
    - `:default-selected` optional initial selected color keyword
    - `:background` optional `:blur`
    - `:on-select` optional callback `(fn [color-kw] ...)`
    - `:style` optional caller style (map/vector/js style)
    - Any additional keys are forwarded to `:rn/scroll-view`."
  [{:keys [default-selected background on-select on-layout]
    :or   {default-selected :color/blue}
    :as   props}]
  (let [[selected-color
         set-selected-color!] (rn/use-state default-selected)
        [viewport-width
         set-viewport-width!] (rn/use-state nil)
        scroll-ref    (rn/use-ref nil)
        select-color! (rn/use-callback (fn [color]
                                         (set-selected-color! color)
                                         (when on-select
                                           (on-select color)))
                                       [on-select])
        on-layout!    (rn/use-callback (fn [event]
                                         (set-viewport-width!
                                          (j/get-in event [:nativeEvent :layout :width]))
                                         (when on-layout
                                           (on-layout event)))
                                       [on-layout])]
    (rn/use-effect
     (fn []
       (let [scroll-view (j/get scroll-ref :current)]
         (when (and scroll-view viewport-width)
           (let [selected-index (or (some (fn [[index color]]
                                            (when (= color selected-color)
                                              index))
                                          (map-indexed vector picker-colors))
                                    0)
                 max-offset     (max 0 (- base-content-width viewport-width))
                 scroll-x       (-> (+ (* selected-index (+ item-size item-gap))
                                       (/ item-size 2)
                                       (- (/ viewport-width 2)))
                                    (max 0)
                                    (min max-offset))]
             (j/call scroll-view :scrollTo #js{:x scroll-x
                                               :animated true})))))
     [selected-color viewport-width])
    [:rn/scroll-view (-> props
                         (dissoc :default-selected :background :on-select :style :content-container-style
                                 :horizontal :shows-horizontal-scroll-indicator :on-layout)
                         (assoc :ref        scroll-ref
                                :horizontal true
                                :shows-horizontal-scroll-indicator false
                                :on-layout  on-layout!
                                :style      (rec.xf/add-styles
                                             style/container-base
                                             (:style props))
                                :content-container-style style/content-container-base))
     (into [:rn/view {:style [style/row-base
                              (when (and viewport-width
                                         (<= base-content-width viewport-width))
                                style/row-centered)]}]
           (map (fn [color]
                  [picker-item {:color         color
                                :selected?     (= color selected-color)
                                :background    background
                                :select-color! select-color!}]))
           picker-colors)]))
