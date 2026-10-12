(ns xquo.demo.text-combinations
  (:require [react-native.react.core :as react]
            [react-native.utils :refer [style]]
            [xquo.context :as context]
            [xquo.core :as xquo]
            [xquo.demo.assets :as assets]
            [xquo.demo.controls :as controls]
            [xquo.demo.screen :as screen]
            [xquo.foundations.colors :as colors]))

(defn- pressed! [] (js/alert "Pressed"))

(defn- preview [{:keys [example title caption description right tag? avatar? search? blur?]}]
  (let [{:keys [theme dark-theme?]} (context/use-theme-color)
        username?                   (= example :username)
        channel?                    (= example :channel)
        page-top?                   (= example :page-top)
        component                   (if page-top? xquo/text-combinations.page-top xquo/text-combinations.title)
        secondary                   (cond
                                      (not dark-theme?) (colors/get-color :color/neutral-40)
                                      blur?             (colors/get-color :color/white-40)
                                      :else             (colors/get-color :color/neutral-50))]
    [:rn/view {:style (style {:width "100%" :padding-horizontal (when-not page-top? 20)})}
     [component (cond-> {:title   title
                         :caption (when username? caption)
                         :type    (when username? :username)
                         :icons   (cond
                                    username? [{:name :icon/check-verified :color (colors/themed theme :color/success)}]
                                    channel?  [{:name :icon/lock-open} {:name :icon/bell-mute :color secondary}]
                                    :else     nil)
                         :tag     (when tag? {:label "New"})
                         :right   (case right
                                    :button {:type :button :button {:icon {:name :icon/add} :on-press pressed!}}
                                    :text   {:type :text :text {:value "12/24"}}
                                    :tag    {:type :tag :tag {:content {:type :icon :icon {:name :icon/info}}}}
                                    nil)
                         :blur?   blur?}
                  page-top? (assoc :description description
                                   :content (when avatar? {:type :avatar :avatar {:content {:type :image :image {:source assets/profile}}}})
                                   :search (when search? {:placeholder "Search..."})))]]))

(def ^:private titles
  {:standard "Your collection" :username "Alex" :channel "# general" :page-top "Community"})

(defn screen [_]
  (let [[example set-example!]         (react/use-state :standard)
        [title set-title!]             (react/use-state "Your collection")
        [caption set-caption!]         (react/use-state "Alex Morgan")
        [description set-description!] (react/use-state "Share updates and ideas with your community.")
        [right set-right!]             (react/use-state :button)
        [tag? set-tag!]                (react/use-state nil)
        [avatar? set-avatar!]          (react/use-state true)
        [search? set-search!]          (react/use-state true)
        [blur? set-blur!]              (react/use-state nil)
        change-example!                (react/use-callback (fn [value]
                                                             (set-example! value)
                                                             (set-title! (titles value)))
                                                           [])]
    [screen/screen {:title   "Text combinations"
                    :blur?   blur?
                    :preview [preview {:example     example
                                       :title       title
                                       :caption     caption
                                       :description description
                                       :right       right
                                       :tag?        tag?
                                       :avatar?     avatar?
                                       :search?     search?
                                       :blur?       blur?}]}
     [controls/choice {:label     "Example"
                       :value     example
                       :on-change change-example!
                       :options   [{:value :standard :label "Title"}
                                   {:value :username :label "Username"}
                                   {:value :channel :label "Channel"}
                                   {:value :page-top :label "Page top"}]}]
     [xquo/input {:label "Title" :value title :on-change-text set-title!}]
     (when (= example :username)
       [xquo/input {:label "Caption" :value caption :on-change-text set-caption!}])
     [controls/choice {:label     "Right"
                       :value     right
                       :on-change set-right!
                       :options   [{:value nil :label "None"}
                                   {:value :button :label "Button"}
                                   {:value :text :label "Counter"}
                                   {:value :tag :label "Tag"}]}]
     [controls/checkbox {:label "Title tag" :value tag? :on-change set-tag!}]
     (when (= example :page-top)
       [:<>
        [xquo/input {:label "Description" :value description :on-change-text set-description!}]
        [controls/checkbox {:label "Avatar" :value avatar? :on-change set-avatar!}]
        [controls/checkbox {:label "Search" :value search? :on-change set-search!}]])
     [controls/checkbox {:label "Blur" :value blur? :on-change set-blur!}]]))
