(ns xquo.components.emoji-picker.view
  (:require [applied-science.js-interop :as j]
            [clojure.string :as string]
            [goog.functions :as gfns]
            [react-native.core :as rn]
            [react-native.safe-area-context :as safe-area]
            [reagent-extended.react :as react]
            [reagent.core :as reagent]
            [xquo.components.button.view :as button]
            [xquo.components.divider.divider-label.view :as divider-label]
            [xquo.components.emoji-picker.data :as emoji-picker.data]
            [xquo.components.emoji-picker.style :as style]
            [xquo.components.input.view :as input]
            [xquo.context :as context]
            [xquo.extra.components.above-keyboad-view.view :as above-keyboad-view]))

(def ^:private emojis-per-row 7)
(def ^:private search-debounce-ms 200)

(def ^:private section-sorted
  (sort-by val emoji-picker.data/section-header-indexes))

(defn- emoji-size [window-width]
  (let [available-space (- window-width (* 20 2))
        min-emoji-space (* 18.5 6)]
    (/ (- available-space min-emoji-space) 7)))

(defn- emoji-row-height [emoji-size]
  (+ emoji-size 16))

(def ^:private emoji-section-height 42)

(defn- data-layout-offsets [row-height section-height]
  (reduce (fn [offsets {:keys [header?]}]
            (conj offsets (+ (peek offsets)
                             (if header? section-height row-height))))
          [0]
          emoji-picker.data/flatten-data))

(defn- current-focused-index [layout-offsets offset]
  (loop [low 0
         high (dec (count layout-offsets))
         focused-index -1]
    (if (> low high)
      focused-index
      (let [mid (quot (+ low high) 2)]
        (if (<= (nth layout-offsets mid) offset)
          (recur (inc mid) high mid)
          (recur low (dec mid) focused-index))))))

(defn- index->category [index]
  (or (reduce (fn [category [next-category section-index]]
                (if (<= section-index index)
                  next-category
                  (reduced category)))
              nil
              section-sorted)
      (ffirst section-sorted)))

(defn- search-term? [input]
  (seq (emoji-picker.data/normalize-search-text input)))

(defn- indexed-candidates [search-string]
  (reduce (fn [smallest gram]
            (let [matches (get emoji-picker.data/emoji-search-index gram [])]
              (if (or (nil? smallest)
                      (< (count matches) (count smallest)))
                matches
                smallest)))
          nil
          (emoji-picker.data/search-grams search-string)))

(defn- search-candidates [search-string]
  (if (< (count search-string) 2)
    emoji-picker.data/emoji-data
    (or (indexed-candidates search-string) [])))

(defn- matching-emoji-rows [emojis search-string]
  (let [emoji-count (count emojis)]
    (loop [index 0
           rows     (transient [])
           row      (transient [])
           row-size 0]
      (if (< index emoji-count)
        (let [emoji (nth emojis index)]
          (if (string/includes? (:search-text emoji) search-string)
            (let [row      (conj! row emoji)
                  row-size (inc row-size)]
              (if (= row-size emojis-per-row)
                (recur (inc index) (conj! rows (persistent! row)) (transient []) 0)
                (recur (inc index) rows row row-size)))
            (recur (inc index) rows row row-size)))
        (let [rows (if (pos? row-size)
                     (conj! rows (persistent! row))
                     rows)]
          (persistent! rows))))))

(defn- emojis-to-render [search-string]
  (if (seq search-string)
    (matching-emoji-rows (search-candidates search-string) search-string)
    emoji-picker.data/flatten-data))

(def ^:private title-kw->text
  {:t/emoji-people   "Personas"
   :t/emoji-food     "Comida"
   :t/emoji-nature   "Naturaleza"
   :t/emoji-travel   "Viajes"
   :t/emoji-activity "Actividades"
   :t/emoji-symbols  "Símbolos"
   :t/emoji-objects  "Objetos"
   :t/emoji-flags    "Banderas"})

(defn- emoji-section [{:keys [title]}]
  [divider-label/divider-label {:title        (get title-kw->text title title)
                                :compact?     false
                                :collapsible? false}])

(defn- emoji-button [{:keys [on-emoji-press size unicode]}]
  (let [on-press! (react/use-callback #(on-emoji-press unicode) [on-emoji-press unicode])]
    [:rn/pressable {:style    [style/emoji-pressable-base (style/emoji-pressable-size size)]
                    :on-press on-press!}
     [:rn/text {:style                style/emoji-text
                :adjustsFontSizeToFit true}
      unicode]]))

(defn- emoji-row [{:keys [emojis on-emoji-press size]}]
  (into [:rn/view {:style style/emoji-row}]
        (map (fn [{:keys [unicode]}]
               [emoji-button {:on-emoji-press on-emoji-press
                              :size           size
                              :unicode        unicode}]))
        emojis))

(defn- render-item [{:keys [item on-emoji-press size]}]
  (if (:header? item)
    [emoji-section item]
    [emoji-row {:emojis         item
                :on-emoji-press on-emoji-press
                :size           size}]))

(defn- key-extractor [item index]
  (if (:header? item)
    (str "header-" index)
    (:unicode (first item))))

(defn- get-item-layout [layout-offsets index]
  (let [offset      (get layout-offsets index)
        next-offset (get layout-offsets (inc index))]
    #js{:length (- next-offset offset)
        :offset offset
        :index  index}))

(defn- get-search-item-layout [row-height index]
  #js{:length row-height
      :offset (* row-height index)
      :index  index})

(defn- release-scroll-target [state next-category]
  (-> state
      (assoc :category next-category)
      (dissoc :scroll-target-category)))

(defn- category-scroll-state [state next-category release-target?]
  (let [{:keys [category scroll-target-category]} state]
    (cond
      release-target?
      (if (or scroll-target-category (not= category next-category))
        (release-scroll-target state (or scroll-target-category next-category))
        state)

      (= scroll-target-category next-category)
      (release-scroll-target state next-category)

      scroll-target-category
      state

      (= category next-category)
      state

      :else
      (assoc state :category next-category))))

(defn- category-from-scroll-event [layout-offsets event]
  (let [offset (j/get-in event [:nativeEvent :contentOffset :y])]
    (index->category (current-focused-index layout-offsets offset))))

(defn- sync-category-from-scroll! [state* layout-offsets release-target? event]
  (let [next-category (category-from-scroll-event layout-offsets event)
        state         @state*
        next-state    (category-scroll-state state next-category release-target?)]
    (when-not (identical? state next-state)
      (reset! state* next-state))))

(defn- emoji-list
  [{:keys [state*]}]
  (let [input* (reagent/cursor state* [:input])]
    (fn [{:keys [on-emoji-press state*]}]
      (let [scroll-ref       (react/use-ref nil)
            {:keys [width]}  (safe-area/use-window)
            size             (emoji-size width)
            row-height       (emoji-row-height size)
            layout-offsets   (data-layout-offsets row-height emoji-section-height)
            layout-id        (peek layout-offsets)
            bottom-safe-area (safe-area/use-bottom)
            input            @input*
            [search-term set-search-term!] (react/use-state nil)
            search-string      (emoji-picker.data/normalize-search-text search-term)
            searching?         (seq search-string)
            data               (with-meta (emojis-to-render search-string) {:keep-items true})
            scroll!            (react/use-callback (fn [event]
                                                     (sync-category-from-scroll! state* layout-offsets false event))
                                                   [layout-id state*])
            scroll-end!        (react/use-callback (fn [event]
                                                     (sync-category-from-scroll! state* layout-offsets true event))
                                                   [layout-id state*])
            render-item!       (react/use-callback
                                (fn [js-data]
                                  (reagent/as-element
                                   [render-item {:item           (j/get js-data :item)
                                                 :on-emoji-press on-emoji-press
                                                 :size           size}]))
                                [on-emoji-press size])
            get-item-layout!   (react/use-callback (fn [_ index]
                                                      (get-item-layout layout-offsets index))
                                                    [layout-id])
            get-search-item-layout! (react/use-callback
                                     (fn [_ index]
                                       (get-search-item-layout row-height index))
                                     [row-height])
            change-search!     (react/use-memo #(gfns/debounce set-search-term! search-debounce-ms) [])]
        (react/use-effect
         (fn []
           (swap! state* assoc
                  :layout-id layout-id
                  :layout-offsets layout-offsets
                  :scroll-ref scroll-ref))
         [layout-id state*])
        (react/use-effect #(change-search! input)
                          [input])
        [:rn/flat-list (cond-> {:ref                             scroll-ref
                                :style                           style/list-root
                                :content-container-style         (style/list-content bottom-safe-area searching?)
                                :data                            data
                                :render-item                     render-item!
                                :key-extractor                   key-extractor
                                :initial-num-to-render           10
                                :max-to-render-per-batch         10
                                :window-size                     7
                                :update-cells-batching-period    50
                                :remove-clipped-subviews         true
                                :shows-vertical-scroll-indicator false
                                :keyboard-should-persist-taps    :always
                                :get-item-layout                 (if searching?
                                                                   get-search-item-layout!
                                                                   get-item-layout!)}
                         (not searching?) (assoc :scroll-event-throttle 160
                                                 :on-scroll scroll!
                                                 :on-momentum-scroll-end scroll-end!))]))))

(def ^:private category-icons
  {:people   :icon/face-happy
   :nature   :icon/leaf
   :food     :icon/food
   :activity :icon/activity
   :travel   :icon/ship
   :objects  :icon/floor-lamp
   :symbols  :icon/hashtag
   :flags    :icon/flag})

(defn- scroll-to-category! [scroll-ref layout-offsets category]
  (let [index (get emoji-picker.data/section-header-indexes category)]
    (when (and index scroll-ref)
      (when-let [scroll-list (j/get scroll-ref :current)]
        (let [offset (get layout-offsets index)]
          (cond
            (and (some? offset) (j/get scroll-list :scrollToOffset))
            (do
              (j/call scroll-list :scrollToOffset #js{:offset   offset
                                                      :animated true})
              true)

            (j/get scroll-list :scrollToIndex)
            (do
              (j/call scroll-list :scrollToIndex #js{:index        index
                                                     :animated     true
                                                     :viewPosition 0})
              true)))))))

(defn- on-category-press [scroll-ref layout-offsets category state*]
  (swap! state* assoc
         :category category
         :scroll-target-category category)
  (reagent/flush)
  (when-not (scroll-to-category! scroll-ref layout-offsets category)
    (swap! state* dissoc :scroll-target-category)))

(defn- category-button [{:keys [category layout-id layout-offsets scroll-ref selected? state*]}]
  (let [on-press! (react/use-callback (fn []
                                        (on-category-press scroll-ref layout-offsets category state*))
                                      [category layout-id scroll-ref state*])]
    [button/button {:type     (if selected? :grey :ghost)
                    :icon     {:name (get category-icons category)}
                    :size     32
                    :on-press on-press!}]))

(defn emoji-picker-header [{:keys [state*]}]
  (let [theme-color (context/use-theme-color)
        set-input! (react/use-callback #(swap! state* assoc :input %) [])]
    [:rn/view {:style [style/search-input-container
                       (style/sheet-region-background theme-color)]}
     [input/input {:size           32
                   :placeholder    "Busca emojis"
                   :icon           {:name :icon/search}
                   :on-change-text set-input!}]]))

(defn- category-footer [{:keys [category layout-id layout-offsets scroll-ref state*]}]
  (let [bottom-inset  (safe-area/use-bottom)
        footer-inset  (if @above-keyboad-view/keyboard-visible? 0 bottom-inset)
        theme-color   (context/use-theme-color)]
    (into [:effect/view {:style     [(style/category-footer-container footer-inset)
                                     (when rn/android? (style/sheet-region-background theme-color))]
                         :effect    :glass
                         :intensity :regular
                         :theme     (:theme theme-color)}]
          (map (fn [{:keys [id]}]
                 [category-button {:category       id
                                   :layout-id      layout-id
                                   :layout-offsets layout-offsets
                                   :scroll-ref     scroll-ref
                                   :selected?      (= category id)
                                   :state*         state*}]))
          emoji-picker.data/categories)))

(defn emoji-picker [{:keys [on-emoji-press state*]}]
  (let [theme-color (context/use-theme-color)]
    [:rn/view {:style [style/root (style/sheet-region-background theme-color)]}
     [emoji-list {:on-emoji-press on-emoji-press
                  :state*         state*}]]))

(defn emoji-picker-footer [{:keys [state*]}]
  (when-let [{:keys [category input layout-id layout-offsets scroll-ref]} @state*]
    (when-not (search-term? input)
      [category-footer {:category         (or category :people)
                        :layout-id        layout-id
                        :layout-offsets   layout-offsets
                        :scroll-ref       scroll-ref
                        :state*           state*}])))
