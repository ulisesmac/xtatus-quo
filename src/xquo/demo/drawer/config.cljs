(ns xquo.demo.drawer.config
  (:require [react-native.core :as rn]
            [react-native.react.core :as react]
            [react-native.react-navigation.native :as navigation]
            [react-native.react-navigation.navigation :as nav]
            [xquo.core :as xquo]
            [xquo.context :as context]
            [xquo.demo.controls :as controls]
            [xquo.demo.screen :as screen]))

(def ^:private parts
  [:top :label :body :actions :permissions-context :bottom-context :bottom-actions])

(defn- part-control [{:keys [part pieces set-pieces!]}]
  (let [selected? (contains? pieces part)
        change!   (react/use-callback (fn [value]
                                        (let [update-pieces (if value conj disj)]
                                          (set-pieces! (fn [current]
                                                         (update-pieces current part)))))
                                      [part set-pieces!])]
    [controls/checkbox {:label (str "drawer." (name part)) :value selected? :on-change change!}]))

(defn screen [_]
  (let [device-theme                         (rn/use-color-scheme)
        {:keys [dark-theme?]}                (context/use-theme-color device-theme)
        [mode set-mode!]                     (react/use-state (if dark-theme? :dark :light))
        [pieces set-pieces!]                 (react/use-state #{:top :body :actions :bottom-actions})
        [title set-title!]                   (react/use-state "Drawer title")
        [description set-description!]       (react/use-state "Combine the pieces to build your drawer.")
        [leading set-leading!]               (react/use-state nil)
        [action-type set-action-type!]       (react/use-state :arrow)
        [selected? set-selected!]            (react/use-state false)
        [two-buttons? set-two-buttons!]      (react/use-state true)
        [description-position set-position!] (react/use-state :bottom)
        [error? set-error!]                  (react/use-state false)
        [handle? set-handle!]                (react/use-state true)
        {:keys [navigate]}                   (navigation/use-navigation)
        show!                                (react/use-callback (fn []
                                                                   (navigate "xquo-demo.drawer-sheet"
                                                                             (nav/->js-nav-params {:theme                (if (= mode :light) :theme/light :theme/dark)
                                                                                                   :blur?                (= mode :dark-blur)
                                                                                                   :pieces               (filterv pieces parts)
                                                                                                   :title                title
                                                                                                   :description          description
                                                                                                   :leading              leading
                                                                                                   :action-type          action-type
                                                                                                   :selected?            selected?
                                                                                                   :two-buttons?         two-buttons?
                                                                                                   :description-position description-position
                                                                                                   :error?               error?
                                                                                                   :handle?              handle?})))
                                                                 [navigate mode pieces title description leading action-type selected? two-buttons?
                                                                  description-position error? handle?])]
    [screen/screen {:title   "Drawer"
                    :photo?  true
                    :preview [xquo/button {:on-press show! :style {:align-self :center}} "Show drawer"]}
     [controls/choice {:label     "Appearance"
                       :value     mode
                       :on-change set-mode!
                       :options   [{:value :light :label "Light"}
                                   {:value :dark :label "Dark"}
                                   {:value :dark-blur :label "Dark + blur"}]}]
     (into [:rn/view]
           (map (fn [part]
                  [part-control {:part part :pieces pieces :set-pieces! set-pieces!}]))
           parts)
     [xquo/input {:label ":title" :value title :on-change-text set-title!}]
     [xquo/input {:label ":description" :value description :on-change-text set-description!}]
     [controls/choice {:label     "Top content"
                       :value     leading
                       :on-change set-leading!
                       :options   [{:value nil :label "Text"} {:value :icon :label "Icon"}
                                   {:value :image :label "Image"} {:value :avatar :label "Avatar"}
                                   {:value :tags :label "Context tags"} {:value :counter :label "Counter"}]}]
     [controls/choice {:label     "Action"
                       :value     action-type
                       :on-change set-action-type!
                       :options   [{:value nil :label "nil"} {:value :arrow :label "Arrow"}
                                   {:value :toggle :label "Toggle"} {:value :input :label "Input"}]}]
     [controls/checkbox {:label ":selected?" :value selected? :on-change set-selected!}]
     [controls/checkbox {:label "Two bottom buttons" :value two-buttons? :on-change set-two-buttons!}]
     [controls/choice {:label     "Bottom description"
                       :value     description-position
                       :on-change set-position!
                       :options   [{:value nil :label "nil"} {:value :top :label "Top"} {:value :bottom :label "Bottom"}]}]
     [controls/checkbox {:label "Description error" :value error? :on-change set-error!}]
     [controls/checkbox {:label "Handle" :value handle? :on-change set-handle!}]]))
