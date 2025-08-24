(ns xtatus-quo.components.avatars.account-avatar.view
  (:require
   [clojure.string :as string]
   [xtatus-quo.context :as context]
   [xtatus-quo.components.avatars.account-avatar.style :as style]))

(defn view
  "Opts:
    :type  - keyword -> :default/:watch-only
    :emoji - string -> 🍑 [default]
    :size  - number -> 80 [default] /48/32/28/24/20/16
    :customization-color - keyword or hexstring -> :blue/:army/... or #ABCEDF"
  [{:keys [size emoji]
    :or   {size  style/default-size
           emoji "🍑"}
    :as   opts}]
  (let [theme (context/use-theme)]
    [:rn/view {:style (style/root-container opts theme)}
     [:rn/text {:adjusts-font-size-to-fit true
                :style                    {:font-size (style/get-emoji-size size)}}
      (some-> emoji (string/trim))]]))
