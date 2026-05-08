(ns xquo.worklets.unified-tab)

(def worklets (js/require "../src/js/worklets/unified_tab_content"))

(def pan-on-begin (.-panOnBegin worklets))
(def pan-on-end (.-panOnEnd worklets))
(def pan-on-finalize (.-panOnFinalize worklets))
(def pan-on-update (.-panOnUpdate worklets))
(def select-tab (.-selectTab worklets))
(def use-content-translate-x (.-useContentTranslateX worklets))
(def use-indicator-gap-translate-x (.-useIndicatorGapTranslateX worklets))
(def use-indicator-translate-x (.-useIndicatorTranslateX worklets))
