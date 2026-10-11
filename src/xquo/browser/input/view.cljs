(ns xquo.browser.input.view
  (:require [clojure.string :as string]
            [react-native.react.core :as react]
            [react-native.utils :as rn.utils]
            [xquo.browser.input.style :as style]
            [xquo.components.favicon.view :as favicon]
            [xquo.components.icon.view :as icon]
            [xquo.components.text.view :as text]
            [xquo.context :as context]
            [xquo.foundations.colors :as colors]
            [xquo.foundations.typography :as typography]))

(defn- display-domain [value]
  (first (string/split (string/replace value #"^[a-zA-Z][a-zA-Z0-9+.-]*://" "") #"[/#?]")))

(defn input [{:keys [value on-change-text image secure? blur? placeholder on-focus on-blur]
              :or   {value "" placeholder "Search or enter dapp domain"}
              :as   props}]
  (let [{:keys [theme color dark-theme?]} (context/use-theme-color)
        [focused? set-focused!]           (react/use-state false)
        domain                            (display-domain value)
        cursor-level                      (if (and dark-theme? (or (not blur?) (empty? value))) 60 50)
        edit!                             (react/use-callback #(set-focused! true) [])
        blur!                             (react/use-callback (fn [event]
                                                                (set-focused! false)
                                                                (when on-blur (on-blur event)))
                                                              [on-blur])
        clear!                            (react/use-callback #(on-change-text "") [on-change-text])]
    [:rn/view {:style (rn.utils/add-styles style/container (:style props))}
     (if focused?
       [:rn/view {:style style/content}
        [:rn/text-input (-> props
                            (dissoc :image :secure? :blur? :style)
                            (assoc :value                  value
                                   :placeholder            placeholder
                                   :auto-focus             true
                                   :auto-capitalize        :none
                                   :auto-correct           false
                                   :select-text-on-focus   true
                                   :keyboard-type          :url
                                   :return-key-type        :go
                                   :on-focus               on-focus
                                   :on-blur                blur!
                                   :placeholder-text-color (style/placeholder-color theme blur?)
                                   :selection-color        (colors/get-color color 50 20)
                                   :cursor-color           (colors/get-color color cursor-level)
                                   :style                  (rn.utils/add-styles (typography/get-style :font/regular-15)
                                                                                style/input
                                                                                (style/text-color theme))))]
        (when (seq value)
          [:rn/pressable {:on-press clear! :hit-slop 8 :style style/clear}
           [icon/view {:name  :icon/cross-circle
                       :size  12
                       :color [(style/clear-color theme blur?)
                               (colors/get-color :color/white-100)]}]])]
       [:rn/pressable {:on-press edit! :style style/content}
        (if (seq value)
          [:rn/view {:style style/display}
           [:rn/view {:style style/address}
            [favicon/favicon {:content (if image
                                         {:type :image :image image}
                                         {:type :initials :text (string/upper-case (str (first domain)))})
                              :blur?   blur?}]
            [text/text {:font :font/medium-15 :number-of-lines 1 :style style/domain} domain]]
           (when secure?
             [icon/view {:name  :icon/lock
                         :size  16
                         :color (style/secondary-color theme blur?)
                         :style style/lock}])]
          [text/text {:font  :font/regular-15
                      :style [style/domain {:color (style/placeholder-color theme blur?)}]}
           placeholder])])]))
