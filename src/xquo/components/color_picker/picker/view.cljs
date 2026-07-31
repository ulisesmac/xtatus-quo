(ns xquo.components.color-picker.picker.view
  (:require [applied-science.js-interop :as j]
            [react-native.react.core :as react]
            [react-native.utils :as rn.utils]
            [xquo.components.color-picker.color.view :as color]
            [xquo.components.color-picker.picker.style :as style]))

(def ^:private item-size 48)
(def ^:private item-gap 8)

(defn- picker-item [{:keys [color selected? blur? select-color!]}]
  (let [on-press (react/use-callback #(select-color! color) [select-color! color])]
    [color/color {:color     color
                  :selected? selected?
                  :blur?     blur?
                  :on-press  on-press}]))

(defn picker
  "Color picker.

  - `props` map
    - `:default-selected` optional initial selected color keyword
    - `:colors` vector of color keywords
    - `:blur?` optional boolean
    - `:on-select` optional callback `(fn [color-kw] ...)`
    - `:style` optional caller style (map/vector/js style)
    - Any additional keys are forwarded to `:rn/scroll-view`."
  [{:keys [default-selected colors blur? on-select on-layout]
    :or   {default-selected :color/primary}
    :as   props}]
  (let [[selected-color
         set-selected-color!] (react/use-state default-selected)
        [viewport-width
         set-viewport-width!] (react/use-state nil)
        scroll-ref            (react/use-ref nil)
        select-color!         (react/use-callback (fn [color]
                                                    (set-selected-color! color)
                                                    (when on-select
                                                      (on-select color)))
                                                  [on-select])
        on-layout!            (react/use-callback (fn [event]
                                                    (set-viewport-width!
                                                     (j/get-in event [:nativeEvent :layout :width]))
                                                    (when on-layout
                                                      (on-layout event)))
                                                  [on-layout])
        content-width        (+ (* (count colors) item-size)
                                (* (dec (count colors)) item-gap))]
    (react/use-effect
     (fn []
       (let [scroll-view (j/get scroll-ref :current)]
         (when (and scroll-view viewport-width)
           (let [selected-index (or (some (fn [[index color]]
                                            (when (= color selected-color)
                                              index))
                                          (map-indexed vector colors))
                                    0)
                 scroll-x       (+ 16
                                   (* selected-index (+ item-size item-gap))
                                   (/ item-size 2)
                                   (- (/ viewport-width 2)))]
             (j/call scroll-view :scrollTo #js{:x scroll-x
                                               :animated true})))))
     [selected-color viewport-width colors])
    [:rn/scroll-view (-> props
                         (dissoc :default-selected :colors :blur? :on-select)
                         (assoc :ref scroll-ref
                                :horizontal true
                                :shows-horizontal-scroll-indicator false
                                :on-layout on-layout!
                                :style (rn.utils/add-styles style/container-base (:style props))
                                :content-container-style style/content-container-base))
     (into [:rn/view {:style [style/row-base
                              (when (and viewport-width
                                         (<= content-width viewport-width))
                                style/row-centered)]}]
           (map (fn [color]
                  [picker-item {:color         color
                                :selected?     (= color selected-color)
                                :blur?         blur?
                                :select-color! select-color!}]))
           colors)]))
