(ns xtatus-quo.components.bottom-sheet.view
  (:require
   [applied-science.js-interop :as j]
   [react-native.core :as rn]
   [react-native.safe-area :as safe-area]
   [react-native.reanimated :as reanimated]
   [react-native.gesture :as gesture]))

(def base-sheet-style
  {:position :absolute
   :left 0
   :right 0
   :bottom 0
   :background-color :white
   :border-top-left-radius 20
   :border-top-right-radius 20})

(def handle-container-style
  {:width "100%"
   :padding-top 8
   :padding-bottom 8
   :align-items :center
   :justify-content :center})

(def handle-style
  {:width 32
   :height 4
   :border-radius 100
   :opacity 0.05
   :background-color :black})

;; Smooth, non-spring animations from bottom to top for entering,
;; and to bottom for exiting.
(def entering-animation
  (-> ^js reanimated/slide-in-up-animation
      (.duration 260)
      (.easing (get reanimated/easings :linear))))

(def exiting-animation
  (-> ^js reanimated/slide-out-down-animation
      (.duration 220)
      (.easing (get reanimated/easings :linear))))

(def default-velocity-threshold 900) ;; px/s downward
(def default-distance-threshold 120) ;; px distance pulled down

(defn view
  "Bottom sheet with entering (from bottom) and exiting (to bottom) animations.
   - Top corners radius 20
   - Bottom padding equals safe-area bottom
   - Vector style (no merge)
   - Drag on handle to dismiss if fast-enough downward or far-enough pulled.

   Props:
   - :style map (additional container styles)
   - :content-style map (inner content container)
   - :on-close fn (called to unmount the sheet)
   - :velocity-threshold number (px/s)
   - :distance-threshold number (px)
  "
  [{:keys [style content-style on-close velocity-threshold distance-threshold entering exiting]
    :or {velocity-threshold default-velocity-threshold
         distance-threshold default-distance-threshold}
    :as _opts}
   & children]
  (let [translate-y (reanimated/use-shared-value 0)
        animated-style (reanimated/apply-animations-to-style
                        {:transform [{:translateY translate-y}]}
                        {})
        pan-gesture (-> (gesture/gesture-pan)
                        (gesture/average-touches true)
                        (gesture/on-update (fn [evt]
                                             (let [tY (or (j/get evt :translationY) 0)]
                                               (when (pos? tY)
                                                 (reanimated/set-shared-value translate-y tY)))))
                        (gesture/on-end (fn [evt]
                                          (let [vy (or (j/get evt :velocityY) 0)
                                                tY (reanimated/get-shared-value translate-y)]
                                            (if (or (> vy velocity-threshold)
                                                    (> tY distance-threshold))
                                              (when (fn? on-close) (on-close))
                                              (reanimated/animate translate-y 0))))))]
    [:gh/gesture-detector {:gesture pan-gesture}
     [:animated/view {:style [animated-style
                              base-sheet-style
                              {:padding-bottom safe-area/bottom}
                              style]
                      :entering (or entering entering-animation)
                      :exiting (or exiting exiting-animation)}
      [rn/view {:style handle-container-style}
       [rn/view {:style handle-style}]]
      (into [rn/view {:style content-style}] children)]]))
