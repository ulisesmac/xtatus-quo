(ns xquo.demo.core
  (:require [applied-science.js-interop :as j]
            [react-native.react.core :as react]
            [react-native.react-navigation.native :as navigation]
            [react-native.react-navigation.native-stack :as native-stack]
            [xquo.core :as xquo]
            [xquo.demo.audio.player :as audio-player]
            [xquo.demo.audio.soundtrack :as audio-soundtrack]
            [xquo.demo.audio.recorder :as audio-recorder]
            [xquo.demo.favicon :as favicon]
            [xquo.demo.browser.dapp-item :as browser-dapp-item]
            [xquo.demo.browser.favorites :as browser-favorites]
            [xquo.demo.browser.new-tab :as browser-new-tab]
            [xquo.demo.browser.input :as browser-input]
            [xquo.demo.browser.navigation :as browser-navigation]
            [xquo.demo.banner.standard :as banner-standard]
            [xquo.demo.banner.alert :as banner-alert]
            [xquo.demo.banner.global :as banner-global]
            [xquo.demo.calendar.day :as calendar-day]
            [xquo.demo.calendar.year :as calendar-year]
            [xquo.demo.calendar.picker :as calendar-picker]
            [xquo.demo.camera.shutter :as camera-shutter]
            [xquo.demo.camera.controls :as camera-controls]
            [xquo.demo.camera.crop :as camera-crop]
            [xquo.demo.card.banner :as card-banner]
            [xquo.demo.card.wallet :as card-wallet]
            [xquo.demo.segmented-tab :as segmented-tab]
            [xquo.demo.tag.standard :as tag-standard]
            [xquo.demo.tag.permissions :as tag-permissions]
            [xquo.demo.tag.asset :as tag-asset]
            [xquo.demo.community.stat :as community-stat]
            [xquo.demo.community.role :as community-role]
            [xquo.demo.community.channel-action :as community-channel-action]
            [xquo.demo.community.channel-actions :as community-channel-actions]
            [xquo.demo.community.main-card :as community-main-card]
            [xquo.demo.community.token-gating :as community-token-gating]
            [xquo.demo.dapp.list-card :as dapp-list-card]
            [xquo.demo.dapp.group :as dapp-group]
            [xquo.demo.dapp.approvals :as dapp-approvals]
            [xquo.demo.dapp.activity :as dapp-activity]
            [xquo.demo.drawer.core :as drawer]
            [xquo.demo.dropdown :as dropdown]
            [xquo.demo.empty-state :as empty-state]
            [xquo.demo.info :as info]
            [xquo.demo.input :as input]
            [xquo.demo.text-combinations :as text-combinations]
            [xquo.demo.search-input :as search-input]
            [xquo.demo.profile-input :as profile-input]
            [xquo.demo.profile.card :as profile-card]
            [xquo.demo.profile.tile :as profile-tile]
            [xquo.demo.profile.showcase-card :as showcase-card]
            [xquo.demo.list-item :as list-item]
            [xquo.demo.navigation.bottom-nav-tab :as bottom-nav-tab]
            [xquo.demo.navigation.bottom-nav :as bottom-nav]
            [xquo.demo.navigation.page-nav :as page-nav]
            [xquo.demo.markdown.list-item :as markdown-list-item]
            [xquo.demo.markdown.text :as markdown-text]
            [xquo.demo.profile.link-card :as link-card]
            [xquo.demo.link-preview :as link-preview]
            [xquo.demo.url-preview :as url-preview]
            [xquo.demo.notification.core :as notification]
            [xquo.demo.avatar :as avatar]
            [xquo.demo.button :as button]
            [xquo.demo.counter :as counter]
            [xquo.demo.divider-label :as divider-label]
            [xquo.demo.divider-line :as divider-line]
            [xquo.demo.divider-new :as divider-new]
            [xquo.demo.divider-strength :as divider-strength]
            [xquo.demo.step :as step]
            [xquo.demo.selectors :as selectors]
            [xquo.demo.screen :as screen]
            [xquo.demo.option-card :as option-card]))

(defn- index [_]
  (let [{:keys [navigate]}              (navigation/use-navigation)
        open-audio-player!              (react/use-callback #(navigate "xquo-demo.audio-player") [navigate])
        open-audio-soundtrack!          (react/use-callback #(navigate "xquo-demo.audio-soundtrack") [navigate])
        open-audio-recorder!            (react/use-callback #(navigate "xquo-demo.audio-recorder") [navigate])
        open-favicon!                   (react/use-callback #(navigate "xquo-demo.favicon") [navigate])
        open-browser-dapp-item!         (react/use-callback #(navigate "xquo-demo.browser-dapp-item") [navigate])
        open-browser-favorites!         (react/use-callback #(navigate "xquo-demo.browser-favorites") [navigate])
        open-browser-new-tab!           (react/use-callback #(navigate "xquo-demo.browser-new-tab") [navigate])
        open-browser-input!             (react/use-callback #(navigate "xquo-demo.browser-input") [navigate])
        open-browser-navigation!        (react/use-callback #(navigate "xquo-demo.browser-navigation") [navigate])
        open-banner-standard!           (react/use-callback #(navigate "xquo-demo.banner-standard") [navigate])
        open-banner-alert!              (react/use-callback #(navigate "xquo-demo.banner-alert") [navigate])
        open-banner-global!             (react/use-callback #(navigate "xquo-demo.banner-global") [navigate])
        open-calendar-day!              (react/use-callback #(navigate "xquo-demo.calendar-day") [navigate])
        open-calendar-year!             (react/use-callback #(navigate "xquo-demo.calendar-year") [navigate])
        open-calendar-picker!           (react/use-callback #(navigate "xquo-demo.calendar-picker") [navigate])
        open-camera-shutter!            (react/use-callback #(navigate "xquo-demo.camera-shutter") [navigate])
        open-camera-controls!           (react/use-callback #(navigate "xquo-demo.camera-controls") [navigate])
        open-camera-crop!               (react/use-callback #(navigate "xquo-demo.camera-crop") [navigate])
        open-card-banner!               (react/use-callback #(navigate "xquo-demo.card-banner") [navigate])
        open-card-wallet!               (react/use-callback #(navigate "xquo-demo.card-wallet") [navigate])
        open-segmented-tab!             (react/use-callback #(navigate "xquo-demo.segmented-tab") [navigate])
        open-tag-standard!              (react/use-callback #(navigate "xquo-demo.tag-standard") [navigate])
        open-tag-permissions!           (react/use-callback #(navigate "xquo-demo.tag-permissions") [navigate])
        open-tag-asset!                 (react/use-callback #(navigate "xquo-demo.tag-asset") [navigate])
        open-community-stat!            (react/use-callback #(navigate "xquo-demo.community-stat") [navigate])
        open-community-role!            (react/use-callback #(navigate "xquo-demo.community-role") [navigate])
        open-community-channel-action!  (react/use-callback #(navigate "xquo-demo.community-channel-action") [navigate])
        open-community-channel-actions! (react/use-callback #(navigate "xquo-demo.community-channel-actions") [navigate])
        open-community-main-card!       (react/use-callback #(navigate "xquo-demo.community-main-card") [navigate])
        open-community-token-gating!    (react/use-callback #(navigate "xquo-demo.community-token-gating") [navigate])
        open-dapp-list-card!            (react/use-callback #(navigate "xquo-demo.dapp-list-card") [navigate])
        open-dapp-group!                (react/use-callback #(navigate "xquo-demo.dapp-group") [navigate])
        open-dapp-approvals!            (react/use-callback #(navigate "xquo-demo.dapp-approvals") [navigate])
        open-dapp-activity!             (react/use-callback #(navigate "xquo-demo.dapp-activity") [navigate])
        open-drawer!                    (react/use-callback #(navigate "xquo-demo.drawer") [navigate])
        open-dropdown-button!           (react/use-callback #(navigate "xquo-demo.dropdown-button") [navigate])
        open-dropdown-input!            (react/use-callback #(navigate "xquo-demo.dropdown-input") [navigate])
        open-empty-state!               (react/use-callback #(navigate "xquo-demo.empty-state") [navigate])
        open-info-message!              (react/use-callback #(navigate "xquo-demo.info-message") [navigate])
        open-information-box!           (react/use-callback #(navigate "xquo-demo.information-box") [navigate])
        open-text-combinations!         (react/use-callback #(navigate "xquo-demo.text-combinations") [navigate])
        open-input!                     (react/use-callback #(navigate "xquo-demo.input") [navigate])
        open-search-input!              (react/use-callback #(navigate "xquo-demo.search-input") [navigate])
        open-profile-input!             (react/use-callback #(navigate "xquo-demo.profile-input") [navigate])
        open-profile-card!              (react/use-callback #(navigate "xquo-demo.profile-card") [navigate])
        open-showcase-card!             (react/use-callback #(navigate "xquo-demo.showcase-card") [navigate])
        open-profile-tile!              (react/use-callback #(navigate "xquo-demo.profile-tile") [navigate])
        open-list-info-box!             (react/use-callback #(navigate "xquo-demo.list-info-box") [navigate])
        open-link-card!                 (react/use-callback #(navigate "xquo-demo.link-card") [navigate])
        open-link-preview!             (react/use-callback #(navigate "xquo-demo.link-preview") [navigate])
        open-url-preview!              (react/use-callback #(navigate "xquo-demo.url-preview") [navigate])
        open-list-item!                 (react/use-callback #(navigate "xquo-demo.list-item") [navigate])
        open-bottom-nav-tab!           (react/use-callback #(navigate "xquo-demo.bottom-nav-tab") [navigate])
        open-bottom-nav!               (react/use-callback #(navigate "xquo-demo.bottom-nav") [navigate])
        open-page-nav!                 (react/use-callback #(navigate "xquo-demo.page-nav") [navigate])
        open-markdown-list-item!        (react/use-callback #(navigate "xquo-demo.markdown-list-item") [navigate])
        open-markdown-text!             (react/use-callback #(navigate "xquo-demo.markdown-text") [navigate])
        open-notification-toast!        (react/use-callback #(navigate "xquo-demo.notification-toast") [navigate])
        open-notification-in-app!       (react/use-callback #(navigate "xquo-demo.notification-in-app") [navigate])
        open-card!                      (react/use-callback #(navigate "xquo-demo.option-card") [navigate])
        open-avatar!                    (react/use-callback #(navigate "xquo-demo.avatar") [navigate])
        open-button!                    (react/use-callback #(navigate "xquo-demo.button") [navigate])
        open-counter!                   (react/use-callback #(navigate "xquo-demo.counter") [navigate])
        open-selector!                 (react/use-callback #(navigate "xquo-demo.selector") [navigate])
        open-disclaimer!               (react/use-callback #(navigate "xquo-demo.disclaimer") [navigate])
        open-filter!                   (react/use-callback #(navigate "xquo-demo.filter") [navigate])
        open-step!                      (react/use-callback #(navigate "xquo-demo.step") [navigate])
        open-divider-label!             (react/use-callback #(navigate "xquo-demo.divider-label") [navigate])
        open-divider-line!              (react/use-callback #(navigate "xquo-demo.divider-line") [navigate])
        open-divider-new!               (react/use-callback #(navigate "xquo-demo.divider-new") [navigate])
        open-divider-strength!          (react/use-callback #(navigate "xquo-demo.divider-strength") [navigate])]
    [screen/screen {:title "Xquo components"}
     [xquo/settings-item {:title    "Audio player"
                          :image    {:type :none}
                          :right    {:action {:type :arrow}}
                          :on-press open-audio-player!}]
     [xquo/settings-item {:title    "Audio recorder"
                          :image    {:type :none}
                          :right    {:action {:type :arrow}}
                          :on-press open-audio-recorder!}]
     [xquo/settings-item {:title    "Audio soundtrack"
                          :image    {:type :none}
                          :right    {:action {:type :arrow}}
                          :on-press open-audio-soundtrack!}]
     [xquo/settings-item {:title    "Avatar"
                          :image    {:type :none}
                          :right    {:action {:type :arrow}}
                          :on-press open-avatar!}]
     [xquo/settings-item {:title    "Banner alert"
                          :image    {:type :none}
                          :right    {:action {:type :arrow}}
                          :on-press open-banner-alert!}]
     [xquo/settings-item {:title    "Banner global"
                          :image    {:type :none}
                          :right    {:action {:type :arrow}}
                          :on-press open-banner-global!}]
     [xquo/settings-item {:title    "Banner standard"
                          :image    {:type :none}
                          :right    {:action {:type :arrow}}
                          :on-press open-banner-standard!}]
     [xquo/settings-item {:title    "Bottom nav"
                          :image    {:type :none}
                          :right    {:action {:type :arrow}}
                          :on-press open-bottom-nav!}]
     [xquo/settings-item {:title    "Bottom nav tab"
                          :image    {:type :none}
                          :right    {:action {:type :arrow}}
                          :on-press open-bottom-nav-tab!}]
     [xquo/settings-item {:title    "Browser dapp item"
                          :image    {:type :none}
                          :right    {:action {:type :arrow}}
                          :on-press open-browser-dapp-item!}]
     [xquo/settings-item {:title    "Browser favorites"
                          :image    {:type :none}
                          :right    {:action {:type :arrow}}
                          :on-press open-browser-favorites!}]
     [xquo/settings-item {:title    "Browser input"
                          :image    {:type :none}
                          :right    {:action {:type :arrow}}
                          :on-press open-browser-input!}]
     [xquo/settings-item {:title    "Browser navigation"
                          :image    {:type :none}
                          :right    {:action {:type :arrow}}
                          :on-press open-browser-navigation!}]
     [xquo/settings-item {:title    "Browser new tab"
                          :image    {:type :none}
                          :right    {:action {:type :arrow}}
                          :on-press open-browser-new-tab!}]
     [xquo/settings-item {:title    "Button"
                          :image    {:type :none}
                          :right    {:action {:type :arrow}}
                          :on-press open-button!}]
     [xquo/settings-item {:title    "Calendar day"
                          :image    {:type :none}
                          :right    {:action {:type :arrow}}
                          :on-press open-calendar-day!}]
     [xquo/settings-item {:title    "Calendar picker"
                          :image    {:type :none}
                          :right    {:action {:type :arrow}}
                          :on-press open-calendar-picker!}]
     [xquo/settings-item {:title    "Calendar year"
                          :image    {:type :none}
                          :right    {:action {:type :arrow}}
                          :on-press open-calendar-year!}]
     [xquo/settings-item {:title    "Camera controls"
                          :image    {:type :none}
                          :right    {:action {:type :arrow}}
                          :on-press open-camera-controls!}]
     [xquo/settings-item {:title    "Camera crop"
                          :image    {:type :none}
                          :right    {:action {:type :arrow}}
                          :on-press open-camera-crop!}]
     [xquo/settings-item {:title    "Camera shutter"
                          :image    {:type :none}
                          :right    {:action {:type :arrow}}
                          :on-press open-camera-shutter!}]
     [xquo/settings-item {:title    "Card banner"
                          :image    {:type :none}
                          :right    {:action {:type :arrow}}
                          :on-press open-card-banner!}]
     [xquo/settings-item {:title    "Card wallet"
                          :image    {:type :none}
                          :right    {:action {:type :arrow}}
                          :on-press open-card-wallet!}]
     [xquo/settings-item {:title    "Community channel action"
                          :image    {:type :none}
                          :right    {:action {:type :arrow}}
                          :on-press open-community-channel-action!}]
     [xquo/settings-item {:title    "Community channel actions"
                          :image    {:type :none}
                          :right    {:action {:type :arrow}}
                          :on-press open-community-channel-actions!}]
     [xquo/settings-item {:title    "Community main card"
                          :image    {:type :none}
                          :right    {:action {:type :arrow}}
                          :on-press open-community-main-card!}]
     [xquo/settings-item {:title    "Community role"
                          :image    {:type :none}
                          :right    {:action {:type :arrow}}
                          :on-press open-community-role!}]
     [xquo/settings-item {:title    "Community stat"
                          :image    {:type :none}
                          :right    {:action {:type :arrow}}
                          :on-press open-community-stat!}]
     [xquo/settings-item {:title    "Community token gating"
                          :image    {:type :none}
                          :right    {:action {:type :arrow}}
                          :on-press open-community-token-gating!}]
     [xquo/settings-item {:title    "Counter"
                          :image    {:type :none}
                          :right    {:action {:type :arrow}}
                          :on-press open-counter!}]
     [xquo/settings-item {:title    "dApp activity"
                          :image    {:type :none}
                          :right    {:action {:type :arrow}}
                          :on-press open-dapp-activity!}]
     [xquo/settings-item {:title    "dApp approvals"
                          :image    {:type :none}
                          :right    {:action {:type :arrow}}
                          :on-press open-dapp-approvals!}]
     [xquo/settings-item {:title    "dApp group"
                          :image    {:type :none}
                          :right    {:action {:type :arrow}}
                          :on-press open-dapp-group!}]
     [xquo/settings-item {:title    "dApp list card"
                          :image    {:type :none}
                          :right    {:action {:type :arrow}}
                          :on-press open-dapp-list-card!}]
     [xquo/settings-item {:title    "Disclaimer"
                          :image    {:type :none}
                          :right    {:action {:type :arrow}}
                          :on-press open-disclaimer!}]
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
     [xquo/settings-item {:title    "Drawer"
                          :image    {:type :none}
                          :right    {:action {:type :arrow}}
                          :on-press open-drawer!}]
     [xquo/settings-item {:title    "Dropdown button"
                          :image    {:type :none}
                          :right    {:action {:type :arrow}}
                          :on-press open-dropdown-button!}]
     [xquo/settings-item {:title    "Dropdown input"
                          :image    {:type :none}
                          :right    {:action {:type :arrow}}
                          :on-press open-dropdown-input!}]
     [xquo/settings-item {:title    "Empty state"
                          :image    {:type :none}
                          :right    {:action {:type :arrow}}
                          :on-press open-empty-state!}]
     [xquo/settings-item {:title    "Favicon"
                          :image    {:type :none}
                          :right    {:action {:type :arrow}}
                          :on-press open-favicon!}]
     [xquo/settings-item {:title    "Filter selector"
                          :image    {:type :none}
                          :right    {:action {:type :arrow}}
                          :on-press open-filter!}]
     [xquo/settings-item {:title    "Info message"
                          :image    {:type :none}
                          :right    {:action {:type :arrow}}
                          :on-press open-info-message!}]
     [xquo/settings-item {:title    "Information box"
                          :image    {:type :none}
                          :right    {:action {:type :arrow}}
                          :on-press open-information-box!}]
     [xquo/settings-item {:title    "Input"
                          :image    {:type :none}
                          :right    {:action {:type :arrow}}
                          :on-press open-input!}]
     [xquo/settings-item {:title    "Link card"
                          :image    {:type :none}
                          :right    {:action {:type :arrow}}
                          :on-press open-link-card!}]
     [xquo/settings-item {:title    "Link preview"
                          :image    {:type :none}
                          :right    {:action {:type :arrow}}
                          :on-press open-link-preview!}]
     [xquo/settings-item {:title    "List info box"
                          :image    {:type :none}
                          :right    {:action {:type :arrow}}
                          :on-press open-list-info-box!}]
     [xquo/settings-item {:title    "List item"
                          :image    {:type :none}
                          :right    {:action {:type :arrow}}
                          :on-press open-list-item!}]
     [xquo/settings-item {:title    "Markdown list item"
                          :image    {:type :none}
                          :right    {:action {:type :arrow}}
                          :on-press open-markdown-list-item!}]
     [xquo/settings-item {:title    "Markdown text"
                          :image    {:type :none}
                          :right    {:action {:type :arrow}}
                          :on-press open-markdown-text!}]
     [xquo/settings-item {:title    "Notification in-app"
                          :image    {:type :none}
                          :right    {:action {:type :arrow}}
                          :on-press open-notification-in-app!}]
     [xquo/settings-item {:title    "Notification toast"
                          :image    {:type :none}
                          :right    {:action {:type :arrow}}
                          :on-press open-notification-toast!}]
     [xquo/settings-item {:title    "Option card"
                          :image    {:type :none}
                          :right    {:action {:type :arrow}}
                          :on-press open-card!}]
     [xquo/settings-item {:title    "Page nav"
                          :image    {:type :none}
                          :right    {:action {:type :arrow}}
                          :on-press open-page-nav!}]
     [xquo/settings-item {:title    "Profile card"
                          :image    {:type :none}
                          :right    {:action {:type :arrow}}
                          :on-press open-profile-card!}]
     [xquo/settings-item {:title    "Profile input"
                          :image    {:type :none}
                          :right    {:action {:type :arrow}}
                          :on-press open-profile-input!}]
     [xquo/settings-item {:title    "Profile tile"
                          :image    {:type :none}
                          :right    {:action {:type :arrow}}
                          :on-press open-profile-tile!}]
     [xquo/settings-item {:title    "Search input"
                          :image    {:type :none}
                          :right    {:action {:type :arrow}}
                          :on-press open-search-input!}]
     [xquo/settings-item {:title    "Segmented tab"
                          :image    {:type :none}
                          :right    {:action {:type :arrow}}
                          :on-press open-segmented-tab!}]
     [xquo/settings-item {:title    "Selector"
                          :image    {:type :none}
                          :right    {:action {:type :arrow}}
                          :on-press open-selector!}]
     [xquo/settings-item {:title    "Showcase card"
                          :image    {:type :none}
                          :right    {:action {:type :arrow}}
                          :on-press open-showcase-card!}]
     [xquo/settings-item {:title    "Step"
                          :image    {:type :none}
                          :right    {:action {:type :arrow}}
                          :on-press open-step!}]
     [xquo/settings-item {:title    "Tag asset"
                          :image    {:type :none}
                          :right    {:action {:type :arrow}}
                          :on-press open-tag-asset!}]
     [xquo/settings-item {:title    "Tag permissions"
                          :image    {:type :none}
                          :right    {:action {:type :arrow}}
                          :on-press open-tag-permissions!}]
     [xquo/settings-item {:title    "Tag standard"
                          :image    {:type :none}
                          :right    {:action {:type :arrow}}
                          :on-press open-tag-standard!}]
     [xquo/settings-item {:title    "Text combinations"
                          :image    {:type :none}
                          :right    {:action {:type :arrow}}
                          :on-press open-text-combinations!}]
     [xquo/settings-item {:title    "URL preview"
                          :image    {:type :none}
                          :right    {:action {:type :arrow}}
                          :on-press open-url-preview!}]]))

(def ^:private navigator
  (j/call (native-stack/create-native-stack-navigator {:initial-route-name :screen/xquo-demo.index
                                                       :screen-options     {:header-shown false}
                                                       :screens            {:screen/xquo-demo.index                     #'index
                                                                            :screen/xquo-demo.audio-player              #'audio-player/screen
                                                                            :screen/xquo-demo.audio-soundtrack          #'audio-soundtrack/screen
                                                                            :screen/xquo-demo.audio-recorder            #'audio-recorder/screen
                                                                            :screen/xquo-demo.favicon                   #'favicon/screen
                                                                            :screen/xquo-demo.browser-dapp-item         #'browser-dapp-item/screen
                                                                            :screen/xquo-demo.browser-favorites         #'browser-favorites/screen
                                                                            :screen/xquo-demo.browser-new-tab           #'browser-new-tab/screen
                                                                            :screen/xquo-demo.browser-input             #'browser-input/screen
                                                                            :screen/xquo-demo.browser-navigation        #'browser-navigation/screen
                                                                            :screen/xquo-demo.banner-standard           #'banner-standard/screen
                                                                            :screen/xquo-demo.banner-alert              #'banner-alert/screen
                                                                            :screen/xquo-demo.banner-global             #'banner-global/screen
                                                                            :screen/xquo-demo.calendar-day              #'calendar-day/screen
                                                                            :screen/xquo-demo.calendar-year             #'calendar-year/screen
                                                                            :screen/xquo-demo.calendar-picker           #'calendar-picker/screen
                                                                            :screen/xquo-demo.camera-shutter            #'camera-shutter/screen
                                                                            :screen/xquo-demo.camera-controls           #'camera-controls/screen
                                                                            :screen/xquo-demo.camera-crop               #'camera-crop/screen
                                                                            :screen/xquo-demo.card-banner               #'card-banner/screen
                                                                            :screen/xquo-demo.card-wallet               #'card-wallet/screen
                                                                            :screen/xquo-demo.segmented-tab             #'segmented-tab/screen
                                                                            :screen/xquo-demo.tag-standard              #'tag-standard/screen
                                                                            :screen/xquo-demo.tag-permissions           #'tag-permissions/screen
                                                                            :screen/xquo-demo.tag-asset                 #'tag-asset/screen
                                                                            :screen/xquo-demo.community-stat            #'community-stat/screen
                                                                            :screen/xquo-demo.community-role            #'community-role/screen
                                                                            :screen/xquo-demo.community-channel-action  #'community-channel-action/screen
                                                                            :screen/xquo-demo.community-channel-actions #'community-channel-actions/screen
                                                                            :screen/xquo-demo.community-main-card       #'community-main-card/screen
                                                                            :screen/xquo-demo.community-token-gating    #'community-token-gating/screen
                                                                            :screen/xquo-demo.dapp-list-card            #'dapp-list-card/screen
                                                                            :screen/xquo-demo.dapp-group                #'dapp-group/screen
                                                                            :screen/xquo-demo.dapp-approvals            #'dapp-approvals/screen
                                                                            :screen/xquo-demo.dapp-activity             #'dapp-activity/screen
                                                                            :screen/xquo-demo.drawer                    #'drawer/screen
                                                                            :screen/xquo-demo.dropdown-button           #'dropdown/button-screen
                                                                            :screen/xquo-demo.dropdown-input            #'dropdown/input-screen
                                                                            :screen/xquo-demo.empty-state               #'empty-state/screen
                                                                            :screen/xquo-demo.info-message              #'info/message-screen
                                                                            :screen/xquo-demo.information-box           #'info/box-screen
                                                                            :screen/xquo-demo.text-combinations         #'text-combinations/screen
                                                                            :screen/xquo-demo.input                     #'input/screen
                                                                            :screen/xquo-demo.search-input              #'search-input/screen
                                                                            :screen/xquo-demo.profile-input             #'profile-input/screen
                                                                            :screen/xquo-demo.profile-card              #'profile-card/screen
                                                                            :screen/xquo-demo.showcase-card             #'showcase-card/screen
                                                                            :screen/xquo-demo.profile-tile              #'profile-tile/screen
                                                                            :screen/xquo-demo.list-info-box             #'info/list-screen
                                                                            :screen/xquo-demo.link-card                 #'link-card/screen
                                                                            :screen/xquo-demo.link-preview              #'link-preview/screen
                                                                            :screen/xquo-demo.url-preview               #'url-preview/screen
                                                                            :screen/xquo-demo.list-item                 #'list-item/screen
                                                                            :screen/xquo-demo.markdown-list-item        #'markdown-list-item/screen
                                                                            :screen/xquo-demo.markdown-text             #'markdown-text/screen
                                                                            :screen/xquo-demo.notification-toast        #'notification/toast-screen
                                                                            :screen/xquo-demo.notification-in-app       #'notification/in-app-screen
                                                                            :screen/xquo-demo.button                    #'button/screen
                                                                            :screen/xquo-demo.counter                   #'counter/screen
                                                                            :screen/xquo-demo.divider-label             #'divider-label/screen
                                                                            :screen/xquo-demo.divider-line              #'divider-line/screen
                                                                            :screen/xquo-demo.divider-new               #'divider-new/screen
                                                                            :screen/xquo-demo.divider-strength          #'divider-strength/screen
                                                                            :screen/xquo-demo.selector                  #'selectors/selector-screen
                                                                            :screen/xquo-demo.filter                    #'selectors/filter-screen
                                                                            :screen/xquo-demo.disclaimer                #'selectors/disclaimer-screen
                                                                            :screen/xquo-demo.step                      #'step/screen
                                                                            :screen/xquo-demo.avatar                    #'avatar/screen
                                                                            :screen/xquo-demo.bottom-nav-tab            #'bottom-nav-tab/screen
                                                                            :screen/xquo-demo.bottom-nav                #'bottom-nav/screen
                                                                            :screen/xquo-demo.page-nav                  #'page-nav/screen
                                                                            :screen/xquo-demo.option-card               #'option-card/screen}})
          :getComponent))

(defn screen [_]
  [xquo/notification.provider
   [:> navigator]])
