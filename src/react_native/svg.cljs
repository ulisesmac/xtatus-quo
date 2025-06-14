(ns react-native.svg
  (:require
    ["react-native-svg" :as Svg]
    [reagent.core :as reagent]))

(def svg (reagent/adapt-react-class Svg/default))
(def path (reagent/adapt-react-class Svg/Path))
(def rect (reagent/adapt-react-class Svg/Rect))
(def clip-path (reagent/adapt-react-class Svg/ClipPath))
(def defs (reagent/adapt-react-class Svg/Defs))
(def circle (reagent/adapt-react-class Svg/Circle))
(def svg-xml (reagent/adapt-react-class Svg/SvgXml))
(def linear-gradient (reagent/adapt-react-class Svg/LinearGradient))
(def stop (reagent/adapt-react-class Svg/Stop))
