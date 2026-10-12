(ns xquo.markdown.list-item.view
  (:require [react-native.utils :as rn.utils]
            [xquo.components.counter.step.view :as step]
            [xquo.components.text.view :as text]
            [xquo.context :as context]
            [xquo.markdown.list-item.style :as style]))

(defn- text-content [{:keys [content font props]}]
  (let [parts (if (string? content) [content] content)]
    (into [:rn/view {:style style/inline}]
          (map (fn [part]
                 (if (string? part)
                   [text/text (assoc props
                                     :font  (:font props font)
                                     :style (rn.utils/add-styles style/text (:style props)))
                    part]
                   part)))
          parts)))

(defn list-item [{:keys [title description title-props description-props step blur?]
                  :as   props}]
  (let [{:keys [dark-theme?]}   (context/use-theme-color)
        {:keys [value type]
         :or   {type :neutral}} step
        parts                   (concat (when (sequential? title) title)
                                        (when (sequential? description) description))
        rich?                   (some vector? parts)
        highlighted?            (and step (not= type :neutral))]
    [:rn/view (-> props
                  (dissoc :title :description :title-props :description-props :step :blur?)
                  (assoc :style (rn.utils/add-styles (style/container dark-theme? title description rich? step highlighted?)
                                                     (:style props))))
     (if step
       [step/step (-> step
                      (dissoc :value)
                      (assoc :blur? (:blur? step blur?)))
        value]
       [:rn/view {:style style/marker}
        [:rn/view {:style (style/bullet dark-theme? blur?)}]])
     [:rn/view {:style style/content}
      (when title
        [text-content {:content title :font :font/semibold-13 :props title-props}])
      (when description
        [text-content {:content description :font :font/regular-13 :props description-props}])]]))
