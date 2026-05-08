(ns xquo.extra.components.above-keyboard-container.view
  (:require ["react" :refer [useLayoutEffect useEffect useState useCallback useRef]]
            ["react-native-reanimated" :refer [useSharedValue withSpring]]
            ["react-native" :refer [Keyboard]]))
;; TODO: As soon as the refactor starts:
;; Export a React Native lib for ClojureScript

(def animation-params #js{:mass      1
                          :damping   18
                          :stiffness 150})

(defn view [_ _]
  (let [initial-content-height (atom nil)
        animate-above!         #(set! (.-value %1) (withSpring %2 animation-params))]
    (fn [{:keys [content-ref above above-container-style offset skip-scroll-view?
                 scroll-component gap-to-window-top flat-list-props flat-list-component]}
         child]
      (let [above-ref                      ^js (useRef)
            ^js content-ref                (or content-ref (useRef))
            above-position                 ^js (useSharedValue 0)
            measure-layout!                (useCallback
                                            ;; TODO: check double re-render
                                            (fn [^js e]
                                              (let [keyboard-up?  (.isVisible Keyboard)
                                                    layout-height (.. e -nativeEvent -layout -height)]
                                                (if-not keyboard-up?
                                                  (animate-above! above-position 0)
                                                  (animate-above! above-position (+ (- layout-height @initial-content-height)
                                                                                    offset
                                                                                    (- (or gap-to-window-top 0)))))))
                                            #js[])
            [extra-padding set-extra-padding!] (useState 0)
            bottom-padding                 (+ (- extra-padding offset)
                                              (or gap-to-window-top 0))
            container-component            (cond
                                             flat-list-props   (or flat-list-component :rn/flat-list)
                                             skip-scroll-view? :rn/view
                                             scroll-component  scroll-component
                                             :else             :rn/scroll-view)]
        (useLayoutEffect
         (fn []
           (let [content-current (.-current content-ref)
                 ^js content-node (if (and flat-list-props
                                           (some-> content-current .-getNativeScrollRef))
                                    (.getNativeScrollRef content-current)
                                    content-current)]
             (some-> above-ref .-current (.measureInWindow #(set-extra-padding! %4)))
             (some-> content-node (.measureInWindow #(reset! initial-content-height %4))))
           js/undefined)
         #js[])
        [:rn/keyboard-avoiding-view {:style       {:flex 1}
                                     :behavior    :padding
                                     :collapsable false}
         (if flat-list-props
           [container-component (-> flat-list-props
                                    (dissoc :style :content-container-style)
                                    (assoc :style                           [{:flex 1} (:style flat-list-props)]
                                           :ref                             content-ref
                                           :content-container-style         [{:padding-bottom bottom-padding}
                                                                             (:content-container-style flat-list-props)]
                                           :shows-vertical-scroll-indicator false
                                           :on-layout                       measure-layout!
                                           :keyboard-should-persist-taps    :handled))]
           [container-component {:style                           {:flex 1}
                                 :ref                             content-ref
                                 :content-container-style         {:padding-bottom bottom-padding}
                                 :shows-vertical-scroll-indicator false
                                 :on-layout                       measure-layout!
                                 :keyboard-should-persist-taps    :handled}
            child])
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
