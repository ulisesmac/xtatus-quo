(ns xquo.demo.assets
  (:require [react-native.utils :refer [asset-require]]))

(def illustration (asset-require "xquo-demo/pencil-flower.png"))
(def profile-testing-1 (asset-require "xquo-demo/profile-testing-1.png"))
(def profile-testing-2 (asset-require "xquo-demo/profile-testing-2.png"))
(def profile-testing-3 (asset-require "xquo-demo/profile-testing-3.png"))
(def profile profile-testing-1)
