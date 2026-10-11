(ns xquo.demo.drawer.sheet
  (:require [applied-science.js-interop :as j]
            [react-native.react.core :as react]
            [react-native.react-navigation.native :as navigation]
            [react-native.react-navigation.navigation :as nav]
            [xquo.core :as xquo]
            [xquo.demo.assets :as assets]
            [xquo.demo.dapp.data :as dapp-data]))

(defn- pressed! [] (js/alert "Pressed"))

(defn options [props]
  (let [params (nav/->clj-nav-params (j/get-in props [:route :params]))]
    (xquo/drawer.screen-options (assoc (select-keys params [:theme :blur? :handle?])
                                       :detents     [:auto]
                                       :dismissible (:handle? params)
                                       :draggable   (:handle? params)))))

(defn- piece [{:keys [part blur? title description leading action-type two-buttons? description-position error?]
               :as   params}]
  (let [[selected? set-selected!] (react/use-state (:selected? params))
        [input-text set-input!]   (react/use-state "")
        {:keys [go-back]}         (navigation/use-navigation)
        close!                    (react/use-callback (fn [] (pressed!) (go-back)) [go-back])]
    (case part
      :top
      [xquo/drawer.top {:title        title
                        :compact?     (= (second (:pieces params)) :body)
                        :description  (when (not= leading :tags) description)
                        :content      (case leading
                                        :icon {:type :icon :icon {:name :icon/file}}
                                        :image {:type :image :image {:source assets/illustration}}
                                        :avatar {:type :avatar :avatar dapp-data/account-avatar}
                                        nil)
                        :context-tags (when (= leading :tags)
                                        [{:type :default :image-source assets/illustration :label "Aave" :on-press pressed!}
                                         "and" {:type :icon :icon {:name :icon/users} :label "Members" :on-press pressed!}])
                        :counter      (when (= leading :counter) "00/00")
                        :blur?        blur?}]

      :label
      [xquo/drawer.label {:label "Drawer label" :blur? blur?}]

      :body
      [xquo/drawer.body {:blur? blur?} description]

      :actions
      [xquo/drawer.actions {:blur? blur?
                            :items [{:title     "Action"
                                     :content   {:type :icon :icon {:name :icon/browser}}
                                     :action    action-type
                                     :selected? selected?
                                     :on-select set-selected!
                                     :input     {:value input-text :on-change-text set-input! :placeholder "Enter a value"}
                                     :on-press  pressed!}
                                    {:title       "Action with description"
                                     :description description
                                     :content     {:type :icon :icon {:name :icon/info}}
                                     :action      :arrow
                                     :on-press    pressed!}
                                    :divider
                                    {:title    "Remove"
                                     :danger?  true
                                     :content  {:type :icon :icon {:name :icon/trash}}
                                     :on-press pressed!}]}]

      :permissions-context
      [xquo/drawer.permissions-context {:blur? blur?}
       "Hold"
       [xquo/tag.asset {:image {:source assets/illustration} :label "10 ETH" :blur? blur? :on-press pressed!}]
       "to post"]

      :bottom-context
      [xquo/drawer.bottom-context {:blur? blur?
                                   :left  [{:icon {:name :icon/options} :on-press pressed!}]
                                   :right [{:icon {:name :icon/link} :on-press pressed!}
                                           {:icon {:name :icon/share} :on-press pressed!}]}]

      :bottom-actions
      [xquo/drawer.bottom-actions {:blur?       blur?
                                   :buttons     (cond-> []
                                                  two-buttons? (conj {:label "Cancel" :on-press close!})
                                                  :always (conj {:label    (if (:handle? params) "Request to join" "Close")
                                                                 :on-press (if (:handle? params) pressed! close!)}))
                                   :description (when description-position
                                                  {:position description-position
                                                   :status   (when error? :error)
                                                   :text     (cond
                                                               error?
                                                               "Requirements are not met"

                                                               (= description-position :top)
                                                               "Eligible to join as"

                                                               :else
                                                               "Joining the community will reveal your public addresses to the node owner")})
                                   :context-tag (when (and (= description-position :top) (not error?))
                                                  {:type :icon :icon {:name :icon/users} :label "Member" :on-press pressed!})}])))

(defn screen [{params :route-params}]
  (let [pieces (cond-> (:pieces params)
                 (and (not (:handle? params))
                      (not (some #{:bottom-actions} (:pieces params))))
                 (conj :bottom-actions))]
    (into [xquo/drawer.content (select-keys params [:theme :blur? :handle?])]
          (map (fn [part]
                 [piece (assoc params :part part)]))
          pieces)))
