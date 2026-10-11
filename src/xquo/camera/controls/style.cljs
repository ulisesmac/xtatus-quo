(ns xquo.camera.controls.style
  (:require [react-native.utils :refer [defstyle style]]
            [xquo.foundations.colors :as colors]))

(defstyle container
  {:align-self :stretch})

(defstyle zoom-area
  {:height          72
   :padding         20
   :align-items     :center
   :justify-content :center})

(defn zoom-track [orientation]
  (style {:flex-direction   :row
          :height           32
          :border-radius    10
          :padding          2
          :gap              2
          :overflow         :hidden
          :background-color (colors/get-color :color/neutral 80 (if (= orientation :horizontal) 20 10))}))

(defn zoom-slot [orientation]
  (style {:width           (if (= orientation :horizontal) 40 48)
          :height          28
          :align-items     :center
          :justify-content :center}))

(defn zoom-tab [orientation selected?]
  (let [horizontal? (= orientation :horizontal)]
    (style {:width            (if horizontal? 28 48)
            :height           (if horizontal? 40 28)
            :padding-left     0
            :padding-right    0
            :background-color (when selected? (colors/get-color :color/neutral-80-40))
            :transform        [{:rotate (if horizontal? "90deg" "0deg")}]})))

(defn zoom-label [selected?]
  (style {:color (colors/get-color (if selected? :color/warning-50 :color/white-100))}))
