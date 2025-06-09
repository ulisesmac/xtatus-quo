(ns quo.components.numbered-keyboard.keyboard-key.style
  (:require
    [quo.foundations.colors :as colors]))

(defn get-label-color
  [disabled? theme blur?]
  (cond
    (and disabled? (or (= :theme/dark theme) blur?))  colors/white-opa-30
    (and disabled? (or (= :theme/light theme) blur?)) colors/neutral-30
    (or (= :theme/dark theme) blur?)                  colors/white
    :else                                       colors/neutral-100))

(defn toggle-background-color
  [pressed-in? blur? theme]
  (if pressed-in?
    (cond
      blur?            colors/white-opa-10
      (= :theme/light theme) colors/neutral-10
      (= :theme/dark theme)  colors/neutral-80)
    :transparent))

(defn container
  [color]
  {:width            48
   :height           48
   :justify-content  :center
   :align-items      :center
   :border-radius    999
   :background-color color})
