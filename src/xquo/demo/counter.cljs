(ns xquo.demo.counter
  (:require [react-native.react.core :as react]
            [xquo.context :as context]
            [xquo.core :as xquo]
            [xquo.demo.controls :as controls]
            [xquo.demo.screen :as screen]))

(defn screen [_]
  (let [[value set-value!] (react/use-state "5")
        [type set-type!]   (react/use-state :default)
        [size set-size!]   (react/use-state :default)
        [color set-color!] (react/use-state nil)
        [blur? set-blur!]  (react/use-state false)]
    [context/provider {:color color}
     [screen/screen {:title   "Counter"
                     :blur?   blur?
                     :preview [xquo/counter {:type  type
                                             :size  size
                                             :blur? blur?
                                             :style {:align-self :center}}
                               value]}
      [xquo/input {:label "value" :value value :on-change-text set-value!}]
      [controls/choice {:label     ":type"
                        :value     type
                        :on-change set-type!
                        :options   (map (fn [type]
                                          {:value type :label (str type)})
                                        [:default :secondary :grey :outline :outline-color :warning :error])}]
      [controls/choice {:label     ":size"
                        :value     size
                        :on-change set-size!
                        :options   [{:value :default :label ":default"}
                                    {:value :large :label ":large"}]}]
      [controls/choice {:label     "context :color"
                        :value     color
                        :on-change set-color!
                        :options   [{:value nil :label "Inherited"}
                                    {:value :color/primary :label ":color/primary"}
                                    {:value :color/success :label ":color/success"}
                                    {:value :color/warning :label ":color/warning"}
                                    {:value :color/danger :label ":color/danger"}]}]
      [controls/checkbox {:label ":blur?" :value blur? :on-change set-blur!}]]]))
