(ns xquo.demo.step
  (:require [react-native.react.core :as react]
            [xquo.context :as context]
            [xquo.core :as xquo]
            [xquo.demo.controls :as controls]
            [xquo.demo.screen :as screen]))

(defn screen [_]
  (let [[value set-value!] (react/use-state "1")
        [type set-type!]   (react/use-state :neutral)
        [color set-color!] (react/use-state nil)
        [blur? set-blur!]  (react/use-state false)]
    [context/provider {:color color}
     [screen/screen {:title   "Step"
                     :blur?   blur?
                     :preview [xquo/step {:type  type
                                          :blur? blur?
                                          :style {:align-self :center}}
                               value]}
      [xquo/input {:label "value" :value value :on-change-text set-value!}]
      [controls/choice {:label     ":type"
                        :value     type
                        :on-change set-type!
                        :options   (map (fn [type]
                                          {:value type :label (str type)})
                                        [:neutral :complete :active])}]
      [controls/choice {:label     "context :color"
                        :value     color
                        :on-change set-color!
                        :options   [{:value nil :label "Inherited"}
                                    {:value :color/primary :label ":color/primary"}
                                    {:value :color/success :label ":color/success"}
                                    {:value :color/warning :label ":color/warning"}
                                    {:value :color/danger :label ":color/danger"}]}]
      [controls/checkbox {:label ":blur?" :value blur? :on-change set-blur!}]]]))
