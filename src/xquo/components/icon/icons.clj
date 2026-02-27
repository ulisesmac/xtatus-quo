(ns xquo.components.icon.icons)

(def ^:private icon-require-path "../xtatus-quo/resources/icons/")
(def ^:private required-icons-edn "./xtatus-quo/resources/icons/required.edn")

(defn- parse-icon+size [icon-kw]
  (let [[_ icon-name size] (re-matches #"(.+)-([0-9]+)$" (name icon-kw))]
    [icon-name (Integer/parseInt size)]))

(defn- require-icon [size icon-name]
  (let [path (str icon-require-path size "/")
        key  (str icon-name size)
        path-png (str path icon-name ".png")]
    [key `(js/require ~path-png)]))

(defmacro resolve-icons []
  (->> required-icons-edn
       slurp
       read-string
       (map parse-icon+size)
       (map (fn [[icon-name size]]
              (require-icon size icon-name)))
       (into {})))
