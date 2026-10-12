(ns xquo.markdown.text.style
  (:require [react-native.utils :refer [style]]))

(def fonts
  {:h1         :font/semibold-27
   :h2         :font/semibold-19
   :h3         :font/semibold-15
   :h3-regular :font/regular-15
   :body-bold  :font/semibold-13
   :body       :font/regular-13})

(def ^:private padding
  {:h1         [12 12]
   :h2         [12 4]
   :h3         [12 8]
   :h3-regular [4 8]
   :body-bold  [8 8]
   :body       [8 8]})

(defn container [type]
  (let [[top bottom] (padding type)]
    (style {:padding-horizontal 20
            :padding-top        top
            :padding-bottom     bottom})))

(def ^:private line-heights
  {:h1         32
   :h2         26
   :h3         22
   :h3-regular 22
   :body-bold  18
   :body       18})

(defn text [type]
  (style {:include-font-padding false
          :text-align-vertical  :center
          :line-height          (line-heights type)}))
