(ns xquo.markdown.text.view
  (:require [react-native.utils :as rn.utils]
            [xquo.components.text.view :as text]
            [xquo.markdown.text.style :as style]))

(defn text [{:keys      [type text-style]
             root-style :style
             :or        {type :body}
             :as        props}
            & children]
  [:rn/view {:style (rn.utils/add-styles (style/container type) root-style)}
   (into [text/text (-> props
                        (dissoc :type :text-style)
                        (assoc :font  (style/fonts type)
                               :style (rn.utils/add-styles (style/text type) text-style)))]
         children)])
