(ns xquo.foundations.colors
  (:require [clojure.string :as string]))

;; Generated from Figma Foundations page node 619:5995.

(def colors
  {;; Neutrals
   :neutral   {2.5 {:base "#FCFCFC"}
               5   {:base "#F5F6F8"
                    :opa  {70 "#F5F6F8B3"}}
               10  {:base "#F0F2F5"}
               20  {:base "#E7EAEE"}
               30  {:base "#DCE0E5"}
               40  {:base "#A1ABBD"}
               50  {:base "#647084"}
               60  {:base "#303D55"}
               70  {:base "#202C42"}
               80  {:base "#1B273D"
                    :opa  {1  "#1B273D03"
                           5  "#1B273D0D"
                           10 "#1B273D1A"
                           20 "#1B273D33"
                           30 "#1B273D4D"
                           40 "#1B273D66"
                           50 "#1B273D80"
                           60 "#1B273D99"
                           70 "#1B273DB3"
                           80 "#1B273DCC"
                           90 "#1B273DE6"
                           95 "#1B273DF2"}}
               90  {:base "#131D2F"
                    :opa  {70 "#131D2FB3"}}
               95  {:base "#0D1625"
                    :opa  {70 "#0D1625B3"}}
               100 {:base "#09101C"
                    :opa  {70 "#09101CB3"}}}
   :white     {5   {:base "#FFFFFF0D"}
               10  {:base "#FFFFFF1A"}
               20  {:base "#FFFFFF33"}
               30  {:base "#FFFFFF4D"}
               40  {:base "#FFFFFF66"}
               50  {:base "#FFFFFF80"}
               60  {:base "#FFFFFF99"}
               70  {:base "#FFFFFFB3"}
               80  {:base "#FFFFFFCC"}
               90  {:base "#FFFFFFE6"}
               100 {:base "#FFFFFF"}}
   :black     {50  {:base "#000000"}
               100 {:base "#000000"}}
   ;; Customization
   :primary   {50 {:base "#2A4AF5"}
               60 {:base "#223BC4"}}
   :privacy   {50 {:base "#7140FD"
                   :opa  {5  "#7140FD0D"
                          10 "#7140FD1A"
                          20 "#7140FD33"
                          30 "#7140FD4D"
                          40 "#7140FD66"}}
               60 {:base "#5A33CA"}}
   :blue      {50 {:base "#2A4AF5"
                   :opa  {5  "#2A4AF50D"
                          10 "#2A4AF51A"
                          20 "#2A4AF533"
                          30 "#2A4AF54D"
                          40 "#2A4AF566"}}
               60 {:base "#223BC4"}}
   :army      {50 {:base "#216266"
                   :opa  {5  "#2162660D"
                          10 "#2162661A"
                          20 "#21626633"
                          30 "#2162664D"
                          40 "#21626666"}}
               60 {:base "#1A4E52"}}
   :sky       {50 {:base "#1992D7"
                   :opa  {5  "#1992D70D"
                          10 "#1992D71A"
                          20 "#1992D733"
                          30 "#1992D74D"
                          40 "#1992D766"}}
               60 {:base "#1475AC"}}
   :turquoise {50 {:base "#2A799B"
                   :opa  {5  "#2A799B0D"
                          10 "#2A799B1A"
                          20 "#2A799B33"
                          30 "#2A799B4D"
                          40 "#2A799B66"}}
               60 {:base "#22617C"}}
   :magenta   {50 {:base "#EC266C"
                   :opa  {5  "#EC266C0D"
                          10 "#EC266C1A"
                          20 "#EC266C33"
                          30 "#EC266C4D"
                          40 "#EC266C66"}}
               60 {:base "#BD1E56"}}
   :pink      {50 {:base "#F66F8F"
                   :opa  {5  "#F66F8F0D"
                          10 "#F66F8F1A"
                          20 "#F66F8F33"
                          30 "#F66F8F4D"
                          40 "#F66F8F66"}}
               60 {:base "#C55972"}}
   :orange    {10 {:opa {5  "#FF7D460D"
                         10 "#FF7D461A"
                         20 "#FF7D4633"
                         30 "#FF7D464D"
                         40 "#FF7D4666"}}
               50 {:base "#FF7D46"}
               60 {:base "#CC6438"}}
   :yellow    {50 {:base "#F6B03C"
                   :opa  {5  "#F6B03C0D"
                          10 "#F6B03C1A"
                          20 "#F6B03C33"
                          30 "#F6B03C4D"
                          40 "#F6B03C66"}}
               60 {:base "#C58D30"}}
   :camel     {50 {:base "#C78F67"}
               60 {:base "#9F7252"}}
   :copper    {50 {:base "#CB6256"}
               60 {:base "#A24E45"}}
   ;; Semantic status
   :success   {50 {:base "#23ADA0"
                   :opa  {5  "#23ADA00D"
                          10 "#23ADA01A"
                          20 "#23ADA033"
                          30 "#23ADA04D"
                          40 "#23ADA066"}}
               60 {:base "#1C8A80"}}
   :warning   {10 {:opa {5  "#FF7D460D"
                         10 "#FF7D461A"
                         20 "#FF7D4633"
                         30 "#FF7D464D"
                         40 "#FF7D4666"}}
               50 {:base "#FF7D46"}
               60 {:base "#CC6438"}}
   :danger    {10 {:opa {5  "#E954600D"
                         10 "#E954601A"
                         20 "#E9546033"
                         30 "#E954604D"
                         40 "#E9546066"}}
               50 {:base "#E95460"}
               60 {:base "#BA434D"}}})

(defn- parse-number [s]
  (if (string/includes? s ".")
    (js/parseFloat s)
    (js/parseInt s 10)))

(defn- as-hex-byte [value]
  (-> (.toString (js/Math.round value) 16)
      (.padStart 2 "0")
      string/upper-case))

(defn- hex->rgb [hex-color]
  (let [hex-value (subs hex-color 1)]
    [(js/parseInt (subs hex-value 0 2) 16)
     (js/parseInt (subs hex-value 2 4) 16)
     (js/parseInt (subs hex-value 4 6) 16)]))

(defn- rgb->hex [[r g b]]
  (str "#" (as-hex-byte r) (as-hex-byte g) (as-hex-byte b)))

(defn- mix-channel [base target weight]
  (js/Math.round (+ (* base (- 1 weight))
                    (* target weight))))

(def compute-color
  (memoize
   (fn [base-color-50 intensity opacity]
     (let [base-rgb    (hex->rgb base-color-50)
           offset      (/ (- intensity 50) 50)
           weight      (js/Math.abs offset)
           target-rgb  (cond
                         (neg? offset) [255 255 255]
                         (pos? offset) [0 0 0]
                         :else         base-rgb)
           color-rgb   (if (= intensity 50)
                         base-rgb
                         (map mix-channel base-rgb target-rgb (repeat weight)))
           color-hex   (rgb->hex color-rgb)
           opacity-hex (when (some? opacity)
                         (as-hex-byte (* 255 (/ opacity 100))))]
       (if opacity-hex
         (str color-hex opacity-hex)
         color-hex)))))

(def get-color*
  (memoize
   (fn [color-kw]
     (let [[_ color-name level opa] (->> color-kw
                                         (name)
                                         (re-matches #"([a-z-]+)-([0-9]+(?:\.[0-9]+)?)(?:-([0-9]+))?"))
           color-key (keyword color-name)
           level-key (parse-number level)]
       (when (and color-key level-key)
         (if opa
           (get-in colors [color-key level-key :opa (js/parseInt opa 10)])
           (get-in colors [color-key level-key :base])))))))

(def get-color
  (memoize
   (fn
     ([color-kw]
      (let [[_ color-name level opa] (->> color-kw
                                          (name)
                                          (re-matches #"([a-z-]+?)(?:-([0-9]+(?:\.[0-9]+)?)(?:-([0-9]+))?)?$"))]
        (get-color (keyword (namespace color-kw) color-name)
                   (if level (parse-number level) 50)
                   (when opa (js/parseInt opa 10)))))
     ([color-kw level]
      (get-color color-kw level nil))
     ([color-kw level opacity]
      (let [color-key  (keyword (name color-kw))
            neutral?   (or (= color-key :neutral) (= color-key :white))
            base-color (if neutral?
                         (get-in colors [color-key level :base])
                         (get-in colors [color-key 50 :base]))]
        (if opacity
          (compute-color base-color 50 opacity)
          (if neutral?
            base-color
            (compute-color base-color level nil))))))))
