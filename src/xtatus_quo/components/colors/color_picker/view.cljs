(ns xtatus-quo.components.colors.color-picker.view
  (:require
   [oops.core :as oops]
   [quo.foundations.colors :as colors]
   [react-native.core :as rn]
   [reagent.core :as reagent]
   [xtatus-quo.components.icon :as icons]))

;; TODO: separate the styles into another namespace as the pattern in this repo.
;; Optimize style redefinitions.

(defn color-selected-border [theme color]
  [:rn/view {:style {:flex-direction :row
                     :flex           1}}
   [:rn/view {:style {:height                    48
                      :flex                      1
                      :background-color          (colors/resolve-color color theme 20)
                      :border-top-left-radius    24
                      :border-bottom-left-radius 24}}]
   [:rn/view {:style {:height                     48
                      :flex                       1
                      :background-color           (colors/resolve-color color theme 40)
                      :border-top-right-radius    24
                      :border-bottom-right-radius 24}}]])

(defn scroll-to-index [^js ref index]
  (when (.. ref -current -scrollToIndex)
    (.. ref -current (scrollToIndex #js{:animated     true
                                        :index        index
                                        :viewPosition 0.5}))))

(defn color-item [{:keys [color selected set-selected! on-change ^js ref item-index]}]
  (let [theme      (quo.context/use-theme)
        selected?  (= @selected color)
        on-select! (rn/use-callback
                    (fn []
                      (set-selected! color)
                      (scroll-to-index ref item-index)
                      (on-change color))
                    [])]
    [:rn/pressable {:style    {:width          48
                               :height         48
                               :flex-direction :row}
                    :on-press on-select!}
     (when selected?
       [color-selected-border theme color])
     [:rn/view {:style {:position         :absolute
                        :top              0
                        :left             0
                        :width            40
                        :height           40
                        :background-color (colors/resolve-color color theme)
                        :border-radius    20
                        :transform        [{:translate-x 4} {:translate-y 4}]
                        :justify-content  :center
                        :align-items      :center}}
      (when selected?
        [icons/icon :i/check {:size  20
                              :color colors/white}])]]))

(defn get-item-layout [_data index]
  #js{:length 48
      :offset (+ (* 48 index)
                 (* 8 index) ;; spacing
                 16) ;; left padding
      :index  index})

(defn view [{:keys [default-selected]}]
  (let [selected      (reagent/atom default-selected)
        set-selected! #(reset! selected %)]
    (fn [{:keys [on-change]}]
      (let [ref            (rn/use-ref nil)
            default-index  (.indexOf colors/account-colors default-selected)
            render-item-fn (rn/use-callback
                            (fn [js-data]
                              (let [item (oops/oget js-data "item")]
                                (reagent/as-element [color-item {:color         item
                                                                 :selected      selected
                                                                 :set-selected! set-selected!
                                                                 :on-change     on-change
                                                                 :ref           ref
                                                                 :item-index    (.indexOf colors/account-colors item)}])))
                            [on-change])]
        (rn/use-effect
         (fn []
           (js/setTimeout (fn []
                            (scroll-to-index ref default-index))
                          300))
         [])
        [:gh/flat-list {:ref                               ref
                        :content-container-style           {:column-gap         8
                                                            :padding-vertical   8
                                                            :padding-horizontal 16}
                        :data                              (with-meta colors/account-colors {:keep-items true})
                        :render-item                       render-item-fn
                        :key-extractor                     str
                        :get-item-layout                   get-item-layout
                        :horizontal                        true
                        :shows-horizontal-scroll-indicator false}]))))
