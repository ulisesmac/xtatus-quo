(ns xquo.components.icon.svg
  (:require [reagent-extended-compiler.utils.transforms :refer [defstyle]]))

(def default-color "#09101C")
(def white-color "white")
(def loading-circle-background-color "#647084")

(defstyle loading-spin
  {:animation-name            {"0%"   {:transform [{:rotate "0deg"} {:scale 0.985}]}
                               "50%"  {:transform [{:rotate "180deg"} {:scale 1}]}
                               "100%" {:transform [{:rotate "360deg"} {:scale 0.985}]}}
   :animation-duration        "920ms"
   :animation-timing-function "linear"
   :animation-iteration-count "infinite"})

(defn- icon-color [color]
  (or color default-color))

(defn loading-12 [{:keys [color size style]}]
  (let [color (icon-color color)]
    [:animated/svg {:width   size
                    :height  size
                    :viewBox "0 0 12 12"
                    :fill    "none"
                    :style   [style loading-spin]}
     [:svg/path {:d            "M11 6C11 6.98891 10.7068 7.95561 10.1573 8.77785C9.60794 9.6001 8.82705 10.241 7.91342 10.6194C6.99979 10.9978 5.99445 11.0969 5.02455 10.9039C4.05464 10.711 3.16373 10.2348 2.46447 9.53553C1.7652 8.83627 1.289 7.94536 1.09607 6.97545C0.903147 6.00555 1.00216 5.00021 1.3806 4.08658C1.75904 3.17295 2.3999 2.39206 3.22215 1.84265C4.04439 1.29324 5.01109 1 6 1"
                 :stroke       color
                 :stroke-width 1.1}]
     [:svg/path {:opacity      0.2
                 :d            "M6 1A5 5 0 0 1 7.91342 1.3806"
                 :stroke       color
                 :stroke-width 1.1}]
     [:svg/path {:opacity      0.45
                 :d            "M7.91342 1.3806A5 5 0 0 1 9.53553 2.46447"
                 :stroke       color
                 :stroke-width 1.1}]
     [:svg/path {:opacity      0.7
                 :d            "M9.53553 2.46447A5 5 0 0 1 10.6194 4.08658"
                 :stroke       color
                 :stroke-width 1.1}]
     [:svg/path {:d            "M10.6194 4.08658A5 5 0 0 1 11 6"
                 :stroke       color
                 :stroke-width 1.1}]]))

(defn bridge-12 [{:keys [color size style]}]
  (let [color (icon-color color)]
    [:svg/svg {:width   size
               :height  size
               :viewBox "0 0 12 12"
               :fill    "none"
               :style   style}
     [:svg/defs
      [:svg/linear-gradient {:id             "xquo-bridge-12-gradient"
                             :x1             "7.8"
                             :y1             "2.7"
                             :x2             "9.3"
                             :y2             "4.8"
                             :gradient-units "userSpaceOnUse"}
       [:svg/stop {:stop-color   color
                   :stop-opacity 0}]
       [:svg/stop {:offset     "1"
                   :stop-color color}]]
      [:svg/mask {:id         "xquo-bridge-12-mask"
                  :mask-type  "alpha"
                  :mask-units "userSpaceOnUse"
                  :x          0
                  :y          0
                  :width      12
                  :height     12}
       [:svg/rect {:width     12
                   :height    12
                   :transform "matrix(4.37114e-08 -1 -1 -4.37114e-08 12 12)"
                   :fill      "url(#xquo-bridge-12-gradient)"}]]]
     [:svg/g {:mask "url(#xquo-bridge-12-mask)"}
      [:svg/path {:d              "M2.7 6C2.7 6 3.30021 3 6.00018 3C8.70018 3 9.3 6 9.3 6"
                  :stroke         color
                  :stroke-width   1.1
                  :stroke-linecap "round"}]]
     [:svg/circle {:cx           2.7
                   :cy           7.5
                   :r            1.5
                   :stroke       color
                   :stroke-width 1}]
     [:svg/circle {:cx           9.3
                   :cy           7.5
                   :r            1.5
                   :stroke       color
                   :stroke-width 1}]]))

(defn bridge-16 [{:keys [color size style]}]
  (let [color (icon-color color)]
    [:svg/svg {:width   size
               :height  size
               :viewBox "0 0 16 16"
               :fill    "none"
               :style   style}
     [:svg/path {:fill-rule "evenodd"
                 :clip-rule "evenodd"
                 :d         "M11.6 4.92363C12.2235 5.56968 13.1538 6.09994 14.5 6.09994V4.89994C13.243 4.89994 12.5387 4.2889 12.1242 3.66712C11.9114 3.34792 11.7761 3.02454 11.6942 2.77896C11.6536 2.65712 11.627 2.55707 11.611 2.48988C11.603 2.45636 11.5978 2.43125 11.5947 2.41597L11.5917 2.40066L11.5917 2.40078L11.5893 2.4013L10.4103 2.4013L10.4082 2.40087L10.408 2.40219L10.4048 2.41911C10.4016 2.43549 10.3963 2.46176 10.3884 2.49647C10.3727 2.56606 10.3471 2.66874 10.309 2.79335C10.2322 3.04477 10.1081 3.37391 9.91924 3.69759C9.54243 4.34352 8.95764 4.89988 8 4.89988C7.04236 4.89988 6.45757 4.34352 6.08076 3.69759C5.89195 3.37391 5.76782 3.04477 5.691 2.79335C5.65292 2.66874 5.62732 2.56606 5.61158 2.49647C5.60373 2.46176 5.59838 2.43549 5.59519 2.41911L5.59201 2.40219L5.59184 2.4013L5.59176 2.40087L5.58974 2.4013L4.41066 2.4013L4.40825 2.40078L4.40833 2.40031L4.40845 2.39959L4.40857 2.39893L4.40825 2.40066L4.4053 2.41597C4.40225 2.43125 4.39695 2.45636 4.38897 2.48988C4.37298 2.55707 4.3464 2.65712 4.30579 2.77896C4.22393 3.02454 4.08857 3.34792 3.87577 3.66712C3.46126 4.2889 2.75696 4.89994 1.5 4.89994V6.09994C2.84622 6.09994 3.77647 5.56968 4.4 4.92363V8.39994H2V9.59994H4.4V13.4999H5.6V9.59994H10.4V13.4999H11.6V9.59994H14V8.39994H11.6V4.92363ZM10.4 5.05556C9.86244 5.63123 9.08696 6.09988 8 6.09988C6.91304 6.09988 6.13755 5.63123 5.6 5.05556V8.39994H10.4V5.05556ZM10.4084 2.39967C10.4084 2.39964 10.4084 2.39964 10.4084 2.39968V2.39967Z"
                 :fill      color}]]))

(defn bridge-20 [{:keys [color size style]}]
  (let [color (icon-color color)]
    [:svg/svg {:width   size
               :height  size
               :viewBox "0 0 20 20"
               :fill    "none"
               :style   style}
     [:svg/defs
      [:svg/linear-gradient {:id             "xquo-bridge-20-gradient"
                             :x1             "13"
                             :y1             "4.5"
                             :x2             "15.5"
                             :y2             "8"
                             :gradient-units "userSpaceOnUse"}
       [:svg/stop {:stop-color   color
                   :stop-opacity 0}]
       [:svg/stop {:offset     "1"
                   :stop-color color}]]
      [:svg/mask {:id         "xquo-bridge-20-mask"
                  :mask-type  "alpha"
                  :mask-units "userSpaceOnUse"
                  :x          0
                  :y          0
                  :width      20
                  :height     20}
       [:svg/rect {:width     20
                   :height    20
                   :transform "matrix(4.37114e-08 -1 -1 -4.37114e-08 20 20)"
                   :fill      "url(#xquo-bridge-20-gradient)"}]]]
     [:svg/g {:mask "url(#xquo-bridge-20-mask)"}
      [:svg/path {:d      "M4.5 10C4.5 10 5.50035 5 10.0003 5C14.5003 5 15.5 10 15.5 10"
                  :stroke color}]]
     [:svg/circle {:cx           4.5
                   :cy           12.5
                   :r            2.5
                   :stroke       color
                   :stroke-width 1.2}]
     [:svg/circle {:cx           15.5
                   :cy           12.5
                   :r            2.5
                   :stroke       color
                   :stroke-width 1.2}]]))

(defn loading-16 [{:keys [color size style]}]
  (let [color (icon-color color)]
    [:animated/svg {:width   size
                    :height  size
                    :viewBox "0 0 16 16"
                    :fill    "none"
                    :style   [style loading-spin]}
     [:svg/path {:d            "M14.5332 7.99989C14.5332 9.29206 14.15 10.5552 13.4322 11.6296C12.7143 12.704 11.6939 13.5414 10.5001 14.0359C9.30627 14.5304 7.99264 14.6598 6.7253 14.4077C5.45795 14.1556 4.29382 13.5334 3.38012 12.6197C2.46642 11.7059 1.84418 10.5418 1.59209 9.27448C1.34 8.00713 1.46938 6.6935 1.96387 5.49969C2.45837 4.30588 3.29576 3.28551 4.37016 2.56762C5.44456 1.84973 6.70771 1.46655 7.99989 1.46655"
                 :stroke       color
                 :stroke-width 1.2}]
     [:svg/path {:opacity      0.2
                 :d            "M8 1.46655C8.85797 1.46655 9.70754 1.63554 10.5002 1.96387"
                 :stroke       color
                 :stroke-width 1.2}]
     [:svg/path {:opacity      0.45
                 :d            "M10.5002 1.96387C11.2929 2.2922 12.0131 2.77345 12.6198 3.38012"
                 :stroke       color
                 :stroke-width 1.2}]
     [:svg/path {:opacity      0.7
                 :d            "M12.6198 3.38012C13.2264 3.9868 13.7077 4.70703 14.036 5.49969"
                 :stroke       color
                 :stroke-width 1.2}]
     [:svg/path {:d            "M14.036 5.49969C14.3643 6.29235 14.5333 7.14192 14.5333 7.99989"
                 :stroke       color
                 :stroke-width 1.2}]]))

(defn loading-20 [{:keys [color size style]}]
  (let [color (icon-color color)]
    [:animated/svg {:width   size
                    :height  size
                    :viewBox "0 0 20 20"
                    :fill    "none"
                    :style   [style loading-spin]}
     [:svg/defs
      [:svg/linear-gradient {:id             "xquo-loading-20-gradient"
                             :x1             "11"
                             :y1             "3"
                             :x2             "15.3957"
                             :y2             "5.9094"
                             :gradient-units "userSpaceOnUse"}
       [:svg/stop {:stop-color   color
                   :stop-opacity 0}]
       [:svg/stop {:offset     "1"
                   :stop-color color}]]
      [:svg/mask {:id         "xquo-loading-20-mask"
                  :mask-type  "alpha"
                  :mask-units "userSpaceOnUse"
                  :x          9
                  :y          1
                  :width      10
                  :height     10}
       [:svg/rect {:x      9
                   :y      1
                   :width  10
                   :height 10
                   :fill   "url(#xquo-loading-20-gradient)"}]]]
     [:svg/path {:d            "M17 10C17 11.3845 16.5895 12.7378 15.8203 13.889C15.0511 15.0401 13.9579 15.9373 12.6788 16.4672C11.3997 16.997 9.99223 17.1356 8.63437 16.8655C7.2765 16.5954 6.02922 15.9287 5.05025 14.9497C4.07128 13.9708 3.4046 12.7235 3.1345 11.3656C2.86441 10.0078 3.00303 8.6003 3.53284 7.32122C4.06266 6.04213 4.95986 4.94888 6.11101 4.17971C7.26215 3.41054 8.61553 3 10 3"
                 :stroke       color
                 :stroke-width 1.2}]
     [:svg/g {:mask "url(#xquo-loading-20-mask)"}
      [:svg/path {:d            "M10 3C10.9193 3 11.8295 3.18106 12.6788 3.53284C13.5281 3.88463 14.2997 4.40024 14.9497 5.05025C15.5998 5.70026 16.1154 6.47194 16.4672 7.32122C16.8189 8.1705 17 9.08075 17 10"
                  :stroke       color
                  :stroke-width 1.2}]]]))

(defn loading-circle-20 [{:keys [size style]}]
  [:animated/svg {:width   size
                  :height  size
                  :viewBox "0 0 20 20"
                  :fill    "none"
                  :style   [style loading-spin]}
   [:svg/path {:fill-rule "evenodd"
               :clip-rule "evenodd"
               :d         "M3 10C3 6.13401 6.13401 3 10 3C13.866 3 17 6.13401 17 10C17 13.866 13.866 17 10 17C6.13401 17 3 13.866 3 10Z"
               :fill      loading-circle-background-color}]
   [:svg/path {:d            "M14 10C14 10.7911 13.7654 11.5645 13.3259 12.2223C12.8864 12.8801 12.2616 13.3928 11.5307 13.6955C10.7998 13.9983 9.99556 14.0775 9.21964 13.9231C8.44371 13.7688 7.73098 13.3878 7.17157 12.8284C6.61216 12.269 6.2312 11.5563 6.07686 10.7804C5.92252 10.0044 6.00173 9.20017 6.30448 8.46927C6.60723 7.73836 7.11992 7.11365 7.77772 6.67412C8.43552 6.2346 9.20887 6 10 6"
               :stroke       white-color
               :stroke-width 1.2}]
   [:svg/path {:opacity      0.2
               :d            "M10 6C10.5253 6 11.0454 6.10346 11.5307 6.30448"
               :stroke       white-color
               :stroke-width 1.2}]
   [:svg/path {:opacity      0.55
               :d            "M11.5307 6.30448C12.016 6.5055 12.457 6.80014 12.8284 7.17157"
               :stroke       white-color
               :stroke-width 1.2}]
   [:svg/path {:d            "M12.8284 7.17157C13.5714 7.91444 14 8.92893 14 10"
               :stroke       white-color
               :stroke-width 1.2}]])

(defn bridge-blur-20 [{:keys [color size style]}]
  (let [color (icon-color color)]
    [:svg/svg {:width   size
               :height  size
               :viewBox "0 0 20 20"
               :fill    "none"
               :style   style}
     [:svg/defs
      [:svg/linear-gradient {:id             "xquo-bridge-blur-20-gradient"
                             :x1             "13"
                             :y1             "4.5"
                             :x2             "15.5"
                             :y2             "8"
                             :gradient-units "userSpaceOnUse"}
       [:svg/stop {:stop-color   color
                   :stop-opacity 0}]
       [:svg/stop {:offset     "1"
                   :stop-color color}]]
      [:svg/mask {:id         "xquo-bridge-blur-20-mask"
                  :mask-type  "alpha"
                  :mask-units "userSpaceOnUse"
                  :x          0
                  :y          0
                  :width      20
                  :height     20}
       [:svg/rect {:x         -0.5
                   :y         -0.5
                   :width     19
                   :height    19
                   :transform "matrix(4.37114e-08 -1 -1 -4.37114e-08 19 19)"
                   :fill      "url(#xquo-bridge-blur-20-gradient)"
                   :stroke    "#647084"}]]]
     [:svg/g {:mask "url(#xquo-bridge-blur-20-mask)"}
      [:svg/path {:fill-rule "evenodd"
                  :clip-rule "evenodd"
                  :d         "M4.99017 10.0984C4.9902 10.0983 4.99024 10.0981 4.49996 10C4.00967 9.90191 4.00972 9.90168 4.00977 9.90144L4.00988 9.90085L4.01019 9.89933L4.0111 9.89493L4.01409 9.88075C4.01662 9.86896 4.02024 9.85252 4.02502 9.83174C4.03458 9.7902 4.0488 9.73125 4.06833 9.65737C4.10736 9.50973 4.16775 9.30179 4.25465 9.05355C4.42789 8.5587 4.7095 7.89499 5.14342 7.22749C6.01477 5.8871 7.52768 4.5 10.0003 4.5C12.4729 4.5 13.9857 5.88711 14.8569 7.22753C15.2907 7.89504 15.5722 8.55875 15.7454 9.05361C15.8323 9.30185 15.8926 9.50979 15.9316 9.65743C15.9511 9.73131 15.9654 9.79026 15.9749 9.83181C15.9797 9.85258 15.9833 9.86902 15.9858 9.88082L15.9888 9.89499L15.9897 9.8994L15.99 9.90092L15.9902 9.9015C15.9902 9.90175 15.9903 9.90198 15.5 10C15.0097 10.098 15.0097 10.0982 15.0097 10.0984L15.0096 10.0978L15.0081 10.0905C15.0065 10.0834 15.004 10.0717 15.0003 10.0559C14.9931 10.0242 14.9814 9.97572 14.9648 9.91288C14.9316 9.78709 14.8787 9.6044 14.8015 9.3839C14.6466 8.94125 14.397 8.35496 14.0184 7.77247C13.2647 6.61289 12.0277 5.5 10.0003 5.5C7.97293 5.5 6.73567 6.61291 5.98184 7.77252C5.60318 8.35501 5.35345 8.9413 5.19849 9.38396C5.1213 9.60446 5.06838 9.78715 5.03512 9.91295C5.01851 9.97579 5.00685 10.0243 4.99956 10.056C4.99591 10.0718 4.99336 10.0834 4.99183 10.0906L4.9903 10.0978L4.99017 10.0984Z"
                  :fill      color}]]
     [:svg/circle {:cx           4.5
                   :cy           12.5
                   :r            2.5
                   :stroke       color
                   :stroke-width 1.2}]
     [:svg/circle {:cx           15.5
                   :cy           12.5
                   :r            2.5
                   :stroke       color
                   :stroke-width 1.2}]]))

(def icons
  {12 {:icon/bridge  bridge-12
       :icon/loading loading-12}
   16 {:icon/bridge  bridge-16
       :icon/loading loading-16}
   20 {:icon/bridge         bridge-20
       :icon/bridge-blur    bridge-blur-20
       :icon/loading        loading-20
       :icon/loading-circle loading-circle-20
       :icon/loading-fade   loading-20}})

(def iconset (->> icons vals (mapcat keys) set))
