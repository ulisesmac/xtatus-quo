(ns xquo.audio.recorder.view
  (:require [applied-science.js-interop :as j]
            [react-native.gesture-handler.core :as gh]
            [react-native.react.core :as react]
            [react-native.utils :refer [add-styles]]
            [xquo.audio.recorder.geometry :as geometry]
            [xquo.audio.recorder.style :as style]
            [xquo.audio.soundtrack.view :as soundtrack]
            [xquo.components.button.view :as button]
            [xquo.context :as context]
            [xquo.foundations.colors :as colors]))

(def ^:private record-icons
  {:default           :icon/audio
   :locking           :icon/audio
   :release-to-lock   :icon/lock
   :locked            :icon/stop
   :deleting          :icon/audio
   :release-to-delete :icon/trash
   :sending           :icon/audio
   :release-to-send   :icon/arrow-top})

(def ^:private waves
  [{:size 96 :opacity 0.5 :alpha 5 :neutral-opacity 0.1}
   {:size 88 :opacity 1 :alpha 5 :neutral-opacity 0.2}
   {:size 80 :opacity 1 :alpha 10 :neutral-opacity 0.4}
   {:size 72 :opacity 1 :alpha 20 :neutral-opacity 0.6}
   {:size 64 :opacity 1 :alpha 30 :neutral-opacity 1}])

(defn- recording-background [{:keys [state]}]
  (let [theme     (context/use-theme)
        color     (context/use-color)
        locking?  (#{:locking :release-to-lock} state)
        deleting? (#{:deleting :release-to-delete} state)
        fill      (style/recording-fill theme color state)
        shape     (geometry/shapes state)]
    [:rn/view {:pointer-events :none :style (style/recording-position state)}
     (for [{:keys [size opacity alpha neutral-opacity]} waves]
       ^{:key size}
       [:rn/view {:style (style/wave size
                                     (if locking?
                                       fill
                                       (colors/get-color (if deleting? :color/danger color) 50 alpha))
                                     (if locking? neutral-opacity opacity))}])
     (if shape
       [:svg/svg {:width  (:width shape)
                  :height (:height shape)
                  :style  (style/shape (:left shape) (:top shape))}
        [:svg/path {:d (:path shape) :fill fill}]]
       [:rn/view {:style (style/circle fill)}])]))

(defn- drag-state [event]
  (let [x (j/get event :translationX)
        y (j/get event :translationY)]
    (cond
      (and (< x -42) (< y -42)) :release-to-lock
      (and (< x -15) (< y -15)) :locking
      (< x -57)                 :release-to-delete
      (< x -19)                 :deleting
      (< y -57)                 :release-to-send
      (< y -19)                 :sending
      :else                     :default)))

(defn- record-control [{:keys [state on-record on-stop on-delete on-send on-lock on-state-change
                               set-drag-state!]}]
  (let [theme         (context/use-theme)
        gesture-state (react/use-memo #(atom :default) [])
        locked?       (= state :locked)
        locking?      (#{:locking :release-to-lock} state)
        begin!        (react/use-callback (fn []
                                            (reset! gesture-state :default)
                                            (when on-record (on-record)))
                                          [on-record])
        update!       (react/use-callback (fn [event]
                                            (let [next-state (drag-state event)]
                                              (when (not= next-state @gesture-state)
                                                (reset! gesture-state next-state)
                                                (set-drag-state! next-state)
                                                (when on-state-change (on-state-change next-state)))))
                                          [on-state-change set-drag-state!])
        end!          (react/use-callback (fn []
                                            (let [callback (case @gesture-state
                                                             :release-to-lock on-lock
                                                             :release-to-delete on-delete
                                                             :release-to-send on-send
                                                             on-stop)]
                                              (when callback (callback))))
                                          [on-delete on-lock on-send on-stop])
        finalize!     (react/use-callback #(set-drag-state! nil) [set-drag-state!])
        gesture       (react/use-memo (fn []
                                        (doto (gh/gesture-pan)
                                          (j/call :enabled (not locked?))
                                          (j/call :runOnJS true)
                                          (j/call :minDistance 0)
                                          (j/call :onBegin begin!)
                                          (j/call :onUpdate update!)
                                          (j/call :onEnd end!)
                                          (j/call :onFinalize finalize!)))
                                      [locked? begin! update! end! finalize!])]
    [:rn/view {:style (style/recording-position state)}
     [:gh/gesture-detector {:gesture gesture}
      [button/button {:size     32
                      :style    style/record-button
                      :icon     {:name  (record-icons state)
                                 :color (if locking?
                                          (colors/themed theme :color/neutral-100 :color/white-100)
                                          (colors/get-color :color/white-100))}
                      :on-press (when locked? on-stop)}]]]))

(defn recorder [{:keys [state time progress warning? on-lock on-delete on-send
                        on-play on-pause on-seek]
                 :or   {state :default progress 0}
                 :as   props}]
  (let [{:keys [theme light-theme?]} (context/use-theme-color)
        [drag set-drag-state!]       (react/use-state nil)
        current-state                (or drag state)
        playback?                    (#{:stopped :listening} current-state)
        lock-type                    (if (and light-theme? (not= current-state :default))
                                       :dark-grey
                                       :grey)]
    [:rn/view {:style (add-styles (style/container playback?) (:style props))}
     (if playback?
       [:rn/view {:style style/playback}
        [button/button {:type     :grey
                        :size     32
                        :icon     {:name (if (= current-state :listening) :icon/pause :icon/play)}
                        :on-press (if (= current-state :listening) on-pause on-play)}]
        [soundtrack/soundtrack {:state    :listening
                                :time     time
                                :progress progress
                                :on-seek  on-seek
                                :style    style/playback-track}]]
       [soundtrack/soundtrack {:state    (if (= current-state :release-to-delete) :deleting :default)
                               :time     time
                               :progress progress
                               :warning? warning?
                               :style    style/fill}])
     (when-not playback?
       [recording-background {:state current-state}])
     [button/button {:type     :danger
                     :size     (if playback? 24 32)
                     :icon     {:name :icon/trash :color (colors/get-color :color/white-100)}
                     :on-press on-delete}]
     (when (not= current-state :release-to-send)
       [button/button {:size     32
                       :style    (when-not playback? style/send)
                       :icon     {:name :icon/arrow-top :color (colors/get-color :color/white-100)}
                       :on-press on-send}])
     (when (and (not playback?) (not= current-state :locked) (not= current-state :release-to-lock))
       [button/button {:type     lock-type
                       :size     32
                       :style    style/lock
                       :icon     {:name  :icon/lock-open
                                  :color (colors/themed theme :color/neutral-100 :color/white-100)}
                       :on-press on-lock}])
     (when-not playback?
       [record-control (assoc props
                              :state           current-state
                              :set-drag-state! set-drag-state!)])]))
