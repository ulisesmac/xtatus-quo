(ns xquo.list.item.view
  (:require [applied-science.js-interop :as j]
            [react-native.react.core :as react]
            [react-native.utils :as rn.utils :refer [style]]
            [xquo.components.avatar.view :as avatar]
            [xquo.components.button.style :as button-style]
            [xquo.components.button.view :as button]
            [xquo.components.counter.view :as counter]
            [xquo.components.icon.view :as icon]
            [xquo.components.selectors.selector.view :as selector]
            [xquo.components.tags.context-tag.view :as context-tag]
            [xquo.components.text.view :as text]
            [xquo.context :as context]
            [xquo.foundations.colors :as colors]
            [xquo.foundations.typography :as typography]
            [xquo.list.item.style :as styles]
            [xquo.list.item.values :as values]))

(defn- text-view [{:keys [value font color props]}]
  (let [{:keys [font number-of-lines ellipsize-mode style]
         :or   {font font number-of-lines 1 ellipsize-mode :tail}} props
        line-height (j/get (typography/get-style font) :lineHeight)]
    (if (string? value)
      [text/text (assoc props
                        :font            font
                        :number-of-lines number-of-lines
                        :ellipsize-mode  ellipsize-mode
                        :style           (rn.utils/add-styles (styles/text color line-height (= number-of-lines 1)) style))
       value]
      value)))

(defn- icon-slot [{:keys [theme blur? disabled? props]}]
  (let [{:keys          [on-press on-press-in on-press-out hit-slop size color]
         icon-disabled? :disabled?
         :or            {hit-slop 6
                         size     20
                         color    (styles/icon-color theme blur?)}} props
        component               (if on-press :animated/pressable :rn/view)
        [pressed? set-pressed!] (react/use-state false)
        press!                  (react/use-callback (fn [event]
                                                      (j/call event :stopPropagation)
                                                      (when on-press (on-press event)))
                                                    [on-press])
        press-in!               (react/use-callback (fn [event]
                                                      (j/call event :stopPropagation)
                                                      (set-pressed! true)
                                                      (when on-press-in (on-press-in event)))
                                                    [on-press-in])
        press-out!              (react/use-callback (fn [event]
                                                      (j/call event :stopPropagation)
                                                      (set-pressed! false)
                                                      (when on-press-out (on-press-out event)))
                                                    [on-press-out])]
    [component
     (cond-> {}
       :always (assoc :style [styles/icon
                              (when on-press
                                (if (and pressed? (not disabled?))
                                  button-style/pressable-pressed-state-style
                                  button-style/pressable-default-state-style))])
       on-press (assoc :on-press     press!
                       :on-press-in  press-in!
                       :on-press-out press-out!
                       :hit-slop     hit-slop
                       :disabled     (or disabled? icon-disabled?)))
     [icon/view (-> props
                    (dissoc :on-press :on-press-in :on-press-out :hit-slop :disabled?)
                    (assoc :size  size
                           :color color))]]))

(defn- button-slot [{:keys [blur? disabled? props]}]
  (let [{:keys            [on-press type size photo? label style]
         button-disabled? :disabled?
         :or              {type :outline size 24}} props
        press! (react/use-callback (fn [event]
                                     (j/call event :stopPropagation)
                                     (when on-press (on-press event)))
                                   [on-press])]
    [button/button (-> props
                       (dissoc :label)
                       (assoc :type      type
                              :size      size
                              :blur?     (:blur? props (and blur? (not photo?)))
                              :disabled? (or disabled? button-disabled?)
                              :on-press  press!
                              :style     (rn.utils/add-styles (when disabled? styles/inherited-disabled) style)))
     label]))

(defn- selector-slot [{:keys [blur? disabled? props]}]
  (let [{:keys              [on-press style]
         selector-disabled? :disabled?
         selector-blur?     :blur?
         :or                {selector-blur? blur?}} props
        press! (react/use-callback (fn [event]
                                     (j/call event :stopPropagation)
                                     (when on-press (on-press event)))
                                   [on-press])]
    [selector/selector (assoc props
                              :blur?      selector-blur?
                              :disabled?  (or disabled? selector-disabled?)
                              :on-press   press!
                              :style      (rn.utils/add-styles (when disabled? styles/inherited-disabled) style))]))

(defn- image-view [{:keys [source url resize-mode style]
                    :or   {resize-mode :cover}
                    :as   props}]
  [:rn/image (-> props
                 (dissoc :url)
                 (assoc :source      (or source {:uri url})
                        :resize-mode resize-mode
                        :style       (rn.utils/add-styles styles/image style)))])

(defn- avatar-view [{:keys [blur? disabled? props]}]
  (let [{:keys [size] avatar-blur? :blur? :or {size 32 avatar-blur? blur?}} props]
    [avatar/avatar (cond-> props
                     :always (assoc :size size :blur? avatar-blur?)
                     disabled? (dissoc :on-press))]))

(defn- leading-view [{:keys [theme blur? disabled? content]}]
  (case (:type content)
    :avatar [avatar-view {:blur? blur? :disabled? disabled? :props (:avatar content)}]
    :image [image-view (:image content)]
    :icon [icon-slot {:theme theme :blur? blur? :disabled? disabled? :props (:icon content)}]
    :custom (:view content)
    nil))

(defn- slot-view [{:keys [theme blur? disabled? entry]}]
  (case (:type entry)
    :icon
    [icon-slot {:theme theme :blur? blur? :disabled? disabled? :props (:icon entry)}]

    :button
    [button-slot {:blur? blur? :disabled? disabled? :props (:button entry)}]

    :selector
    [selector-slot {:blur? blur? :disabled? disabled? :props (:selector entry)}]

    :counter
    (let [{:keys [value] counter-blur? :blur? :or {counter-blur? blur?} :as props} (:counter entry)]
      [counter/counter (-> props (dissoc :value) (assoc :blur? counter-blur?)) value])

    :avatar
    [avatar-view {:blur? blur? :disabled? disabled? :props (:avatar entry)}]

    :image
    [image-view (:image entry)]

    :text
    (let [{:keys [value] :as props} (:text entry)]
      [text-view {:value value
                  :font  :font/regular-15
                  :color (styles/secondary theme blur?)
                  :props (dissoc props :value)}])

    :values
    (let [{values-blur? :blur? :or {values-blur? blur?} :as props} (:values entry)]
      [values/view (assoc props :theme theme :blur? values-blur?)])

    :tag
    (let [{:keys [label] tag-blur? :blur? :or {tag-blur? blur?} :as props} (:tag entry)]
      [context-tag/context-tag (-> props (dissoc :label) (assoc :blur? tag-blur?)) label])

    :custom
    (:view entry)

    nil))

(defn- cluster [{:keys [theme blur? disabled? entries balanced?]}]
  (let [pair? (and (= (count entries) 2)
                   (#{:counter :icon} (:type (first entries)))
                   (= (:type (second entries)) :icon))
        gap   (if pair? 2 8)]
    (into [:rn/view {:style (rn.utils/add-styles styles/slots
                                                 (style {:gap gap})
                                                 (when balanced? styles/balanced-values))}]
          (map (fn [entry]
                 [slot-view {:theme theme :blur? blur? :disabled? disabled? :entry entry}]))
          entries)))

(defn item [{:keys      [type compact? title description title-props description-props content left right caption
                         active? selected? disabled? blur? on-press on-long-press on-press-in on-press-out
                         pointer-events]
             item-style :style
             :or        {type :default pointer-events :auto}
             :as        props}]
  (let [{:keys [theme color] :as theme-color} (context/use-theme-color)
        [pressed? set-pressed!]               (react/use-state false)
        [leading-width set-leading-width!]    (react/use-state nil)
        pressable?                            (or on-press on-long-press)
        component                             (if pressable? :animated/pressable :animated/view)
        pressed?                              (and pressable? pressed? (not disabled?))
        press-in!                             (react/use-callback (fn [event]
                                                                    (set-pressed! true)
                                                                    (when on-press-in (on-press-in event)))
                                                                  [on-press-in])
        press-out!                            (react/use-callback (fn [event]
                                                                    (set-pressed! false)
                                                                    (when on-press-out (on-press-out event)))
                                                                  [on-press-out])
        measure-leading!                      (react/use-callback #(set-leading-width! (j/get-in % [:nativeEvent :layout :width])) [])
        density                               (if compact? :compact :regular)
        title-font                            (get-in styles/title-font [type density])
        gap                                   (styles/leading-gap type compact? (= (:type content) :image))
        content?                              (or content (seq left))
        paired?                               (and (= (count right) 2)
                                                   (#{:counter :icon} (:type (first right)))
                                                   (= (:type (second right)) :icon))
        balanced?                             (= (get-in (first right) [:values :layout]) :balanced)
        leading-size                          (case (:type content)
                                                :avatar (get-in content [:avatar :size] 32)
                                                :image 40
                                                :icon (get-in content [:icon :size] 20)
                                                0)
        caption-indent                        (cond
                                                (not content?) 0
                                                leading-width  (+ leading-width gap)
                                                :else          (+ leading-size gap))
        primary-color                         (colors/themed theme :color/neutral-100 :color/white-100)]
    [component
     (cond-> props
       :always (dissoc :type :compact? :title :description :title-props :description-props :content
                       :left :right :caption :active? :selected? :disabled? :blur? :on-press-in :on-press-out)
       :always (assoc :pointer-events (if disabled? :none pointer-events)
                      :style          (rn.utils/add-styles (when pressable?
                                                             (if pressed?
                                                               button-style/pressable-pressed-state-style
                                                               button-style/pressable-default-state-style))
                                                           (styles/container type compact? disabled? (seq right))
                                                           (styles/appearance theme-color color type compact? blur? pressed? active? selected?)
                                                           (styles/shadow theme-color type blur? pressed?)
                                                           item-style))
       pressable? (assoc :disabled     disabled?
                         :on-press-in  press-in!
                         :on-press-out press-out!))
     (when (= type :outline)
       [:animated/view {:pointer-events :none :style (styles/border theme type compact? blur? pressed?)}])
     [:rn/view {:style (styles/row type compact? (seq left) paired? balanced?)}
      [:rn/view {:style (rn.utils/add-styles styles/info (style {:gap gap}))}
       (when content?
         [:rn/view {:on-layout (when caption measure-leading!) :style styles/leading-group}
          (when (seq left)
            [cluster {:theme theme :blur? blur? :disabled? disabled? :entries left}])
          (when content
            [leading-view {:theme theme :blur? blur? :disabled? disabled? :content content}])])
       [:rn/view {:style styles/text-column}
        [text-view {:value title :font title-font :color primary-color :props title-props}]
        (when description
          [text-view {:value description
                      :font  :font/regular-13
                      :color (styles/secondary theme blur?)
                      :props description-props}])]]
      (when (seq right)
        [cluster {:theme theme :blur? blur? :disabled? disabled? :entries right :balanced? balanced?}])
      (when (and selected? (not (seq right)))
        [icon/view {:name :icon/check :size 20 :color (colors/themed theme color)}])]
     (when caption
       (let [{:keys [icon text text-props]} caption]
         [:rn/view {:style (styles/caption caption-indent)}
          (when icon
            [icon/view (assoc icon
                              :size  (:size icon 12)
                              :color (:color icon (styles/secondary theme blur?)))])
          [text-view {:value text
                      :font  :font/regular-11
                      :color (styles/secondary theme blur?)
                      :props (assoc text-props
                                    :style (rn.utils/add-styles (style {:flex 1}) (:style text-props)))}]]))]))
