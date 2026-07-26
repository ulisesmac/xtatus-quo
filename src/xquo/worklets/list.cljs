(ns xquo.worklets.list
  (:require [react-native.utils :as rn.utils]))

(def worklets (rn.utils/asset-require "xquo/worklets/list_content.js"))

(def use-collapsible-style (.-useCollapsibleStyle worklets))
