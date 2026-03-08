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

(defn- layout-type [icon clearable? button-props]
  (cond
    (and button-props icon clearable?) :button-icon-clear
    (and button-props icon)            :button-icon
    (and button-props clearable?)      :button-clear
    button-props                       :button
    (and icon clearable?)              :icon-clear
    icon                               :icon
    clearable?                         :clear))

(defn- labels-view [{:keys [background current-value label max-length]}]
  (let [{:keys [dark-theme?]} (context/use-theme-color)
        labels-color-style    (style/labels-color dark-theme? background)]
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

(defn- leading-icon-view [{:keys [background icon]}]
  (let [{:keys [dark-theme?]} (context/use-theme-color)]
    [icon/icon {:icon  icon
                :size  20
                :color (:color (style/leading-icon-color dark-theme? background))
                :style style/icon-slot-base}]))

(defn- clear-button-view [_]
  (let [clear-timeout (atom nil)]
    (fn [{:keys [background clear-input! disabled?]}]
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
                                                      120)))
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
                         :on-press     on-press!
                         :on-press-in  on-press-in!
                         :on-press-out on-press-out!
                         :style        style/clear-icon-slot}
          [icon/icon {:icon    :icon/clear
                      :size    20
                      :color   (:color (style/clear-icon-color dark-theme? background))
                      :color-2 (colors/get-color :color/white-100)}]]]))))

(defn- surface-vertical-padding [size button-props]
  (cond
    (and button-props (= size 32)) 8
    button-props                   16
    (= size 32)                    10
    :else                          18))

(defn- normalize-multiline-height [content-height]
  (* 22 (max 1 (js/Math.round (/ content-height 22)))))

(defn input
  "Input component.

  API:
  - `props` map
    - `:size` one of `40` or `32` (default `40`)
    - `:background` one of `:none` or `:blur` (default `:none`)
    - `:label` optional label text rendered above the field
    - `:max-length` optional character limit. When present, the counter is
      computed internally from the current value
    - `:placeholder` optional placeholder text
    - `:value` optional controlled value
    - `:default-value` optional uncontrolled initial value
    - `:multiline?` optional boolean. When true, the field switches to the
      multiline layout and top-aligned text behavior
    - `:min-height` optional minimum surface height for multiline inputs. When
      omitted, multiline starts at the single-line height and grows from
      content
    - `:max-height` optional maximum surface height for multiline inputs
    - `:icon` optional leading icon
    - `:clearable?` optional boolean that renders the clear button when the
      input has content
    - `:on-clear` optional callback fired when the clear button is pressed
    - `:button-props` optional trailing button map. `:label` is rendered as the
      button content; `:size 24`, `:type :outline`, and inherited
      `:background` are enforced internally
    - `:error?` optional boolean
    - `:disabled?` optional boolean
    - `:style` optional caller style for the outer component wrapper
    - Any additional keys are forwarded to `:rn/text-input`."
  [{:keys               [background button-props clearable? default-value disabled? error? icon
                         label max-height max-length min-height multiline? on-blur
                         on-change-text on-clear on-content-size-change on-focus
                         size value]
    component-style     :style
    :or                 {background  :none
                         size        40}
    :as                 props}]
  (let [{:keys [color dark-theme?]} (context/use-theme-color)
        controlled?                 (contains? props :value)
        input-ref                   (rn/use-ref nil)
        [focused? set-focused!]     (rn/use-state false)
        [internal-value
         set-internal-value!] (rn/use-state (or default-value ""))
        [content-height
         set-content-height!] (rn/use-state nil)
        current-value           (if controlled? value internal-value)
        show-clear-button?      (and clearable? (seq current-value))
        selection-color         (style/selection-color color)
        layout                  (layout-type icon show-clear-button? button-props)
        slot-gap-style          (get container-slot-gap-styles size)
        vertical-padding        (surface-vertical-padding size button-props)
        min-input-height        (if min-height
                                  (max 22 (- min-height vertical-padding))
                                  22)
        max-input-height        (when max-height
                                  (max 22 (- max-height vertical-padding)))
        input-height            (when (and multiline? content-height)
                                  (let [next-height (max min-input-height content-height)]
                                    (if max-input-height
                                      (min next-height max-input-height)
                                      next-height)))
        content-overflow?       (and max-input-height
                                     content-height
                                     (> content-height max-input-height))
        on-change-text!         (rn/use-callback (fn [next-value]
                                                   (when-not controlled?
                                                     (set-internal-value! next-value))
                                                   (when on-change-text
                                                     (on-change-text next-value)))
                                                 [controlled? on-change-text])
        on-content-size-change! (rn/use-callback (fn [event]
                                                   (let [next-height (-> event
                                                                         (j/get-in [:nativeEvent :contentSize :height] 22)
                                                                         (js/Math.ceil)
                                                                         (normalize-multiline-height))]
                                                     (when multiline?
                                                       (set-content-height!
                                                        #(if (= % next-height)
                                                           %
                                                           next-height)))
                                                     (when on-content-size-change
                                                       (on-content-size-change event))))
                                                 [multiline? on-content-size-change])
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
                                                 [controlled? on-clear])
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
    [:rn/view {:style (rec.xf/add-styles style/root-base
                                         (when (or label max-length) style/root-gap-8)
                                         (when disabled? style/root-disabled)
                                         component-style)}
     (when (or label max-length)
       [labels-view {:background    background
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
                                (style/container-color-style dark-theme? background error? focused?)
                                (when (and multiline? min-height)
                                  {:min-height min-height})
                                (when (and multiline? max-height)
                                  {:max-height max-height})
                                )}
      [:rn/view {:style [style/content-base
                         (if multiline? style/content-multiline style/content-single-line)
                         slot-gap-style]}
       (when icon
         [leading-icon-view {:background background
                             :icon       icon}])
       [:rn/text-input (cond-> props
                         :always (dissoc :background :button-props :default-value :disabled?
                                         :error? :icon :clearable? :input-container-style
                                         :label :max-height :max-length :min-height
                                         :multiline :multiline? :on-blur :on-change-text
                                         :on-clear :on-content-size-change :on-focus
                                         :size :style :value)
                         :always (assoc :ref input-ref
                                        :style [style/text-input-base
                                                style/text-input-font
                                                (if dark-theme?
                                                  text/dark-text-style
                                                  text/light-text-style)
                                                (if multiline?
                                                  (if rn/platform-android?
                                                    style/text-input-multiline-android
                                                    style/text-input-multiline-ios)
                                                  (if rn/platform-android?
                                                    style/text-input-single-line-android
                                                    style/text-input-single-line-ios))
                                                (when multiline?
                                                  {:min-height min-input-height})
                                                (when input-height
                                                  {:height input-height})]
                                        :cursor-color selection-color
                                        :on-blur on-blur!
                                        :on-change-text on-change-text!
                                        :on-content-size-change on-content-size-change!
                                        :on-focus on-focus!
                                        :placeholder-text-color (style/placeholder-color dark-theme? background focused?)
                                        :selection-color selection-color
                                        :underline-color-android selection-color
                                        :value current-value)
                         disabled? (assoc :editable false)
                         max-length (assoc :max-length max-length)
                         multiline? (assoc :multiline      true
                                           :scroll-enabled content-overflow?))]]
      (when show-clear-button?
        [clear-button-view {:background   background
                            :clear-input! clear-input!
                            :disabled?    disabled?}])
      (when-let [{:keys [label type]
                  :or   {label "Button" type :outline}} button-props]
        [button/button (assoc button-props
                         :background background
                         :disabled? (or disabled? (:disabled? button-props))
                         :size 24
                         :type type)
         label])]]))
