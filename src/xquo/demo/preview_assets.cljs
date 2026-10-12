(ns xquo.demo.preview-assets
  (:require [react-native.utils :refer [asset-require]]))

(def coast (asset-require "xquo-demo/previews/coast.png"))
(def ceramics (asset-require "xquo-demo/previews/ceramics.png"))

(def examples
  {:coast    {:title       "Wanderlight — Coastal trails"
              :community   "Wanderlight"
              :description "Find quiet paths, hidden coves and a slower way to explore the coast."
              :url         "wanderlight.example"
              :favicon     {:content {:type :initials :text "W"}}
              :image       {:source coast}}
   :ceramics {:title       "Form & Field — Studio notes"
              :community   "Form & Field"
              :description "Meet the makers shaping everyday objects with color, clay and care."
              :url         "formandfield.example"
              :favicon     {:content {:type :initials :text "F"}}
              :image       {:source ceramics}}})
