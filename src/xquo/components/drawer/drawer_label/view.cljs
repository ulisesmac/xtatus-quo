(ns xquo.components.drawer.drawer-label.view
  (:require [react-native.utils :as rn.utils]
            [xquo.components.drawer.drawer-label.style :as style]
            [xquo.components.text.view :as text]
            [xquo.context :as context]))

(defn drawer-label
  "Drawer label component.

  API:
  - `props` map
    - `:text` label text string
    - `:blur?` optional boolean for blur styling
    - `:style` optional caller style
    - Any additional keys are forwarded to `:rn/view`."
  [{label-text :text :keys [blur?] :as props}]
  (let [theme (context/use-theme)]
    [:rn/view (-> props
                  (dissoc :text :blur? :style)
                  (assoc :style (rn.utils/add-styles style/container-base (:style props))))
     [text/text {:font  :font/medium-13
                 :style (style/text-style theme blur?)}
      label-text]]))
