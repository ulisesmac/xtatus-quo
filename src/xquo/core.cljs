(ns ^:dev/always xquo.core
  (:refer-clojure :exclude [filter])
  (:require [xquo.components.button.view]
            [xquo.components.color-picker.color.view]
            [xquo.components.color-picker.picker.view]
            [xquo.components.counter.step.view]
            [xquo.components.drawer.bottom-actions.view]
            [xquo.components.drawer.drawer.view]
            [xquo.components.drawer.drawer-action.view]
            [xquo.components.drawer.top.view]
            [xquo.components.icon.view]
            [xquo.components.info.info-message.view]
            [xquo.components.info.information-box.view]
            [xquo.components.input.view]
            [xquo.components.list-items.result-list.view]
            [xquo.components.page-nav.view]
            [xquo.components.settings.category.view]
            [xquo.components.settings.item.view]
            [xquo.components.settings.section-label.view]
            [xquo.components.settings.section-title.view]
            [xquo.components.settings.sort-item.view]
            [xquo.components.selectors.disclaimer.view]
            [xquo.components.selectors.filter.view]
            [xquo.components.selectors.selector.view]
            [xquo.components.text.view]))

(def button xquo.components.button.view/button)
(def color xquo.components.color-picker.color.view/color)
(def color-picker xquo.components.color-picker.picker.view/picker)
(def step xquo.components.counter.step.view/step)
(def bottom-actions xquo.components.drawer.bottom-actions.view/bottom-actions)
(def drawer xquo.components.drawer.drawer.view/drawer)
(def drawer-action xquo.components.drawer.drawer-action.view/drawer-action)
(def drawer-top xquo.components.drawer.top.view/drawer-top)
(def icon xquo.components.icon.view/icon)
(def info-message xquo.components.info.info-message.view/info-message)
(def information-box xquo.components.info.information-box.view/information-box)
(def input xquo.components.input.view/input)
(def result-list xquo.components.list-items.result-list.view/result-list)
(def page-nav xquo.components.page-nav.view/page-nav)
(def page-nav-left-action xquo.components.page-nav.view/nav-left-action)
(def page-nav-title xquo.components.page-nav.view/nav-title)
(def category xquo.components.settings.category.view/category)
(def settings-item xquo.components.settings.item.view/settings-item)
(def section-label xquo.components.settings.section-label.view/section-label)
(def section-title xquo.components.settings.section-title.view/section-title)
(def sort-item xquo.components.settings.sort-item.view/sort-item)
(def disclaimer xquo.components.selectors.disclaimer.view/disclaimer)
(def filter xquo.components.selectors.filter.view/filter)
(def selector xquo.components.selectors.selector.view/selector)
(def text xquo.components.text.view/text)
