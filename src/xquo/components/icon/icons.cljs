(ns xquo.components.icon.icons
  (:require-macros [xquo.components.icon.icons :as icons]))

(def ^:private icons (icons/resolve-icons))

(defn icon-source [icon]
  (get icons (name icon)))
