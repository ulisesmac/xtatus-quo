(ns xquo.react-native
  (:require
   ["react-native" :refer [LogBox TouchableOpacity StatusBar Platform useColorScheme AppState
                           Dimensions Keyboard Animated
                           ;;
                           KeyboardAvoidingView
                           Appearance
                           StyleSheet
                           ;;
                           Linking PermissionsAndroid Share
                           useColorScheme
                           BackHandler]]
   ["react" :refer [useEffect useState useCallback useMemo useRef]]))

(defn use-color-scheme []
  (keyword "theme" (or (useColorScheme) "light")))

(def hairline-width (.-hairlineWidth StyleSheet))
(def style-sheet-absolute-fill (.-absoluteFill StyleSheet))

(def platform-os (keyword (.-OS Platform)))
(def platform-android? (= platform-os :android))

(def status-bar-height (.. StatusBar -currentHeight))
(def screen-height (.. Dimensions (get "screen") -height))

(defn window-width []
  (.. Dimensions (get "window") -width))

(defn window-height []
  (.. Dimensions (get "window") -height))

(def log-box LogBox)

(def keyboard Keyboard)

(defn safe-area-view [children]
  [:view {:style {:flex 1}}
   children])

(def animated Animated)

(defn color-scheme []
  (keyword (useColorScheme)))

(defn get-color-scheme []
  (keyword (.getColorScheme Appearance)))

(def app-state AppState)

(defn open-url [link]
  (.openURL Linking link))

(defn get-initial-url []
  (.getInitialURL Linking))

(defn add-url-listener [listener]
  (.addEventListener Linking "url" listener))

(defn share-text [text]
  (.share Share #js {:message text}))

(def permissions-android PermissionsAndroid)

(defn check-fine-location-android-permission [on-response]
  (-> permissions-android
      (.check (.-ACCESS_FINE_LOCATION (.-PERMISSIONS permissions-android)))
      (.then (fn [grated?]
               (when (fn? on-response)
                 (on-response grated?))))
      (.catch (fn [e]
                (prn e)))))

(defn request-fine-location-android-permission [on-response]
  (-> permissions-android
      (.request (.-ACCESS_FINE_LOCATION (.-PERMISSIONS permissions-android)))
      (.then (fn [response]
               (when (fn? on-response)
                 (on-response response))))
      (.catch (fn [e]
                (prn e)))))

(defn- fn-wrapper [f]
  (fn []
    (let [result (f)]
      (if (fn? result)
        result
        js/undefined))))

(defn use-effect
  ([f] (useEffect (fn-wrapper f)))
  ([f deps] (useEffect (fn-wrapper f) (clj->js deps))))

(defn use-callback [f deps]
  (useCallback f (to-array deps)))

(def use-state useState)

(defn use-memo [f deps]
  (useMemo f (to-array deps)))

(defn use-ref [initial-value]
  ^js (useRef initial-value))

(def back-handler BackHandler)

(defn use-pass-clj-data
  "Specific for Reagent"
  [coll]
  (use-memo #(with-meta coll {:keep-items true})
            [(hash coll)]))
