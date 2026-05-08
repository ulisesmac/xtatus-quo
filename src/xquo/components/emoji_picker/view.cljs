(ns xquo.components.emoji-picker.view
  (:require [applied-science.js-interop :as j]
            [clojure.string :as string]
            [goog.functions :as gfns]
            [react-native.core :as rn]
            [react-native.safe-area-context :as safe-area]
            [reagent-extended.react :as react]
            [reagent.core :as reagent]
            [status-im.common.emoji-picker.data :as emoji-picker.data]
            [xquo.components.button.view :as button]
            [xquo.components.divider.divider-label.view :as divider-label]
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
  (loop [index 0]
    (if (and (< index (count layout-offsets))
             (<= (nth layout-offsets index) offset))
      (recur (inc index))
      (dec index))))

(defn- index->category [index]
  (or (reduce (fn [category [next-category section-index]]
                (if (<= section-index index)
                  next-category
                  (reduced category)))
              nil
              section-sorted)
      (ffirst section-sorted)))

(defn- search-term? [input]
  (some-> input string/trim seq some?))

(defn- emojis-to-render [input]
  (if (search-term? input)
    (let [search-string (string/lower-case input)]
      (->> emoji-picker.data/emoji-data
           (filter (fn [{:keys [label tags]}]
                     (or (string/includes? label search-string)
                         (some #(string/includes? % search-string) tags))))
           (partition-all emojis-per-row)))
    emoji-picker.data/flatten-data))

(defn- emoji-section [{:keys [title]}]
  [divider-label/divider-label {:title        (get title-kw->text title title)
                                :compact?     false
                                :collapsible? false}])

(defn- emoji-button [{:keys [emoji-size on-emoji-press unicode]}]
  (let [on-press! (react/use-callback (fn []
                                        (on-emoji-press unicode))
                                      [on-emoji-press unicode])]
    [:rn/pressable {:style    [style/emoji-pressable-base (style/emoji-pressable-size emoji-size)]
                    :on-press on-press!}
     [:rn/text {:style                style/emoji-text
                :adjustsFontSizeToFit true}
      unicode]]))

(defn- emoji-row [{:keys [emoji-size emojis on-emoji-press]}]
  (into [:rn/view {:style style/emoji-row}]
        (map (fn [{:keys [unicode]}]
               [emoji-button {:emoji-size     emoji-size
                              :on-emoji-press on-emoji-press
                              :unicode        unicode}]))
        emojis))

(defn- render-item [{:keys [emoji-size item on-emoji-press]}]
  (if (:header? item)
    [emoji-section item]
    [emoji-row {:emoji-size     emoji-size
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
        searching?       (search-term? search-term)
        data             (react/use-memo #(with-meta (emojis-to-render search-term) {:keep-items true})
                                         [search-term])
        layout-id        (peek layout-offsets)
        set-category!    (react/use-callback #(swap! state* assoc :category %) [])
        scroll!          (react/use-callback (fn [event]
                                               (on-scroll category set-category! layout-offsets event))
                                             [category layout-id set-category!])
        render-item!     (react/use-callback
                          (fn [js-data]
                            (reagent/as-element
                             [render-item {:emoji-size     emoji-size
                                           :item           (j/get js-data :item)
                                           :on-emoji-press on-emoji-press}]))
                          [emoji-size on-emoji-press])
        get-item-layout! (react/use-callback
                          (fn [data index]
                            (get-item-layout layout-offsets row-height emoji-section-height data index))
                          [layout-id row-height])
        change-search!   (react/use-memo #(gfns/debounce set-search-term! search-debounce-ms) [])]
    (react/use-effect #(change-search! input)
                      [input])
    [:rn/flat-list (cond-> {:ref                             scroll-ref
                            :style                           style/list-root
                            :content-container-style         (style/list-content bottom-safe-area searching?)
                            :data                            data
                            :render-item                     render-item!
                            :key-extractor                   key-extractor
                            :shows-vertical-scroll-indicator false
                            :keyboard-should-persist-taps    :always}
                     (not searching?) (assoc :scroll-event-throttle 300
                                             :on-scroll scroll!
                                             :get-item-layout get-item-layout!))]))

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
              :category active-category
              :layout-offsets layout-offsets
              :scroll-ref scroll-ref)
       nil)
     [active-category row-height])
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
