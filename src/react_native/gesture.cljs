(ns react-native.gesture
  (:require
    ["react-native-gesture-handler" :refer
     (Gesture
      GestureDetector
      Directions
      RectButton
      Swipeable
      TouchableWithoutFeedback
      gestureHandlerRootHOC
      FlatList
      ScrollView)]
    [react-native.core :as rn]
    [react-native.flat-list :as rn-flat-list]
    [reagent.core :as reagent]))

(def directions (js->clj Directions :keywordize-keys true))

(def gesture-detector (reagent/adapt-react-class GestureDetector))

(def gesture-handler-root-hoc gestureHandlerRootHOC)

(defn gesture-tap [] (.Tap ^js Gesture))

(defn gesture-pan [] (.Pan ^js Gesture))

(defn gesture-native [] (.Native ^js Gesture))

(defn gesture-long-press [] (.LongPress ^js Gesture))

(defn gesture-pinch [] (.Pinch ^js Gesture))

(defn gesture-fling [] (.Fling ^js Gesture))

(defn on-begin [gesture handler] (.onBegin ^js gesture handler))

(defn on-start [gesture handler] (.onStart ^js gesture handler))

(defn on-update [gesture handler] (.onUpdate ^js gesture handler))

(defn on-end [gesture handler] (.onEnd ^js gesture handler))

(defn on-finalize [gesture handler] (.onFinalize ^js gesture handler))

(defn max-pointers [gesture amount] (.maxPointers ^js gesture amount))

(defn min-distance [gesture dist] (.minDistance ^js gesture dist))

(defn number-of-taps [gesture amount] (.numberOfTaps ^js gesture amount))

(defn enabled [gesture enabled?] (.enabled ^js gesture enabled?))

(defn average-touches [gesture average-touches?] (.averageTouches ^js gesture average-touches?))

(defn with-test-ID [gesture test-ID] (.withTestId ^js gesture (str test-ID)))

(defn simultaneous
  ([g1 g2] (.Simultaneous ^js Gesture g1 g2))
  ([g1 g2 g3] (.Simultaneous ^js Gesture g1 g2 g3)))

(defn exclusive [g1 g2] (.Exclusive ^js Gesture g1 g2))

;; RN Gesture Handler touchables are drop-in replacements for the RN ones. In
;; some cases, it's the only touchable that works with Swipeable components.
(def touchable-without-feedback (reagent/adapt-react-class TouchableWithoutFeedback))

(def rect-button (reagent/adapt-react-class RectButton))

(def ^:private swipeable-component
  (reagent/adapt-react-class Swipeable))

(defn swipeable
  [{:keys [render-left-actions render-right-actions] :as props} & children]
  (into [swipeable-component
         (cond-> props
           render-left-actions
           (assoc :render-left-actions
                  (fn [& args]
                    (reagent/as-element (apply render-left-actions args))))

           render-right-actions
           (assoc :render-right-actions
                  (fn [& args]
                    (reagent/as-element (apply render-right-actions args)))))]
        children))

(def gesture-flat-list (reagent/adapt-react-class FlatList))

(defn flat-list
  [props]
  [gesture-flat-list (rn-flat-list/base-list-props props)])

(def scroll-view (reagent/adapt-react-class ScrollView))

;;; Custom gesture section-list
(defn- flatten-sections
  [sections]
  (mapcat (fn [{:keys [title data]}]
            (into [{:title title :header? true}] data))
   sections))

(defn- find-sticky-indices
  [data]
  (->> data
       (map-indexed (fn [index item] (when (:header? item) index)))
       (remove nil?)
       (vec)))

(defn- is-last-item-in-section
  [data index]
  (let [next-item (nth data (inc index) nil)]
    (or (nil? next-item) (:header? next-item))))

(defn section-list
  [{:keys [sections sticky-section-headers-enabled render-section-header-fn render-section-footer-fn
           render-fn]
    :or   {sticky-section-headers-enabled true}
    :as   props}]
  (let [data        (flatten-sections sections)
        render-item (rn/use-callback
                     (fn [p1 p2 p3 p4]
                       [:<>
                        (if (:header? p1)
                          [render-section-header-fn p1 p2 p3 p4]
                          [render-fn p1 p2 p3 p4])
                        (when (and render-section-footer-fn
                                   (is-last-item-in-section data p2))
                          [render-section-footer-fn p1 p2 p3 p4])])
                     [render-fn render-section-header-fn render-section-footer-fn])]
    [flat-list
     (merge props
            {:data data
             :render-fn render-item
             :sticky-header-indices
             (when sticky-section-headers-enabled
               (find-sticky-indices data))})]))
