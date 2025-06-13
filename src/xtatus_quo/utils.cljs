(ns xtatus-quo.utils)

(defn if-theme [theme light-value dark-value]
  (if (= theme :theme/light)
    light-value
    dark-value))
