(ns xtra-quo.components.emoji-picker
  (:require
   [clojure.string :as string]
   [goog.functions :as gfns]
   [oops.core :as oops]
   [quo.context]
   [quo.foundations.colors :as colors]
   [quo.extra.components.above-keyboard-container.view :as above-keyboard]
   [react-native.core :as rn]
   [react-native.safe-area :as safe-area]
   [reagent.core :as reagent]
   [status-im.common.emoji-picker.constants :as constants]
   [xtra-quo.components.emoji-picker.style :as style]
   [status-im.common.emoji-picker.data :as emoji-picker.data]
   [xtatus-quo.core :as quo]))

(def title-kw->text
  {:t/emoji-people   "Personas"
   :t/emoji-food     "Comida"
   :t/emoji-nature   "Naturaleza"
   :t/emoji-travel   "Viajes"
   :t/emoji-activity "Actividades"
   :t/emoji-symbols  "Símbolos"
   :t/emoji-objects  "Objetos"
   :t/emoji-flags    "Banderas"})

(defn- emoji-section [item]
  [:rn/view {:style style/emoji-selection}
   [quo/divider-label {:tight? false}
    (title-kw->text (:title item) (:title item))]])

(def emoji-size
  (let [available-space (- (:width (rn/get-window))
                           (* 20 2))
        min-emoji-space (* 18.5 6)]
    (/ (- available-space min-emoji-space)
       7)))

(defn emoji-row [emojis on-emoji-press]
  [:rn/view {:style style/emoji-row}
   (map (fn [{:keys [unicode]}]
          ^{:key unicode}
          [:rn/pressable {:style    (assoc style/emoji-pressable
                                      :width emoji-size
                                      :height emoji-size)
                          :on-press #(on-emoji-press unicode)}
           [:rn/text {:style                style/emoji-text
                      :adjustsFontSizeToFit true}
            unicode]])
        emojis)])

(defn render-item [item on-emoji-press]
  (if (:header? item)
    [emoji-section item]
    [emoji-row item on-emoji-press]))

(def emoji-row-height (+ emoji-size 16))
(def emoji-section-height (+ 42 12))

(def data-layout-offset
  (reduce (fn [acc {:keys [header?]}]
            (let [prev-height    (peek acc)
                  current-height (if header?
                                   emoji-section-height
                                   emoji-row-height)]
              (conj acc (+ prev-height current-height))))
          [0]
          emoji-picker.data/flatten-data))

(defn get-item-layout [data index]
  (let [height (if (:header? (aget data index))
                 emoji-section-height
                 emoji-row-height)]
    #js{:length height
        :offset (get data-layout-offset index)
        :index  index}))

(defn key-extractor [item index]
  (if (:header? item)
    (str "header-" index)
    (reduce str (map :unicode item))))

(defn- on-category-press
  [^js scroll-ref category-id set-category]
  (let [index (emoji-picker.data/section-header-indexes category-id)]
    (when (.. scroll-ref -current -scrollToOffset)
      (.. scroll-ref -current (scrollToOffset #js{:offset   (+ (get data-layout-offset index) 10)
                                                  :animated false})))
    (set-category category-id)
    (reagent/flush)))

(def section-sorted
  (sort-by val emoji-picker.data/section-header-indexes))

(defn index->category [n]
  (or (some->> section-sorted
        (filterv #(<= (val %) n))
        (peek)
        (key))
      (ffirst section-sorted)))

(defn current-focused-index [offset]
  (->> data-layout-offset
       (filterv #(<= % offset))
       (count)
       (dec)))

(defn- on-scroll [set-category ^js e]
  (let [offset (oops/oget e "nativeEvent.contentOffset.y")]
    (set-category (index->category (current-focused-index offset)))))

(defn search-term? [input]
  (some-> input string/trim seq some?))

(defn emojis-to-render [input]
  (if (search-term? input)
    (let [search-string (string/lower-case input)]
      (->> emoji-picker.data/emoji-data
           (filter (fn [{:keys [label tags] :as _emoji}]
                     (or (string/includes? label search-string)
                         (some #(string/includes? % search-string) tags))))
           (partition-all constants/emojis-per-row)))
    emoji-picker.data/flatten-data))

;; TODO: add recently used to emoji-picker.data/flatten-data
(defn emoji-list [scroll-ref set-category input on-emoji-press]
  (let [[search-term
         set-search-term!] (rn/use-state nil)
        data           (rn/use-memo #(with-meta (emojis-to-render search-term) {:keep-items true})
                                    [search-term])
        scroll-fn      (rn/use-callback (partial on-scroll set-category) [])
        render-item-fn (rn/use-callback
                        (fn [js-data]
                          (reagent/as-element [render-item (oops/oget js-data "item") on-emoji-press]))
                        [])
        change-search! (rn/use-memo #(gfns/debounce set-search-term! 500)
                                    [])]
    (rn/use-effect #(change-search! input)
                   [input])
    [:gh/flat-list {:ref                             scroll-ref
                    :style                           {:height (- (:height (rn/get-screen))
                                                                 safe-area/top
                                                                 20
                                                                 32)}
                    :content-container-style         {:padding-top    (when (search-term? search-term)
                                                                        12)
                                                      :padding-bottom (+ safe-area/bottom 56)}
                    :data                            data
                    :scroll-event-throttle           300
                    :on-scroll                       scroll-fn
                    :render-item                     render-item-fn
                    :get-item-layout                 get-item-layout
                    :key-extractor                   key-extractor
                    :shows-vertical-scroll-indicator false}]))

(defn category-button [{:keys [category set-category scroll-ref selected? icon]}]
  (let [on-press (rn/use-callback
                  #(on-category-press scroll-ref category set-category)
                  [])]
    [quo/button {:type       (if selected? :grey :ghost)
                 :icon-only? true
                 :size       32
                 :on-press   on-press}
     icon]))

(defn bottom-nav [scroll-ref category set-category]
  (let [theme    (quo.context/use-theme)
        bg-color (colors/theme-colors colors/white colors/neutral-95 theme)]
    [:rn/view {:style (assoc style/bottom-nav :background-color bg-color)}
     (map (fn [{:keys [id icon]}]
            ^{:key (str id)}
            [category-button {:category     id
                              :set-category set-category
                              :scroll-ref   scroll-ref
                              :selected?    (= category id)
                              :icon         icon}])
          emoji-picker.data/categories)]))

(defn input-patch-view []
  (let [theme    (quo.context/use-theme)
        bg-color (colors/theme-colors colors/white colors/neutral-95 theme)]
    [:rn/view {:style (assoc style/input-patch :background-color bg-color)}]))

(defn view [{:keys [on-emoji-press]}]
  (let [scroll-ref              (rn/use-ref nil)
        [category set-category] (rn/use-state :people)
        [input set-input]       (rn/use-state nil)
        searching?              (search-term? input)]
    [:rn/view
     [:rn/view {:style {:padding-horizontal 20}}
      [quo/input
       {:small?         true
        :placeholder    "Busca emojis"
        :icon-name      :i/search
        :on-change-text set-input}]]
     [emoji-list scroll-ref set-category input on-emoji-press]
     [input-patch-view]
     (when-not searching?
       [bottom-nav scroll-ref category set-category])]))
