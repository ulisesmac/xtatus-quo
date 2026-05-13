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
            [xquo.context :as context]))

(def ^:private emojis-per-row 7)
(def ^:private search-debounce-ms 200)

(def ^:private title-kw->text
  {:t/emoji-people   "Personas"
   :t/emoji-food     "Comida"
   :t/emoji-nature   "Naturaleza"
   :t/emoji-travel   "Viajes"
   :t/emoji-activity "Actividades"
   :t/emoji-symbols  "Símbolos"
   :t/emoji-objects  "Objetos"
   :t/emoji-flags    "Banderas"})

(def ^:private category-icons
  {:people   :icon/face-happy
   :nature   :icon/leaf
   :food     :icon/food
   :activity :icon/activity
   :travel   :icon/ship
   :objects  :icon/floor-lamp
   :symbols  :icon/hashtag
   :flags    :icon/flag})

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

(defn- emoji-section [{:keys [title]}]
  [divider-label/divider-label {:title        (get title-kw->text title title)
                                :compact?     false
                                :collapsible? false}])

(defn- emoji-press-handler [on-emoji-press unicode]
  (fn []
    (on-emoji-press unicode)))

(defn- emoji-row [{:keys [emoji-style emojis on-emoji-press]}]
  (into [:rn/view {:style style/emoji-row}]
        (map (fn [{:keys [unicode]}]
               ^{:key unicode}
               [:rn/pressable {:style    [style/emoji-pressable-base emoji-style]
                                :on-press (emoji-press-handler on-emoji-press unicode)}
                [:rn/text {:style                style/emoji-text
                           :adjustsFontSizeToFit true}
                 unicode]]))
        emojis))

(defn- render-item [{:keys [emoji-style item on-emoji-press]}]
  (if (:header? item)
    [emoji-section item]
    [emoji-row {:emoji-style    emoji-style
                :emojis         item
                :on-emoji-press on-emoji-press}]))

(defn- key-extractor [item index]
  (if (:header? item)
    (str "header-" index)
    (:unicode (first item))))

(defn- get-item-layout [layout-offsets row-height section-height data index]
  (let [height (if (:header? (aget data index)) section-height row-height)]
    #js{:length height
        :offset (get layout-offsets index)
        :index  index}))

(defn- get-search-item-layout [row-height _ index]
  #js{:length row-height
      :offset (* row-height index)
      :index  index})

(defn- on-category-press [scroll-ref layout-offsets category set-category!]
  (let [index (get emoji-picker.data/section-header-indexes category)]
    (when-let [scroll-list (j/get scroll-ref :current)]
    (when (j/get scroll-list :scrollToOffset)
      (j/call scroll-list :scrollToOffset #js{:offset   (+ (get layout-offsets index) 10)
                                              :animated false})))
    (set-category! category)
    (reagent/flush)))

(defn- on-scroll [category set-category! layout-offsets event]
  (let [offset        (j/get-in event [:nativeEvent :contentOffset :y])
        next-category (index->category (current-focused-index layout-offsets offset))]
    (when-not (= category next-category)
      (set-category! next-category))))

(defn- emoji-list
  [{:keys [category emoji-size input layout-offsets on-emoji-press row-height scroll-ref state*]}]
  (let [bottom-safe-area (safe-area/use-bottom)
        [search-term set-search-term!] (react/use-state nil)
        search-string      (emoji-picker.data/normalize-search-text search-term)
        searching?         (seq search-string)
        data               (react/use-memo #(with-meta (emojis-to-render search-string) {:keep-items true})
                                           [search-string])
        emoji-style        (react/use-memo #(style/emoji-pressable-size emoji-size) [emoji-size])
        list-content-style (react/use-memo #(style/list-content bottom-safe-area searching?)
                                           [bottom-safe-area searching?])
        layout-id          (peek layout-offsets)
        set-category!      (react/use-callback #(swap! state* assoc :category %) [])
        scroll!            (react/use-callback (fn [event]
                                                 (on-scroll category set-category! layout-offsets event))
                                               [category layout-id set-category!])
        render-item!       (react/use-callback
                            (fn [js-data]
                              (reagent/as-element
                               [render-item {:emoji-style    emoji-style
                                             :item           (j/get js-data :item)
                                             :on-emoji-press on-emoji-press}]))
                            [emoji-style on-emoji-press])
        get-item-layout!   (react/use-callback
                            (fn [data index]
                              (get-item-layout layout-offsets row-height emoji-section-height data index))
                            [layout-id row-height])
        get-search-item-layout! (react/use-callback
                                 (fn [data index]
                                   (get-search-item-layout row-height data index))
                                 [row-height])
        change-search!     (react/use-memo #(gfns/debounce set-search-term! search-debounce-ms) [])]
    (react/use-effect #(change-search! input)
                      [input])
    [:rn/flat-list (cond-> {:ref                             scroll-ref
                            :style                           style/list-root
                            :content-container-style         list-content-style
                            :data                            data
                            :render-item                     render-item!
                            :key-extractor                   key-extractor
                            :initial-num-to-render           6
                            :max-to-render-per-batch         4
                            :window-size                     5
                            :update-cells-batching-period    32
                            :remove-clipped-subviews         true
                            :shows-vertical-scroll-indicator false
                            :keyboard-should-persist-taps    :always
                            :get-item-layout                 (if searching?
                                                               get-search-item-layout!
                                                               get-item-layout!)}
                     (not searching?) (assoc :scroll-event-throttle 300
                                             :on-scroll scroll!))]))

(defn- category-button [{:keys [category layout-offsets scroll-ref selected? set-category!]}]
  (let [layout-id      (peek layout-offsets)
        on-press!      (react/use-callback #(on-category-press scroll-ref layout-offsets category set-category!)
                                           [category layout-id set-category!])]
    [button/button {:type     (if selected? :grey :ghost)
                    :icon     {:name (get category-icons category)}
                    :size     32
                    :on-press on-press!}]))

(defn emoji-picker-header [{:keys [state*]}]
  (let [theme      (context/use-theme)
        set-input! (react/use-callback #(swap! state* assoc :input %) [])]
    [:rn/view {:style [style/search-input-container
                       (style/sheet-region-background theme)]}
     [input/input {:size           32
                   :placeholder    "Busca emojis"
                   :icon           {:name :icon/search}
                   :on-change-text set-input!}]]))

(defn- category-footer [{:keys [category layout-offsets scroll-ref state*]}]
  (let [bottom-inset  (safe-area/use-bottom)
        [keyboard-visible? set-keyboard-visible!] (react/use-state (rn/keyboard-visible?))
        footer-inset  (if keyboard-visible? 0 bottom-inset)
        theme         (context/use-theme)
        set-category! (react/use-callback #(swap! state* assoc :category %) [])]
    (react/use-effect
     (fn []
       (let [show-sub (rn/add-keyboard-listener! :keyboardDidShow #(set-keyboard-visible! true))
             hide-sub (rn/add-keyboard-listener! :keyboardDidHide #(set-keyboard-visible! false))]
         (fn []
           (j/call show-sub :remove)
           (j/call hide-sub :remove))))
     [])
    (into [:rn/view {:style [(style/category-footer-container footer-inset)
                             (style/sheet-region-background theme)]}]
          (map (fn [{:keys [id]}]
                 [category-button {:category       id
                                   :layout-offsets layout-offsets
                                   :scroll-ref     scroll-ref
                                   :selected?      (= category id)
                                   :set-category!  set-category!}]))
          emoji-picker.data/categories)))

(defn emoji-picker [{:keys [on-emoji-press state*]}]
  (let [scroll-ref              (react/use-ref nil)
        {:keys [width]}         (safe-area/use-window)
        {:keys [category input]} @state*
        active-category         (or category :people)
        size                    (emoji-size width)
        row-height              (emoji-row-height size)
        layout-offsets          (react/use-memo #(data-layout-offsets row-height emoji-section-height)
                                                [row-height])]
    (react/use-effect
     (fn []
       (swap! state* assoc
              :layout-offsets layout-offsets
              :scroll-ref scroll-ref)
       nil)
     [row-height])
    [:rn/view {:style style/root}
     [emoji-list {:category         active-category
                  :emoji-size       size
                  :input            input
                  :layout-offsets   layout-offsets
                  :on-emoji-press   on-emoji-press
                  :row-height       row-height
                  :scroll-ref       scroll-ref
                  :state*           state*}]]))

(defn emoji-picker-footer [{:keys [state*]}]
  (when-let [{:keys [category input layout-offsets scroll-ref]} @state*]
    (when-not (search-term? input)
      [category-footer {:category         (or category :people)
                        :layout-offsets   layout-offsets
                        :scroll-ref       scroll-ref
                        :state*           state*}])))
