(ns quo.components.inputs.recovery-phrase.style
  (:require
    [quo.components.markdown.text :as text]
    [quo.foundations.colors :as colors]))

(defn container
  [container-style]
  (merge {:min-height         40
          :flex               1
          :padding-vertical   4
          :padding-horizontal 20}
         container-style))

(defn input
  [theme]
  (assoc (text/text-style {} theme)
         :height              32
         :flex-grow           1
         :padding-vertical    5
         :text-align-vertical :top))

(defn placeholder-color
  [input-state theme blur?]
  (cond
    (and (= input-state :focused) blur?)
    (colors/theme-colors colors/neutral-80-opa-20 colors/white-opa-20 theme)

    (= input-state :focused) ; Not blur
    (colors/theme-colors colors/neutral-30 colors/neutral-60 theme)

    blur? ; :default & blur
    (colors/theme-colors colors/neutral-80-opa-40 colors/white-opa-30 theme)

    :else ; :default & not blur
    (colors/theme-colors colors/neutral-40 colors/neutral-50 theme)))

(defn cursor-color
  [customization-color theme]
  (colors/resolve-color customization-color theme))

(defn error-word
  [theme]
  {:height             22
   :padding-horizontal 20
   :background-color   colors/danger-50-opa-10
   :color              (colors/theme-colors colors/danger-50 colors/danger-60 theme)})
