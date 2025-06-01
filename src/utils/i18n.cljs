(ns utils.i18n
  (:require
   ;["i18n-js" :as i18n]
    [clojure.string :as string]))

(defn setup
  [default-device-language translations-by-locale])

(defn get-translations
  []
  )

(defn set-language
  [lang]
  )

;;:zh, :zh-hans-xx, :zh-hant-xx have been added until this bug will be fixed
;;https://github.com/fnando/i18n-js/issues/460

(def delimeters
  "This function is a hack: mobile Safari doesn't support toLocaleString(), so we need to pass
  this map to WKWebView to make number formatting work."
  (let [n          (.toLocaleString ^js (js/Number 1000.1))
        delimiter? (= (count n) 7)]
    (if delimiter?
      {:delimiter (subs n 1 2)
       :separator (subs n 5 6)}
      {:delimiter ""
       :separator (subs n 4 5)})))

(defn label-number
  [number]
  (when number
    (let [{:keys [delimiter separator]} delimeters]
      )))

(def default-option-value "<no value>")

(defn label-options
  [options]
  ;; i18n ignores nil value, leading to misleading messages
  (into {} (for [[k v] options] [k (or v default-option-value)])))

(defn label-fn
  ([path] (label-fn path {}))
  ([path options]
   ))

(def label (memoize label-fn))

(defn label-pluralize
  [amount path & options]
  )

(def locale nil)
