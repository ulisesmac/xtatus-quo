(ns xquo.components.settings.section-title.view
  (:require [reagent-extended-compiler.utils.transforms :as rec.xf]
            [xquo.components.icon.view :as icon]
            [xquo.components.settings.section-title.style :as style]
            [xquo.components.text.view :as text]
            [xquo.context :as context]))

(defn section-title
  "Settings section title.

  API:
  - `props` map
    - `:label` title text (default `\"Title\"`)
    - `:counter?` show counter when truthy
    - `:right-icon?` show right info icon when truthy
    - `:style` optional caller style (map/vector/js style)
    - Any additional keys are forwarded to `:rn/view`."
  [{:keys [label counter? right-icon?]
    :or   {label "Title"}
    :as   props}]
  (let [theme (context/use-theme)]
    [:rn/view (-> props
                  (dissoc :label :counter-value :counter? :right-icon? :background :style)
                  (assoc :style (rec.xf/add-styles
                                 style/container-base
                                 (when right-icon?
                                   style/container-gap-4)
                                 (:style props))))
     [:rn/view {:style [style/content-base
                        (when counter? style/content-gap-4)]}
      [text/text {:font            :font/semibold-15
                  :number-of-lines 1
                  :style           {:color (style/label-color theme)}}
       label]
      (when counter?
        ;; TODO: replace red placeholder with the real counter component once available.
        [:rn/view {:style style/counter-placeholder}])]
     (when right-icon?
       [icon/icon {:icon  :icon/info
                   :size  20
                   :color (style/right-icon-color theme)}])]))
