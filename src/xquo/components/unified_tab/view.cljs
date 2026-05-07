(ns xquo.components.unified-tab.view
  (:require [applied-science.js-interop :as j]
            [react-native-gesture-handler :as gh]
            [react-native.core :as rn-core]
            [xquo.components.icon.view :as icon]
            [xquo.components.unified-tab.style :as style]
            [xquo.context :as context]
            [xquo.foundations.typography :as typography]
            [xquo.react-native :as rn]
            [xquo.react-native-reanimated :as rnr]
            [xquo.worklets.unified-tab :as unified-tab-worklets]))

(defn- create-pan-gesture
  [content-count gesture-start-index last-tab-index select-index! tab-progress window-width]
  (let [gesture (gh/gesture-pan)]
    (j/call gesture :enabled (> content-count 1))
    (j/call gesture :activeOffsetX #js[-12 12])
    (j/call gesture :failOffsetY #js[-14 14])
    (j/call gesture :onBegin (unified-tab-worklets/pan-on-begin tab-progress gesture-start-index))
    (j/call gesture :onUpdate (unified-tab-worklets/pan-on-update tab-progress
                                                                  gesture-start-index
                                                                  last-tab-index
                                                                  window-width))
    (j/call gesture :onEnd (unified-tab-worklets/pan-on-end tab-progress
                                                            gesture-start-index
                                                            window-width
                                                            last-tab-index
                                                            select-index!))
    gesture))

(defn- tab-text [{:keys [content-style dark-theme? ellipsize-mode selected? size]} child]
  [:animated/text {:style           [(typography/get-style
                                      (case size
                                        24 :font/medium-11
                                        32 :font/medium-15
                                        nil))
                                     content-style
                                     (style/text-color dark-theme? selected?)]
                   :number-of-lines 1
                   :ellipsize-mode  ellipsize-mode}
   child])

(defn- selected-tab-index [items selected-tab]
  (first (keep-indexed (fn [index {:keys [id]}]
                         (when (= id selected-tab)
                           index))
                       items)))

(defn- selected-indicator-view
  [{:keys [blur? dark-theme? gap-translate-x item-count selected-index translate-x type]}]
  (into [:rn/view {:style          style/selected-indicator-frame
                   :pointer-events :none}]
        (map (fn [index]
               [:rn/view {:style style/selected-indicator-slot}
                (when (zero? index)
                  [:animated/view {:style (style/selected-indicator
                                           selected-index
                                           translate-x
                                           gap-translate-x
                                           type
                                           dark-theme?
                                           blur?)}])]))
        (range item-count)))

(defn- tab-item [{:keys [content dark-theme? emoji icon id index selected-icon selected?
                         select-tab! size]}]
  (let [on-press! (rn/use-callback (fn []
                                     (select-tab! id index))
                                   [id index select-tab!])]
    [:rn/pressable {:style               style/tab-item
                    :on-press            on-press!
                    :accessibility-state {:selected selected?}}
     [:rn/view {:style style/tab-content}
      (if icon
        [icon/view (if selected?
                     (-> icon
                         (assoc :color style/selected-content-color)
                         (into selected-icon))
                     icon)]
        (when emoji
          [tab-text {:dark-theme?    dark-theme?
                     :ellipsize-mode :clip
                     :selected?      selected?
                     :size           size}
           emoji]))
      (when content
        (if (string? content)
          [tab-text {:content-style  style/content-text
                     :dark-theme?    dark-theme?
                     :ellipsize-mode :tail
                     :selected?      selected?
                     :size           size}
           content]
          content))]]))

(defn unified-tab-content [{:keys [active-index translate-x]} & children]
  (let [content-count (count children)
        track-state   (if translate-x
                        (style/content-track-animated-state translate-x)
                        (style/content-track-state active-index content-count))]
    [:rn/view {:style style/content-root}
     (into [:animated/view {:style [style/content-track
                                    (style/content-track-layout content-count)
                                    track-state]}]
           (map (fn [child]
                  [:rn/view {:style style/content-page}
                   child]))
           children)]))

(defn- tabs-section
  [{:keys [blur? current-selected-tab items select-tab! selected-index size tab-progress type]}]
  (let [{:keys [dark-theme?]} (context/use-theme-color)
        indicator-translate-x     (unified-tab-worklets/use-indicator-translate-x tab-progress)
        indicator-gap-translate-x (unified-tab-worklets/use-indicator-gap-translate-x tab-progress)
        press-tab!                (rn/use-callback
                                   (fn [id index]
                                     (unified-tab-worklets/select-tab tab-progress index)
                                     (select-tab! id index))
                                   [select-tab!])]
    [:rn/view {:style style/root}
     (into [:rn/view {:style (style/tab-container size type dark-theme? blur?)}
            [selected-indicator-view {:blur?           blur?
                                      :dark-theme?     dark-theme?
                                      :gap-translate-x indicator-gap-translate-x
                                      :item-count      (count items)
                                      :selected-index  selected-index
                                      :translate-x     indicator-translate-x
                                      :type            type}]]
           (map-indexed (fn [index item]
                          [tab-item (assoc item
                                      :dark-theme? dark-theme?
                                      :index index
                                      :selected? (= (:id item) current-selected-tab)
                                      :select-tab! press-tab!
                                      :size size)]))
           items)]))

(defn- content-section
  [{:keys [items select-tab! selected-index tab-progress]}
   & children]
  (let [content-count       (count children)
        last-tab-index      (dec (count items))
        window-width        (:width (rn-core/use-window-dimensions))
        gesture-start-index (rnr/use-shared-value selected-index)
        content-translate-x (unified-tab-worklets/use-content-translate-x tab-progress window-width)
        select-index!       (rn/use-callback
                             (fn [index]
                               (select-tab! (:id (nth items index)) index))
                             [items select-tab!])
        pan-gesture         (rn/use-memo
                             (fn []
                               (create-pan-gesture content-count
                                                   gesture-start-index
                                                   last-tab-index
                                                   select-index!
                                                   tab-progress
                                                   window-width))
                             [content-count last-tab-index select-index! window-width])]
    [:gh/gesture-detector {:gesture pan-gesture}
     (into [unified-tab-content {:active-index selected-index
                                 :translate-x  content-translate-x}]
           children)]))

(defn view
  [{:keys [blur? initial-selected items on-select selected-tab size type]}
   & children]
  (let [[internal-selected-tab
         set-internal-selected-tab!] (rn/use-state (or initial-selected (:id (first items))))
        current-selected-tab (or selected-tab internal-selected-tab)
        selected-index       (selected-tab-index items current-selected-tab)
        tab-progress         (rnr/use-shared-value selected-index)
        select-tab!          (rn/use-callback
                              (fn [id index]
                                (when-not selected-tab
                                  (set-internal-selected-tab! id))
                                (when on-select
                                  (on-select id index)))
                              [on-select selected-tab])]
    (rn/use-effect
     (fn []
       (unified-tab-worklets/select-tab tab-progress selected-index))
     [selected-index])
    (if (seq children)
      [:rn/view {:style style/root-with-content}
       [tabs-section {:blur?                blur?
                      :current-selected-tab current-selected-tab
                      :items                items
                      :select-tab!          select-tab!
                      :selected-index       selected-index
                      :size                 size
                      :tab-progress         tab-progress
                      :type                 type}]
       (into [content-section {:items          items
                               :select-tab!    select-tab!
                               :selected-index selected-index
                               :tab-progress   tab-progress}]
             children)]
      [tabs-section {:blur?                blur?
                     :current-selected-tab current-selected-tab
                     :items                items
                     :select-tab!          select-tab!
                     :selected-index       selected-index
                     :size                 size
                     :tab-progress         tab-progress
                     :type                 type}])))
