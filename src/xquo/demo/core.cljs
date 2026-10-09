(ns xquo.demo.core
  (:require [applied-science.js-interop :as j]
            [react-native.react.core :as react]
            [react-native.react-navigation.native :as navigation]
            [react-native.react-navigation.native-stack :as native-stack]
            [xquo.core :as xquo]
            [xquo.demo.avatar :as avatar]
            [xquo.demo.button :as button]
            [xquo.demo.counter :as counter]
            [xquo.demo.divider-label :as divider-label]
            [xquo.demo.divider-line :as divider-line]
            [xquo.demo.divider-new :as divider-new]
            [xquo.demo.divider-strength :as divider-strength]
            [xquo.demo.step :as step]
            [xquo.demo.screen :as screen]
            [xquo.demo.option-card :as option-card]))

(defn- index [_]
  (let [{:keys [navigate]}     (navigation/use-navigation)
        open-card!             (react/use-callback #(navigate "xquo-demo.option-card") [navigate])
        open-avatar!           (react/use-callback #(navigate "xquo-demo.avatar") [navigate])
        open-button!           (react/use-callback #(navigate "xquo-demo.button") [navigate])
        open-counter!          (react/use-callback #(navigate "xquo-demo.counter") [navigate])
        open-step!             (react/use-callback #(navigate "xquo-demo.step") [navigate])
        open-divider-label!    (react/use-callback #(navigate "xquo-demo.divider-label") [navigate])
        open-divider-line!     (react/use-callback #(navigate "xquo-demo.divider-line") [navigate])
        open-divider-new!      (react/use-callback #(navigate "xquo-demo.divider-new") [navigate])
        open-divider-strength! (react/use-callback #(navigate "xquo-demo.divider-strength") [navigate])]
    [screen/screen {:title "Xquo components"}
     [xquo/settings-item {:title    "Avatar"
                          :image    {:type :none}
                          :right    {:action {:type :arrow}}
                          :on-press open-avatar!}]
     [xquo/settings-item {:title    "Button"
                          :image    {:type :none}
                          :right    {:action {:type :arrow}}
                          :on-press open-button!}]
     [xquo/settings-item {:title    "Counter"
                          :image    {:type :none}
                          :right    {:action {:type :arrow}}
                          :on-press open-counter!}]
     [xquo/settings-item {:title    "Divider label"
                          :image    {:type :none}
                          :right    {:action {:type :arrow}}
                          :on-press open-divider-label!}]
     [xquo/settings-item {:title    "Divider line"
                          :image    {:type :none}
                          :right    {:action {:type :arrow}}
                          :on-press open-divider-line!}]
     [xquo/settings-item {:title    "Divider new"
                          :image    {:type :none}
                          :right    {:action {:type :arrow}}
                          :on-press open-divider-new!}]
     [xquo/settings-item {:title    "Divider strength"
                          :image    {:type :none}
                          :right    {:action {:type :arrow}}
                          :on-press open-divider-strength!}]
     [xquo/settings-item {:title    "Option card"
                          :image    {:type :none}
                          :right    {:action {:type :arrow}}
                          :on-press open-card!}]
     [xquo/settings-item {:title    "Step"
                          :image    {:type :none}
                          :right    {:action {:type :arrow}}
                          :on-press open-step!}]]))

(def ^:private navigator
  (j/call (native-stack/create-native-stack-navigator
           {:initial-route-name :screen/xquo-demo.index
            :screen-options     {:header-shown false}
            :screens            {:screen/xquo-demo.index            #'index
                                 :screen/xquo-demo.button           #'button/screen
                                 :screen/xquo-demo.counter          #'counter/screen
                                 :screen/xquo-demo.divider-label    #'divider-label/screen
                                 :screen/xquo-demo.divider-line     #'divider-line/screen
                                 :screen/xquo-demo.divider-new      #'divider-new/screen
                                 :screen/xquo-demo.divider-strength #'divider-strength/screen
                                 :screen/xquo-demo.step             #'step/screen
                                 :screen/xquo-demo.avatar           #'avatar/screen
                                 :screen/xquo-demo.option-card      #'option-card/screen}})
          :getComponent))

(defn screen [_]
  [:> navigator])
