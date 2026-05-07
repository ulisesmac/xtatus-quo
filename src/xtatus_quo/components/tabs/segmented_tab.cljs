(ns xtatus-quo.components.tabs.segmented-tab
  (:require
   [xtatus-quo.components.tabs.tab.view :as tab]
   [quo.context :as quo.context]
   [quo.foundations.colors :as colors]
   [react-native.corex :as rn]))

(def themes-for-blur
  {:theme/light {:background-color colors/neutral-80-opa-5}
   :theme/dark  {:background-color colors/white-opa-5}})

(def themes
  {:theme/light {:background-color colors/neutral-10}
   :theme/dark  {:background-color colors/neutral-90}})

(defn- default-before-props [size]
  {:container-style {:margin-right 4}})

(defn segmented-control
  [{:keys [data size blur? container-style item-container-style
           active-item-container-style default-active on-change]}]
  (let [theme    (quo.context/use-theme)
        [active-tab-id
         set-active-tab-id] (rn/use-state default-active)
        on-press (rn/use-callback
                  (fn [tab-id]
                    (set-active-tab-id tab-id)
                    (when on-change (on-change tab-id)))
                  [on-change])]
    [rn/view {:style [{:flex-direction   :row
                       :background-color (get-in (if blur? themes-for-blur themes)
                                                 [theme :background-color])
                       :border-radius    (case size
                                           32 10
                                           28 10
                                           24 8
                                           20 6
                                           6)
                       :padding          2}
                      container-style]}
     (for [[indx {:keys [label id before icon before-props icon-props]}] (map-indexed vector data)]
       (let [before*       (or before icon)
             before-props* (or before-props
                               icon-props
                               (when before*
                                 (default-before-props size)))]
         ^{:key id}
         [rn/view {:style {:margin-left (if (= 0 indx) 0 2)
                           :flex        1}}
          [tab/view
           {:id                          id
            :before                      before*
            :before-props                before-props*
            :active-item-container-style active-item-container-style
            :item-container-style        item-container-style
            :segmented?                  true
            :size                        size
            :blur?                       blur?
            :active                      (= id active-tab-id)
            :on-press                    on-press}
           label]]))]))
