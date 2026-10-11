(ns xquo.calendar.picker.date
  (:require [clojure.string :as string]
            [react-native.core :as rn]))

(defn- two-digits [number]
  (.padStart (str number) 2 "0"))

(defn month-parts [month]
  (mapv js/parseInt (string/split month #"-")))

(defn resolve-locale [locale]
  (let [language (-> (or locale (:lang rn/device-language))
                     name
                     string/lower-case
                     (string/split #"[-_]")
                     first)]
    (if (= language "es") "es" "en")))

(defn- utc-date [year month day]
  (doto (js/Date. 0)
    (.setUTCFullYear year (dec month) day)))

(defn today []
  (let [date (js/Date.)]
    (str (.getFullYear date) "-" (two-digits (inc (.getMonth date))) "-" (two-digits (.getDate date)))))

(defn month-string [year month]
  (str (.padStart (str year) 4 "0") "-" (two-digits month)))

(defn shift-month [month offset]
  (let [[year month] (month-parts month)
        date         (utc-date year (+ month offset) 1)]
    (month-string (.getUTCFullYear date) (inc (.getUTCMonth date)))))

(defn month-label [month locale]
  (let [[year month] (month-parts month)
        formatter    (js/Intl.DateTimeFormat. locale #js {:month "long" :year "numeric" :timeZone "UTC"})]
    (.format formatter (utc-date year month 1))))

(defn weekday-labels [locale first-day-of-week]
  (let [formatter (js/Intl.DateTimeFormat. locale #js {:weekday "short" :timeZone "UTC"})]
    (mapv (fn [offset]
            (let [weekday (mod (+ first-day-of-week offset) 7)
                  label   (.format formatter (utc-date 2023 1 (inc weekday)))]
              (subs label 0 (min 2 (count label)))))
          (range 7))))

(defn weeks [month first-day-of-week]
  (let [[year month-number] (month-parts month)
        first-day           (utc-date year month-number 1)
        offset              (mod (- (.getUTCDay first-day) first-day-of-week) 7)
        day-count           (.getUTCDate (utc-date year (inc month-number) 0))
        week-count          (max 5 (js/Math.ceil (/ (+ offset day-count) 7)))]
    (->> (range (* 7 week-count))
         (map (fn [index]
                (let [date       (utc-date year month-number (+ 1 (- index offset)))
                      date-month (month-string (.getUTCFullYear date) (inc (.getUTCMonth date)))
                      day        (.getUTCDate date)]
                  {:date           (str date-month "-" (two-digits day))
                   :day            day
                   :current-month? (= month date-month)})))
         (partition 7)
         (mapv vec))))

(defn outside-bounds? [date min-date max-date]
  (or (and min-date (neg? (compare date min-date)))
      (and max-date (pos? (compare date max-date)))))

(defn next-range [value date]
  (let [{:keys [start end]} value]
    (cond
      (or (nil? start) end)
      {:start date}

      (neg? (compare date start))
      {:start date :end start}

      :else
      {:start start :end date})))
