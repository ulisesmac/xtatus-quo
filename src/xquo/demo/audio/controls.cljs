(ns xquo.demo.audio.controls
  (:require [xquo.demo.controls :as controls]))

(defn progress [{:keys [value on-change]}]
  [controls/choice {:label     ":progress"
                    :value     value
                    :on-change on-change
                    :options   [{:value 0 :label "0"}
                                {:value (/ 20 164) :label "0.122"}
                                {:value 0.25 :label "0.25"}
                                {:value 0.5 :label "0.5"}
                                {:value (/ 155 164) :label "0.945"}
                                {:value 1 :label "1"}]}])

(defn color [{:keys [value on-change]}]
  [controls/choice {:label     "context :color"
                    :value     value
                    :on-change on-change
                    :options   [{:value nil :label "Inherited"}
                                {:value :color/primary :label ":color/primary"}
                                {:value :color/success :label ":color/success"}
                                {:value :color/warning :label ":color/warning"}
                                {:value :color/danger :label ":color/danger"}]}])
