(ns quo.extra.components.above-keyboard-container.view
  (:require ["react" :refer [useLayoutEffect useEffect useState useCallback useRef]]
            ["react-native-reanimated" :refer [useSharedValue withSpring]]
            ["react-native" :refer [useWindowDimensions Keyboard]]))

;; TODO: As soon as the refactor starts:
;; Export a React Native lib for ClojureScript

(def animation-params #js{:mass      1
                          :damping   18
                          :stiffness 150})

(defn view
  "Render `child` as the main content area and `above` as a bottom overlay that
  moves with the keyboard.

  Intended use:
  - Put the main screen content in `child`
  - Put a bottom action area, input row, or custom keyboard-aware panel in `above`

  Props:
  - `:above` hiccup rendered above the main content
  - `:offset` extra bottom spacing added when the keyboard is open
  - `:skip-scroll-view?` when true, wrap `child` in `:rn/view` instead of
    `:rn/scroll-view`
  - `:content-ref` optional ref for the main content wrapper
  - `:above-container-style` optional style for the floating `above` container
  - `:fill-space-on-open?` when true, the `above` container expands upward into
    the remaining keyboard-adjusted space while the keyboard is open

  Behavior:
  - When the keyboard is closed, `above` behaves like a bottom overlay/footer
  - When the keyboard opens, `above` is translated upward by the keyboard shift
  - For scroll-view consumers, bottom padding is derived from the closed-keyboard
    `above` height so content stays visible above the overlay"
  [_ _]
  (let [initial-content-height (atom nil)
        animate-above!         #(set! (.-value %1) (withSpring %2 animation-params))]
    (fn [{:keys [content-ref above above-container-style offset skip-scroll-view?
                 fill-space-on-open?]}
         child]
      (let [above-ref           ^js (useRef)
            ^js content-ref     (or content-ref (useRef))
            above-position      ^js (useSharedValue 0)
            [open-kb-remaining-height
             set-open-kb-remaining-height!] (useState nil)
            measure-layout!     (useCallback
                                 ;; TODO: check double re-render
                                 (fn [^js e]
                                   (let [keyboard-up?           (.isVisible Keyboard)
                                         current-content-height (.. e -nativeEvent -layout -height)
                                         ;;
                                         kb-height              (- current-content-height @initial-content-height)
                                         space-used-by-kb       (- @initial-content-height current-content-height offset)]
                                     (if-not keyboard-up?
                                       (do
                                         (animate-above! above-position 0)
                                         (when fill-space-on-open?
                                           (set-open-kb-remaining-height! nil)))
                                       (do
                                         (animate-above! above-position (+ kb-height offset))
                                         (when fill-space-on-open?
                                           (set-open-kb-remaining-height! space-used-by-kb))))))
                                 #js[])
            [extra-padding set-extra-padding!] (useState 0)
            container-component (if skip-scroll-view? :rn/view :rn/scroll-view)]
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
         [container-component {:style                           {:flex 1}
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
                                      (when (and fill-space-on-open? open-kb-remaining-height)
                                        {:top open-kb-remaining-height})
                                      above-container-style]
                          :on-layout (fn [^js e]
                                       (set-extra-padding! (.. e -nativeEvent -layout -height)))}
          above]]))))
