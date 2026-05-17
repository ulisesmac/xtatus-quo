(ns xquo.foundations.typography
  (:require [react-native.utils :as rn.utils]))

(def font-families
  {:regular   "Inter-Regular"
   :medium    "Inter-Medium"
   :semibold  "Inter-SemiBold"
   :monospace "InterStatus-Regular"
   :code      "UbuntuMono-Regular"})

(def label-11
  {:font-size      11
   :line-height    15.62
   :letter-spacing -0.055})

(def label-11-uppercase
  {:font-size      11
   :line-height    15.62
   :letter-spacing 0.055})

(def paragraph-2-13
  {:font-size      13
   :line-height    18.2
   :letter-spacing -0.039})

(def paragraph-1-15
  {:font-size      15
   :line-height    21.75
   :letter-spacing -0.135})

(def heading-2-19
  {:font-size      19
   :line-height    25.65
   :letter-spacing -0.304})

(def heading-1-27
  {:font-size      27
   :line-height    32
   :letter-spacing -0.567})

(def body-19
  {:font-size      19
   :line-height    28
   :letter-spacing -0.304})

(def code-13
  {:font-size      13
   :line-height    18.2
   :letter-spacing -0.039})

(def code-15
  {:font-size      15
   :line-height    22
   :letter-spacing -0.135})

(def monospace-11
  {:font-size      11
   :line-height    15.62
   :letter-spacing 0.055
   :font-variant   ["tabular-nums"]})

(def monospace-13
  (assoc paragraph-2-13 :font-variant ["tabular-nums"]))

(def monospace-15
  (assoc paragraph-1-15 :font-variant ["tabular-nums"]))

(def font-styles
  {11 {:regular            (assoc label-11 :font-family (:regular font-families))
       :medium             (assoc label-11 :font-family (:medium font-families))
       :semibold           (assoc label-11 :font-family (:semibold font-families))
       :regular-uppercase  (assoc label-11-uppercase :font-family (:regular font-families))
       :medium-uppercase   (assoc label-11-uppercase :font-family (:medium font-families))
       :semibold-uppercase (assoc label-11-uppercase :font-family (:semibold font-families))
       :monospace          (assoc monospace-11 :font-family (:monospace font-families))}
   13 {:regular   (assoc paragraph-2-13 :font-family (:regular font-families))
       :medium    (assoc paragraph-2-13 :font-family (:medium font-families))
       :semibold  (assoc paragraph-2-13 :font-family (:semibold font-families))
       :monospace (assoc monospace-13 :font-family (:monospace font-families))
       :code      (assoc code-13 :font-family (:code font-families))}
   15 {:regular   (assoc paragraph-1-15 :font-family (:regular font-families))
       :medium    (assoc paragraph-1-15 :font-family (:medium font-families))
       :semibold  (assoc paragraph-1-15 :font-family (:semibold font-families))
       :monospace (assoc monospace-15 :font-family (:monospace font-families))
       :code      (assoc code-15 :font-family (:code font-families))}
   19 {:regular       (assoc heading-2-19 :font-family (:regular font-families))
       :medium        (assoc heading-2-19 :font-family (:medium font-families))
       :semibold      (assoc heading-2-19 :font-family (:semibold font-families))
       :body-regular  (assoc body-19 :font-family (:regular font-families))
       :body-medium   (assoc body-19 :font-family (:medium font-families))
       :body-semibold (assoc body-19 :font-family (:semibold font-families))}
   27 {:regular  (assoc heading-1-27 :font-family (:regular font-families))
       :medium   (assoc heading-1-27 :font-family (:medium font-families))
       :semibold (assoc heading-1-27 :font-family (:semibold font-families))}})

(defn- parse-font-keyword [font]
  (when (and (keyword? font) (= "font" (namespace font)))
    (let [[_ variant size] (re-matches #"([a-z-]+)-([0-9]+)" (name font))]
      (when (and variant size)
        [(js/parseInt size 10) (keyword variant)]))))

(def get-style
  (memoize
   (fn [font]
     (let [[size variant] (parse-font-keyword font)]
       (some-> (get-in font-styles [size variant])
               (rn.utils/->js-prop-obj))))))
