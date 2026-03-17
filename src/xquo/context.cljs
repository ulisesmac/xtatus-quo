(ns xquo.context
  (:require ["react" :as react]
            [applied-science.js-interop :as j]
            [reagent.core :as r]
            [xquo.react-native :as rn]))

(defonce ^:private app-context
  (react/createContext nil))

(defonce ^:private theme-atom (r/atom nil))
(defonce ^:private color-atom (r/atom :color/primary))

(defn current-theme []
  (or @theme-atom
      (rn/get-color-scheme)))

(defn current-color []
  @color-atom)

(defn set-theme! [theme]
  (reset! theme-atom theme))

(defn set-light-theme! []
  (set-theme! :theme/light))

(defn set-dark-theme! []
  (set-theme! :theme/dark))

(defn set-color! [color]
  (reset! color-atom color))

(defn provider [& args]
  (let [props           (first args)
        overrides?      (map? props)
        children        (if overrides?
                          (rest args)
                          args)
        theme           (when overrides? (:theme props))
        color           (when overrides? (:color props))
        current-value   (react/useContext app-context)
        inherited-theme (when current-value (j/get current-value :theme))
        inherited-color (when current-value (j/get current-value :color))
        theme-value     (or theme inherited-theme (current-theme))
        color-value     (or color inherited-color (current-color))
        provider-value  (rn/use-memo
                         (fn []
                           #js {:theme theme-value
                                :color color-value})
                         [(some-> theme-value name)
                          (some-> color-value name)])]
    (into [:> (j/get app-context :Provider) {:value provider-value}]
          children)))

(defn use-theme-color []
  (let [context-value (react/useContext app-context)
        theme-value   (or (when context-value (j/get context-value :theme))
                          (current-theme))
        color-value   (or (when context-value (j/get context-value :color))
                          (current-color))]
    {:theme        theme-value
     :color        color-value
     :dark-theme?  (= theme-value :theme/dark)
     :light-theme? (= theme-value :theme/light)}))

(defn use-theme []
  (:theme (use-theme-color)))

(defn use-color []
  (:color (use-theme-color)))
