(ns xquo.list.item.values
  (:require [react-native.utils :as rn.utils :refer [style]]
            [xquo.components.icon.view :as icon]
            [xquo.components.text.view :as text]
            [xquo.foundations.colors :as colors]
            [xquo.list.item.style :as styles]))

(def ^:private direction-icons
  {:positive :icon/positive :negative :icon/negative})

(defn- change-view [{:keys [theme blur? direction percentage value]}]
  (let [color (styles/change-color theme blur? direction)]
    [:rn/view {:style styles/change}
     (when percentage
       [text/text {:font :font/regular-13 :number-of-lines 1 :style (styles/text color 18.2 true)} percentage])
     (when (and percentage value)
       [:rn/view {:style (rn.utils/add-styles styles/change-separator (style {:background-color color}))}])
     (when value
       [text/text {:font :font/regular-13 :number-of-lines 1 :style (styles/text color 18.2 true)} value])
     (when direction
       [icon/view {:name (direction-icons direction) :size 16 :color color}])]))

(defn view [{:keys [theme blur? primary secondary primary-props secondary-props style]}]
  (let [{:keys         [font number-of-lines]
         primary-color :color
         primary-style :style
         :or           {font            :font/medium-13
                        number-of-lines 1
                        primary-color   (colors/themed theme :color/neutral-100 :color/white-100)}} primary-props
        change? (= (:type secondary) :change)]
    [:rn/view {:style (rn.utils/add-styles (styles/values change?) style)}
     [text/text (assoc primary-props
                       :font            font
                       :number-of-lines number-of-lines
                       :style           (rn.utils/add-styles (styles/text primary-color 18.2 (= number-of-lines 1))
                                                             primary-style))
      primary]
     (when secondary
       (case (:type secondary)
         :change
         [change-view (assoc secondary :theme theme :blur? blur?)]

         (let [{:keys [font number-of-lines style]
                :or   {font :font/regular-13 number-of-lines 1}} secondary-props]
           [text/text (assoc secondary-props
                             :font            font
                             :number-of-lines number-of-lines
                             :style           (rn.utils/add-styles (styles/text (styles/secondary theme blur?) 18.2 (= number-of-lines 1)) style))
            secondary])))]))
