(ns xquo.context
  (:require [applied-science.js-interop :as j]
            [react-native.react.core :as react]
            [react-native.core :as rn]
            [reagent.core :as r]))

(defonce ^:private app-context (react/create-context nil))
(defonce ^:private theme-atom (r/atom nil))
(defonce ^:private color-atom (r/atom :color/primary))

(defn provider [{new-theme :theme new-color :color} & children]
  (let [prev-context   (react/use-context app-context)
        theme          (or new-theme (j/get prev-context :theme))
        value          (or new-color (j/get prev-context :color))
        provider-value (react/use-memo (fn [] #js{:theme theme :color value})
                                       [theme value])]
    (into [:> (j/get app-context :Provider) {:value provider-value}]
          children)))

(defn use-theme-color []
  (let [context     (react/use-context app-context)
        theme-value (j/get context :theme)
        color-value (j/get context :color)]
    {:theme        theme-value
     :color        color-value
     :dark-theme?  (= theme-value :theme/dark)
     :light-theme? (= theme-value :theme/light)}))

(defn current-color [] @color-atom)

(defn use-theme []
  (:theme (use-theme-color)))

(defn use-color []
  (:color (use-theme-color)))

(defn use-current-theme []
  (or @theme-atom (rn/use-color-scheme)))

(defn set-theme! [theme]
  (reset! theme-atom theme))

(defn set-light-theme! []
  (set-theme! :theme/light))

(defn set-dark-theme! []
  (set-theme! :theme/dark))

(defn set-color! [color]
  (reset! color-atom color))
