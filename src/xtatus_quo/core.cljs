(ns xtatus-quo.core
  (:require
   xtatus-quo.components.buttons.button.view
   xtatus-quo.components.drawers.bottom-actions.view
   xtatus-quo.components.inputs.input.view
   xtatus-quo.components.markdown.text
   xtatus-quo.components.navigation.page-nav.view
   xtatus-quo.components.notifications.toast.view
   xtatus-quo.components.overlay.view
   xtatus-quo.components.settings.category.view
   xtatus-quo.components.selectors.selectors.view
   xtatus-quo.components.text-combinations.page-top.view
   xtatus-quo.components.text-combinations.standard-title.view
   xtatus-quo.components.drawers.drawer-top.view
   xtatus-quo.components.selectors.disclaimer.view
   xtatus-quo.components.selectors.selector-button
   xtatus-quo.components.tags.context-tag.view
   xtatus-quo.components.inputs.title-input.view
   quo.components.info.info-message.view
   xtatus-quo.components.dividers.divider-line.view
   xtatus-quo.components.colors.color-picker.view))

;; Avatars
(def account-avatar xtatus-quo.components.avatars.account-avatar.view/view)

;; Colors
(def color-picker xtatus-quo.components.colors.color-picker.view/view)

;; Dividers
(def divider-line xtatus-quo.components.dividers.divider-line.view/view)

;; Inputs
(def input xtatus-quo.components.inputs.input.view/input)
(def title-input xtatus-quo.components.inputs.title-input.view/view)

(def text xtatus-quo.components.markdown.text/text)
(def button xtatus-quo.components.buttons.button.view/button)

;; Selectors
(def selector xtatus-quo.components.selectors.selectors.view/view)
(def selector-button xtatus-quo.components.selectors.selector-button/view)
(def disclaimer xtatus-quo.components.selectors.disclaimer.view/view)

;; Settings
(def category xtatus-quo.components.settings.category.view/category)

;; Text combinations
(def page-top xtatus-quo.components.text-combinations.page-top.view/view)
(def standard-title xtatus-quo.components.text-combinations.standard-title.view/view)

;; Navigation
(def page-nav xtatus-quo.components.navigation.page-nav.view/page-nav)

;; Drawer
(def drawer-top xtatus-quo.components.drawers.drawer-top.view/view)
(def bottom-actions xtatus-quo.components.drawers.bottom-actions.view/view)

;; Notifications
(def toast xtatus-quo.components.notifications.toast.view/toast)

;; Tags
(def context-tag xtatus-quo.components.tags.context-tag.view/view)

;; Icon
(def icon xtatus-quo.components.icon/icon)

;; info
(def info-message quo.components.info.info-message.view/view)
