(ns xquo.foundations.colors
  (:require [cljs.math :as math]
            [clojure.string :as string]))

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
   :purple    {50 {:base "#7140FD"
                   :opa  {5  "#7140FD0D"
                          10 "#7140FD1A"
                          20 "#7140FD33"
                          30 "#7140FD4D"
                          40 "#7140FD66"}}
               60 {:base "#5A33CA"}}
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
   :flamingo  {50 {:base "#F66F8F"
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

(def colors-1
  #:color{:neutral                {2.5 "#FBFCFC"
                                   5   "#F4F7F8"
                                   10  "#EEF2F4"
                                   20  "#E7ECEF"
                                   30  "#DCE4E8"
                                   40  "#A4B4BC"
                                   50  "#687D88"
                                   60  "#304A53"
                                   70  "#20383F"
                                   80  "#1A3036"
                                   90  "#13262B"
                                   95  "#0D1C21"
                                   100 "#071215"}
          :white                  {50 "#FFFFFF"}
          :black                  {50 "#000000"}
          :primary                {50 "#2C7F8E"}
          :success                {50 "#2AA56B"}
          :warning                {50 "#D89622"}
          :danger                 {50 "#D64249"}
          ;;
          :color/metro.line-1     {50 "#F56394"}
          :color/metro.line-2     {50 "#0064A8"}
          :color/metro.line-3     {50 "#b3ad00"}
          :color/metro.line-4     {50 "#6FB7AE"}
          :color/metro.line-5     {50 "#FDDF00"}
          :color/metro.line-6     {50 "#FF1100"}
          :color/metro.line-7     {50 "#FF6309"}
          :color/metro.line-8     {50 "#018749"}
          :color/metro.line-9     {50 "#5B2C2A"}
          :color/metro.line-a     {50 "#81017e"}
          :color/metro.line-b     {50 "#A8A8A8"}
          :color/metro.line-b-alt {50 "#00673e"}
          :color/metro.line-12    {50 "#c59e51"}})

(defonce inner-colors (atom colors-1))

(defn set-colors! [app-colors]
  (reset! inner-colors (conj colors-1 app-colors)))

(defn- parse-number [s]
  (if (string/includes? s ".")
    (js/parseFloat s)
    (js/parseInt s 10)))

(defn- as-hex-byte [value]
  (-> (.toString (math/round value) 16)
      (.padStart 2 "0")
      string/upper-case))

(defn- opacity->hex [opacity]
  (as-hex-byte (* 255 (/ opacity 100))))

(defn- hex->rgb [hex-color]
  (let [hex-value (subs hex-color 1)]
    [(js/parseInt (subs hex-value 0 2) 16)
     (js/parseInt (subs hex-value 2 4) 16)
     (js/parseInt (subs hex-value 4 6) 16)]))

(defn- rgb->hex [[r g b]]
  (str "#" (as-hex-byte r) (as-hex-byte g) (as-hex-byte b)))

(defn- mix-channel [base target weight]
  (math/round (+ (* base (- 1 weight))
                 (* target weight))))

(defn- compute-color-intensity [base-color-50 intensity]
  (let [base-rgb       (hex->rgb base-color-50)
        weight         (abs (/ (- intensity 50) 50))
        target-channel (if (< intensity 50) 255 0)]
    (->> base-rgb
         (map #(mix-channel % target-channel weight))
         rgb->hex)))

(defn compute-color [base-color-50 intensity opacity]
  (cond-> base-color-50
    (not= intensity 50) (compute-color-intensity intensity)
    opacity             (str (opacity->hex opacity))))

(defn- split-number-suffix [s]
  (when-let [[_ prefix suffix] (re-matches #"(.+)-([0-9]+(?:\.[0-9]+)?)" s)]
    [prefix (parse-number suffix)]))

(defn- opacity-color? [color]
  (#{:color/white :color/black} color))

(defn- known-color-parts [color]
  (let [[base-name suffix]  (split-number-suffix (name color))
        base-color          (when base-name (keyword "color" base-name))
        [family-name level] (when base-name (split-number-suffix base-name))
        family-color        (when family-name (keyword "color" family-name))]
    (cond
      (@inner-colors color)
      [color 50 nil]

      (and (@inner-colors base-color) (opacity-color? base-color))
      [base-color 50 suffix]

      (@inner-colors base-color)
      [base-color suffix nil]

      (and (@inner-colors family-color) (not (opacity-color? family-color)))
      [family-color level suffix])))

(defn color-parts [color]
  (if-let [parts (known-color-parts color)]
    parts
    (let [[_ color-name level opacity] (->> color
                                            name
                                            (re-matches #"([a-z-]+?)(?:-([0-9]+(?:\.[0-9]+)?)(?:-([0-9]+))?)?$"))
          color-kw          (keyword "color" color-name)
          level-as-opacity? (and level (opacity-color? color-kw))]
      [color-kw
       (cond
         level-as-opacity? 50
         level             (parse-number level)
         :else             50)
       (cond
         (and level-as-opacity? level) (parse-number level)
         level-as-opacity?             50
         opacity                       (js/parseInt opacity 10))])))

(def get-color
  (memoize
   (fn get-color-internal
     ([color]
      (if (string? color)
        color
        (apply get-color (color-parts color))))
     ([color level]
      (if (#{:color/black :color/white} color)
        (get-color color 50 level)
        (get-color color level nil)))
     ([color level opacity]
      (cond
        (string? color)
        (compute-color color level opacity)

        (#{:color/neutral :color/black :color/white} color)
        (-> @inner-colors (get-in [color level]) (compute-color 50 opacity))

        :else
        (compute-color (get-in @inner-colors [color 50]) level opacity))))))
