(ns xquo.drawer.bottom-actions.style
  (:require [react-native.utils :refer [defstyle style]]
            [xquo.foundations.colors :as colors]))

(defstyle container
  {:width "100%"})

(defstyle actions
  {:flex-direction     :row
   :align-items        :center
   :gap                12
   :padding-horizontal 20
   :padding-vertical   12})

(defstyle action
  {:flex 1})

(defn description [position error?]
  (style {:flex-direction     :row
          :align-items        :center
          :justify-content    :center
          :gap                (if error? 4 5)
          :padding-top        (when (= position :top)
                                (if error? 15 12))
          :padding-bottom     (cond
                                (= position :bottom) 12
                                error?               7
                                :else                4)
          :padding-horizontal (if (= position :bottom) 40 20)}))

(defn text [{:keys [dark-theme? light-theme?]} blur? scroll? position error?]
  (style {:text-align  :center
          :flex-shrink 1
          :color       (cond
                         (and error? dark-theme?)
                         (colors/get-color :color/danger 60)

                         error?
                         (colors/get-color :color/danger 50)

                         (and light-theme? scroll? (= position :bottom))
                         (colors/get-color :color/neutral-80-70)

                         light-theme?
                         (colors/get-color :color/neutral-50)

                         (and scroll? (or blur? (= position :bottom)))
                         (colors/get-color :color/white-70)

                         blur?
                         (colors/get-color :color/white-40)

                         :else
                         (colors/get-color :color/neutral-40))}))
