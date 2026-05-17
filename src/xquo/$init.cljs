(ns xquo.$init
  (:require [applied-science.js-interop :as j]
            ["react-native" :as react-native]
            ["react-native-gesture-handler" :as gesture-handler]
            ["react-native-reanimated" :default Animated]
            ["react-native-safe-area-context" :as safe-area-context]
            ["react-native-svg" :as svg]))

(def effect (js/require "xquo/effect"))

(def animated-svg (.createAnimatedComponent Animated (.-Svg svg)))
(def animated-pressable (.createAnimatedComponent Animated (.-Pressable react-native)))
(def animated-effect-view (.createAnimatedComponent Animated (.-View effect)))
(def animated-effect-pressable (.createAnimatedComponent Animated (.-Pressable effect)))

(def js-component-libs
  {:root      react-native
   :rn        react-native
   :gh        gesture-handler
   :safe-area safe-area-context
   :animated  (j/assoc! Animated
                          :Svg animated-svg
                          :Pressable animated-pressable
                          :EffectView animated-effect-view
                          :EffectPressable animated-effect-pressable)
   :svg       svg
   :effect    effect})

(def compiler-props
  {:function-components         true
   :convert-props-in-vectors    #{:style :content-container-style :transform}
   :js-component-libs           js-component-libs
   :kebab-case-component-names? true})
