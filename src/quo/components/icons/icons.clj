(ns quo.components.icons.icons
  (:require
    [clojure.java.io :as io]
    [clojure.string :as string]))

(def ^:private icon-path "./resources/images/icons2/")
(def ^:private used-icons-edn "./resources/images/icons/required.edn")

(defn- require-icon
  [size path]
  (fn [el]
    (let [s (str "." path el ".png")
          k (-> el
                (string/replace "_" "-")
                (string/replace " " "-")
                (string/lower-case)
                (str size))]
      [k `(js/require ~s)])))

(defn- get-files
  [path]
  (->> (io/file path)
       file-seq
       (filter #(string/ends-with? % "png"))
       (map #(first (string/split (.getName %) #"@")))
       distinct))

(defn- get-icons
  [size]
  (let [path (str icon-path size "x" size "/")]
    (into {} (map (require-icon size path) (get-files path)))))

(defn- ensure-used-icons-file!
  []
  (let [f (io/file used-icons-edn)]
    (when-not (.exists f)
      (some-> f .getParent io/file .mkdirs)
      (spit f "#{}\n"))
    f))

(defn- read-used-icons
  []
  (ensure-used-icons-file!)
  (try
    (let [data (read-string (slurp used-icons-edn))]
      (if (set? data) data #{}))
    (catch Throwable _ #{})))

(defn- entry->name+size
  [entry]
  ;; Accept only vectors like [:i/globe 20]
  (when (and (vector? entry)
             (= 2 (count entry)))
    (let [[kw sz] entry]
      (when (and (keyword? kw)
                 (number? sz))
        [(-> kw name
             (string/replace "_" "-")
             (string/replace " " "-")
             (string/lower-case))
         (int sz)]))))

(defn- requires-from-used-icons
  []
  (let [entries (read-used-icons)]
    (->> entries
         (map entry->name+size)
         (remove nil?)
         (group-by second)
         (reduce-kv
          (fn [acc size name+size-vec]
            (merge acc
                   (let [path (str icon-path size "x" size "/")]
                     (into {}
                           (map (fn [[n _]]
                                  (let [k (str n size)
                                        s (str "." path n ".png")]
                                    [k `(js/require ~s)]))
                                name+size-vec)))))
          {}))))

(defmacro resolve-icons
  []
  ;; Read the curated set from EDN; create the file if missing.
  (requires-from-used-icons))
