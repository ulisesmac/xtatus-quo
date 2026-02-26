(ns xquo.foundations.shadows
  (:require [reagent-extended-compiler.utils.transforms :as transforms]))

;; Source: Figma Foundations/Shadows node 624:750.
;; NOTE: In Figma source, "Shadow/Inverted/Light/04" has positive y (12), not negative.

(def shadow-tokens
  {:normal
   {:theme/light
    {1 {:x 0 :y 2 :blur 20 :spread 0 :color "#09101C" :opacity 0.04}
     2 {:x 0 :y 4 :blur 20 :spread 0 :color "#09101C" :opacity 0.08}
     3 {:x 0 :y 8 :blur 30 :spread 0 :color "#09101C" :opacity 0.12}
     4 {:x 0 :y 12 :blur 56 :spread 0 :color "#09101C" :opacity 0.16}}
    :theme/dark
    {1 {:x 0 :y 4 :blur 40 :spread 0 :color "#09101C" :opacity 0.5}
     2 {:x 0 :y 8 :blur 40 :spread 0 :color "#09101C" :opacity 0.64}
     3 {:x 0 :y 12 :blur 50 :spread 0 :color "#09101C" :opacity 0.64}
     4 {:x 0 :y 16 :blur 64 :spread 0 :color "#09101C" :opacity 0.72}}}
   :inverted
   {:theme/light
    {1 {:x 0 :y -2 :blur 20 :spread 0 :color "#09101C" :opacity 0.04}
     2 {:x 0 :y -4 :blur 20 :spread 0 :color "#09101C" :opacity 0.08}
     3 {:x 0 :y -8 :blur 30 :spread 0 :color "#09101C" :opacity 0.12}
     4 {:x 0 :y 12 :blur 56 :spread 0 :color "#09101C" :opacity 0.16}}
    :theme/dark
    {1 {:x 0 :y -4 :blur 40 :spread 0 :color "#09101C" :opacity 0.5}
     2 {:x 0 :y -8 :blur 40 :spread 0 :color "#09101C" :opacity 0.64}
     3 {:x 0 :y -12 :blur 50 :spread 0 :color "#09101C" :opacity 0.64}
     4 {:x 0 :y -16 :blur 64 :spread 0 :color "#09101C" :opacity 0.72}}}
   :inner
   {:theme/light
    {1 {:x 0 :y 0 :blur 4 :spread 0 :color "#09101C" :opacity 0.08}}
    ;; Figma provides only light inner-shadow token.
    ;; Reuse it in dark until design provides a dark-specific token.
    :theme/dark
    {1 {:x 0 :y 0 :blur 4 :spread 0 :color "#09101C" :opacity 0.08}}}})

(defn get-shadow
  ([theme level]
   (get-shadow theme :normal level))
  ([theme scale level]
   (get-in shadow-tokens [scale theme level])))

(defn- hex->rgba
  [hex opacity]
  (let [r (js/parseInt (subs hex 1 3) 16)
        g (js/parseInt (subs hex 3 5) 16)
        b (js/parseInt (subs hex 5 7) 16)]
    (str "rgba(" r ", " g ", " b ", " opacity ")")))

(defn to-box-shadow-value
  ([shadow] (to-box-shadow-value shadow false))
  ([{:keys [x y blur spread color opacity]} inset?]
   ;; React Native BoxShadowValue uses: positive offsetY => up, negative => down.
   ;; Figma uses the opposite sign for vertical offset.
   (transforms/->js-prop-obj
    {:offset-x        x
     :offset-y        (- y)
     :blur-radius     blur
     :spread-distance spread
     :color           (hex->rgba color opacity)
     :inset           inset?})))

(def get-box-shadow
  (memoize
   (fn get-box-shadow
     ([theme level] (get-box-shadow theme :normal level))
     ([theme scale level]
      (let [shadow (get-shadow theme scale level)
            inset? (= scale :inner)]
        (some-> shadow
          (to-box-shadow-value inset?)
          (array)))))))
