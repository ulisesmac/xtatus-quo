(ns xquo.core
  (:require [xquo.components.text.view]
            [xquo.context]))

(def text xquo.components.text.view/text)
(def provider xquo.context/provider)
(def use-theme xquo.context/use-theme)
