(ns xquo.demo.controls
  (:require [react-native.react.core :as react]
            [xquo.core :as xquo]
            [xquo.demo.style :as style]))

(defn checkbox [{:keys [label value on-change]}]
  [xquo/settings-item {:title label
                       :image {:type :none}
                       :right {:action {:type          :selector
                                        :selector-type :checkbox
                                        :selected?     value
                                        :on-select     on-change}}}])

(defn- icon-option [{:keys [icon value on-change]}]
  (let [on-press! (react/use-callback #(on-change icon) [icon on-change])]
    [xquo/button {:type     (if (= icon value) :primary :grey)
                  :size     32
                  :icon     (when icon {:name icon})
                  :on-press on-press!}
     (when-not icon "None")]))

(defn icon-picker [{:keys [label value on-change]}]
  [:rn/view {:style style/field}
   [xquo/text {:font :font/medium-13}
    label]
   [:rn/view {:style style/choices}
    (for [icon [nil :icon/external :icon/arrow-right :icon/info]]
      ^{:key (str icon)}
      [icon-option {:icon icon :value value :on-change on-change}])]])

(defn- choice-option [{:keys [option value on-change]}]
  (let [option-value (:value option)
        on-press!    (react/use-callback #(on-change option-value) [option-value on-change])]
    [xquo/button {:type     (if (= value option-value) :primary :grey)
                  :size     32
                  :on-press on-press!}
     (:label option)]))

(defn choice [{:keys [label value options on-change]}]
  [:rn/view {:style style/field}
   [xquo/text {:font :font/medium-13}
    label]
   [:rn/view {:style style/choices}
    (for [option options]
      ^{:key (str (:value option))}
      [choice-option {:option option :value value :on-change on-change}])]])
