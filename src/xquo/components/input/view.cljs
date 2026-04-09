(ns xquo.components.input.view
  (:require [applied-science.js-interop :as j]
            [reagent-extended-compiler.utils.transforms :as rec.xf]
            [xquo.components.button.style :as button.style]
            [xquo.components.button.view :as button]
            [xquo.components.icon.view :as icon]
            [xquo.components.input.style :as style :refer [container-slot-gap-styles]]
            [xquo.components.text.view :as text]
            [xquo.context :as context]
            [xquo.foundations.colors :as colors]
            [xquo.react-native-reanimated :as rnr]
            [xquo.react-native :as rn]))

(def clear-button-delay 120)
(def text-input-content-height 22)
(def trailing-button-hit-slops
  {32 {:top    4
       :right  4
       :bottom 4}
   40 {:top    8
       :right  8
       :bottom 8}})

(defn- layout-type [icon clearable? trailing-button]
  (cond
    (and trailing-button icon clearable?) :button-icon-clear
    (and trailing-button icon)            :button-icon
    (and trailing-button clearable?)      :button-clear
    trailing-button                       :button
    (and icon clearable?)              :icon-clear
    icon                               :icon
    clearable?                         :clear))

(defn- labels-view [{:keys [blur? current-value label max-length]}]
  (let [{:keys [dark-theme?]} (context/use-theme-color)
        labels-color-style    (style/labels-color dark-theme? blur?)]
    [:rn/view {:style style/top-row-base}
     (if label
       [text/text {:style           [style/label-slot labels-color-style]
                   :font            :font/medium-13
                   :number-of-lines 1}
        label]
       [:rn/view {:style style/label-slot}])
     (when max-length
       [text/text {:style           [style/counter-slot labels-color-style]
                   :font            :font/regular-13
                   :number-of-lines 1}
        (str (count current-value) "/" max-length)])]))

(defn- leading-icon-view [{:keys [blur? icon]}]
  (let [{:keys [dark-theme?]} (context/use-theme-color)]
    [icon/icon {:icon  icon
                :size  20
                :color (:color (style/leading-icon-color dark-theme? blur?))
                :style style/icon-slot-base}]))

(defn- clear-button-view [_]
  (let [clear-timeout (atom nil)]
    (fn [{:keys [blur? clear-input! disabled?]}]
      (let [{:keys [dark-theme?]} (context/use-theme-color)
            [pressed? set-pressed!] (rn/use-state false)
            on-press-in!  (rn/use-callback #(set-pressed! true) [])
            on-press-out! (rn/use-callback #(set-pressed! false) [])
            on-press!     (rn/use-callback (fn []
                                             (set-pressed! false)
                                             (some-> @clear-timeout js/clearTimeout)
                                             (reset! clear-timeout
                                                     (js/setTimeout
                                                      (fn []
                                                        (reset! clear-timeout nil)
                                                        (clear-input!))
                                                      clear-button-delay)))
                                           [clear-input!])]
        (rn/use-effect
         (fn []
           #(some-> @clear-timeout js/clearTimeout))
         [])
        [:animated/view {:entering (rnr/appear-in)
                         :exiting  (rnr/disappear-out)
                         :layout   rnr/linear-transition
                         :style    (if pressed?
                                     button.style/pressable-pressed-state-style
                                     button.style/pressable-default-state-style)}
         [:rn/pressable {:disabled     disabled?
                         :hit-slop     6
                         :on-press     on-press!
                         :on-press-in  on-press-in!
                         :on-press-out on-press-out!
                         :style        style/clear-icon-slot}
          [icon/icon {:icon    :icon/clear
                      :size    20
                      :color   (:color (style/clear-icon-color dark-theme? blur?))
                      :color-2 (colors/get-color :color/white-100)}]]]))))

(defn- surface-vertical-padding [size trailing-button]
  (cond
    (and trailing-button (= size 32)) 8
    trailing-button                   16
    (= size 32)                    10
    :else                          18))

(defn- normalize-multiline-height [content-height]
  (* text-input-content-height
     (max 1 (js/Math.round (/ content-height text-input-content-height)))))

(defn- next-content-height [event]
  (-> event
      (j/get-in [:nativeEvent :contentSize :height] text-input-content-height)
      (js/Math.ceil)
      (normalize-multiline-height)))

(defn- updated-content-height [current-height next-height]
  (if (= current-height next-height)
    current-height
    next-height))

(defn- bounded-content-height [height vertical-padding]
  (when height
    (max text-input-content-height
         (- height vertical-padding))))

(defn- min-content-height [min-height vertical-padding]
  (or (bounded-content-height min-height vertical-padding)
      text-input-content-height))

(defn- content-overflows? [content-height max-content-height]
  (and max-content-height
       content-height
       (> content-height max-content-height)))

(defn- input-height [content-height min-content-height max-content-height]
  (cond
    (not content-height) nil
    max-content-height   (min (max min-content-height content-height)
                              max-content-height)
    :else                (max min-content-height content-height)))

(defn- text-input-view
  [{:keys [blur? controlled? disabled? focused? input-ref max-height
           max-length min-height multiline? on-blur on-change-text on-content-size-change
           on-focus set-focused! set-internal-value! size value]
    trailing-button :button
    :as   props}]
  (let [{:keys [color dark-theme?]} (context/use-theme-color)
        [content-height
         set-content-height!] (rn/use-state nil)
        selection-color         (style/selection-color color)
        vertical-padding        (surface-vertical-padding size trailing-button)
        min-content-height      (min-content-height min-height vertical-padding)
        max-content-height      (bounded-content-height max-height vertical-padding)
        content-overflow?       (content-overflows? content-height max-content-height)
        input-height            (input-height content-height min-content-height max-content-height)
        placeholder-text-color  (style/placeholder-color dark-theme? blur? focused?)
        text-input-layout-style (cond
                                  (and multiline? rn/platform-android?) style/text-input-multiline-android
                                  multiline? style/text-input-multiline-ios
                                  rn/platform-android? style/text-input-single-line-android
                                  (not rn/platform-android?) style/text-input-single-line-ios)
        on-change-text!         (rn/use-callback (fn [next-value]
                                                   (when-not controlled?
                                                     (set-internal-value! next-value))
                                                   (when on-change-text
                                                     (on-change-text next-value)))
                                                 [controlled? on-change-text])
        on-content-size-change! (rn/use-callback
                                 (fn [event]
                                   (when multiline?
                                     (set-content-height! #(updated-content-height % (next-content-height event))))
                                   (when on-content-size-change
                                     (on-content-size-change event)))
                                 [multiline? on-content-size-change])
        on-focus!               (rn/use-callback (fn [event]
                                                   (set-focused! true)
                                                   (when on-focus
                                                     (on-focus event)))
                                                 [on-focus])
        on-blur!                (rn/use-callback (fn [event]
                                                   (set-focused! false)
                                                   (when on-blur
                                                     (on-blur event)))
                                                 [on-blur])]
    [:rn/text-input (cond-> props
                      :always (dissoc :blur? :button :default-value :disabled?
                                      :error? :icon :clearable? :input-container-style
                                      :label :max-height :max-length :min-height :multiline
                                      :multiline? :on-blur :on-change-text :on-clear
                                      :on-content-size-change :on-focus :size :style :value)
                      :always (assoc :ref input-ref
                                     :style [style/text-input-base
                                             (if dark-theme?
                                               text/dark-text-style
                                               text/light-text-style)
                                             text-input-layout-style
                                             (when multiline?
                                               {:min-height min-content-height})
                                             (when input-height
                                               {:height input-height})]
                                     :cursor-color selection-color
                                     :on-blur on-blur!
                                     :on-change-text on-change-text!
                                     :on-content-size-change on-content-size-change!
                                     :on-focus on-focus!
                                     :placeholder-text-color placeholder-text-color
                                     :selection-color selection-color
                                     :value value)
                      disabled? (assoc :editable false)
                      max-length (assoc :max-length max-length)
                      multiline? (assoc :multiline      true
                                        :scroll-enabled content-overflow?))]))

(defn input
  "Input component.

  API:
  - `props` map
    - `:size` one of `40` or `32` (default `40`)
    - `:blur?` optional boolean that uses the blur treatment
    - `:label` optional label text rendered above the field
    - `:max-length` optional character limit. When present, the counter is computed
                    internally from the current value
    - `:placeholder` optional placeholder text
    - `:value` optional controlled value
    - `:default-value` optional uncontrolled initial value
    - `:multiline?` optional boolean. When true, the field switches to the multiline
                    layout and top-aligned text behavior
    - `:min-height` optional minimum surface height for multiline inputs. When omitted,
                    multiline starts at the single-line height and grows from content
    - `:max-height` optional maximum surface height for multiline inputs
    - `:icon` optional leading icon
    - `:clearable?` optional boolean that renders the clear button when the input has content
    - `:on-clear` optional callback fired when the clear button is pressed
    - `:button` optional trailing button map. `:label` is rendered as the button
                      content; `:size 24`, `:type :outline`, and inherited
      blur styling are enforced internally
    - `:error?` optional boolean
    - `:disabled?` optional boolean
    - `:style` optional caller style for the outer component wrapper
    - Any additional keys are forwarded to `:rn/text-input`."
  [{:keys               [blur? clearable? default-value disabled? error? icon
                         label max-height max-length min-height multiline? on-blur
                         on-change-text on-clear on-content-size-change on-focus
                         size value]
    trailing-button     :button
    component-style     :style
    :or                 {size 40}
    :as                 props}]
  (let [{:keys [dark-theme?]}       (context/use-theme-color)
        controlled?                 (contains? props :value)
        input-ref                   (rn/use-ref nil)
        [focused? set-focused!]     (rn/use-state false)
        [internal-value
         set-internal-value!] (rn/use-state (or default-value ""))
        current-value           (if controlled? value internal-value)
        show-clear-button?      (and clearable? (seq current-value))
        layout                  (layout-type icon show-clear-button? trailing-button)
        slot-gap-style          (get container-slot-gap-styles size)
        focus-input!            (rn/use-callback (fn []
                                                   (when-not disabled?
                                                     (when-let [input-instance (j/get input-ref :current)]
                                                       (j/call input-instance :focus))))
                                                 [disabled?])
        clear-input!            (rn/use-callback (fn []
                                                   (when-not controlled?
                                                     (set-internal-value! ""))
                                                   (when on-clear
                                                     (on-clear)))
                                                 [controlled? on-clear])]
    [:rn/view {:style [style/root-base
                       (when (or label max-length) style/root-gap-8)
                       (when disabled? style/root-disabled)]}
     (when (or label max-length)
       [labels-view {:blur?         blur?
                     :current-value current-value
                     :label         label
                     :max-length    max-length}])
     [:rn/pressable {:disabled disabled?
                     :on-press focus-input!
                     :style    (rec.xf/add-styles
                                style/container-base
                                (if multiline?
                                  style/container-multiline
                                  style/container-single-line)
                                slot-gap-style
                                (style/container-layout-style size layout)
                                (style/container-color-style dark-theme? blur? error? focused?)
                                (when (and multiline? min-height)
                                  {:min-height min-height})
                                (when (and multiline? max-height)
                                  {:max-height max-height})
                                component-style)}
      [:rn/view {:style [style/content-base
                         (if multiline? style/content-multiline style/content-single-line)
                         slot-gap-style]}
       (when icon
         [leading-icon-view {:blur? blur?
                             :icon  icon}])
       [text-input-view (assoc props
                          :blur? blur?
                          :controlled? controlled?
                          :focused? focused?
                          :input-ref input-ref
                          :set-focused! set-focused!
                          :set-internal-value! set-internal-value!
                          :value current-value)]]
      (when show-clear-button?
        [clear-button-view {:blur?        blur?
                            :clear-input! clear-input!
                            :disabled?    disabled?}])
      (when-let [{:keys [label type]
                  :or   {label "Button" type :outline}} trailing-button]
        [button/button (assoc trailing-button
                         :background (when blur? :blur)
                         :disabled?  (or disabled? (:disabled? trailing-button))
                         :hit-slop   (get trailing-button-hit-slops size)
                         :size       24
                         :type       type)
         label])]]))
