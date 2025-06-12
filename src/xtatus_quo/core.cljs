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
   xtatus-quo.components.text-combinations.page-top.view
   xtatus-quo.components.text-combinations.standard-title.view
   xtatus-quo.components.drawers.drawer-top.view
   xtatus-quo.components.tags.context-tag.view))

(def input xtatus-quo.components.inputs.input.view/input)

(def text xtatus-quo.components.markdown.text/text)
(def button xtatus-quo.components.buttons.button.view/button)

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
