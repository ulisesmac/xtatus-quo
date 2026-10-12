(ns xquo.components.text.combination.view
  (:require [react-native.utils :as rn.utils]
            [xquo.components.button.view :as button]
            [xquo.components.icon.view :as icon]
            [xquo.components.text.view :as text]
            [xquo.components.text.combination.style :as style]
            [xquo.context :as context]
            [xquo.tag.standard.view :as tag]))

(defn- tag-view [{:keys [props blur? size]}]
  (let [{:keys [label]} props]
    [tag/tag (-> props
                 (dissoc :label)
                 (assoc :size  (:size props size)
                        :blur? (:blur? props blur?)))
     label]))

(defn- right-view [{:keys [entry blur? theme]}]
  (case (:type entry)
    :button
    (let [{:keys [label] :as props} (:button entry)]
      [button/button (-> props
                         (dissoc :label)
                         (assoc :size  (:size props 32)
                                :blur? (:blur? props blur?)))
       label])

    :tag
    [tag-view {:props (:tag entry) :blur? blur? :size 32}]

    :text
    (let [{:keys [value] :as props} (:text entry)]
      [:rn/view {:style style/right-text}
       [text/text (-> props
                      (dissoc :value)
                      (assoc :font  (:font props :font/regular-13)
                             :style (rn.utils/add-styles (style/counter-text theme blur?) (:style props))))
        value]])

    :custom
    (:view entry)

    nil))

(defn title [{:keys [title title-props caption caption-props icons tag right blur? type]
              :as   props}]
  (let [{:keys [theme] :as theme-color} (context/use-theme-color)]
    [:rn/view (-> props
                  (dissoc :title :title-props :caption :caption-props :icons :tag :right :blur? :type)
                  (assoc :style (rn.utils/add-styles style/row (:style props))))
     [:rn/view {:style style/body}
      [text/text (assoc title-props
                        :font  (:font title-props :font/semibold-27)
                        :style (rn.utils/add-styles style/heading (:style title-props)))
       title]
      (when caption
        [:<>
         [:rn/view {:style style/caption}
          [text/text {:font :font/medium-15 :style (style/caption-text theme-color blur?)} "∙"]]
         [:rn/view {:style style/caption}
          [text/text (assoc caption-props
                            :font            (:font caption-props :font/medium-15)
                            :number-of-lines (:number-of-lines caption-props 1)
                            :style           (rn.utils/add-styles (style/caption-text theme-color blur?) (:style caption-props)))
           caption]]])
      (when (seq icons)
        (into [:rn/view {:style (style/icons type caption theme-color)}]
              (map (fn [props]
                     [icon/view (assoc props
                                       :size  (:size props 20)
                                       :color (:color props (style/icon-color theme-color blur?)))]))
              icons))
      (when tag
        [:rn/view {:style style/tag}
         [tag-view {:props tag :blur? blur? :size 24}]])]
     (when right
       [right-view {:entry right :blur? blur? :theme theme}])]))
