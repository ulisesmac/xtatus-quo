(ns xtatus-quo.components.inputs.title-input.view
  (:require
   [xtatus-quo.components.icon :as icon]
   [xtatus-quo.components.inputs.title-input.style :as style]
   [xtatus-quo.components.markdown.text :as text]
   [quo.context :as quo.context]
   [react-native.core :as rn]))

(defn- pad-0
  [value]
  (if (<= (count value) 1)
    (str 0 value)
    value))

(defn view
  [{:keys [blur? on-change-text auto-focus placeholder max-length default-value return-key-type
           size on-focus on-blur container-style customization-color disabled?]
    :or   {max-length    0
           auto-focus    false
           default-value ""}}]
  (let [theme                  (quo.context/use-theme)
        [focused? set-focused] (rn/use-state auto-focus)
        [value set-value]      (rn/use-state default-value)
        input-ref              (rn/use-ref-atom nil)
        on-inpur-ref           (rn/use-callback #(reset! input-ref %))
        on-press               (rn/use-callback
                                #(when-not disabled? (.focus ^js @input-ref))
                                [disabled?])
        on-change              (rn/use-callback
                                (fn [v]
                                  (set-value v)
                                  (when on-change-text (on-change-text v))))
        on-focus               (rn/use-callback
                                (fn []
                                  (when (fn? on-focus) (on-focus))
                                  (set-focused true)))
        on-blur                (rn/use-callback
                                (fn []
                                  (when (fn? on-blur) (on-blur))
                                  (set-focused false)))]
    [rn/view {:style [(style/container disabled?) container-style]}
     [rn/view {:style style/text-input-container}
      [rn/text-input
       {:style                  (text/text2-style
                                 {:font (case (or size :heading-1)
                                          :label       :font/semibold-11
                                          :paragraph-2 :font/semibold-13
                                          :paragraph-1 :font/semibold-15
                                          :heading-2   :font/semibold-19
                                          :heading-1   :font/semibold-27
                                          :font/semibold-27)}
                                 theme)
        :default-value          default-value
        :accessibility-label    :profile-title-input
        :keyboard-appearance    theme
        :return-key-type        return-key-type
        :on-focus               on-focus
        :on-blur                on-blur
        :auto-focus             auto-focus
        :input-mode             :text
        :on-change-text         on-change
        :editable               (not disabled?)
        :max-length             max-length
        :placeholder            placeholder
        :ref                    on-inpur-ref
        :selection-color        (style/get-selection-color customization-color blur? theme)
        :placeholder-text-color (if focused?
                                  (style/get-focused-placeholder-color blur? theme)
                                  (style/get-placeholder-color blur? theme))}]]
     [rn/view {:style (style/counter-container focused?)}
      (if focused?
        [text/text2
         [text/text2
          {:style (style/char-count blur? theme)
           :font  :font/regular-13}
          (str (count value))]
         [text/text2
          {:style (style/char-count blur? theme)
           :font  :font/regular-13}
          (str "/" (pad-0 (str max-length)))]]
        [rn/pressable {:on-press on-press}
         [icon/icon :i/edit {:color (style/get-char-count-color blur? theme)}]])]]))
