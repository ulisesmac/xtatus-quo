(ns xquo.core
  (:refer-clojure :exclude [filter list])
  (:require [xquo.markdown.list-item.view]
            [xquo.markdown.text.view]
            [xquo.link-preview.view]
            [xquo.url-preview.view]
            [xquo.dropdown.view]
            [xquo.drawer.action.view]
            [xquo.drawer.actions.view]
            [xquo.drawer.body.view]
            [xquo.drawer.bottom-actions.view]
            [xquo.drawer.bottom-context.view]
            [xquo.drawer.content.view]
            [xquo.drawer.divider.view]
            [xquo.drawer.label.view]
            [xquo.drawer.permissions-context.view]
            [xquo.drawer.sheet]
            [xquo.drawer.top.view]
            [xquo.dapp.activity.view]
            [xquo.dapp.group.view]
            [xquo.dapp.list-card.view]
            [xquo.community.token-gating.view]
            [xquo.community.main-card.view]
            [xquo.community.channel-actions.view]
            [xquo.community.channel-action.view]
            [xquo.community.role.view]
            [xquo.community.stat.view]
            [xquo.tag.asset.view]
            [xquo.tag.permissions.view]
            [xquo.tag.standard.view]
            [xquo.card.wallet.view]
            [xquo.card.banner.view]
            [xquo.calendar.picker.view]
            [xquo.calendar.year.view]
            [xquo.calendar.day.view]
            [xquo.camera.crop.view]
            [xquo.camera.controls.view]
            [xquo.camera.shutter.view]
            [xquo.components.segmented-tab.view]
            [xquo.banner.global.view]
            [xquo.banner.alert.view]
            [xquo.banner.standard.view]
            [xquo.browser.navigation.view]
            [xquo.browser.input.view]
            [xquo.browser.new-tab.view]
            [xquo.browser.favorites.view]
            [xquo.browser.dapp-item.view]
            [xquo.components.favicon.view]
            [xquo.audio.recorder.view]
            [xquo.audio.player.view]
            [xquo.audio.soundtrack.view]
            [xquo.components.avatar.view]
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
            [xquo.components.drawer.top.view]
            [xquo.components.emoji-picker.view]
            [xquo.components.empty-state.view]
            [xquo.components.icon.view]
            [xquo.components.info.info-message.view]
            [xquo.components.info.information-box.view]
            [xquo.components.info.list-info-box.view]
            [xquo.input.text.view]
            [xquo.input.search.view]
            [xquo.input.profile.view]
            [xquo.profile.card.view]
            [xquo.profile.tile.view]
            [xquo.profile.showcase-card.view]
            [xquo.profile.link-card.view]
            [xquo.components.list-items.result-list.view]
            [xquo.components.list-items.simple-item.view]
            [xquo.components.list.view]
            [xquo.list.item.view]
            [xquo.navigation.bottom-nav-tab.view]
            [xquo.navigation.bottom-nav.view]
            [xquo.navigation.page-nav.view]
            [xquo.notification.host.view]
            [xquo.notification.toast.view]
            [xquo.notification.in-app.view]
            [xquo.components.page-nav.view]
            [xquo.components.text.page-top.view]
            [xquo.components.text.combination.view]
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

(def audio
  "Audio UI; host owns media operations. Components inherit theme and context color.
  audio.soundtrack: :progress (0–1, default 0), optional :time, :state
  (:default/:deleting/:listening), :warning?, :on-seek (progress), :style.
  audio.player: :progress, :time, :state (:default/:playing/:loading),
  :on-press, :on-seek (progress), :style. Reuses Soundtrack and Xquo Button.
  audio.recorder: :state, :time, :progress, :warning?, :style; callbacks :on-record,
  :on-stop, :on-lock, :on-delete, :on-send, :on-play, :on-pause, :on-seek.
  Owns transient drag feedback; :on-state-change optionally reports drag states."
  #js {:soundtrack xquo.audio.soundtrack.view/soundtrack
       :player     xquo.audio.player.view/player
       :recorder   xquo.audio.recorder.view/recorder})

(def favicon
  "16 px site mark. :content is {:type :image :image {...}} or
  {:type :initials :text ...}; supports :blur? and :style, inherits theme."
  xquo.components.favicon.view/favicon)

(def browser
  "Browser UI; host owns navigation/web content and native safe-area insets.
  browser.dapp-item: :title, :image, :on-press, :style.
  browser.favorites: :items of dapp-item props, :style.
  browser.new-tab: :title, :on-press, full :button props, :style.
  browser.input: :value, :on-change-text, :on-submit-editing, :placeholder,
  :image, :secure?, :blur?, :style; native focus and text selection.
  browser.navigation: :input, :avatar, :back, :forward, :reload props maps,
  :blur? and :style. Composes Avatar and Button; drawer chrome belongs to the host."
  #js {:dapp_item  xquo.browser.dapp-item.view/dapp-item
       :favorites  xquo.browser.favorites.view/favorites
       :new_tab    xquo.browser.new-tab.view/new-tab
       :input      xquo.browser.input.view/input
       :navigation xquo.browser.navigation.view/navigation})

(def dropdown
  "dropdown.button [props text]: :type :outline/:grey/:customisation/:ghost,
  numeric :size 24/32/40, :content {:type :icon :icon {...}} or
  {:type :image :image {...}}, :active?, :disabled?, :blur?, :photo?, :color,
  :on-press, :style and Button press props. Omit text for a square content-only
  trigger. Customisation inherits context color. :photo? owns blur 40;
  :blur? adapts to parent blur. These two flags are alternatives.
  dropdown.input [props value]: optional :label/:content, :active?, :disabled?,
  :blur?, :on-press and Button press props. Fixed 40 px field; :style applies
  to its outer container. Both are triggers; the host owns menu/selection."
  #js {:button xquo.dropdown.view/dropdown-button
       :input  xquo.dropdown.view/dropdown-input})

(def banner
  "banner.standard: :title, optional :icon map/:counter, :blur?, :on-press,
  :style and pressable props. Inherits context color and theme; parent owns blur.
  banner.alert: :title, optional standard :icon map (defaults to alert),
  full :button props plus :label, :style.
  Uses danger tokens and actual Button24; text wraps naturally.
  banner.global: :items of {:type :error|:alert :title ...}, :style.
  Semantic danger/warning rows; parent owns system insets."
  #js {:standard xquo.banner.standard.view/banner-standard
       :alert    xquo.banner.alert.view/banner-alert
       :global   xquo.banner.global.view/banner-global})

(def calendar
  "calendar.day [props label]: :selected?, :today?, :disabled?, :on-press, :style.
  calendar.year [props year]: :selected?, :on-press, :style.
  calendar.picker: controlled :month (YYYY-MM), :mode :single/:range, :value
  (ISO date or {:start ISO :end ISO}), :on-change, :on-month-change, :today,
  :min-date/:max-date, optional :years/:locale, :first-day-of-week (0), :style.
  Device Spanish/English locale by default; other locales fall back to English.
  Sunday first matches Figma. Actual Gregorian dates, inherited theme/color."
  #js {:day    xquo.calendar.day.view/calendar-day
       :year   xquo.calendar.year.view/calendar-year
       :picker xquo.calendar.picker.view/calendar-picker})

(def segmented-tab
  "Segmented tab [props label] with numeric :size 28/20, :type :grey/:dark-grey,
  :selected?, :disabled?, :blur?, :on-press, :style and :label-style.
  Optional :symbol is tagged :icon/:image/:emoji using standard content maps."
  xquo.components.segmented-tab.view/segmented-tab)

(def camera
  "Camera UI; host owns capture/crop and system insets.
  camera.shutter: :action :shutter/:confirm, :on-press, :disabled?, :style.
  camera.controls: :zoom, :zoom-options, :on-zoom-change, :flash?, full maps
  :flash/:flip/:shutter, :orientation :vertical/:horizontal, :style.
  camera.crop: :confirm shutter props, :rotate Button props, :orientation, :style.
  Composes existing Button and Segmented tab on the camera's dark photo surface."
  #js {:shutter  xquo.camera.shutter.view/shutter
       :controls xquo.camera.controls.view/controls
       :crop     xquo.camera.crop.view/crop})

(def card
  "card.banner and card.wallet take :title, :description, standard :image,
  :on-press and :style. Wallet additionally accepts :on-dismiss.
  Actual image content is supplied by callers; theme styling is internal."
  #js {:banner xquo.card.banner.view/banner
       :wallet xquo.card.wallet.view/wallet})

(def tag
  "tag.standard [props label]: :content image/icon/emoji, :size 24/32, :selected?,
  :disabled?, :blur?, :on-press, :style. Inherits theme/context.
  tag.permissions: :groups of {:tokens [image-maps] :total optional-count},
  :size 24/32, :locked?, :blur?, :more-groups?, :separator-label, optional semantic
  :background-color and :style. Pure display; host owns permission decisions.
  tag.asset: :variant :token/:collectible, :image, :label, :size 24/32,
  :option nil/:hold/:add/:loading, :blur?, :on-press, :style."
  #js {:standard    xquo.tag.standard.view/tag
       :permissions xquo.tag.permissions.view/permissions-tag
       :asset       xquo.tag.asset.view/asset-tag})

(def community
  "Community presentation components; domain/eligibility decisions stay in the host.
  community.stat [props value]: :icon map, :style.
  community.role: :label, :tag (ContextTag props plus :label), :style.
  community.channel-action: :title, :icon, :counter, :disabled?, :on-press, :style.
  community.channel-actions: :items of action props, :columns (3), :style.
  community.main-card: :title, :description, :avatar, :cover, :permissions,
  :stats, :tags, :loading?, :on-press, :style.
  community.token-gating: :variant :community/:channel-list/:channel-detail/
  :community-detail; :sections containing :groups containing :rows of :items
  (Tag Asset props), optional row :prefix. Accepts full :header, :info, :button,
  :information maps, :footer-text, :separator-label and :style.
  Channel/detail variants are content only; the host Xquo drawer owns its chrome."
  #js {:stat            xquo.community.stat.view/stat
       :role            xquo.community.role.view/role
       :channel_action  xquo.community.channel-action.view/channel-action
       :channel_actions xquo.community.channel-actions.view/channel-actions
       :main_card       xquo.community.main-card.view/main-card
       :token_gating    xquo.community.token-gating.view/token-gating})

(def dapp
  "dApp presentation; host owns navigation, transactions and approval decisions.
  dapp.list-card: :variant :account/:dapp/:smart-contract/:approval, full :avatar,
  :title, :description, :description-icon, :action, :blur?, :on-press, :style.
  dapp.group: full :header and list-card :items, :blur?, :style.
  dapp.activity [props & description]: :title, :timestamp, :icon, optional :status
  (:pending/:confirmed/:finalized/:error), :counter, :unread?, :blur?, :on-press,
  :style. Description children can compose existing ContextTags."
  #js {:list_card xquo.dapp.list-card.view/list-card
       :group     xquo.dapp.group.view/group
       :activity  xquo.dapp.activity.view/activity})

(def avatar
  "Avatar with one tagged content slot. Theme/color inherit from Xquo context.

  :size        16, 20, 24, 28, 32, 36, 48, 56, 64, or 80; default 32.
  :shape       :circle (default), :rounded-square, or :hexagon.
  :content     {:type :image :image {:source ...}} (or :url, optional :style),
               {:type :icon :icon {...}}, {:type :emoji :text ...},
               {:type :emoji-image :image {...}}, or
               {:type :initials :text ...}. :type selects the renderer.
               Omit content for an empty frame. Images fill the whole shape;
               transparent pixels reveal the resolved context/color prop color.
  :color       Xquo color family; defaults to context color.
  :variant     :solid, :soft (default), or nil for no fill.
  :border      Optional :solid or :dashed edge.
  :blur?       When truthy, adapts styling to a blurred backdrop; never creates a blur view.
  :badge       Optional {:status :online/:offline/:pending/:confirmed/:finalized/:error},
               {:icon {...}}, or
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
(def empty-state
  "Centered :title and :description (text/Hiccup), optional :image native source,
  :image-tint and :image-spacing (default 12). :buttons is [primary secondary],
  each accepting the full Button props plus :label. Defaults: size 32, Primary
  first, Grey second (Dark Grey in plain dark). Two actions share their natural
  widest width. :blur? styles the secondary action; the parent owns blur.
  :style and other native view props apply to the root. No placeholder image."
  xquo.components.empty-state.view/empty-state)
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
(def drawer
  "Composable TrueSheet drawer pieces. screen-options accepts :theme, :blur?,
  :handle? and TrueSheet options. content takes matching props and children;
  it owns one backdrop and native handle spacing, never an OS navigation bar.

  top: :title, :description, tagged :content (icon/image/avatar), :context-tags,
  :button, :counter, :title-icon, :description-icon, :trailing-icon, :compact?.
  action: :title, :description, tagged :content (icon/image), :action
  (nil/:arrow/:toggle/:input), :input, :selected?, :on-select, press callbacks.
  actions: :items of action maps, with group spacing. label: :label/:counter.
  body: string or Hiccup children. bottom-actions: full :buttons maps with
  :label, :description {:position :status :text}, real :context-tag, :scroll?.
  bottom-context: :left/:right button maps. permissions-context: children or
  :button.
  Pieces accept :blur?/:style. Button maps preserve caller overrides.
  Compose these pieces inside content in an existing TrueSheet navigator."
  #js {:screen_options      xquo.drawer.sheet/screen-options
       :content             xquo.drawer.content.view/content
       :top                 xquo.drawer.top.view/top
       :action              xquo.drawer.action.view/action
       :actions             xquo.drawer.actions.view/actions
       :divider             xquo.drawer.divider.view/divider
       :label               xquo.drawer.label.view/label
       :body                xquo.drawer.body.view/body
       :bottom_actions      xquo.drawer.bottom-actions.view/bottom-actions
       :bottom_context      xquo.drawer.bottom-context.view/bottom-context
       :permissions_context xquo.drawer.permissions-context.view/permissions-context})
(def drawer-action xquo.components.drawer.drawer-action.view/drawer-action)
(def drawer-label xquo.components.drawer.drawer-label.view/drawer-label)
(def drawer-handle xquo.components.drawer.top.view/drawer-handle)
(def drawer-top xquo.components.drawer.top.view/drawer-top)
(def emoji-picker xquo.components.emoji-picker.view/emoji-picker)
(def emoji-picker-header xquo.components.emoji-picker.view/emoji-picker-header)
(def emoji-picker-footer xquo.components.emoji-picker.view/emoji-picker-footer)
(def icon xquo.components.icon.view/view)
(def info-message
  "[info-message props content]: :status :default/:success/:warning/:error,
  numeric :size 13 (default) or 11, :blur?, :color, full :icon map, :style/native
  view props. Legacy :size :default/:tiny and :background :blur remain accepted;
  explicit :blur? takes precedence. Parent owns blur. Text wraps naturally."
  xquo.components.info.info-message.view/info-message)
(def information-box
  "[information-box props optional-content]: :status :default/:info/:error,
  also :success/:warning; optional :title, :description (text/Hiccup), :icon map,
  :button (full Button props plus :label; defaults to 24 and Primary/Danger),
  :close-button (pressable props and optional :icon map), :blur?, :style/native
  view props. :description takes precedence over content. Numeric :size 13/15
  controls body text; legacy :use-15-font? remains accepted. Parent owns blur."
  xquo.components.info.information-box.view/information-box)
(def list-info-box
  "List container with optional :title, :items (full existing List item maps),
  :style and native view props. Compact rows reuse List and preserve supplied
  content/icons/actions. Inherits light/dark theme; no fixed example content."
  xquo.components.info.list-info-box.view/list-info-box)
(def input
  "Input with numeric :size 32/40, :value or :default-value, :on-change-text,
  :label/:max-length, :placeholder, :blur?, :error?, :disabled?, and :clearable?.
  :content is {:type :icon :icon {...}} or {:type :image :image {...}}.
  :button accepts full Button props plus :label; defaults to Outline/24.
  :style targets the root, :field-style the bordered field. Flex is caller-owned.
  :multiline?, :min-height/:max-height, :font, :input-ref and :auto-focus? are
  supported. Other native TextInput props/events pass through. Clear emits
  on-change-text with an empty string, then optional :on-clear. Parent owns blur."
  xquo.input.text.view/input)
(def search-input
  "Search field, 32 px high. :value or :default-value, :on-change-text,
  :placeholder, :clearable? (true), :disabled?, :blur?, :input-ref,
  :auto-focus?, :on-clear and native TextInput props/events. :style is the root.
  :tags contains {:tag {...}} or {:type :context :context-tag {...}}; :type defaults
  to :standard. Cursor and selection highlight follow the customization color;
  tag maps accept full props plus :label, default size 32. Tags may be mixed.
  Clear emits on-change-text with an empty string, then on-clear; the host removes
  controlled tags there. Clear is hidden when text and tags are empty.
  Parent owns blur; focus and selection stay native."
  xquo.input.search.view/search-input)
(def profile-input
  "Borderless Profile Input, 42 px high with semibold 19 px text. :value or
  :default-value, :on-change-text, :placeholder, :max-length, :disabled?, :blur?,
  :input-ref, :auto-focus?, :style (root), and native TextInput props/events.
  Shows the edit icon at rest and a character counter while focused when
  :max-length is supplied. The native field enforces that limit. Parent owns blur."
  xquo.input.profile.view/profile-input)
(def profile
  "profile.card: required :name and :avatar (full Avatar props, size 48 default).
  Optional :description (15px monospace), :name-icon (full Icon props, size 20),
  :tag (full standard Tag props plus :label), :button (full Button props plus
  :label), :color (customization family, inherited by default), :style and native
  View props. Tag/Button default to size 32 and blur treatment. Always a dark
  content context with accent fill at 40%; the parent owns any backdrop blur.
  No emoji hash or identicon ring. Root is display-only; child controls own presses.

  profile.tile: shared community/account tile. :title, :description, full :avatar
  props (48 px default), optional :description-props, :cover {:type :image :image
  {...}} or {:type :color :color optional-customization-family}, :footer Hiccup,
  :loading?, :disabled?, press callbacks and root :style/native props.
  Omit :cover for the plain account layout. 176 px cell includes 8 px spacing
  around a 160 px card. Cover color inherits context at 20% opacity; theme-aware
  card body. Loading disables interaction and substitutes measured placeholders.

  profile.link-card: :title, :description, :content {:type :icon :icon {...}} or
  {:type :image :image {...}}, optional :color (inherits customization context),
  :disabled?, press callbacks and native root props. Height 88, radius 16;
  host controls width (Figma reference 160). Shared animated press feedback.
  Color tints the radial fill at 6%; full icon/image props are preserved.
  Icons default to size 20 and theme foreground; images default to 20x20 with
  contain scaling. Override icon size/color or image style/resize-mode as needed.

  profile.showcase-card: :title, :disabled?, press callbacks, root :style/native
  props, and Hiccup children in a 32px preview slot. 176x104 cell includes 8px
  spacing around its 160x88 surface. Theme-aware fill, radius 16, shared press
  motion; host supplies preview content without an entity-type enum."
  #js {:card          xquo.profile.card.view/card
       :tile          xquo.profile.tile.view/tile
       :link_card     xquo.profile.link-card.view/link-card
       :showcase_card xquo.profile.showcase-card.view/showcase-card})

(def list
  "list.item: :title, optional :description, :title-props/:description-props,
  :type (:default/:outline/:grey/:elevated), :compact? (48px; otherwise 56px),
  tagged :content (avatar/image/icon/custom), ordered :left/:right slot entries,
  :caption {:text :icon :text-props}, :active?, :selected?, :disabled?, :blur?,
  :style and pressable callbacks. Heights are minima; richer content can grow.
  Slots support icon/button/selector/counter/avatar/image/text/values/tag/custom.
  Each receives its full props map; button adds :label, counter/text add :value.
  Values: :primary, :secondary text or {:type :change :direction :positive/:negative
  :percentage :value}, optional :layout :balanced and text-props overrides.
  :custom renders :view Hiccup. Preview-list stays a separate pending component.
  Theme/color inherit context; parent owns blur. Omitted disabled? stays nil.
  Static without :on-press/:on-long-press. Child controls retain their own callbacks.
  Selected rows show a check when no explicit right slot is supplied.
  list.view retains the legacy :items/section container while callers migrate."
  #js {:item xquo.list.item.view/item
       :view xquo.components.list.view/view})
(def result-list xquo.components.list-items.result-list.view/result-list)
(def simple-item xquo.components.list-items.simple-item.view/simple-item)
(def navigation
  "navigation.bottom-nav-tab: Scroll treatment, :icon props (24 px default),
  :active?, :badge, :disabled?, native press callbacks and root :style.
  :badge is {:counter {... :value n}} by default, or {:type :notification}.
  Reuses Icon and Counter; colors inherit theme/customization context.
  Reference size 90x40; caller may override width/flex. Only the pressed background
  animates, with no movement. Parent owns the blurred backdrop. No Soon variant.

  navigation.bottom-nav: :items is a vector of up to five bottom-nav-tab props maps.
  Host owns active state and callbacks. Always dark, including its tabs, with
  Neutral 100 at 70% and its own backdrop blur; customization color is inherited.
  Optional :background-color accepts a resolved native color for the blur tint.
  :blur-amount defaults to 40.
  56 px high with 8 px padding and 18 px radius. Width fits its parent, capped at
  68 px per tab
  plus padding. Root :style/native View props supported; host owns safe-area placement.

  navigation.page-nav [props & children]: 56 px row; :left is complete Button props
  or Hiccup, :right is a vector of those. Buttons default to size 32.
  :center is nil, custom Hiccup (centered), or a map with :title, :description,
  inline :caption, :content {:type :avatar/:image/:icon :avatar/:image/:icon {...}},
  :icons (Icon props), :text-align (:left default or :center), :on-press,
  :on-press-in/:on-press-out, :disabled? and :style. Image :size defaults to 24,
  Avatar to 32, Icon to 20. Only title text changes color while its region is pressed.
  :blur?/:photo? select control treatment; parent owns the overall backdrop.
  Root :style/native View props supported. Children compose below the row.
  No implicit back navigation, action truncation or system status-bar inset."
  #js {:bottom_nav_tab xquo.navigation.bottom-nav-tab.view/bottom-nav-tab
       :bottom_nav     xquo.navigation.bottom-nav.view/bottom-nav
       :page_nav       xquo.navigation.page-nav.view/page-nav})
(def notification
  "Blurred toast and in-app notification components. Wrap navigation in provider;
  use-notifications supplies show! (Hiccup + optional duration/on-dismiss) and dismiss!.
  Timers pause during touch; upward swipe dismisses, downward drag springs back."
  #js {:toast             xquo.notification.toast.view/toast
       :in_app            xquo.notification.in-app.view/in-app
       :provider          xquo.notification.host.view/provider
       :use_notifications xquo.notification.host.view/use-notifications})
(def page-nav xquo.components.page-nav.view/page-nav)
(def page-nav-left-action xquo.components.page-nav.view/nav-left-action)
(def page-nav-title xquo.components.page-nav.view/nav-title)
(def text-combinations
  "title: :title (string or Text children), :title-props, optional :caption and
  :caption-props, :icons (full Icon maps), :tag (full Tag props plus :label),
  :right {:type :button/:tag/:text/:custom, :button/:tag/:text/:view ...}, :blur?.
  Right text accepts :value plus Text props. :type :username applies the name
  status-icon spacing; omit for ordinary/channel titles. Font defaults to 27px.
  Root :style/native View props pass through. Captions use measured 15px baseline.

  page-top: same heading props, :content {:type :avatar/:image/:icon, ...},
  :description (string or Hiccup), :description-props, :description-spacing (8),
  :search (full Search Input props), or :footer Hiccup with :footer-style.
  Adds 20px horizontal/12px vertical padding; footer adds its measured spacing
  and a bottom divider. Use spacing 6 for collection/community identity rows.
  Legacy :button and :leading-image remain supported by this same implementation.
  Theme-aware; :blur? changes tokens only and the parent owns backdrop blur."
  #js {:title    xquo.components.text.combination.view/title
       :page_top xquo.components.text.page-top.view/page-top})

(def page-top
  "Compatibility export of text-combinations.page-top; same implementation."
  xquo.components.text.page-top.view/page-top)
(def category xquo.components.settings.category.view/category)
(def settings-item xquo.components.settings.item.view/settings-item)
(def section-label xquo.components.settings.section-label.view/section-label)
(def section-title xquo.components.settings.section-title.view/section-title)
(def sort-item xquo.components.settings.sort-item.view/sort-item)
(def disclaimer
  "Disclaimer with checkbox and label child. :selected?, :disabled?, :blur?,
  :on-select (next boolean), optional full :icon map, root :style/native props.
  Includes Figma outer spacing (top 12, horizontal 20, bottom 8). Missing selected?
  is uncontrolled; a supplied nil is controlled off. Parent owns backdrop blur."
  xquo.components.selectors.disclaimer.view/disclaimer)
(def filter
  "Filter selector: controlled :selected?, :size 24/32 (32 default), :blur?,
  :disabled?, full :icon map (unread default), :on-select (next boolean),
  press callbacks and native props. Optional label child; omit for icon-only.
  Color inherits context; parent owns blur. :style applies to the control."
  xquo.components.selectors.filter.view/filter)
(def options xquo.components.selectors.options.view/view)
(def selector
  "Selector: :type :toggle (default), :radio, :checkbox or :filled-checkbox;
  :selected?, :disabled?, :blur?, :on-select (next boolean), press callbacks,
  :hit-slop (6 default), :style and native Pressable props. Supplied selected?
  is controlled, including nil as off; omission enables internal state.
  Toggle is 30x20; other types 20x20. Color inherits context; parent owns blur."
  xquo.components.selectors.selector.view/selector)
(def context-tag xquo.components.tags.context-tag.view/context-tag)
(def title-input xquo.components.title-input.view/title-input)
(def unified-tab xquo.components.unified-tab.view/view)
(def unified-tab-content xquo.components.unified-tab.view/unified-tab-content)
(def text xquo.components.text.view/text)

(def markdown
  "markdown.text: :type defaults to :body; also :body-bold, :h1, :h2, :h3,
  :h3-regular. Children are text/inline Text Hiccup, not Markdown source. :style
  styles the container; :text-style and native Text props style its content.

  markdown.list-item: :title/:description accept a string or ordered vector of
  strings and Hiccup controls; controls retain their own props. :step accepts full
  Step props plus :value; omitted gives a bullet. :blur? styles the marker; the
  parent owns blur. :title-props/:description-props customize text; :style the row."
  #js {:text      xquo.markdown.text.view/text
       :list_item xquo.markdown.list-item.view/list-item})

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

(def link-preview
  "Link card: :title, :description, :url, full :favicon props, :image {:source/:url}.
  :type defaults to :default (139px media); :detail uses square media; :url renders
  only the wrapping customization-colored URL. :on-press and native press props;
  :style controls width. Host owns fetching metadata and opening links."
  xquo.link-preview.view/link-preview)

(def url-preview
  "56px composer preview: :title, :url, full :favicon props, optional :image map,
  :channel {:community :name}, :on-remove, :loading?, :loading-label (defaults to
  Generating preview), :style. Host owns fetching and removal."
  xquo.url-preview.view/url-preview)
