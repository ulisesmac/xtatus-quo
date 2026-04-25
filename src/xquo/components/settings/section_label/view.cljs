(ns xquo.components.settings.section-label.view
  (:require [reagent-extended-compiler.utils.transforms :as rec.xf]
            [xquo.components.settings.section-label.style :as style]
            [xquo.components.text.view :as text]
            [xquo.context :as context]))

(defn section-label
  "Settings section label.

  API:
  - `props` map
    - `:label` label text (default `\"Account\"`)
    - `:counter` optional right-side value (for example `\"00/00\"`)
    - `:description` optional second line. When present, the component uses
      the description variant and ignores `:counter`.
    - `:blur?` optional boolean for blur styling
    - `:style` optional caller style (map/vector/js style)
    - Any additional keys are forwarded to `:rn/view`."
  [{:keys [label counter description blur?]
    :or   {label "Account"}
    :as   props}]
  (let [theme         (context/use-theme)
        counter?      (and (not description) counter)
        label-color   (style/label-color theme blur?)]
    [:rn/view (-> props
                  (dissoc :label :counter :description :blur? :style)
                  (assoc :style (rec.xf/add-styles
                                 (if description style/description-base style/row-base)
                                 (when counter? style/row-gap-12)
                                 (:style props))))
     [text/text {:font  (if description :font/medium-15 :font/medium-13)
                 :style [(if description style/description-line style/label-slot)
                         label-color]}
      label]
     (cond
       description
       [text/text {:font  :font/regular-15
                   :style [style/description-line label-color]}
        description]

       counter?
       [text/text {:font  :font/regular-13
                   :style [style/counter-value (style/counter-color theme blur?)]}
        (str counter)])]))
