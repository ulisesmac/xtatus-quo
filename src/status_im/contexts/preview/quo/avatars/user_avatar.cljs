(ns status-im.contexts.preview.quo.avatars.user-avatar
  (:require
    [quo.core :as quo]
    [reagent.core :as reagent]
    [status-im.common.resources :as resources]
    [status-im.contexts.preview.quo.preview :as preview]))

(def descriptor
  [{:key     :size
    :type    :select
    :options [{:key :big}
              {:key :size-64}
              {:key :medium}
              {:key :small}
              {:key :xs}
              {:key :xxs}
              {:key :xxxs}]}
   (preview/customization-color-option)
   {:key  :online?
    :type :boolean}
   {:key  :status-indicator?
    :type :boolean}
   {:key  :full-name
    :type :text}
   {:key     :profile-picture
    :type    :select
    :options [{:value "None"
               :key   nil}
              {:value "Alicia Keys"
               :key   (resources/get-mock-image :user-picture-female2)}
              {:value "pedro.eth"
               :key   (resources/get-mock-image :user-picture-male4)}]}])

(defn view
  []
  (let [state (reagent/atom {:full-name           "A Y"
                             :status-indicator?   true
                             :online?             true
                             :size                :medium
                             :customization-color :blue})]
    (fn []
      [preview/preview-container {:state state :descriptor descriptor}
       [quo/user-avatar @state]])))
