(ns xquo.demo.navigation.bottom-nav
  (:require [react-native.react.core :as react]
            [react-native.utils :refer [defstyle style]]
            [xquo.context :as context]
            [xquo.core :as xquo]
            [xquo.demo.controls :as controls]
            [xquo.demo.screen :as screen]))

(def ^:private icons
  [:icon/wallet :icon/messages :icon/multi-profile :icon/browser :icon/calendar])

(defstyle ^:private preview
  {:width           "100%"
   :min-height      200
   :align-items     :center
   :justify-content :flex-end})

(defn- selected! [set-selected! index]
  (set-selected! index)
  (js/alert "Pressed"))

(defn- preview-bar [{:keys [item-count selected on-select badges? disabled?]}]
  (let [items (mapv (fn [index]
                      (cond-> {:icon      {:name (icons index)}
                               :active?   (= index selected)
                               :disabled? disabled?
                               :on-press  (partial selected! on-select index)}
                        (and badges? (= index 1)) (assoc :badge {:counter {:value 5}})
                        (and badges? (= index 2)) (assoc :badge {:type :notification})))
                    (range item-count))]
    [xquo/navigation.bottom-nav {:items items}]))

(defn screen [_]
  (let [[item-count set-item-count!]  (react/use-state 4)
        [selected set-selected!]      (react/use-state 0)
        [badges? set-badges!]         (react/use-state true)
        [disabled? set-disabled!]     (react/use-state nil)
        [narrow? set-narrow!]         (react/use-state nil)
        [color set-color!]            (react/use-state :color/primary)]
    [screen/screen {:title   "Bottom nav"
                    :photo?  true
                    :preview [context/provider {:color color}
                              [:rn/view {:style [preview (when narrow? (style {:max-width 240 :align-self :center}))]}
                               [preview-bar {:item-count item-count
                                             :selected   selected
                                             :on-select  set-selected!
                                             :badges?    badges?
                                             :disabled?  disabled?}]]]}
     [controls/choice {:label     "Tabs"
                       :value     item-count
                       :on-change set-item-count!
                       :options   [{:value 2 :label "2"}
                                   {:value 3 :label "3"}
                                   {:value 4 :label "4"}
                                   {:value 5 :label "5"}]}]
     [controls/choice {:label     "Customization color"
                       :value     color
                       :on-change set-color!
                       :options   [{:value :color/primary :label "Primary"}
                                   {:value :color/success :label "Green"}
                                   {:value :color/danger :label "Red"}]}]
     [controls/checkbox {:label "Badges" :value badges? :on-change set-badges!}]
     [controls/checkbox {:label "240 px container" :value narrow? :on-change set-narrow!}]
     [controls/checkbox {:label ":disabled?" :value disabled? :on-change set-disabled!}]]))
