(ns xtatus-quo.components.tags.status-tag.view
  (:require
    [quo.components.tags.status-tags :as status-tags]))

(defn view
  [props]
  [status-tags/status-tag props])
