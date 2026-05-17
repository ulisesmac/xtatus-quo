(ns xquo.worklets.unified-tab
  (:require [react-native.utils :as rn.utils]))

(def worklets (rn.utils/asset-require "xquo/worklets/unified_tab_content.js"))

(def pan-on-begin (.-panOnBegin worklets))
(def pan-on-end (.-panOnEnd worklets))
(def pan-on-finalize (.-panOnFinalize worklets))
(def pan-on-update (.-panOnUpdate worklets))
(def select-tab (.-selectTab worklets))
(def use-content-translate-x (.-useContentTranslateX worklets))
(def use-indicator-gap-translate-x (.-useIndicatorGapTranslateX worklets))
(def use-indicator-translate-x (.-useIndicatorTranslateX worklets))
