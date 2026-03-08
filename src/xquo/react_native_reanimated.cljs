(ns xquo.react-native-reanimated
  (:require ["react-native-reanimated"
             :refer [LinearTransition SlideInUp SlideOutUp
                     useSharedValue withTiming withSpring
                     FadeIn FadeOut
                     FadeInUp]
             :as reanimated
             :default Animated]
            [applied-science.js-interop :as j])
  (:refer-clojure :exclude [set get]))

(def linear-transition LinearTransition)
(def slide-in-up SlideInUp)
(def slide-out-up SlideOutUp)

(def fade-in FadeIn)
(def fade-in-up FadeInUp)
(def fade-out FadeOut)

(def use-shared-value useSharedValue)

(def appear-in-duration 220)
(def disappear-out-duration 120)

(defn get [shared-value]
  (j/call shared-value :get))

(defn set [shared-value v]
  (j/call shared-value :set v))

(def with-timing withTiming)
(def with-spring withSpring)

(defn appear-in []
  (j/call fade-in :duration appear-in-duration))

(defn disappear-out []
  (j/call fade-out :duration disappear-out-duration))
