(ns xquo.demo.community.assets
  (:require [react-native.utils :refer [asset-require]]))

(defonce status (asset-require "xquo-demo/community/status.png"))
(defonce cover (asset-require "xquo-demo/community/cover.png"))
(defonce money (asset-require "xquo-demo/community/money.png"))
(defonce growth (asset-require "xquo-demo/community/growth.png"))
(defonce diamond (asset-require "xquo-demo/community/diamond.png"))
