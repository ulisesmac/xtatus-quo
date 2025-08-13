(ns xtatus-quo.components.colors.color-picker.view
  (:require
   [oops.core :as oops]
   [quo.foundations.colors :as colors]
   [react-native.core :as rn]
   [reagent.core :as reagent]
   [xtatus-quo.components.icon :as icons]
   [xtatus-quo.components.colors.color-picker.style :as style]))

(defn color-selected-border [theme color]
  [:rn/view {:style style/color-selected-border}
   [:rn/view {:style (style/selected-left theme color)}]
   [:rn/view {:style (style/selected-right theme color)}]])

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
    [:rn/pressable {:style              style/item-container
                    :accessibility-label :color-picker-item
                    :on-press           on-select!}
     (when selected?
       [color-selected-border theme color])
     [:rn/view {:style (style/color-circle theme color)}
      (when selected?
        [icons/icon :i/check {:size  20
                              :color colors/white}])]]))

(defn get-item-layout [_data index]
  #js{:length style/item-outer-size
      :offset (+ (* style/item-outer-size index)
                 (* style/content-gap index)
                 style/content-padding-h)
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
                            (when (pos? default-index)
                              (scroll-to-index ref default-index)))
                          350))
         [])
        [:gh/flat-list {:ref                               ref
                        :content-container-style           style/content-container
                        :data                              (with-meta colors/account-colors {:keep-items true})
                        :render-item                       render-item-fn
                        :key-extractor                     str
                        :get-item-layout                   get-item-layout
                        :horizontal                        true
                        :shows-horizontal-scroll-indicator false}]))))
