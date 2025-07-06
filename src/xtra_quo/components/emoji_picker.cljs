(ns xtra-quo.components.emoji-picker
  (:require
   [clojure.string :as string]
   [oops.core :as oops]
   [quo.context]
   [react-native.safe-area :as safe-area]
   [xtatus-quo.core :as quo]
   [quo.foundations.colors :as colors]
   [react-native.core :as rn]
   [react-native.gesture :as gesture]
   [react-native.platform :as platform]
   [reagent.core :as reagent]
   [status-im.common.emoji-picker.constants :as constants]
   [status-im.common.emoji-picker.data :as emoji-picker.data]
   [status-im.common.emoji-picker.style :as style]
   [status-im.common.emoji-picker.utils :as emoji-picker.utils]
   [utils.debounce :as debounce]
   [utils.i18n :as i18n]))

(defn- on-press-category
  [{:keys [id index active-category scroll-ref]}]
  (reset! active-category id)
  (some-> ^js @scroll-ref
          (.scrollToIndex #js
                           {:index    (emoji-picker.data/get-section-header-index-in-data index)
                            :animated false})))

(defn- handle-on-viewable-items-changed
  [{:keys [event active-category should-update-active-category?]}]
  (when should-update-active-category?
    (let [viewable-item (some-> event
                                (oops/oget "viewableItems")
                                (aget 0)
                                (oops/oget "item"))
          header?       (and (map? viewable-item) (:header? viewable-item))
          section-key   (if header?
                          (:id viewable-item)
                          (-> viewable-item first :group emoji-picker.data/emoji-group->category :id))]
      (when (and (some? section-key) (not= @active-category section-key))
        (reset! active-category section-key)))))

(defn- get-item-layout
  [_ index]
  #js
   {:length constants/item-height
    :offset (* constants/item-height index)
    :index  index})

(defn- section-header
  [{:keys [title]} {:keys [theme]}]
  [quo/divider-label
   {:tight?          false
    :container-style (style/section-header theme)}
   title])

(defn- emoji-item
  [{:keys [unicode] :as emoji} col-index on-select]
  (let [on-press          (fn []
                            (when on-select
                              (on-select unicode emoji)))
        last-item-on-row? (= (inc col-index) constants/emojis-per-row)]
    (fn []
      [rn/pressable
       {:style    (style/emoji-container last-item-on-row?)
        :on-press on-press}
       [rn/text
        {:style                    {:font-size constants/emoji-size}
         :adjusts-font-size-to-fit true
         :allow-font-scaling       false}
        unicode]])))

(defn- emoji-row
  [row-data {:keys [on-select]}]
  (into [rn/view {:style style/emoji-row-container}]
        (map-indexed (fn [col-index {:keys [hexcode] :as emoji}]
                       ^{:key hexcode}
                       [emoji-item emoji col-index on-select]))
        row-data))

(defn- render-item
  [item _ _ render-data]
  (if (:header? item)
    [section-header item render-data]
    [emoji-row item render-data]))

(defn- empty-result
  []
  #_[quo/empty-state
   {:title           (i18n/label :t/emoji-no-results-title)
    :description     (i18n/label :t/emoji-no-results-description)
    :placeholder?    true
    :container-style style/empty-results}])

(defn- render-list
  [{:keys [filtered-data on-viewable-items-changed scroll-enabled? on-scroll
           on-select set-scroll-ref sheet-animating?]}]
  (let [theme (quo.context/use-theme)]
    [gesture/flat-list
     {:ref                             set-scroll-ref
      :style                           {:height (- (:height safe-area/window)
                                                   safe-area/top
                                                   64)}
      :scroll-enabled                  true ;@scroll-enabled?
      :data                            (or filtered-data emoji-picker.data/flatten-data)
      :initial-num-to-render           14
      :max-to-render-per-batch         10
      :render-fn                       render-item
      :get-item-layout                 get-item-layout
      :keyboard-dismiss-mode           :on-drag
      :keyboard-should-persist-taps    :handled
      :shows-vertical-scroll-indicator false
      :on-scroll-to-index-failed       identity
      :empty-component                 [empty-result]
      :on-scroll                       on-scroll
      :render-data                     {:theme     theme
                                        :on-select on-select}
      :content-container-style         style/list-container
      :viewability-config              {:item-visible-percent-threshold 100
                                        :minimum-view-time              200}
      :on-viewable-items-changed       on-viewable-items-changed
      :window-size                     10}]))

(defn- footer
  [{:keys [active-category scroll-ref]}]
  (let [on-press (fn [id index]
                   (on-press-category
                    {:id              id
                     :index           index
                     :active-category active-category
                     :scroll-ref      scroll-ref}))]
    (fn []
      [rn/view {:style style/category-container}
       [:blur/blur-view {:style       [rn/stylesheet-absolute-fill]
                         :blur-radius 20
                         :blur-type   :light}]
       [quo/showcase-nav
        {:state     :scroll
         :active-id @active-category
         :data      emoji-picker.data/categories
         :on-press  on-press}]])))

(defn- clear
  [{:keys [active-category filtered-data search-text]}]
  (reset! active-category constants/default-category)
  (reset! filtered-data nil)
  (reset! search-text ""))

(defn sheet-view
  [{:keys [search-active? on-change-text clear-states active-category scroll-ref]
    :as   params}]
  [rn/keyboard-avoiding-view
   {:style                    style/flex-spacer
    :keyboard-vertical-offset 8}
   [rn/view {:style style/flex-spacer}
    [rn/view {:style style/search-input-container}
     [quo/input
      {:small?         true
       :placeholder    "Busca emojis"
       :icon-name      :i/search
       :on-change-text on-change-text
       :clearable?     search-active?
       :on-clear       clear-states}]]
    [render-list params]
    (when-not search-active?
      [footer
       {:active-category active-category
        :scroll-ref      scroll-ref}])]])

(defn view
  [_]
  (let [scroll-ref                (atom nil)
        set-scroll-ref            #(reset! scroll-ref %)
        search-text               (reagent/atom "")
        filtered-data             (reagent/atom nil)
        active-category           (reagent/atom constants/default-category)
        clear-states              #(clear {:active-category active-category
                                           :filtered-data   filtered-data
                                           :search-text     search-text})
        search-emojis             (debounce/debounce
                                   (fn []
                                     (when (pos? (count @search-text))
                                       (reset! filtered-data (emoji-picker.utils/search-emoji
                                                              @search-text))))
                                   constants/search-debounce-ms)
        on-change-text            (fn [text]
                                    (if (string/blank? text)
                                      (clear-states)
                                      (do
                                        (reset! search-text text)
                                        (search-emojis))))
        on-viewable-items-changed (fn [event]
                                    (handle-on-viewable-items-changed
                                     {:event                          event
                                      :active-category                active-category
                                      :should-update-active-category? (nil? @filtered-data)}))]
    ;; TODO: move to another screen and just import it
    (fn [params]
      (let [pop-to!    (-> params :navigation :pop-to)
            on-select! (fn [emoji-selected]
                         (pop-to! :screen/customize-package {:emoji emoji-selected}))]
        [sheet-view
         {:search-active?            (pos? (count @search-text))
          :on-change-text            on-change-text
          :clear-states              clear-states
          :filtered-data             @filtered-data
          :set-scroll-ref            set-scroll-ref
          :on-select                 on-select!
          :on-viewable-items-changed on-viewable-items-changed
          :active-category           active-category
          :scroll-ref                scroll-ref}]))))


;; #_(defn emoji [emoji-text]
;  [:rn/pressable {:style {:width            32
;                          :height           32
;                          :overflow         :hidden
;                          :justify-content  :center
;                          :align-items      :center}}
;   [:rn/text {:style {:font-size            27
;                      :include-font-padding false}}
;    emoji-text]])
;
;#_(defn render-item [item]
;  (let [header? (map? item)]
;    (if header?
;      [:rn/view {:style {:padding-top      12}}
;       [quo/divider-label {:tight? false}
;        (string/capitalize (:title item))]]
;      [:rn/view {:style {:flex-direction     :row
;                         :justify-content :space-between
;                         :padding-vertical   8
;                         :padding-horizontal 20}}
;       (map (fn [{:keys [hexcode unicode]}]
;              ^{:key hexcode}
;              [emoji unicode])
;            item)])))
;
;#_(defn view
;  [_]
;  (let [render-fn (rn/use-callback
;                   (fn [^js data]
;                     (def --d data)
;                     (let [item (.-item data)]
;                       (def --i item)
;                       (r/as-element [render-item (js->clj item :keywordize-keys true)])))
;                   [])]
;    [:rn/view {:style {;:background-color :blue
;                       :flex 1}
;               ;:collapsable false
;               }
;     [:rn/view {:style {:padding-horizontal 20
;                        }}
;      [quo/input {:placeholder "Busca emojis"
;                  :small?      true
;                  :icon-name   :i/search}]]
;
;     ;; TODO: optimize flat list by not transforming the data back to JS
;     ;; or feed it as already JS (ugly)
;     [:gh/flat-list {:style                   {:height (- (:height safe-area/window)
;                                                          safe-area/top
;                                                          64)}
;                     :content-container-style {:padding-bottom (+ 56 safe-area/bottom)}
;                     :data                    emoji-picker.data/flatten-data
;                     :render-item             render-fn
;                     :shows-vertical-scroll-indicator false
;                     }]
;     [:rn/view {:style {:position           :absolute
;                        :left               0
;                        :right              0
;                        :bottom             0
;                        :padding-top        12
;                        :padding-bottom     (+ safe-area/bottom 12)
;                        :padding-horizontal 20
;                        :flex-direction     :row
;                        ;:column-gap 8
;                        :justify-content    :space-between
;                        }}
;      [:blur/blur-view {:style       rn/stylesheet-absolute-fill
;                        :blur-type   :light
;                        :blur-radius 25
;                        ;:overlay-color blur-overlay-color
;                        }]
;      ;; TODO: recent
;      #_[quo/button {:type       :grey
;                     :size       32
;                     :icon-only? true
;                     :background :blur}
;         :i/recent]
;      [quo/button {:type       :grey
;                   :size       32
;                   :icon-only? true
;                   :background :blur}
;       :i/faces]
;      [quo/button {:type       :grey
;                   :size       32
;                   :icon-only? true
;                   :background :blur}
;       :i/nature]
;      [quo/button {:type       :grey
;                   :size       32
;                   :icon-only? true
;                   :background :blur}
;       :i/food]
;      [quo/button {:type       :grey
;                   :size       32
;                   :icon-only? true
;                   :background :blur}
;       :i/activity]
;      [quo/button {:type       :grey
;                   :size       32
;                   :icon-only? true
;                   :background :blur}
;       :i/travel]
;      [quo/button {:type       :grey
;                   :size       32
;                   :icon-only? true
;                   :background :blur}
;       :i/objects]
;      [quo/button {:type       :grey
;                   :size       32
;                   :icon-only? true
;                   :background :blur}
;       :i/hashtag-square]
;      [quo/button {:type       :grey
;                   :size       32
;                   :icon-only? true
;                   :background :blur}
;       :i/flags]]
;     ]))
;
;;; TODO: rebuild the emoji picker
