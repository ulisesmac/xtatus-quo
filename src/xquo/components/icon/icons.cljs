(ns ^:dev/always xquo.components.icon.icons
  (:require-macros [xquo.components.icon.icons :as icons]))

;; Resolved at compile time from resources/icons/required.edn.
;; Update this namespace when required.edn changes so newly used icons are included.
(def ^:private icons (icons/resolve-icons))

(defn icon-source [icon-key]
  (get icons (name icon-key)))
