(ns xquo.context
  (:require ["react" :as react]
            [applied-science.js-interop :as j]))

(defonce ^:private theme-context
  (react/createContext nil))

(defn provider [{:keys [theme]} & children]
  (into [:> (j/get theme-context :Provider) {:value #js{:theme theme}}]
        children)) ;; TODO: check [:<>] along with just passing the seq

(defn use-theme []
  (j/get (react/useContext theme-context) :theme))
