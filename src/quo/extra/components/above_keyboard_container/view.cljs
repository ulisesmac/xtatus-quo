(ns quo.extra.components.above-keyboard-container.view
  (:require ["react" :refer [useLayoutEffect useState useCallback useRef]]
            ["react-native-reanimated" :refer [useSharedValue withSpring]]
            ["react-native" :refer [useWindowDimensions Keyboard]]
            ["react-native-safe-area-context" :as safe-area]))

;; TODO: As soon as the refactor starts:
;; Export a React Native lib for ClojureScript

(def animation-params #js{:mass      1
                          :damping   18
                          :stiffness 150})

(defn view [_ _]
  (let [initial-content-height (atom nil)
        animate-above!         #(set! (.-value %1) (withSpring %2 animation-params))]
    (def --initial initial-content-height)
    (fn [{:keys [above above-container-style above-offset]} child]
      (let [above-ref       ^js (useRef)
            content-ref     ^js (useRef)
            above-position  ^js (useSharedValue 0)
            measure-layout! (useCallback
                             ;; TODO: check double re-render
                             ;; TODO: refactor
                             (fn [^js e]
                               (prn "CALCULATED HEIGHT:" (.. e -nativeEvent -layout -height))
                               (let [keyboard-up? (< (.-value above-position) 0)]
                                 (if keyboard-up?
                                   (animate-above! above-position 0)
                                   (animate-above! above-position
                                                   (if
                                                    (or (zero? (- (.. e -nativeEvent -layout -height) ;; first time
                                                                  @initial-content-height))
                                                        ;; NOTE: for cases where the frame is taking the whole screen
                                                        ;; TODO: maybe read safe areas dynamically
                                                        (zero? (- (+ (.. e -nativeEvent -layout -height)
                                                                     (.. safe-area -initialWindowMetrics -insets -top)
                                                                     (.. safe-area -initialWindowMetrics -insets -bottom))
                                                                  @initial-content-height)))
                                                     0
                                                     (+ (- (.. e -nativeEvent -layout -height)
                                                           @initial-content-height)
                                                        above-offset))))))
                             #js[])
            [extra-padding set-extra-padding!] (useState 0)]
        (useLayoutEffect
         (fn []
           (some-> above-ref .-current (.measureInWindow #(set-extra-padding! %4)))
           (some-> content-ref .-current (.measureInWindow #(reset! initial-content-height %4)))
           js/undefined)
         #js[])

        [:rn/keyboard-avoiding-view {:style    {:flex 1}
                                     :behavior :padding}
         [:rn/scroll-view {:style                           {:flex 1}
                           :ref                             content-ref
                           :content-container-style         {:padding-bottom (- extra-padding above-offset)}
                           :shows-vertical-scroll-indicator false
                           :on-layout                       measure-layout!
                           :keyboard-should-persist-taps    :handled}
          child]
         [:animated/view {:ref       above-ref
                          :style     [{:position       :absolute
                                       :left           0
                                       :right          0
                                       :bottom         0
                                       :transform      [{:translate-y above-position}]
                                       :pointer-events :box-none}
                                      above-container-style]
                          :on-layout (fn [^js e]
                                       (set-extra-padding! (.. e -nativeEvent -layout -height)))}
          above]]))))
