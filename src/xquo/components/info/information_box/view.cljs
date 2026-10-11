(ns xquo.components.info.information-box.view
  (:require [react-native.utils :as rn.utils]
            [xquo.components.button.view :as button]
            [xquo.components.info.information-box.style :as style]
            [xquo.components.icon.view :as icon]
            [xquo.components.text.view :as text]
            [xquo.context :as context]))

(defn- leading-icon-view [{:keys [theme blur? status title size close-button icon]}]
  (let [icon-size (:size icon (cond
                                close-button 12
                                (= size 15)  20
                                :else        16))]
    [:rn/view {:style (style/leading-icon-slot title size icon-size)}
     [icon/view (assoc icon
                       :name  (:name icon :icon/info-outline)
                       :size  icon-size
                       :color (:color icon (style/icon-color theme blur? status)))]]))

(defn- close-button-view [{:keys [theme rich? close-button]}]
  (let [icon (:icon close-button)]
    [(if (:on-press close-button) :rn/pressable :rn/view)
     (-> close-button
         (dissoc :icon)
         (assoc :style (rn.utils/add-styles (style/close-slot rich?) (:style close-button))))
     [icon/view (assoc icon
                       :name  (:name icon :icon/cross)
                       :size  (:size icon 12)
                       :color (:color icon (style/foreground theme)))]]))

(defn- body-view [{:keys [theme blur? status title size]} content]
  (if (string? content)
    [text/text {:font (style/font size) :style (style/body-style theme blur? status title size)}
     content]
    content))

(defn information-box
  ([props] (information-box props nil))
  ([{:keys [status icon title description use-15-font? button close-button blur?]
     :or   {status :default}
     :as   props}
    content]
   (let [theme (context/use-theme-color)
         size  (:size props (if use-15-font? 15 13))
         rich? (or title button)]
     [:rn/view (-> props
                   (dissoc :status :icon :title :description :size :use-15-font? :button :close-button :blur? :background)
                   (assoc :style (rn.utils/add-styles (style/container theme blur? status rich? close-button)
                                                      (:style props))))
      [leading-icon-view {:theme        theme
                          :blur?        blur?
                          :status       status
                          :title        title
                          :size         size
                          :close-button close-button
                          :icon         icon}]
      [:rn/view {:style style/content}
       [:rn/view {:style style/copy}
        (when title
          [text/text {:font :font/medium-15 :style (style/title-style theme status)} title])
        [body-view {:theme theme :blur? blur? :status status :title title :size size}
         (or description content)]]
       (when button
         [button/button (-> button
                            (dissoc :label)
                            (assoc :size (:size button 24)
                                   :type (:type button (if (= status :error) :danger :primary))))
          (:label button)])]
      (when close-button
        [close-button-view {:theme theme :rich? rich? :close-button close-button}])])))
