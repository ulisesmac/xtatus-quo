(ns xquo.banner.global.view
  (:require [react-native.utils :as rn.utils]
            [xquo.banner.global.style :as style]
            [xquo.components.text.view :as text]
            [xquo.foundations.colors :as colors]))

(defn- background [items height]
  [:svg/svg {:width "100%" :height height :style style/background :pointer-events :none}
   (into [:svg/g {:opacity 0.2}
          [:svg/rect {:width  "100%"
                      :height height
                      :fill   (colors/get-color (style/type-color (:type (first items) :error)) 50)}]]
         (map-indexed (fn [index item]
                        (let [top (* style/row-height (inc index))]
                          [:svg/rect {:y      top
                                      :width  "100%"
                                      :height (+ (- height top) 20)
                                      :rx     20
                                      :fill   (colors/get-color (style/type-color (:type item :error)) 50)}])))
         (rest items))])

(defn- banner-row [{:keys [title type] :or {type :error}}]
  [:rn/view {:style style/row}
   [text/text {:font :font/medium-13 :number-of-lines 1 :style (style/title type)}
    title]])

(defn banner-global [{:keys [items] :as props}]
  (let [height (* style/row-height (count items))]
    [:rn/view (-> props
                  (dissoc :items :style)
                  (assoc :style (rn.utils/add-styles style/container (:style props))))
     (when (seq items)
       (into [:rn/view {:style (style/stack height)}
              [background items height]]
             (map (fn [item]
                    [banner-row item]))
             items))]))
