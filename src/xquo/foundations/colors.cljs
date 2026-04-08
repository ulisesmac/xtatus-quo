(ns xquo.foundations.colors
  (:require [cljs.math :as math]
            [clojure.string :as string]))

(def prev-colors
   {2.5 "#FCFCFC"
   5   "#F5F6F8"
   10  "#F0F2F5"
   20  "#E7EAEE"
   30  "#DCE0E5"
   40  "#A1ABBD"
   50  "#647084"
   60  "#303D55"
   70  "#202C42"
   80  "#1B273D"
   90  "#131D2F"
   95  "#0D1625"
   100 "#09101C"})

(def inner-colors-backup
  #:color{:neutral {2.5 "#FBFCFC"
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
          :white   {50 "#FFFFFF"}
          :black   {50 "#000000"}
          :primary {50 "#2C7F8E"}
          :success {50 "#2AA56B"}
          :warning {50 "#D89622"}
          :danger  {50 "#D64249"}})

(def inner-colors
  #:color{:neutral {2.5 "#FCFCFC"
                    5   "#F5F7F8"
                    10  "#EFF2F4"
                    20  "#E7ECEF"
                    30  "#DBE2E6"
                    40  "#9FAEB7"
                    50  "#62717A"
                    60  "#394952"
                    70  "#2A3942"
                    80  "#22343F"
                    90  "#172731"
                    95  "#0A1216"
                    100 "#050B0E"}
          :white   {50 "#FFFFFF"}
          :black   {50 "#000000"}
          :primary {50 "#3A7690"}
          :success {50 "#2AA56B"}
          :warning {50 "#B37F2F"}
          :danger  {50 "#D64249"}})

(defonce colors (atom inner-colors))

(defn set-colors! [app-colors]
  (swap! colors conj app-colors))

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
      (@colors color)
      [color 50 nil]

      (and (@colors base-color) (opacity-color? base-color))
      [base-color 50 suffix]

      (@colors base-color)
      [base-color suffix nil]

      (and (@colors family-color) (not (opacity-color? family-color)))
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
        (-> @colors (get-in [color level]) (compute-color 50 opacity))

        :else
        (compute-color (get-in @colors [(or color :color/black) 50]) level opacity))))))

(defn themed
  "Returns a color resolved with `get-color` for the given `theme`.

  With one color argument, resolves that color at level `50` for `:theme/light`
  and `60` otherwise.

  With light and dark color arguments, resolves the light color for
  `:theme/light` and the dark color otherwise."
  ([theme color]
   (get-color color (if (= theme :theme/light) 50 60)))
  ([theme light-color dark-color]
   (get-color (if (= theme :theme/light) light-color dark-color))))
