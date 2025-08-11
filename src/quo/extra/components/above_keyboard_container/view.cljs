(ns quo.extra.components.above-keyboard-container.view
  (:require ["react" :refer [useLayoutEffect useEffect useState useCallback useRef]]
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
    (fn [{:keys [content-ref above above-container-style offset]} child]
      (let [above-ref       ^js (useRef)
            ^js content-ref (or content-ref (useRef))
            above-position  ^js (useSharedValue 0)
            measure-layout! (useCallback
                             ;; TODO: check double re-render
                             (fn [^js e]
                               (let [keyboard-up?  (.isVisible Keyboard)
                                     layout-height (.. e -nativeEvent -layout -height)]
                                 (if-not keyboard-up?
                                   (animate-above! above-position 0)
                                   (animate-above! above-position (+ (- layout-height @initial-content-height)
                                                                     offset)))))
                             #js[])
            [extra-padding set-extra-padding!] (useState 0)]
        (useLayoutEffect
         (fn []
           (when (.. above-ref -current -measureInWindow)
             (some-> above-ref .-current (.measureInWindow #(set-extra-padding! %4)))
             (some-> content-ref .-current (.measureInWindow #(reset! initial-content-height %4))))
           js/undefined)
         #js[])

        [:rn/keyboard-avoiding-view {:style       {:flex 1}
                                     :behavior    :padding
                                     :collapsable false}
         [:rn/scroll-view {:style                           {:flex 1}
                           :ref                             content-ref
                           :content-container-style         {:padding-bottom (- extra-padding offset)}
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
