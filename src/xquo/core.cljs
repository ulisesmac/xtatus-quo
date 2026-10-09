(ns xquo.core
  (:refer-clojure :exclude [filter list])
  (:require [xquo.components.avatar.view]
            [xquo.components.button.view]
            [xquo.components.color-picker.color.view]
            [xquo.components.color-picker.picker.view]
            [xquo.components.counter.step.view]
            [xquo.components.counter.view]
            [xquo.divider.label.view]
            [xquo.divider.line.view]
            [xquo.divider.new.view]
            [xquo.divider.strength.view]
            [xquo.components.drawer.bottom-actions.view]
            [xquo.components.drawer.drawer-action.view]
            [xquo.components.drawer.drawer-label.view]
            [xquo.components.drawer.drawer.view]
            [xquo.components.drawer.top.view]
            [xquo.components.emoji-picker.view]
            [xquo.components.empty-state.view]
            [xquo.components.icon.view]
            [xquo.components.info.info-message.view]
            [xquo.components.info.information-box.view]
            [xquo.components.input.view]
            [xquo.components.link-card.view]
            [xquo.components.list-items.result-list.view]
            [xquo.components.list-items.simple-item.view]
            [xquo.components.list.view]
            [xquo.components.notification.view]
            [xquo.components.page-nav.view]
            [xquo.components.page-top.view]
            [xquo.components.selectors.disclaimer.view]
            [xquo.components.selectors.filter.view]
            [xquo.components.selectors.options.view]
            [xquo.components.selectors.selector.view]
            [xquo.components.settings.category.view]
            [xquo.components.settings.item.view]
            [xquo.components.settings.section-label.view]
            [xquo.components.settings.section-title.view]
            [xquo.components.settings.sort-item.view]
            [xquo.components.tags.context-tag.view]
            [xquo.components.text.view]
            [xquo.components.title-input.view]
            [xquo.components.unified-tab.view]
            [xquo.shell.option-card.view]))

(def avatar
  "Avatar with one tagged content slot. Theme/color inherit from Xquo context.

  :size        16, 20, 24, 28, 32, 36, 48, 56, 64, or 80; default 32.
  :shape       :circle (default), :rounded-square, or :hexagon.
  :content     {:type :image :image {:source ...}} (or :url, optional :style),
               {:type :icon :icon {...}}, {:type :emoji :text ...}, or
               {:type :initials :text ...}. :type selects the renderer.
               Omit content for an empty frame. Images fill the whole shape;
               transparent pixels reveal the resolved context/color prop color.
  :color       Xquo color family; defaults to context color.
  :variant     :solid, :soft (default), or nil for no fill.
  :border      Optional :solid or :dashed edge.
  :blur?       When truthy, adapts styling to a blurred backdrop; never creates a blur view.
  :badge       Optional {:status :online/:offline}, {:icon {...}}, or
               {:image {:source ...}} (or :url, optional :style).
               All badge types work at every supported size with every content type.
               Uses exact Figma metrics where specified and scaled metrics elsewhere.
  :on-press    Optional callback; enables button-timed scale-only press feedback.
               Also darkens the avatar with an overlay that excludes the badge.
               Without it, the avatar is static. No Y movement.
  :style       Container overrides. Rings are not supported."
  xquo.components.avatar.view/avatar)

(def button
  "Button with an optional label string or Hiccup child.

  :type        :primary (default), :positive, :grey, :dark-grey, :outline,
               :ghost, :danger, or :black.
  :size        40 (default), 32, or 24.
  :color       Primary color override; defaults to the Xquo context color.
  :photo?      Photo surface styling with a clipped 40 px backdrop blur.
  :blur?       Styling for an already blurred surface. Alternative to :photo?.
  :icon        Standard icon map; :side defaults to :right when there is content.
               Without a child, renders an icon-only button.
  :icons       {:left {...} :right {...}}; explicit sides override :icon placement.
  :disabled?   Disables interaction and applies the Figma disabled style.
  :style       Final layout and appearance overrides.
  :on-press-in / :on-press-out run alongside internal press feedback.
  Other pressable props, including :on-press and :on-long-press, are forwarded.

  Theme comes from Xquo context. Shape, padding, colors and opacity are internal.
  Keeps scale/Y press animations. Photo uses background blur, never glass.
  :blur? does not add blur; it adapts styling to an already blurred backdrop.
  Removed: :background, :glass?, :blur-amount, :blur-intensity, :container-style."
  xquo.components.button.view/button)
(def counter
  "[counter props value] renders a number or string without capping its value.
  :type defaults to :default; also :secondary, :grey, :outline, :outline-color,
  :warning and :error. :size is :default (11 px text) or :large (13 px text).
  Both sizes have a 20 px layout height. :blur? adapts to a blurred backdrop.
  Theme and color come only from Xquo context; the parent owns the blur.
  :style and other view props apply to the root. Longer labels grow to fit."
  xquo.components.counter.view/counter)
(def empty-state xquo.components.empty-state.view/empty-state)
(def color
  "Color swatch with :color, :selected?, :blur?, :style and pressable props.
  :blur? selects dark + blur regardless of parent theme; otherwise inherits theme.
  Styling only: the component does not create a blur view."
  xquo.components.color-picker.color.view/color)
(def color-picker
  "Color picker with :colors, :default-selected, :on-select, :blur? and :style.
  :blur? makes every swatch use dark + blur regardless of parent theme."
  xquo.components.color-picker.picker.view/picker)
(def step
  "[step props value] renders a separate 20 px step indicator.
  :type is :neutral (default), :complete or :active. :blur? replaces :background.
  Theme and color come only from Xquo context; the parent owns the blur.
  :style and other view props apply to the root. Longer labels grow to fit."
  xquo.components.counter.step.view/step)
(def divider
  "Divider components, exported as divider.label, divider.line, divider.new and
  divider.strength. All accept :style and native root props.

  divider.label: :title, :compact? (true), :divider-line? (true), :blur?,
  :chevron (:left/:right), :chevron-icon, :counter and custom :right content.
  Counter selects a left chevron. :collapsible? defaults true; :open? controls
  expansion or :initial-open? initializes local state. Supports :disabled?,
  press callbacks, :toggle-duration and :toggle-timing-function.

  divider.line: 20 px separator with :blur? and :dashed? flags. The 1 px line
  uses 3 px dashes and 4 px gaps. Label/Line inherit theme; parent owns blur.

  divider.new: caller-supplied :title. Inherits color/theme from context;
  gradient and 42 px geometry are internal.

  divider.strength: caller-supplied :title and :status: :informative (default),
  :alert, :very-weak, :weak, :okay, :strong or :very-strong. Status determines
  fixed design colors, icon and progress; does not evaluate passwords.
  Designed for a dark backdrop.

  Replaces the old divider-label / divider-line exports."
  #js {:label    xquo.divider.label.view/divider-label
       :line     xquo.divider.line.view/divider-line
       :new      xquo.divider.new.view/divider-new
       :strength xquo.divider.strength.view/divider-strength})
(def bottom-actions xquo.components.drawer.bottom-actions.view/bottom-actions)
(def drawer xquo.components.drawer.drawer.view/drawer)
(def drawer-action xquo.components.drawer.drawer-action.view/drawer-action)
(def drawer-label xquo.components.drawer.drawer-label.view/drawer-label)
(def drawer-handle xquo.components.drawer.top.view/drawer-handle)
(def drawer-top xquo.components.drawer.top.view/drawer-top)
(def emoji-picker xquo.components.emoji-picker.view/emoji-picker)
(def emoji-picker-header xquo.components.emoji-picker.view/emoji-picker-header)
(def emoji-picker-footer xquo.components.emoji-picker.view/emoji-picker-footer)
(def icon xquo.components.icon.view/view)
(def info-message xquo.components.info.info-message.view/info-message)
(def information-box xquo.components.info.information-box.view/information-box)
(def input xquo.components.input.view/input)
(def link-card xquo.components.link-card.view/link-card)
(def list xquo.components.list.view/view)
(def result-list xquo.components.list-items.result-list.view/result-list)
(def simple-item xquo.components.list-items.simple-item.view/simple-item)
(def notification xquo.components.notification.view/notification)
(def page-nav xquo.components.page-nav.view/page-nav)
(def page-nav-left-action xquo.components.page-nav.view/nav-left-action)
(def page-nav-title xquo.components.page-nav.view/nav-title)
(def page-top xquo.components.page-top.view/page-top)
(def category xquo.components.settings.category.view/category)
(def settings-item xquo.components.settings.item.view/settings-item)
(def section-label xquo.components.settings.section-label.view/section-label)
(def section-title xquo.components.settings.section-title.view/section-title)
(def sort-item xquo.components.settings.sort-item.view/sort-item)
(def disclaimer xquo.components.selectors.disclaimer.view/disclaimer)
(def filter xquo.components.selectors.filter.view/filter)
(def options xquo.components.selectors.options.view/view)
(def selector xquo.components.selectors.selector.view/selector)
(def context-tag xquo.components.tags.context-tag.view/context-tag)
(def title-input xquo.components.title-input.view/title-input)
(def unified-tab xquo.components.unified-tab.view/view)
(def unified-tab-content xquo.components.unified-tab.view/unified-tab-content)
(def text xquo.components.text.view/text)

(def shell
  "Shell components. `shell.option-card` uses :variant :small (default) or :big.
  The parent owns the dark blurred backdrop. Both variants accept :title,
  :description, the standard :image map and :style.

  :small: compact pressable row. :description is optional; :icon can replace
  :image. Supports :right-icon, :disabled? and :on-press. Owns animated press
  fills, foreground, opacity and 16 px radius.

  :big: :title and :description are required. Displays the image above an actual
  Xquo :button (its complete props map plus :label). Supplies button size 40,
  :blur? true and default :type :grey. Image defaults to height 204.

  Replaces shell.small-option-card and shell.big-option-card."
  #js {:option_card xquo.shell.option-card.view/option-card})
