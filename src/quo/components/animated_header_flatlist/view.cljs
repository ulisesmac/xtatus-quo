(ns quo.components.animated-header-flatlist.view
  (:require
    [oops.core :as oops]
    [quo.components.animated-header-flatlist.style :as style]
    [quo.components.icon :as icon]
    [quo.context :as quo.context]
    [quo.foundations.colors :as colors]
    [react-native.core :as rn]
    [react-native.fast-image :as fast-image]
    [react-native.platform :as platform]
    [react-native.reanimated :as reanimated]
    [react-native.safe-area :as safe-area]
    [reagent.core :as reagent]))

(def header-height 234)
(def cover-height 192)
(def blur-view-height 100)
(def threshold (- header-height blur-view-height))

(defn interpolate
  [value input-range output-range]
  (reanimated/interpolate value
                          input-range
                          output-range
                          {:extrapolateLeft  "clamp"
                           :extrapolateRight "clamp"}))

(defn scroll-handler
  [event initial-y scroll-y]
  (let [current-y (- (oops/oget event "nativeEvent.contentOffset.y") initial-y)]
    (reanimated/set-shared-value scroll-y current-y)))

(defn header
  [{:keys [theme-color f-display-picture-comp cover-uri title-comp theme]} top-inset scroll-y]
  (let [input-range        [0 (* threshold 0.33)]
        picture-scale-down 0.4
        size-animation     (interpolate scroll-y input-range [80 (* 80 picture-scale-down)])
        image-animation    (interpolate scroll-y input-range [72 (* 72 picture-scale-down)])
        border-animation   (interpolate scroll-y input-range [12 0])]
    [rn/view
     {:style {:height           header-height
              :background-color (or theme-color
                                    (colors/theme-colors colors/white colors/neutral-95 theme))
              :margin-top       (when platform/ios? (- top-inset))}}
     (when cover-uri
       [fast-image/fast-image
        {:style  {:width  "100%"
                  :height cover-height}
         :source {:uri cover-uri}}])
     [reanimated/view {:style (style/header-bottom-part border-animation theme)}
      [title-comp]]
     [reanimated/view {:style (style/entity-picture size-animation theme)}
      [:f> f-display-picture-comp image-animation]]]))

(defn- f-animated-header-list
  [{:keys [header-comp main-comp back-button-on-press] :as params}]
  (let [theme                   (quo.context/use-theme)
        window-height           (:height (rn/get-window))
        {:keys [top bottom]}    safe-area/insets
        ;; view height calculation is different because window height is different on iOS and
        ;; Android:
        view-height             (if platform/ios?
                                  (- window-height bottom)
                                  (+ window-height top))
        initial-y               (if platform/ios? (- top) 0)
        scroll-y                (reanimated/use-shared-value initial-y)
        opacity-animation       (interpolate scroll-y
                                             [(* threshold 0.33) (* threshold 0.66)]
                                             [0 1])
        translate-animation     (interpolate scroll-y [(* threshold 0.66) threshold] [100 56])
        title-opacity-animation (interpolate scroll-y [(* threshold 0.66) threshold] [0 1])]
    [rn/view {:style (style/container-view view-height)}
     [rn/touchable-opacity
      {:active-opacity 1
       :on-press       back-button-on-press
       :style          (style/button-container {:left 20 :theme theme})}
      [icon/icon :i/arrow-left {:size 20 :color (colors/theme-colors colors/black colors/white theme)}]]
     [rn/touchable-opacity
      {:active-opacity 1
       :style          (style/button-container {:right 20 :theme theme})}
      [icon/icon :i/options {:size 20 :color (colors/theme-colors colors/black colors/white theme)}]]
     [reanimated/blur-view
      {:blurAmount   32
       :blurType     :light
       :overlayColor (if platform/ios? colors/white-opa-70 :transparent)
       :style        (style/blur-view opacity-animation theme)}
      [reanimated/view {:style (style/header-comp translate-animation title-opacity-animation)}
       [header-comp]]]
     [reanimated/flat-list
      {:data                  [nil]
       :render-fn             main-comp
       :key-fn                str
       :header                (reagent/as-element (header (assoc params :theme theme) top scroll-y))
       ;; TODO: https://github.com/status-im/status-mobile/issues/14924
       :scroll-event-throttle 8
       :on-scroll             (fn [event] (scroll-handler event initial-y scroll-y))}]]))

(defn animated-header-list
  [params]
  [:f> f-animated-header-list params])
