(ns xquo.demo.profile.showcase-card
  (:require [react-native.react.core :as react]
            [react-native.utils :refer [defstyle style]]
            [xquo.context :as context]
            [xquo.core :as xquo]
            [xquo.demo.assets :as assets]
            [xquo.demo.controls :as controls]
            [xquo.demo.preview-assets :as preview-assets]
            [xquo.demo.screen :as screen]
            [xquo.foundations.colors :as colors]))

(defstyle ^:private preview
  {:align-items :center})

(defstyle ^:private row
  {:flex-direction :row})

(def ^:private examples
  {:assets      {:title "Assets"
                 :items [{:avatar {:variant :solid :content {:type :emoji :text "💎"}}}
                         {:avatar {:variant :solid :color :color/success :content {:type :initials :text "S"}}}
                         {:avatar {:variant :solid :color :color/warning :content {:type :initials :text "W"}}}
                         {:avatar {:variant :solid :color :color/danger :content {:type :initials :text "A"}}}]}
   :collectibles {:title "Collectibles"
                  :radius 10
                  :items [{:image preview-assets/coast}
                          {:image preview-assets/ceramics}
                          {:image assets/profile}]}
   :communities  {:title "Communities"
                  :items [{:avatar {:content {:type :image :image {:source assets/profile-testing-1}}}}
                          {:avatar {:content {:type :image :image {:source assets/profile-testing-2}}}}
                          {:avatar {:content {:type :image :image {:source assets/profile-testing-3}}}}
                          {:avatar {:content {:type :image :image {:source preview-assets/coast}}}}]}
   :accounts     {:title "Accounts"
                  :radius 10
                  :items [{:avatar {:shape :rounded-square :variant :solid :content {:type :emoji :text "🔥"}}}
                          {:avatar {:shape :rounded-square :variant :solid :color :color/warning :content {:type :emoji :text "🌱"}}}]}})

(defn- preview-item [{:keys [index avatar image radius theme]}]
  [:rn/view {:style (style {:width 32 :height 32 :margin-left (when (pos? index) -8)})}
   (when (pos? index)
     [:rn/view {:style (style {:position         :absolute
                              :top              -2
                              :right            -2
                              :bottom           -2
                              :left             -2
                              :border-radius    (+ radius 2)
                              :background-color (colors/themed theme :color/white :color/neutral-90)})}])
   (if image
     [:rn/image {:source image :style (style {:width 32 :height 32 :border-radius radius})}]
     [xquo/avatar (assoc avatar :size 32)])])

(defn- preview-list [{:keys [items radius] :or {radius 16}}]
  (let [theme (context/use-theme)]
    (into [:rn/view {:style row}]
          (map-indexed (fn [index item]
                         [preview-item (assoc item :index index :radius radius :theme theme)]))
          items)))

(defn- pressed! [] (js/alert "Pressed"))

(defn screen [_]
  (let [[example set-example!]    (react/use-state :assets)
        [title set-title!]        (react/use-state "Assets")
        [disabled? set-disabled!] (react/use-state nil)
        change-example!           (react/use-callback (fn [value]
                                                        (set-example! value)
                                                        (set-title! (get-in examples [value :title])))
                                                      [])]
    [screen/screen {:title   "Showcase card"
                    :preview [:rn/view {:style preview}
                              [xquo/profile.showcase-card {:title     title
                                                           :disabled? disabled?
                                                           :on-press  pressed!}
                               [preview-list (examples example)]]]}
     [controls/choice {:label     "Example"
                       :value     example
                       :on-change change-example!
                       :options   [{:value :assets :label "Assets"}
                                   {:value :collectibles :label "Collectibles"}
                                   {:value :communities :label "Communities"}
                                   {:value :accounts :label "Accounts"}]}]
     [xquo/input {:label "Title" :value title :on-change-text set-title!}]
     [controls/checkbox {:label "Disabled" :value disabled? :on-change set-disabled!}]]))
