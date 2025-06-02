(ns xtatus-quo.components.settings.category.view
  (:require
   [xtatus-quo.components.settings.category.data-item.view :as data-item]
   [xtatus-quo.components.settings.category.settings.view :as settings]))

(defn category
  [{:keys [list-type] :as props}]
  (condp = list-type
    :settings  [settings/settings-category props]
    :data-item [data-item/view props]
    nil))
