(ns xquo.community.token-gating.view
  (:require [react-native.utils :as rn.utils]
            [xquo.components.button.view :as button]
            [xquo.components.icon.view :as icon]
            [xquo.components.info.information-box.view :as information-box]
            [xquo.components.text.page-top.view :as page-top]
            [xquo.components.text.view :as text]
            [xquo.community.token-gating.style :as style]
            [xquo.context :as context]
            [xquo.divider.label.view :as divider-label]
            [xquo.divider.line.view :as divider-line]
            [xquo.foundations.colors :as colors]
            [xquo.tag.asset.view :as asset]))

(defn- heading [{:keys [section variant index info dark-theme?]}]
  [:rn/view {:style (style/heading variant index)}
   [:rn/view {:style style/heading-row}
    [text/text {:font :font/medium-15 :number-of-lines 1 :style style/title}
     (:title section)]
    (when info
      [:rn/pressable {:on-press (:on-press info)}
       [icon/view (assoc (:icon info)
                         :name  (get-in info [:icon :name] :icon/info-outline)
                         :size  (get-in info [:icon :size] 20)
                         :color (get-in info
                                        [:icon :color]
                                        (colors/get-color (if dark-theme? :color/neutral-40 :color/neutral-50))))]])]
   (when (:description section)
     [text/text {:font :font/regular-13 :style style/description}
      (:description section)])])

(defn- requirement-row [{:keys [row dark-theme?]}]
  [:rn/view (-> row
                (dissoc :items :prefix :style)
                (assoc :style (rn.utils/add-styles style/row (:style row))))
   (when (:prefix row)
     [text/text {:font :font/regular-13 :style [style/inline-prefix (style/separator-text dark-theme?)]}
      (:prefix row)])
   (for [[index item] (map-indexed vector (:items row))]
     ^{:key (or (:id item) index)}
     [asset/asset-tag (dissoc item :id)])])

(defn- requirement-group [{:keys [group variant first? last? dark-theme?]}]
  (into [:rn/view (-> group
                      (dissoc :rows :style)
                      (assoc :style (rn.utils/add-styles (style/group variant first? last?) (:style group))))]
        (map (fn [row]
               [requirement-row {:row row :dark-theme? dark-theme?}])
             (:rows group))))

(defn- section [{:keys [section variant index info separator-label dark-theme?]}]
  [:<>
   (when (and (pos? index) (= variant :channel-list))
     [divider-line/divider-line])
   [heading {:section section :variant variant :index index :info info :dark-theme? dark-theme?}]
   (for [[group-index group] (map-indexed vector (:groups section))]
     ^{:key group-index}
     [:<>
      (when (pos? group-index)
        [:rn/view {:style (style/separator-wrapper variant)}
         [divider-label/divider-label {:title         (if (= variant :community)
                                                        separator-label
                                                        [text/text {:font :font/regular-11 :style (style/separator-text dark-theme?)}
                                                         separator-label])
                                       :collapsible?  false
                                       :divider-line? false
                                       :style         (style/separator variant dark-theme?)}]])
      [requirement-group {:group       group
                          :variant     variant
                          :first?      (zero? group-index)
                          :last?       (= group-index (dec (count (:groups section))))
                          :dark-theme? dark-theme?}]])])

(defn token-gating [{:keys [variant sections header info button information footer-text separator-label]
                     :or   {variant :community separator-label "or"}
                     :as   props}]
  (let [dark-theme? (:dark-theme? (context/use-theme-color))]
    [:rn/view (-> props
                  (dissoc :variant :sections :header :info :button :information :footer-text :separator-label :style)
                  (assoc :style (rn.utils/add-styles style/container
                                                     (style/root variant dark-theme?)
                                                     (:style props))))
     (when (and (= variant :channel-list) header)
       [page-top/page-top header])
     (for [[index section-data] (map-indexed vector sections)]
       ^{:key index}
       [section {:section         section-data
                 :variant         variant
                 :index           index
                 :info            (when (zero? index) info)
                 :separator-label separator-label
                 :dark-theme?     dark-theme?}])
     (when (or button footer-text)
       [:rn/view {:style (style/action variant)}
        (when button
          [button/button (-> button
                             (dissoc :label)
                             (assoc :size  (:size button 40)
                                    :style (rn.utils/add-styles style/button (:style button))))
           (:label button)])
        (when footer-text
          [text/text {:font :font/regular-13 :style (style/footer dark-theme?)}
           footer-text])])
     (when information
       [:rn/view {:style style/information}
        [information-box/information-box information]])
     (when (= variant :community)
       [:rn/view {:style (style/card-border dark-theme?) :pointer-events :none}])]))
