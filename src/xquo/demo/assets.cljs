(ns xquo.demo.assets
  (:require [react-native.utils :refer [asset-require]]))

(def illustration (asset-require "xquo-demo/pencil-flower.png"))
(def profile (asset-require "xquo-demo/profile.jpg"))
