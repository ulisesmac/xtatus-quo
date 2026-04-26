(ns xquo.components.drawer.top.view
  (:require [reagent-extended-compiler.utils.transforms :as rec.xf]
            [xquo.components.button.view :as button]
            [xquo.components.drawer.top.style :as style]
            [xquo.components.icon.view :as icon]
            [xquo.components.settings.section-label.view :as section-label]
            [xquo.components.text.view :as text]
            [xquo.context :as context]
            [xquo.foundations.colors :as colors]))

(defn- description-segments [description]
  (if (string? description)
    [{:text description}]
    description))

(defn- title-view [{:keys [theme title]}]
  (if (vector? title)
    title
    [text/text {:font            :font/semibold-19
                :number-of-lines 1
                :style           (style/title-text-style theme)}
     title]))

(defn drawer-handle
  "Drawer handle component.

  API:
  - `props` map
    - `:skip-handle?` optional boolean that keeps the outer container but hides
      the inner bar when true
    - `:style` optional caller style
    - Any additional keys are forwarded to `:rn/view`."
  [{:keys [skip-handle? handle-style] :as props}]
  (let [theme (context/use-theme)]
    [:rn/view (-> props
                  (dissoc :skip-handle?)
                  (assoc :style (rec.xf/add-styles style/handle-container handle-style)))
     (when-not skip-handle?
       [:rn/view {:style (style/handle-bar-style theme)}])]))

(defn- leading-placeholder-view [_]
  ;; TODO: replace this red placeholder with the real drawer leading media components.
  [:rn/view {:style [style/leading-placeholder-base
                     (style/leading-placeholder-style)]}])

(defn- context-item-view [{:keys [theme item]}]
  (cond
    (= (:type item) :text)
    [text/text {:font  :font/regular-13
                :style (style/title-text-style theme)}
     (:text item)]

    (= (:type item) :placeholder)
    ;; TODO: replace this red placeholder with the real context tag component.
    [:rn/view {:style [style/context-placeholder-base
                       (style/context-placeholder-style (:width item))]}]))

(defn- context-tags-view [{:keys [theme context-tags]}]
  (into [:rn/view {:style style/context-row}]
        (map (fn [item]
               [context-item-view {:theme theme
                                   :item  item}]))
        context-tags))

(defn- subcontent-view [{:keys [subcontent]}]
  [:rn/view {:style style/subcontent-slot}
   subcontent])

(defn- description-view [{:keys [theme blur? description leading description-icon]}]
  [:rn/view {:style style/description-row}
   (into [text/text {:font            (if leading :font/monospace-13 :font/regular-15)
                     :number-of-lines 1
                     :style           (style/secondary-text-style theme blur?)}]
         (map (fn [segment]
                [:rn/text {:style (style/description-segment-style (:color segment))}
                 (:text segment)]))
         (description-segments description))
   (when description-icon
     [icon/icon {:name  description-icon
                 :size  20
                 :color (style/icon-color theme blur?)
                 :style style/description-icon-scale}])])

(defn- title-inline-view [{:keys [theme blur? title title-icon]}]
  [:rn/view {:style style/title-inline-row}
   [title-view {:theme theme
                :title title}]
   (when title-icon
     [icon/icon {:name  title-icon
                 :size  20
                 :color (style/icon-color theme blur?)}])])

(defn- trailing-view [{:keys [theme blur? info? counter button]}]
  (cond
    button
    (let [button-type  (or (:type button) :primary)
          color        (context/use-color)]
      [button/button
       (cond-> (assoc button :size 24)
         (= button-type :primary)
         (assoc :icon-color (get button
                                 :icon-color
                                 (colors/get-color :color/white-100))
                :style      (rec.xf/add-styles
                             (style/primary-button-style theme color)
                             (:style button))))])

    info?
    [icon/icon {:name  :icon/info
                :size  20
                :color (style/icon-color theme blur?)}]

    counter
    [text/text {:font            :font/regular-13
                :number-of-lines 1
                :style           [style/counter-text
                                  (style/counter-text-style theme blur?)]}
     counter]))

(defn- counter-row-view [{:keys [theme blur? title counter]}]
  [:rn/view {:style [style/title-row
                     style/title-row-baseline
                     style/title-row-gap-12]}
   [:rn/view {:style style/title-slot}
    [title-view {:theme theme
                 :title title}]]
   [text/text {:font            :font/regular-13
               :number-of-lines 1
               :style           [style/counter-text
                                 (style/counter-text-style theme blur?)]}
    counter]])

(defn- plain-row-view [{:keys [theme blur? title title-icon info? button]}]
  [:rn/view {:style [style/title-row
                     style/title-row-center
                     (cond
                       button style/title-row-gap-20
                       info?        style/title-row-gap-12)]}
   [:rn/view {:style style/title-slot}
    [title-inline-view {:theme      theme
                        :blur?      blur?
                        :title      title
                        :title-icon title-icon}]]
   [trailing-view {:theme  theme
                   :blur?  blur?
                   :info?  info?
                   :button button}]])

(defn- leading-content-view
  [{:keys [theme blur? title description description-icon title-icon leading subcontent
           compact?]}]
  [:rn/view {:style [style/content-base
                     (if compact?
                       style/content-bottom-8
                       style/content-bottom-12)]}
   [:rn/view {:style style/leading-row}
    [leading-placeholder-view {:leading leading}]
    [:rn/view {:style style/leading-column}
     [title-inline-view {:theme      theme
                         :blur?      blur?
                         :title      title
                         :title-icon title-icon}]
     (when description
       [description-view {:theme            theme
                          :blur?            blur?
                          :description      description
                          :leading          leading
                          :description-icon description-icon}])
     (when subcontent
       [subcontent-view {:subcontent subcontent}])]]])

(defn- standard-content-view
  [{:keys [theme blur? title description context-tags info? counter button
           title-icon subcontent compact?]}]
  [:rn/view {:style [style/content-base
                     (if compact?
                       style/content-bottom-8
                       style/content-bottom-12)
                     style/content-column]}
   (if counter
     [counter-row-view {:theme      theme
                        :blur?      blur?
                        :title      title
                        :counter    counter}]
     [plain-row-view {:theme       theme
                      :blur?       blur?
                      :title       title
                      :title-icon  title-icon
                      :info?       info?
                      :button      button}])
   (when context-tags
     [:rn/view {:style style/context-row-slot}
      [context-tags-view {:theme        theme
                          :context-tags context-tags}]])
   (when description
     [:rn/view {:style (if context-tags
                         style/description-row-slot-with-context
                         style/description-row-slot)}
      [description-view {:theme       theme
                         :blur?       blur?
                         :description description}]])
   (when subcontent
     [subcontent-view {:subcontent subcontent}])])

(defn drawer-top
  "Drawer top component.

  API:
  - `props` map
    - `:skip-handle?` optional boolean that keeps the top handle container but
      hides the inner handle bar when true
    - `:label` optional label variant. When present, title props are ignored.
    - `:compact?` optional boolean for the tighter documentation top spacing
    - `:title` title string or hiccup vector (default `\"Title\"`)
    - `:description` optional string or vector of segment maps `{:text ... :color :color/...}`
    - `:subcontent` optional custom hiccup rendered in a fixed 24px slot beneath
      the main text content; overflow outside that height remains visible
    - `:counter` optional right-side counter text (for example `\"00/00\"`)
    - `:info?` optional right-side info icon
    - `:button` optional trailing button props forwarded to `xquo/button`
      and forced to `:size 24`
    - `:title-icon` optional icon shown inline after the title
    - `:description-icon` optional icon shown inline after the description
    - `:leading` optional leading placeholder map
      - `:type` one of `:account-avatar`, `:icon-avatar`, `:user-avatar`, `:token-avatar`
    - `:context-tags` optional vector
      - `{:type :placeholder :width n}`
      - `{:type :text :text \"...\"}`
    - `:blur?` optional boolean for blur styling
    - `:style` optional caller style (map/vector/js style)
    - Any additional keys are forwarded to `:rn/view`."
  [{:keys [skip-handle? label compact? title description subcontent counter info? button
           title-icon description-icon leading context-tags blur? handle-style]
    :or   {title "Title"}
    :as   props}]
  (let [theme (context/use-theme)]
    [:rn/view (-> props
                  (dissoc :skip-handle? :label :compact? :title :description :subcontent :counter
                          :info? :button :title-icon :description-icon :leading :context-tags
                          :blur? :style :handle-style)
                  (assoc :style (rec.xf/add-styles style/container-base (:style props))))
     [drawer-handle {:skip-handle? skip-handle?
                     :handle-style handle-style}]
     (if label
       [:rn/view {:style [style/content-base style/content-bottom-12 style/content-column]}
        [section-label/section-label {:label label :blur? blur?}]
        (when subcontent
          [subcontent-view {:subcontent subcontent}])]
       (if leading
         [leading-content-view {:theme            theme
                                :blur?            blur?
                                :title            title
                                :description      description
                                :subcontent       subcontent
                                :description-icon description-icon
                                :title-icon       title-icon
                                :leading          leading
                                :compact?         compact?}]
         [standard-content-view {:theme       theme
                                 :blur?       blur?
                                 :title       title
                                 :description description
                                 :subcontent  subcontent
                                 :context-tags context-tags
                                 :info?       info?
                                 :counter     counter
                                 :button      button
                                 :title-icon  title-icon
                                 :compact?    compact?}]))]))
