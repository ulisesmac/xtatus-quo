(ns xquo.browser.dapp-item.style
  (:require [react-native.utils :refer [defstyle]]))

(defstyle container
  {:width   80
   :padding 8
   :gap     8})

(defstyle image
  {:width  64
   :height 64})

(defstyle title
  {:height               18
   :include-font-padding false
   :text-align           :center
   :text-align-vertical  :center})
