(ns xquo.demo.navigation.page-nav
  (:require [react-native.react.core :as react]
            [react-native.utils :refer [style]]
            [xquo.context :as context]
            [xquo.core :as xquo]
            [xquo.demo.assets :as assets]
            [xquo.demo.controls :as controls]
            [xquo.demo.screen :as screen]))

(defn- pressed! [] (js/alert "Pressed"))

(defn- preview [image-background?]
  (style {:margin-horizontal (when image-background? -20)}))

(def ^:private action-icons
  [:icon/search :icon/options :icon/edit])

(defn- preview-nav [{:keys [pattern title description centered? action-count surface disabled? banner?]}]
  (let [blur?   (= surface :blur)
        photo?  (= surface :photo)
        content (case pattern
                  :channel {:type   :avatar
                            :avatar {:size    20
                                     :content {:type :emoji :text "🌎"}}}
                  :message {:type   :avatar
                            :avatar {:size    32
                                     :content {:type  :image
                                               :image {:source assets/profile}}}}
                  :token {:type  :image
                          :image {:size 16 :source assets/profile-testing-2}}
                  :community {:type   :avatar
                              :avatar {:size    24
                                       :shape   :circle
                                       :content {:type :initials :text "C"}}}
                  nil)
        center  (cond-> {:title      title
                         :text-align (if centered? :center :left)
                         :disabled?  disabled?
                         :on-press   pressed!}
                  content (assoc :content content)
                  (#{:message :description} pattern) (assoc :description description)
                  (= pattern :token) (assoc :caption "TOK")
                  (#{:channel :message} pattern) (assoc :icons [{:name :icon/lock}]))
        right   (mapv (fn [index]
                        {:icon     {:name (action-icons index)}
                         :on-press pressed!})
                      (range action-count))]
    [xquo/navigation.page-nav {:left   {:icon {:name :icon/arrow-left} :on-press pressed!}
                               :center (case pattern
                                         :empty nil
                                         :dropdown [xquo/dropdown.button {:type     :ghost
                                                                          :size     32
                                                                          :blur?    blur?
                                                                          :photo?   photo?
                                                                          :on-press pressed!}
                                                    title]
                                         center)
                               :right  right
                               :blur?  blur?
                               :photo? photo?}
     (when banner?
       [xquo/banner.standard {:title "Community information" :blur? blur? :on-press pressed!}])]))

(defn screen [_]
  (let [[pattern set-pattern!]         (react/use-state :channel)
        [title set-title!]             (react/use-state "General")
        [description set-description!] (react/use-state "Online")
        [centered? set-centered!]      (react/use-state nil)
        [action-count set-count!]      (react/use-state 1)
        [surface set-surface!]         (react/use-state nil)
        [disabled? set-disabled!]      (react/use-state nil)
        [banner? set-banner!]          (react/use-state nil)
        [color set-color!]             (react/use-state :color/primary)]
    [screen/screen {:title   "Page nav"
                    :blur?   (= surface :blur)
                    :photo?  (= surface :photo)
                    :preview [context/provider {:color color}
                              [:rn/view {:style (preview surface)}
                               [preview-nav {:pattern      pattern
                                             :title        title
                                             :description  description
                                             :centered?    centered?
                                             :action-count action-count
                                             :surface      surface
                                             :disabled?    disabled?
                                             :banner?      banner?}]]]}
     [controls/choice {:label     "Example"
                       :value     pattern
                       :on-change set-pattern!
                       :options   [{:value :title :label "Title"}
                                   {:value :empty :label "No title"}
                                   {:value :description :label "Description"}
                                   {:value :channel :label "Channel"}
                                   {:value :message :label "Message"}
                                   {:value :token :label "Token"}
                                   {:value :community :label "Community"}
                                   {:value :dropdown :label "Dropdown"}]}]
     [xquo/input {:label "Title" :value title :on-change-text set-title!}]
     [xquo/input {:label "Description" :value description :on-change-text set-description!}]
     [controls/choice {:label     "Right actions"
                       :value     action-count
                       :on-change set-count!
                       :options   [{:value 0 :label "0"}
                                   {:value 1 :label "1"}
                                   {:value 2 :label "2"}
                                   {:value 3 :label "3"}]}]
     [controls/choice {:label     "Surface"
                       :value     surface
                       :on-change set-surface!
                       :options   [{:value nil :label "Solid"}
                                   {:value :blur :label "Blur"}
                                   {:value :photo :label "Photo"}]}]
     [controls/choice {:label     "Customization color"
                       :value     color
                       :on-change set-color!
                       :options   [{:value :color/primary :label "Primary"}
                                   {:value :color/success :label "Green"}
                                   {:value :color/danger :label "Red"}]}]
     [controls/checkbox {:label "Centered" :value centered? :on-change set-centered!}]
     [controls/checkbox {:label "Disabled title" :value disabled? :on-change set-disabled!}]
     [controls/checkbox {:label "Banner below" :value banner? :on-change set-banner!}]]))
