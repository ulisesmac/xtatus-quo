(ns ^:dev/always xquo.components.icon.icons
  (:require-macros [xquo.components.icon.icons :as icons]))

;; Resolved at compile time from resources/icons/required.edn.
;; Touch this namespace when required.edn changes so newly used icons are included.
;; Keep this namespace easy to touch so dev watch picks required.edn changes.
;; Reload marker: updated after adding alert-20 to the required icon set.
(def ^:private icons (icons/resolve-icons))

(defn icon-source [icon-key]
  (get icons (name icon-key)))
