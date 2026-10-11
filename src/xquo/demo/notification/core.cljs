(ns xquo.demo.notification.core
  (:require ["@lodev09/react-native-true-sheet" :refer [TrueSheet]]
            [applied-science.js-interop :as j]
            [react-native.core :as rn]
            [react-native.react.core :as react]
            [react-native.react-navigation.native :as navigation]
            [react-native.safe-area-context.core :as safe-area]
            [react-native.utils :refer [asset-require]]
            [reagent.core :as r]
            [xquo.context :as context]
            [xquo.core :as xquo]
            [xquo.demo.assets :as assets]
            [xquo.demo.controls :as controls]
            [xquo.demo.notification.style :as style]
            [xquo.demo.style :as demo-style]))

(def ^:private mountains (asset-require "xquo-demo/mountains.png"))

(defn- pressed! [] (js/alert "Pressed"))

(defn- notification [{:keys [kind type message title description leading attachment action?]}]
  (if (= kind :toast)
    [xquo/notification.toast {:type   type
                              :button (when action?
                                        {:label "Undo" :icon {:name :icon/timeout :side :left} :on-press pressed!})}
     message]
    [xquo/notification.in-app {:title       title
                               :description (when (seq description) description)
                               :content     (case leading
                                              :icon {:type :icon :icon {:name :icon/face-id}}
                                              :initials {:type :avatar :avatar {:variant :solid :content {:type :initials :text "AY"}}}
                                              :image {:type   :avatar
                                                      :avatar {:content {:type :image :image {:source assets/profile}}
                                                               :badge   {:status :online}}})
                               :attachment  (case attachment
                                              :photos {:type :photos :images [{:source mountains} {:source assets/illustration}]}
                                              :sticker {:type :sticker :image {:source assets/illustration}}
                                              nil)
                               :buttons     (when action? [{:label "Undo" :on-press pressed!}])
                               :on-press    pressed!}]))

(defn- blur-preview [{:keys [options]}]
  [:rn/view {:style style/blur-preview}
   [:rn/scroll-view {:content-container-style style/scene}
    [:rn/view {:style style/photos}
     [:rn/image {:source mountains :resize-mode :cover :style style/photo}]
     [:rn/image {:source assets/profile :resize-mode :cover :style style/photo}]]
    [xquo/simple-item {:image       {:source assets/illustration}
                      :title       "Weekend plans"
                      :description "Photos, messages, and a little color."}]
    [:rn/view {:style style/swatches}
     [xquo/text {:font :font/semibold-19 :style (style/swatch :color/primary)} "Aa"]
     [xquo/text {:font :font/semibold-19 :style (style/swatch :color/success)} "123"]
     [xquo/text {:font :font/semibold-19 :style (style/swatch :color/danger)} "Hello"]]
    [xquo/simple-item {:image       {:source assets/profile}
                      :title       "See you tomorrow"
                      :description "Let's meet at the park at 10:30."}]
    [:rn/image {:source mountains :resize-mode :cover :style style/landscape}]]
   [:rn/view {:pointer-events :box-none :style style/preview-notification}
    [notification options]]])

(defn- trigger [{:keys [options duration]}]
  (let [{:keys [show! dismiss!]} (xquo/notification.use-notifications)
        [last-id set-last-id!]   (react/use-state nil)
        show!                    (react/use-callback (fn []
                                                       (set-last-id! (show! [notification options] {:duration duration})))
                                                     [show! options duration])
        dismiss!                 (react/use-callback #(dismiss! last-id) [dismiss! last-id])]
    [:rn/view {:style {:gap 12}}
     [xquo/button {:on-press show!} "Show notification"]
     [xquo/button {:type :grey :on-press dismiss! :disabled? (nil? last-id)} "Dismiss latest"]]))

(defn- screen [{:keys [kind]}]
  (let [top                            (safe-area/use-top)
        bottom                         (safe-area/use-bottom)
        device-theme                   (rn/use-color-scheme)
        [selected-theme set-theme!]    (react/use-state nil)
        {:keys [theme dark-theme?]}    (context/use-theme-color (or selected-theme device-theme))
        [shell? set-shell!]            (react/use-state (= kind :in-app))
        [type set-type!]               (react/use-state :positive)
        [message set-message!]         (react/use-state "Custom name updated")
        [title set-title!]             (react/use-state "Alisher Yakupov")
        [description set-description!] (react/use-state "The quick brown fox jumped over the lazy dog and ate a potatoe.")
        [leading set-leading!]         (react/use-state :image)
        [attachment set-attachment!]   (react/use-state nil)
        [action? set-action!]          (react/use-state false)
        [duration set-duration!]       (react/use-state 5000)
        options                        {:kind        kind
                                        :type        type
                                        :message     message
                                        :title       title
                                        :description description
                                        :leading     leading
                                        :attachment  attachment
                                        :action?     action?}
        preview-theme                  (if shell? :theme/dark theme)
        sheet-ref                      (react/use-ref nil)
        open-drawer!                   (react/use-callback #(j/call (j/get sheet-ref :current) :present) [sheet-ref])
        close-drawer!                  (react/use-callback #(j/call (j/get sheet-ref :current) :dismiss) [sheet-ref])
        switch-theme!                  (react/use-callback #(set-theme! (if dark-theme? :theme/light :theme/dark)) [dark-theme?])
        profile!                       (react/use-callback (fn [] (set-message! "Custom name updated") (set-type! :positive)) [])
        onboarding!                    (react/use-callback (fn [] (set-message! "This is not a pairing QR code") (set-type! :negative)) [])
        {:keys [go-back]}              (navigation/use-navigation)]
    [context/provider {:theme theme}
     [:rn/view {:style (demo-style/root theme top bottom)}
      (when shell?
        [:rn/view {:pointer-events :none :style style/background}
         [:rn/image {:source mountains :resize-mode :cover :style style/fill}]
         [:effect/view {:effect :blur :blur-amount 40 :theme :theme/dark :style style/shell}]])
      [context/provider {:theme preview-theme}
       [xquo/page-nav {:background :none
                       :left       {:icon {:name :icon/arrow-left} :on-press go-back}
                       :right      [{:icon {:name (if dark-theme? :icon/light :icon/dark)} :on-press switch-theme!}]
                       :center     {:type :title :title (if (= kind :toast) "Notification toast" "Notification in-app")}}]
       [:rn/view {:style style/preview}
        (if (= kind :toast)
          [blur-preview {:options options}]
          [:<>
           [trigger {:options options :duration duration}]
           [xquo/button {:type :grey :on-press open-drawer!} "Open drawer"]])]]
      [:rn/scroll-view {:style                        (demo-style/panel theme)
                        :content-container-style      style/controls
                        :keyboard-should-persist-taps :handled}
       (when (= kind :toast)
         [:<>
          [xquo/text {:font :font/regular-13}
           "Scroll the photos and text beneath the toast to compare the blur."]
          [context/provider {:theme preview-theme}
           [trigger {:options options :duration duration}]
           [xquo/button {:type :grey :on-press open-drawer!} "Open drawer"]]])
       [controls/checkbox {:label "Shell background" :value shell? :on-change set-shell!}]
       [controls/choice {:label     "Dismiss after release"
                         :value     duration
                         :on-change set-duration!
                         :options   [{:value 1500 :label "1.5 seconds"}
                                     {:value 5000 :label "5 seconds"}
                                     {:value nil :label "Persistent"}]}]
       (if (= kind :toast)
         [:<>
          [:rn/view {:style demo-style/choices}
           [xquo/button {:size 32 :type :grey :on-press profile!} "Profile example"]
           [xquo/button {:size 32 :type :grey :on-press onboarding!} "Onboarding example"]]
          [controls/choice {:label     "Type"
                            :value     type
                            :on-change set-type!
                            :options   [{:value :positive :label "Positive"}
                                        {:value :negative :label "Negative"}
                                        {:value :neutral :label "Neutral"}]}]
          [xquo/input {:label "Message" :value message :on-change-text set-message!}]]
         [:<>
          [xquo/input {:label "Title" :value title :on-change-text set-title!}]
          [xquo/input {:label "Description" :value description :on-change-text set-description! :multiline true}]
          [controls/choice {:label     "Content"
                            :value     leading
                            :on-change set-leading!
                            :options   [{:value :image :label "Image"}
                                        {:value :initials :label "Initials"}
                                        {:value :icon :label "Icon"}]}]
          [controls/choice {:label     "Attachment"
                            :value     attachment
                            :on-change set-attachment!
                            :options   [{:value nil :label "None"}
                                        {:value :photos :label "Photos"}
                                        {:value :sticker :label "Sticker"}]}]])
       [controls/checkbox {:label "Action button" :value action? :on-change set-action!}]]
      (r/create-element TrueSheet
                        (j/assoc! (xquo/drawer.screen-options {:theme preview-theme :blur? shell?}) :ref sheet-ref)
                        (r/as-element [xquo/drawer.content {:theme preview-theme :blur? shell?}
                                       [xquo/drawer.top {:title "Notifications above drawers" :blur? shell?}]
                                       [xquo/drawer.body {:blur? shell?}
                                        "Show several notifications, then drag one over this drawer. Hold it past its timeout; release to restart the timer."]
                                       [xquo/drawer.body {:blur? shell?}
                                        [trigger {:options options :duration duration}]]
                                       [xquo/drawer.bottom-actions {:blur?   shell?
                                                                    :buttons [{:label "Close drawer" :on-press close-drawer!}]}]]))]]))

(defn toast-screen [_]
  [screen {:kind :toast}])

(defn in-app-screen [_]
  [screen {:kind :in-app}])
