(ns xquo.components.avatar.view
  (:require [react-native.react.core :as react]
            [react-native.utils :as rn.utils]
            [xquo.components.avatar.geometry :as geometry]
            [xquo.components.avatar.style :as style]
            [xquo.components.icon.view :as icon]
            [xquo.components.text.view :as text]
            [xquo.context :as context]
            [xquo.foundations.colors :as colors]))

(defn- outline [{:keys [shape size radius inset] :or {inset 0} :as props}]
  (let [props (dissoc props :shape :size :radius :inset)]
    (case shape
      :circle
      [:svg/circle (assoc props :cx (/ size 2) :cy (/ size 2) :r (- (/ size 2) inset))]

      :rounded-square
      [:svg/rect (assoc props
                        :x      inset
                        :y      inset
                        :width  (- size (* 2 inset))
                        :height (- size (* 2 inset))
                        :rx     (- radius inset))]

      :hexagon
      [:svg/path (assoc props :d (geometry/hexagon-path size))])))

(defn- surface [{:keys [size shape radius fill border border-color image badge-metrics]}]
  (let [badge-size   (:size badge-metrics)
        badge-center (when badge-metrics
                       (- size (:offset badge-metrics) (/ badge-size 2)))]
    [:svg/svg {:key [shape size radius] :width size :height size :view-box (str "0 0 " size " " size)}
     [:svg/defs
      [:svg/clip-path {:id "avatar-shape"}
       [outline {:shape shape :size size :radius radius}]]
      (when badge-metrics
        [:svg/mask {:id "avatar-badge" :x 0 :y 0 :width size :height size :mask-units :userSpaceOnUse}
         [:svg/rect {:width size :height size :fill :white}]
         [:svg/circle {:cx   badge-center
                       :cy   badge-center
                       :r    (+ (/ badge-size 2) (:gap badge-metrics))
                       :fill :black}]])]
     [:svg/g {:clip-path "url(#avatar-shape)"
              :mask      (when badge-metrics "url(#avatar-badge)")}
      [outline {:shape shape :size size :radius radius :fill fill}]
      (when image
        [:svg/image {:href                  (or (:source image) {:uri (:url image)})
                     :width                 size
                     :height                size
                     :preserve-aspect-ratio "xMidYMid slice"
                     :style                 (:style image)}])
      (when border
        [outline {:shape            shape
                  :size             size
                  :radius           radius
                  :inset            0.5
                  :fill             :none
                  :stroke           border-color
                  :stroke-width     (if (= shape :hexagon) 2 1)
                  :stroke-dasharray (when (= border :dashed) "3 4")}])]]))

(defn- content-view [{:keys [content size shape color variant]}]
  (case (:type content)
    :icon
    (let [props (:icon content)
          sizes (if (= variant :solid) style/solid-icon-size style/icon-size)]
      [icon/view (assoc props
                        :size  (:size props (sizes size))
                        :color (:color props color))])

    :emoji
    [:rn/text {:style (style/emoji size shape)}
     (:text content)]

    :emoji-image
    (let [image (:image content)]
      [:rn/image {:source      (or (:source image) {:uri (:url image)})
                  :resize-mode :contain
                  :style       (rn.utils/add-styles (style/emoji-image size shape) (:style image))}])

    :initials
    (let [fonts (if (= variant :solid) style/initials-font style/soft-initials-font)]
      [text/text {:font  (fonts size)
                  :style [(style/initials color)
                          (when (= shape :circle) style/wallet-initials)
                          (when (= size 36) style/notification-initials)]}
       (:text content)])

    nil))

(def ^:private activity-status-icons
  {:pending   :icon/pending-alt
   :confirmed :icon/check-circle-outline-alt
   :finalized :icon/diamond
   :error     :icon/alert-outline-alt})

(defn- badge-view [{:keys [badge metrics theme blur? color]}]
  (let [size        (:size metrics)
        status      (:status badge)
        status-icon (activity-status-icons status)
        fill        (cond
                      status-icon        "transparent"
                      (= :online status) (colors/get-color :color/success-50)
                      status             (colors/get-color :color/neutral-40)
                      :else              "transparent")]
    [:rn/view {:pointer-events :none
               :style          [(style/badge-position (:offset metrics)) (style/badge size fill)]}
     (cond
       status-icon
       [icon/view {:name  status-icon
                   :size  size
                   :color (style/activity-status-color theme blur? color status)}]

       status
       nil

       (:image badge)
       [:rn/image {:source      (or (get-in badge [:image :source]) {:uri (get-in badge [:image :url])})
                   :resize-mode :cover
                   :style       (rn.utils/add-styles (style/frame size) (get-in badge [:image :style]))}]

       (:icon badge)
       (let [props (:icon badge)]
         [icon/view (assoc props
                           :size  (:size props (max 8 (- size 4)))
                           :color (:color props (colors/themed theme :color/neutral-50 :color/neutral-40)))]))]))

(defn avatar [{:keys        [size shape content color variant border blur? badge on-press]
               caller-style :style
               :or          {size 32 shape :circle variant :soft}}]
  (let [{:keys [theme] context-color :color} (context/use-theme-color)
        color                                (or color context-color)
        [pressed? set-pressed!]              (react/use-state false)
        on-press-in!                         (react/use-callback #(set-pressed! true) [])
        on-press-out!                        (react/use-callback #(set-pressed! false) [])
        radius                               (case (:type content)
                                               :image (/ size 4)
                                               nil 6
                                               (style/corner-radius size))
        badge-metrics                        (cond
                                               (activity-status-icons (:status badge))
                                               (style/activity-status-badge size)

                                               (:status badge)
                                               (style/status-badge size)

                                               (:image badge)
                                               (style/image-badge size)

                                               (:icon badge)
                                               (style/icon-badge size))]
    [(if on-press :animated/pressable :rn/view)
     (cond-> {:style (rn.utils/add-styles (style/frame size)
                                          caller-style
                                          (when on-press (style/press-state pressed?)))}
       on-press (assoc :on-press     on-press
                       :on-press-in  on-press-in!
                       :on-press-out on-press-out!))
     [:rn/view {:pointer-events :none}
      [surface {:size          size
                :shape         shape
                :radius        radius
                :border        border
                :fill          (if (= :image (:type content))
                                 (colors/get-color color)
                                 (style/fill-color theme blur? color variant shape (:type content)))
                :border-color  (style/border-color theme blur? border content color)
                :image         (when (= :image (:type content)) (:image content))
                :badge-metrics badge-metrics}]
      [:rn/view {:style style/content}
       [content-view {:content content
                      :shape   shape
                      :size    size
                      :variant variant
                      :color   (style/foreground-color theme color variant (:type content) shape)}]]
      (when on-press
        [:animated/view {:style [style/content (style/press-overlay pressed?)]}
         [surface {:size          size
                   :shape         shape
                   :radius        radius
                   :fill          (colors/get-color :color/neutral-80-20)
                   :badge-metrics badge-metrics}]])]
     (when badge-metrics
       [badge-view {:badge badge :metrics badge-metrics :theme theme :blur? blur? :color context-color}])]))
