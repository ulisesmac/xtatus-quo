(ns quo.extra.components.above-keyboard-container.view
  (:require ["react" :refer [useLayoutEffect useState useCallback useRef]]
            ["react-native-reanimated" :refer [useSharedValue withSpring]]))

;; TODO: As soon as the refactor starts:
;; Export a React Native lib for ClojureScript

(defn view [_ _]
  (let [initial-content-height (atom nil)]
    (fn [{:keys [above above-container-style]} child]
      (let [above-ref                ^js (useRef)
            content-ref              ^js (useRef)
            above-position           ^js (useSharedValue 0)
            animate-above-container! (useCallback
                                      (fn [^js e]
                                        (set! (.-value above-position)
                                              (withSpring (- (.. e -nativeEvent -layout -height)
                                                             @initial-content-height)
                                                          #js{:mass      1
                                                              :damping   18
                                                              :stiffness 150})))
                                      #js[])
            [extra-padding set-extra-padding] (useState 0)]
        (useLayoutEffect
         (fn []
           (some-> above-ref .-current (.measureInWindow #(set-extra-padding %4)))
           (some-> content-ref .-current (.measureInWindow #(reset! initial-content-height %4)))
           js/undefined)
         #js[])
        [:rn/keyboard-avoiding-view {:style    {:flex 1}
                                     :behavior :padding}
         [:rn/scroll-view {:style                           {:flex 1}
                           :ref                             content-ref
                           :content-container-style         {:padding-bottom extra-padding}
                           :shows-vertical-scroll-indicator false
                           :on-layout                       animate-above-container!
                           :keyboard-should-persist-taps    :handled
                           }
          child]
         [:animated/view {:ref   above-ref
                          :style [{:position       :absolute
                                   :left           0
                                   :right          0
                                   :bottom         0
                                   :transform      [{:translate-y above-position}]
                                   :pointer-events :box-none}
                                  above-container-style]}
          above]]))))
