(ns xquo.components.list.style
  (:require [reagent-extended-compiler.utils.transforms :refer [defstyle]]
            [xquo.foundations.colors :as colors]
            [xquo.foundations.spacing :as spacing]))

(defstyle container-base
  {:align-self     :stretch
   :padding-top    (spacing/spacing-values 4)
   :padding-bottom (spacing/spacing-values 6)})

(defstyle element-container
  {:padding-horizontal (spacing/spacing-values 8)
   :padding-vertical   (spacing/spacing-values 3)
   :flex-direction     :row
   :align-items        :flex-start
   :gap                (spacing/spacing-values 4)})

(defstyle content-container
  {:flex      1
   :min-width 0
   :gap       (spacing/spacing-values 1)})

(defstyle button-container
  {:align-self  :center
   :flex-shrink 0})

(defn bullet-color [theme]
  (if (= theme :theme/dark)
    (colors/get-color :color/neutral-50)
    (colors/get-color :color/neutral-40)))
