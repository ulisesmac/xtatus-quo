(ns utils.number
  (:require [clojure.string :as string]
            [utils.money :as money]))

(defn naive-round
  "Quickly and naively round number `n` up to `decimal-places`.

  Example usage: use it to avoid re-renders caused by floating-point number
  changes in Reagent atoms. Such numbers can be rounded up to a certain number
  of `decimal-places` in order to avoid re-rendering due to tiny fractional
  changes.

  Don't use this function for arbitrary-precision arithmetic."
  [n decimal-places]
  (let [scale (Math/pow 10 decimal-places)]
    (/ (Math/round (* n scale))
       scale)))

(defn parse-int
  "Parses `n` as an integer. Defaults to zero or `default` instead of NaN."
  ([n]
   (parse-int n 0))
  ([n default]
   (let [maybe-int (js/parseInt n 10)]
     (if (integer? maybe-int)
       maybe-int
       default))))

(defn parse-float
  "Parses `n` as a float. Defaults to zero or `default` instead of NaN."
  ([n]
   (parse-float n 0))
  ([n default]
   (let [maybe-float (js/parseFloat n 10)]
     (if (js/Number.isNaN maybe-float)
       default
       maybe-float))))

(defn value-in-range
  "Returns `num` if is in the range [`lower-bound` `upper-bound`]
  if `num` exceeds a given bound, then returns the bound exceeded."
  [number lower-bound upper-bound]
  (max lower-bound (min number upper-bound)))

(defn remove-trailing-zeroes
  [num]
  (let [parts (string/split (str num) #"\.")]
    (str (first parts)
         (if-let [decimals (second parts)]
           (if (seq (string/replace decimals #"0+$" ""))
             (str "." (string/replace decimals #"0+$" ""))
             "")
           ""))))

(defn convert-to-whole-number
  "Converts a fractional `amount` to its corresponding whole number representation
  by dividing it by 10 raised to the power of `decimals`. This is often used in financial
  calculations where amounts are stored in their smallest units (e.g., cents) and need
  to be converted to their whole number equivalents (e.g., dollars).

  Example usage:
  (convert-to-whole-number 12345 2) ; => 123.45"
  [amount decimals]
  (when-let [[amount-bn divisor-bn] (money/->bignumbers amount (money/from-decimal decimals))]
    (-> (money/div amount-bn divisor-bn)
        (money/to-fixed decimals)
        remove-trailing-zeroes)))

(defn to-fixed
  [num decimals]
  (-> num
      (money/to-fixed decimals)
      remove-trailing-zeroes))

(defn small-number-threshold
  "Receives a decimal count and returns a string like '<0.001' if the decimal count is 3,
   '<0.000001' if the decimal count is 6, etc."
  ([decimal-count]
   (small-number-threshold decimal-count ""))
  ([decimal-count currency-symbol]
   (if (> decimal-count 0)
     (str "<" currency-symbol "0." (apply str (repeat (dec decimal-count) "0")) "1")
     (str currency-symbol "0"))))

(defn valid-decimal-count?
  "Returns false if the number has more decimals than the decimal count, otherwise true."
  [num decimal-count]
  (let [decimal-part (second (string/split (str num) #"\."))]
    (or (nil? decimal-part) (<= (count decimal-part) decimal-count))))

(defn format-decimal-fixed
  "Formats a number `n` to `decimal-places` without rounding.
   - Ensures the exact number of decimal places (including trailing zeros).
   - Does not round up or down, just trims excess decimals."
  [n decimal-places]
  (let [bn        (money/bignumber n)
        scale     (money/bignumber (money/from-decimal decimal-places))
        truncated (if (money/less-than bn 0)
                    (Math/ceil (money/mul bn scale))
                    (Math/floor (money/mul bn scale)))]
    (-> truncated
        (money/bignumber)
        (money/div scale)
        (money/to-fixed decimal-places))))

(defn format-as-percentage
  "Formats a number to be shown as a percentage with two decimal places."
  [change]
  (-> change
      (* 100)
      (naive-round 2)))

(defn format-number-with-si-suffix
  "Formats a large number using SI suffixes for readability.
  Given a number n, this function converts it to a string representation
  with appropriate SI suffixes:
    - 'K' for thousands (10^3)
    - 'M' for millions (10^6)
    - 'B' for billions (10^9)
    - 'T' for trillions (10^12)
  If the number is less than one million, it is returned as-is without
  any suffix."
  [n]
  (let [letters '("K" "M" "B" "T")]
    (loop [l     letters
           value n]
      (if (< value 1000)
        (str value)
        (let [divided (naive-round (/ value 1000) 1)]
          (if (or (< divided 1000) (empty? l))
            (str divided (first l))
            (recur (rest l) divided)))))))
