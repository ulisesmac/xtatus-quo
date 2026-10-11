(ns xquo.demo.list-item
  (:require [react-native.react.core :as react]
            [react-native.utils :refer [defstyle style]]
            [xquo.core :as xquo]
            [xquo.context :as context]
            [xquo.demo.assets :as assets]
            [xquo.demo.controls :as controls]
            [xquo.demo.screen :as screen]
            [xquo.foundations.colors :as colors]))

(defstyle ^:private preview-style
  {:width "100%" :padding-horizontal 8})

(defstyle ^:private title-line
  {:height 22 :line-height 21.75 :include-font-padding false :text-align-vertical :center})

(def ^:private examples
  {:address     {:label "Address" :type :default :title "0x628...ab1" :content :wallet}
   :pending     {:label       "Pending requests"
                 :type        :default
                 :title       "Pending requests"
                 :description "Alicia, Pedro and 3 others"
                 :content     :pending
                 :right       :counter}
   :channel     {:label    "Channel"
                 :type     :default
                 :compact? true
                 :title    "# channel"
                 :content  :emoji
                 :right    :counter}
   :values      {:label       "Values"
                 :type        :default
                 :title       "Status"
                 :description "0.00 SNT"
                 :content     :image
                 :right       :change}
   :ranked      {:label       "Ranked"
                 :type        :default
                 :title       "Ethereum"
                 :description "€555.5B"
                 :content     :image
                 :left        :rank
                 :right       :change}
   :media       {:label "Media" :type :default :title "Recents" :content :media}
   :embedded    {:label       "Embedded card"
                 :type        :outline
                 :compact?    true
                 :title       "Trip to Vegas"
                 :description "0x21a...49e"
                 :content     :account
                 :right       :options}
   :information {:label   "Information card"
                 :type    :outline
                 :title   "1,000 USDT"
                 :content :image
                 :right   :button}
   :trust       {:label       "Trust card"
                 :type        :grey
                 :title       "Uniswap"
                 :description "coingecko.com"
                 :content     :image
                 :right       :verified}
   :discovery   {:label    "Discovery"
                 :type     :elevated
                 :compact? true
                 :title    "Status"
                 :content  :image
                 :right    :permissions}})

(def ^:private example-order
  [:address :pending :channel :values :ranked :media :embedded :information :trust :discovery])

(defn- pressed! [] (js/alert "Pressed"))

(defn- address-title [{:keys [title]}]
  [xquo/text {:font            :font/semibold-15
              :number-of-lines 1
              :style           [title-line (style {:font-variant ["tabular-nums" "stylistic-two"]})]}
   [xquo/text {:font :font/semibold-15 :style (style {:color "#758EEB"})} "eth:"]
   [xquo/text {:font :font/semibold-15 :style (style {:color "#E76E6E"})} "opt:"]
   title])

(defn- information-title [{:keys [title color]}]
  [:rn/view {:style (style {:flex-direction :row :align-items :center :gap 4})}
   [xquo/text {:font :font/semibold-15 :number-of-lines 1 :style title-line} title]
   [xquo/icon {:name :icon/alert-outline :size 16 :color color}]])

(defn- example [{:keys [kind content right-mode left-mode caption? title description button-size blur?]
                 :as   props}]
  (let [[checked? set-checked!] (react/use-state false)
        theme                   (context/use-theme)
        muted                   (if blur?
                                  (colors/themed theme :color/neutral-80-70 :color/white-70)
                                  (colors/themed theme :color/neutral-50 :color/neutral-40))
        secondary               (if blur?
                                  (colors/themed theme :color/neutral-80-40 :color/white-40)
                                  (colors/themed theme :color/neutral-50 :color/neutral-40))]
    [:rn/view {:style preview-style}
     [xquo/list.item (-> props
                         (dissoc :kind :right-mode :left-mode :caption? :button-size)
                         (assoc :title (case kind
                                         :address [address-title {:title title}]
                                         :information [information-title {:title title :color secondary}]
                                         title)
                                :description (when (seq description) description)
                                :title-props (case kind
                                               :media {:font :font/medium-15}
                                               :channel {:style (style {:color secondary})}
                                               nil)
                                :description-props (when (= kind :embedded) {:font :font/monospace-13})
                                :content (case content
                                           :wallet {:type   :avatar
                                                    :avatar {:shape   :circle
                                                             :color   :color/neutral
                                                             :content {:type :initials :text "0x"}}}
                                           :pending {:type   :avatar
                                                     :avatar {:shape   :circle
                                                              :color   :color/neutral
                                                              :variant nil
                                                              :border  :solid
                                                              :content {:type :icon :icon {:name :icon/user-pending :color muted}}}}
                                           :emoji {:type :avatar :avatar {:shape :circle :content {:type :emoji :text "🍑"}}}
                                           :account {:type :avatar :avatar {:shape :rounded-square :content {:type :emoji :text "🎲"}}}
                                           :image {:type   :avatar
                                                   :avatar {:shape :circle :content {:type :image :image {:source assets/profile}}}}
                                           :media {:type :image :image {:source assets/illustration}}
                                           :icon {:type :icon :icon {:name :icon/info}}
                                           nil)
                                :left (case left-mode
                                        :rank [{:type :counter :counter {:type :outline :value 2}}]
                                        :handle [{:type :custom :view [xquo/icon {:name :icon/menu :size 20}]}]
                                        nil)
                                :right (case right-mode
                                         :counter [{:type :counter :counter {:value 5}}]
                                         :arrow [{:type :icon :icon {:name :icon/chevron-right}}]
                                         :counter-arrow [{:type :counter :counter {:value 5}}
                                                         {:type :icon :icon {:name :icon/chevron-right}}]
                                         :options [{:type :icon :icon {:name :icon/options :on-press pressed!}}]
                                         :button [{:type   :button
                                                   :button {:label    "Edit"
                                                            :type     :outline
                                                            :size     button-size
                                                            :icon     {:name :icon/edit :side :left}
                                                            :on-press pressed!}}]
                                         :selector [{:type :selector :selector {:type :checkbox :selected? checked? :on-select set-checked!}}]
                                         :balance [{:type :values :values {:layout :balanced :primary "0.00 SNT" :secondary "€0.00"}}]
                                         :change [{:type   :values
                                                   :values {:primary   "€0.00"
                                                            :secondary {:type :change :direction :positive :percentage "2.3%" :value "€4.00"}}}]
                                         :verified [{:type :icon :icon {:name :icon/id-verified :color (colors/get-color :color/success)}}
                                                    {:type :icon :icon {:name :icon/chevron-right}}]
                                         :permissions [{:type :custom
                                                        :view [xquo/tag.permissions {:size 24 :groups [{:tokens [{:source assets/illustration}]}]}]}]
                                         nil)
                                :caption (when caption? {:text "Can't be invited due to their privacy settings"
                                                         :icon {:name :icon/info}})
                                :on-press pressed!))]]))

(defn screen [_]
  (let [[kind set-kind!]               (react/use-state :pending)
        [type set-type!]               (react/use-state :default)
        [compact? set-compact!]        (react/use-state nil)
        [title set-title!]             (react/use-state "Pending requests")
        [description set-description!] (react/use-state "Alicia, Pedro and 3 others")
        [content set-content!]         (react/use-state :pending)
        [left-mode set-left!]          (react/use-state nil)
        [right-mode set-right!]        (react/use-state :counter)
        [button-size set-button-size!] (react/use-state 24)
        [caption? set-caption!]        (react/use-state nil)
        [active? set-active!]          (react/use-state nil)
        [selected? set-selected!]      (react/use-state nil)
        [disabled? set-disabled!]      (react/use-state nil)
        [blur? set-blur!]              (react/use-state nil)
        choose-example!                (react/use-callback (fn [kind]
                                                             (let [{:keys [type compact? title description content left right]} (examples kind)]
                                                               (set-kind! kind)
                                                               (set-type! type)
                                                               (set-compact! compact?)
                                                               (set-title! title)
                                                               (set-description! (or description ""))
                                                               (set-content! content)
                                                               (set-left! left)
                                                               (set-right! right)))
                                                           [set-kind! set-type! set-compact! set-title! set-description! set-content! set-left! set-right!])]
    [screen/screen {:title   "List item"
                    :blur?   blur?
                    :preview [example {:kind        kind
                                       :type        type
                                       :compact?    compact?
                                       :title       title
                                       :description description
                                       :content     content
                                       :left-mode   left-mode
                                       :right-mode  right-mode
                                       :button-size button-size
                                       :caption?    caption?
                                       :active?     active?
                                       :selected?   selected?
                                       :disabled?   disabled?
                                       :blur?       blur?}]}
     [controls/choice {:label     "Figma layout"
                       :value     kind
                       :on-change choose-example!
                       :options   (mapv (fn [kind] {:value kind :label (:label (examples kind))}) example-order)}]
     [controls/choice {:label     ":type"
                       :value     type
                       :on-change set-type!
                       :options   [{:value :default :label "Default"} {:value :outline :label "Outline"}
                                   {:value :grey :label "Grey"} {:value :elevated :label "Elevated"}]}]
     [controls/checkbox {:label ":compact?" :value compact? :on-change set-compact!}]
     [xquo/input {:label ":title" :value title :on-change-text set-title!}]
     [xquo/input {:label ":description" :value description :on-change-text set-description!}]
     [controls/choice {:label     ":content"
                       :value     content
                       :on-change set-content!
                       :options   [{:value nil :label "None"} {:value :wallet :label "Initials"}
                                   {:value :pending :label "Pending"} {:value :emoji :label "Emoji"}
                                   {:value :account :label "Account"} {:value :image :label "Image"}
                                   {:value :media :label "Media"} {:value :icon :label "Icon"}]}]
     [controls/choice {:label     ":left"
                       :value     left-mode
                       :on-change set-left!
                       :options   [{:value nil :label "None"} {:value :rank :label "Rank"} {:value :handle :label "Handle"}]}]
     [controls/choice {:label     ":right"
                       :value     right-mode
                       :on-change set-right!
                       :options   [{:value nil :label "None"} {:value :counter :label "Counter"}
                                   {:value :arrow :label "Arrow"} {:value :counter-arrow :label "Counter + arrow"}
                                   {:value :options :label "Options"} {:value :button :label "Button"}
                                   {:value :selector :label "Selector"} {:value :balance :label "Balance"}
                                   {:value :change :label "Change"} {:value :verified :label "Verified"}
                                   {:value :permissions :label "Permissions"}]}]
     (when (= right-mode :button)
       [controls/choice {:label     "Button :size"
                         :value     button-size
                         :on-change set-button-size!
                         :options   [{:value 24 :label "24"} {:value 32 :label "32"}]}])
     [controls/checkbox {:label ":caption" :value caption? :on-change set-caption!}]
     [controls/checkbox {:label ":active?" :value active? :on-change set-active!}]
     [controls/checkbox {:label ":selected?" :value selected? :on-change set-selected!}]
     [controls/checkbox {:label ":disabled?" :value disabled? :on-change set-disabled!}]
     [controls/checkbox {:label ":blur?" :value blur? :on-change set-blur!}]]))
