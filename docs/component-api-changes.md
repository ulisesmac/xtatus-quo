# Xquo component API changes

Track this integration's library changes here before migrating application callers.
**Proposed** entries describe the intended API, not behavior already available in code.
Preserve unrelated work and migrate the app separately after the relevant library change lands.

Design illustrations and content icons are examples, not fixed component content.
Callers supply image sources and icon maps; components own their layout and defaults.
Functional symbols (such as dropdown arrows, close, playback and selection/status
indicators) retain their meaning. The content audit found no fixed design images
outside showcases. Alert banners now accept an optional standard `:icon` map,
defaulting to the existing alert symbol; existing calls need no migration.

## Index

| Component | Library status | Application migration |
| --- | --- | --- |
| [Button](#button) | Implemented; Photo now includes real backdrop blur | Active `src/fc` callers migrated |
| [Effect view](#effect-view) | Blur uses only `:blur-amount` | Active blur callers migrated |
| [Page navigation](#page-navigation) | New `navigation.page-nav` with composable slots and title press feedback | Active app and demo callers migrated; legacy exports retained |
| [Bottom nav tab](#bottom-nav-tab) | Scroll variants implemented; Soon excluded | New `navigation.bottom-nav-tab`; app navigation unchanged |
| [Bottom nav](#bottom-nav) | Blurred, symmetric bar with up to five tabs | New `navigation.bottom-nav`; host owns navigation and safe-area placement |
| [Selectors](#selectors) | Base selector, Disclaimer and Filter corrected; showcases added | `:background` → `:blur?`; `:glass?` removed; library consumers migrated |
| [Text combinations](#text-combinations) | Shared Title/Username/Channel composition and reworked Page Top | Existing `page-top` export points to the new implementation |
| [Input](#input) | Main Input reworked; showcase added | Content union, root/field styles, full button maps and native text ownership; active callers migrated |
| [Search input](#search-input) | Implemented with Input reuse and showcase | New `search-input`; existing public `input` remains unchanged |
| [Link card](#link-card) | Corrected Profile Link card geometry, gradient and showcase | Active contact cards migrated to neutral content props |
| [Link preview](#link-preview) | Implemented; Fold showcase checked | New `link-preview`; separate from Profile `link-card` |
| [URL preview](#url-preview) | Implemented; Fold showcase checked | New `url-preview`; host owns metadata/loading/removal |
| [Showcase card](#showcase-card) | Profile card with composable preview content | New `profile.showcase-card`; four showcase examples |
| [Profile tile](#profile-tile) | Shared Community/Account card and loading states | New `profile.tile`; no separate entity variants |
| [Profile card](#profile-card) | Single card without emoji hashes or identicon rings | New `profile.card`; showcase added |
| [Profile input](#profile-input) | Implemented; Fold showcase verified | New `profile-input`; existing `title-input` remains unchanged |
| [Drawer](#drawer) | Composable pieces and TrueSheet showcase implemented | Bare drawer caller migrated; old hyphenated exports retained |
| [Avatar](#avatar) | New component implemented; visual parity work remains | No older exported avatar API to replace |
| [Color picker](#color-picker) | `:blur?` explicitly forces dark + blur for its swatches | Existing prop retained; no call-site migration required |
| [Counter](#counter) | Figma geometry/colors implemented; separate showcase | Existing calls remain valid; color stays context-only |
| [Step](#step) | Figma geometry/colors implemented; separate showcase | Replace `:background :blur` with `:blur? true`; embedded callers migrated |
| [Divider label](#dividers) | Corrected geometry; showcase added | `divider-label` → `divider.label`; active callers migrated |
| [Divider line](#dividers) | Added dashed/blur variants; showcase added | `divider-line` → `divider.line`; active callers migrated |
| [Divider new](#dividers) | New component and showcase | Use `divider.new` |
| [Divider strength](#dividers) | New component and showcase | Use `divider.strength` |
| [Option card](#option-card) | Unified shell component and showcase | Replace small/big exports with `shell.option-card` and `:variant` |
| [Audio](#audio) | Implemented; A14 captures shared | New grouped API; host owns audio |
| [Browser](#browser) | Implemented; A14 captures shared | New grouped API + Favicon dependency |
| [Banners](#banners) | Implemented; A14 captures shared | New grouped API; semantic/context colors |
| [Calendar](#calendar) | Implemented; A14 captures shared | New grouped API; device English/Spanish locale |
| [Camera](#camera) | Implemented; A14 captures shared | New grouped API + Segmented tab; host owns capture |
| [Cards](#cards) | Implemented; A14 captures shared | New grouped API; actual illustration assets |
| [Community and Tags](#community-and-tag-dependencies) | Implemented; A14 captures shared | New grouped APIs; host owns permission decisions |
| [dApps](#dapps-and-avatar-dependency-corrections) | Implemented; A14 captures shared | New grouped API; Avatar fixes and activity statuses |
| [Code](#code--postponed) | Postponed by user | No implementation in this pass |
| [Dropdowns](#dropdowns) | Button/input triggers and showcases | New exports; network dropdown skipped |
| [Info components](#info-components) | Implemented; layout/color corrections and three showcases | Existing calls remain compatible; prefer numeric size and :blur? |
| [Empty state](#empty-state) | Layout/button corrections and showcase implemented | Existing callers remain compatible |
| [List item](#list-item) | Unified row/card API and showcase implemented | Old `xquo/list` container moves to `xquo/list.view`; new rows use `xquo/list.item` |
| [Markdown](#markdown) | Text/list-item and showcases implemented; Fold verified | New `markdown.text` and `markdown.list-item`; no parser or existing API migration |

## Markdown

`xquo/markdown.text` takes ordinary text/inline Text Hiccup children and `:type`
(`:body` default, `:body-bold`, `:h1`, `:h2`, `:h3`, `:h3-regular`). It owns the
measured paragraph/heading padding. `:style` targets its container, `:text-style`
targets the native text, and native Text props such as `:selectable` pass through.

`xquo/markdown.list-item` accepts `:title`, `:description`, optional `:step`
(the complete Step props map plus `:value`), and `:blur?`. Omit `:step` for bullets.
Use Step `:type :complete` for the highlighted Figma variant; customization color
comes from context. `:title-props`/`:description-props` customize text; `:style`
targets the row. Text fields accept strings or ordered vectors mixing strings
with Hiccup controls. Inline rows supply 4 px gaps and wrap when needed; controls
retain their own props, callbacks, and blur flags.

```clojure
[xquo/markdown.text {:type :h2} "Community guidelines"]

[xquo/markdown.list-item
 {:step        {:value 1 :type :complete}
  :title       "Be respectful"
  :description ["Ask"
                [xquo/context-tag {:size 24 :image-source profile} "Alex"]
                "for help."]}]
```

Sources: Markdown / List `2500:42470` (54 variants) and Text markdowns
`2500:42648` (12 variants). Lists use 20 px horizontal padding, 20 px markers,
8 px marker gap, 2 px title/description gap, and existing 13 px typography.
Bullet dots are 7 px, neutral-40 light, neutral-50 dark, white-40 dark blur.
Single-content rows are generally 32 px; two text lines are 50 px and a title
with 24 px inline controls is 54 px. Source-specific 36 px Step variants retain
their measured padding. Text blocks reuse the six measured typography tokens,
the Figma text-box line heights (32/26/22/18 px), and per-type vertical padding.
These are rendering pieces, not Markdown parsers.

Fold development checks covered light/dark text, plain Step/bullet rows,
highlighted customization color, parent-owned dark blur, inline Tag/Button,
wrapping, and the tag's press callback. Native layout measurements were 56 dp
for H1, 42.286 dp for H2, 50.286 dp for a plain two-line list row, and 54.095 dp
for the inline-tag row; fractional differences reflect the Fold's pixel rounding.
Scoped lint passed and the active watcher compiled these namespaces. Screenshots
were captured for review; this does not claim a pixel comparison of every variant.

## Selectors

Existing public names stay: `xquo/selector`, `xquo/disclaimer`, `xquo/filter`.
No duplicate implementation. Replace `:background :blur` with `:blur? true`;
remove `:glass?`. These controls do not create blur views; the parent owns blur.
The list, drawer and settings selector adapters now forward `:blur?`. Sort-item
and Options keep using the same corrected Filter/Radio components.

```clojure
[xquo/selector {:type      :checkbox
                :selected? selected?
                :disabled? disabled?
                :blur?     blur?
                :on-select set-selected!}]

[xquo/disclaimer {:selected? accepted?
                  :on-select set-accepted!
                  :icon      {:name :icon/lock}}
 "I agree with the community rules"]

[xquo/filter {:size      24
              :selected? unread?
              :icon      {:name :icon/unread}
              :on-select set-unread!}
 "Unread"]
```

Base Selector retains `:toggle` (default), `:radio`, `:checkbox`, and
`:filled-checkbox`. Supplied `:selected?` is controlled, including nil as off;
omission enables internal state. On select emits the next boolean. Existing
press callbacks are preserved; caller `:hit-slop` overrides the default 6.
Radio's existing toggle callback behavior is retained. Context owns accent color.

Filter is controlled and gains the same `:on-select` boolean callback alongside
`:on-press`. It supports `:disabled?`, sizes 24/32 (default 32), full Icon props and
an optional label child. Icon-only now works with a single props argument. Default
icon is the existing unread icon at both sizes instead of a placeholder at 24.

Disclaimer forwards disabled state to its checkbox and applies opacity once.
It includes the source's outer padding: top 12/horizontal 20/bottom 8. Root `:style`
now targets that outer layout; the inner surface is radius 12 with a 1px border
and effective 12px insets (native padding 11 plus border 1). Text is Regular13/18.
Light+blur has no separate Disclaimer design; it retains the light surface while
its Checkbox uses the supported light+blur tokens.

Figma references: Selectors `370:18368`, Disclaimer `2638:40983`, Filter Selector
`9961:166314`. Toggle 30×20, handle 16, travel 10; radio/checkbox 20×20, dot 14,
checkbox radius 6, off outline 1.2, disabled opacity 30%. Dark and blurred off radios
now have transparent interiors; only light non-blur off radio retains White40.
Checkmark retains the existing 8×6 path/1.5 stroke, positioned at the measured
6/7 inset. Filter's native border is compensated in its padding; labels use
Medium 13/18 at 24 and Medium 15/22 at 32, preserving source widths/insets.

Shared Button pressing and existing toggle/radio transition timings remain.
Figma motion inventory is complete with no animated nodes. Indeterminate-labelled
variants have opacity 0 and are excluded, pending clarification. Reaction,
Formatting, Reactions Selector and Selector Button are separate future components.

Showcases: **Selector**, **Disclaimer**, **Filter selector**, alphabetically listed.
Controls cover type/size/content, selected, disabled, blur and customization color.
Selector additionally shows off/on and both disabled states together.

A14 verification after the concurrent blur benchmark released the device:
Toggle light state gallery, Radio and Checkbox dark+blur galleries, Filter32 and
Filter24 selected via its press callback, and Disclaimer light/disabled state.
The native tree reports the Disclaimer checkbox disabled when requested; runtime
logs remained empty. Scoped lint and the active Shadow build pass. Filled Checkbox
and the remaining theme combinations are source-grounded, not an exhaustive device
screenshot comparison of all Figma variants.

## Text combinations

All text renders through the existing `xquo.components.text.view/text`. Shared
composition lives alongside it under `xquo.components.text.combination.*` and
`xquo.components.text.page-top.*`. The old Page Top implementation is removed;
`xquo/page-top` is a compatibility export of the new function. Existing app calls
and the Community token-gating consumer use that one implementation.

```clojure
[xquo/text-combinations.title
 {:title "Alex"
  :type :username
  :caption "Alex Morgan"
  :icons [{:name :icon/check-verified
           :color (colors/get-color :color/success)}]}]

[xquo/text-combinations.page-top
 {:title "Community"
  :content {:type :avatar
            :avatar {:content {:type :image :image {:source portrait}}}}
  :description "Share updates and ideas with your community."
  :right {:type :button
          :button {:icon {:name :icon/add} :on-press add!}}
  :search {:value query :on-change-text set-query!}}]
```

- Title accepts `:title` and full `:title-props`, optional `:caption` and
  `:caption-props`, `:icons` (full Icon maps), `:tag` (full standard Tag props plus
  `:label`), and `:right` (`:button`, `:tag`, `:text`, or `:custom`). Right text's
  map accepts `:value` and native Text props; custom content uses `:view`.
- Omit `:type` for standard/channel headings. `:type :username` selects the
  measured status-icon spacing; nicknames are the title, full names the caption.
  Plain names/ENS strings need no special enum. Caller-supplied status icons and
  semantic colors keep combinations open; no verification/business logic lives here.
- Page Top adds `:content` with standard `:avatar`/`:image`/`:icon` maps,
  `:description` (string or Hiccup), `:description-props`, and
  `:description-spacing` (8 default; use 6 for collection/community identity rows).
  `:search` accepts the existing Search Input's full map. `:footer` accepts other
  controls as Hiccup, with `:footer-style` for their spacing; choose either search
  or footer. Custom descriptions can compose tags and summary rows without fixed
  crypto/domain names. Address/recovery input components are not reimplemented here.
- Both accept `:blur?` and native root props/`:style`. Blur changes text/child tokens;
  the host owns the backdrop. `:button` and `:leading-image` remain supported for
  existing Page Top callers, mapped to right Button and image content respectively.

Figma roots: Username `8737:148440`, Channel Name `8737:151293`, Standard Title
`8824:151325`, Page Top `8737:151877`. The small Avatar set reuses existing Avatar
rather than introducing another avatar abstraction. Emoji hashes/dashes excluded.
All four complete motion inventories contain no animations; composed Buttons keep
shared pressing behavior.

Measured geometry: 27px Semibold/32px line, caption Medium15/22 at y=10; status
icons20 at y=8 (y=10 with caption); channel icon gap4 and title gap6; username
status gap4 (dark caption gap6); adjacent tag24 at y=6; right action/tag32;
right counter Regular13/18 at y=12 and 20px separation. Page Top uses horizontal
padding20, vertical12, leading32/gap8, description Regular15/22 and gap8.
Search adds 8/20/12 padding and a 1px bottom overlay divider without changing height.
Plain Page Top is56 high; two-line description108; with Search160.

The **Text combinations** demo offers Title, Username, Channel, Page Top,
editable text, all right-slot examples, title tag, Avatar, Search, and theme-aware blur.
A14 captures cover Username in light mode and Page Top with Avatar, two-line
description and Search in light and dark+blur. Runtime logs remained empty;
scoped lint and the active Shadow build passed. Other compositions are source-grounded,
not claimed as an exhaustive device comparison of every Figma permutation.

## Showcase card

```clojure
[xquo/profile.showcase-card {:title    "Communities"
                             :on-press open-communities!}
 [my-community-preview]]
```

One component for Assets, Collectibles, Communities, Accounts, and other content.
No entity `:type`: the host supplies `:title` and Hiccup children. The content slot
is 32px high. Optional `:disabled?`, native press callbacks, and root `:style`/props
are passed through. An omitted press callback gives a display-only container.
Shared Button press motion applies to the inner card. No emoji hashes.

Figma `15671:193129`: 176×104 cell with 8px outer spacing, 160×88 surface,
16 radius, top padding 10, horizontal/bottom padding 12, 22px Semibold 15 title,
and 12px gap before preview. White/Neutral-90 surface, Neutral-100/White title,
and the source's Normal/Light/02 shadow in both themes. Complete motion inventory
has no animated nodes; press feedback follows the library convention.

**PREVIEW_LIST_SLOT_PENDING:** Preview List remains deferred per the earlier
instruction. This card deliberately accepts children; its demo composes existing
Avatars and images with the inspected 32px size, 24px step and 2px clearance.
When Preview List is implemented, replace this demo-only composition with it,
as well as completing the pending `list.item` adapter. The card API stays unchanged.

Demo: **Showcase card**, four content examples, editable title, disabled control,
and shared light/dark switching. Existing generic images are reused.
A14 captures verify Assets in light mode and Accounts in dark mode. Scoped lint
and the running Shadow build passed. No component errors appeared after mounting;
two navigation commands issued before the demo navigator was ready were corrected.

## Link card

`xquo/link-card` → `xquo/profile.link-card`. The existing implementation moved to
`xquo.profile.link-card`; there is only one Link card implementation and export. Replace `:social-network` → `:title`,
`:social-handle` → `:description`, `:gradient-color` → `:color`, and the ambiguous
`:image` map with `:content {:type :icon :icon {...}}` or
`:content {:type :image :image {...}}`. App domain fields are unchanged; the active
profile contact-card composition now supplies these component props.

```clojure
[xquo/profile.link-card {:title       "Website"
                 :description "wanderlight.example"
                 :content     {:type :icon :icon {:name :icon/link}}
                 :on-press    open-website!}]
```

Color inherits the customization context; explicit `:color` accepts an Xquo token
or resolved color. It tints the fill at 6%, fading to white at 2%. Brand examples
are caller-supplied images/colors, not a library enum. The generic Figma link uses
neutral-50; callers can pass that token when desired. Light/dark border and text
use the corresponding palette tokens. Native root props, `:style`, `:disabled?`,
and press callbacks are supported. Image/icon maps preserve overrides. Icons accept any existing `:name`, default to
size 20 and theme foreground, and retain explicit `:size`, `:color`, and `:style`.
Images accept `:source` or `:url`, default to a 20×20 contained image, and retain
native image props, `:resize-mode`, and `:style` overrides. These alternatives
share the same slot; no separate icon/image props compete for it.

Figma set `18670:237695`: 160×88 reference, 16 radius, 12 padding, 20 icon/image,
4 gap, 22 title and 18 description. Width remains host-controlled. Border is an
inside overlay so it no longer adds 1px to content insets/2px to total height.
Title now truncates; description keeps the actual Inter Regular 13 typography
(the Figma style name says Monospace, but its font is Inter Regular).
Radial fill uses Figma's exported tilted transform instead of the previous
axis-aligned CSS gradient. Existing shared button press motion and 20% pressed
border are retained; Figma's complete motion inventory contains no animations.

Showcase: **Link card**, with editable text, icon/image, context color, disabled
state, and light/dark theme switching. No new bundled assets.
A14 checked in both themes, icon and image content, and the press alert.
Scoped lint and the running Shadow compiler passed without warnings; captured
runtime logs contained no errors.

## Profile tile

`profile.tile` unifies Community (`1988:31312`) and Accounts (`2051:32616`).
Both use a 176 px cell with 8 px padding around a 160 × 160 px card. Radius 16,
48 px avatar at (12,12), title at y=68 (Semibold 15/22), description at y=90
(Medium 13/18), and footer at 12 px from the left/bottom. The optional cover is
64 px high; the rounded body starts at y=40. Its avatar has the source's 2 px
body-colored clearance, without an identicon ring. Shadow is the source's
Normal/Light/02 token in both themes. Body is White / Neutral 90.

```clojure
[xquo/profile.tile
 {:title       "Wanderlight"
  :description "Member"
  :avatar      {:content {:type :image :image {:source portrait}}}
  :cover       {:type :image :image {:source cover-photo}}
  :footer      [xquo/tag.standard {:size 24} "Joined"]
  :on-press    open!}]
```

Omit `:cover` for the account presentation; supply a rounded-square Avatar and
`:description-props {:font :font/monospace-13}` for an account address. A color
cover is `{:type :color}` (inherited accent at 20%) or has an explicit `:color`
family. `:footer` accepts existing components or custom Hiccup, including the
previously deferred network selector. No account/community enum, network asset
registry, hidden options button, or notification dot is imposed on consumers.

`:loading?` uses the measured avatar/title/description placeholders and, when a
footer exists, its cover/plain reference placeholder. `:disabled?` dims the card;
loading/disabled prevent interaction. Pressable cards use shared button motion.
Avatar and image props retain their existing maps; `:description-props`, root
`:style`, and native press callbacks pass through. The showcase includes both
examples, image/color covers, semantic Joined/Permissions footers, editable text,
loading, disabled and customization colors. Existing demo images are reused.

Validation: scoped lint and the running Shadow compile pass. A short USB A14
check captured the light community and dark account presentations and confirmed
the card press callback. No runtime errors were recorded. Loading geometry was
checked against Figma source/export; it was not separately captured on-device.

## Profile card

`profile.card` lives in `xquo.profile.card.*`. It reuses Avatar, standard Tag,
Button and Text; the card owns no media loading or account state.

```clojure
[xquo/profile.card
 {:name        "Alex Morgan"
  :avatar      {:content {:type :image :image {:source portrait}}}
  :description "zQ3k83euenmcikw7474hfu73t5N"
  :name-icon   {:name :icon/keycard}
  :tag         {:label "Logged in"
                :content {:type :icon :icon {:name :icon/check}}}
  :button      {:icon {:name :icon/options} :on-press options!}}]
```

Required `:name` and complete `:avatar` props; Avatar defaults to 48 px.
Optional `:description`, complete `:name-icon` props (20 px / White 40% default),
complete `:tag` props plus `:label`, and complete `:button` props plus `:label`.
Tag and Button default to 32 px with blur treatment. A supplied tag and button
can coexist; there is no special logged-in flag suppressing either control.
`:color` overrides the inherited customization family. Root `:style` and native
View props pass through. The root is display-only; the child controls own presses.

Figma Profile card `14754:203615`: 335 px reference width, radius 16,
12 px top/horizontal padding, 48 px header, 8 px before the text, 2 px between
text lines, and 10 px bottom padding. This produces 104 px without a description
and 128 px with one. Name uses Semibold 19 / 26 px; description uses the
15/Regular Monospace token / 22 px, White 60%. The optional name icon has a 4 px
horizontal gap and sits 4 px below the name-row top. Actions use an 8 px gap.

The card always provides a dark content context and uses the inherited accent
at intensity 50 / opacity 40. Its parent owns any blurred backdrop. It does not
hardcode Figma's sample turquoise. Avatar content fills the full 48 px; the
identicon ring and emoji-hash area are intentionally absent.

The missing Keycard icon was ported from Figma `16949:193486` into the existing
SVG icon map as `:icon/keycard`; no icon font rebuild or image dependency is needed.
The showcase provides portrait, name, description, accent, tag, name-icon and
button controls in the shared shell preview.

Validation: scoped lint and development compilation pass. USB A14 captures cover
the description and description-free layouts, the reused status tag and the
registered Keycard SVG. No runtime errors were recorded in the final check.

Profile-page queue: Community and Account cards now share `profile.tile`. Next
review Link card, then implement Showcase card one at a time. Select-profile stacks,
Asset and Collectible cards are outside this requested pass.

## Profile input

`xquo/profile-input` lives under `xquo.input.profile.*`. It accepts `:value` or
`:default-value`, `:on-change-text`, `:placeholder` ("Type something"), optional
`:max-length`, `:disabled?`, `:blur?`, `:input-ref`, `:auto-focus?`, `:style` for
the root, and native single-line TextInput props/events. It shows an edit icon at
rest and the current character count/limit while focused when `:max-length` is
supplied. Native maxLength enforces the limit without rewriting typed text.

Figma Inputs / Profile input `14754:207982` defines 20 variants: 42 px height,
12 px horizontal/8 px vertical padding, semibold 19 px text in a 26 px line box,
20 px trailing gap, 20 px edit icon, and regular 13 px counter. Focus changes the
placeholder token; disabled opacity is 30%. Light/dark blur use the measured
transparent neutral/white tokens. The cursor uses customization color on solid
surfaces and foreground color on blur, as defined in this component; selection
highlight follows customization color. The parent supplies backdrop blur.

The existing `title-input` is a distinct 27 px heading field and remains intact.
Profile Input reuses Text, Icon, typography, and native input layout styles.
Uncontrolled text stays native; only its character count is retained in React state.

Fold development verification: default/filled and focused counter presentations,
native typing and 24-character enforcement, disabled editing, light and dark blur.
The measured row occupies 335 × 42 logical pixels at the reference width. Scoped
lint and the running Shadow compile pass; no runtime errors during the checks.
Figma's 20 variants were inspected; platform-native caret/selection rendering is
retained rather than duplicating the design tool's cursor layers.

## Bottom nav

```clojure
[xquo/navigation.bottom-nav
 {:items [{:icon     {:name :icon/wallet}
           :active?  (= selected :wallet)
           :on-press open-wallet!}
          {:icon     {:name :icon/messages}
           :badge    {:counter {:value 5}}
           :active?  (= selected :messages)
           :on-press open-messages!}]}]
```

`:items` accepts up to five complete Bottom Nav Tab props maps. The host controls
selection, navigation callbacks, positioning, and safe-area clearance. The bar
renders its own backdrop blur; no `:blur?` or extended mode is needed.
Optional `:background-color` overrides the existing blur layer's tint with a
resolved native color. `:blur-amount` defaults to 40. The app currently uses
Neutral 80 at 70% opacity with blur 40 in both themes; no extra layer is added.

Uses Figma `8972:155887` (non-extended, On scroll), retaining the 40 px tabs,
8 px padding on every side, blur 40, and Neutral 100 at 70% regardless of app theme.
The bar provides a dark context for its tabs while inheriting the customization
color. It resolves Xquo palette tokens, not Figma hex samples.
The OS reference area is omitted, leaving a 56 px bar. The requested floating
shape uses radius 18 (tab radius 10 + inset 8).

Responsive extension: equal flexible cells shrink with the available parent width.
The centered width is capped at `16 + 68 × item-count`, using the existing app's
68 px tab-center spacing, so five tabs cap at 356 px. Each pressed fill stays in
its cell and reuses the tab's background-only animation. App-specific navbar
movement and persistent cursor are not copied. Root `:style` remains available.
The showcase uses an unblurred image behind the bar, with count, badge, narrow
container, disabled, theme, and customization-color controls.

## Bottom nav tab

`navigation.bottom-nav-tab` lives under `xquo.navigation.bottom-nav-tab.*`.
It implements only Nav Tab's `Position=Scroll` variants from `265:17944`;
the `Soon`/Tag badge and Default-position variants are excluded.

- `:icon` is the existing Icon props map, with size 24 by default.
- `:active?`, `:disabled?`, native press callbacks, and root `:style`.
- `:badge` is absent, `{:counter {... :value n}}` (default type `:counter`),
  or `{:type :notification}`. Counter retains its existing props/API.
- Reference size 90x40, radius10 when pressed; the host may override width/flex.
  The icon is centered at24px. The 20px badge slot starts12px right and8px above
  the icon. Notification dot is8px with a1px transparent clearance; Counter
  clearance follows the actual measured width. Existing Icon and Counter render
  the content; no placeholder asset or new icon is generated.
- Dark Scroll colors: white40 inactive, white100 active, white5 pressed fill.
  Light context resolves the matching neutral colors. Badge colors use the
  customization context. Parent owns the blurred backdrop.
- Press feedback animates only background color with the shared120/220ms timing;
  there is no scale, translation, or movement.

## Search input

`search-input` reuses the existing Input's native editing, refs, controlled/uncontrolled
value handling and clear behavior. Implementations are grouped under `xquo.input.text.*`
and `xquo.input.search.*`; internal imports were updated. Public `xquo/input` remains valid.

- `:value` / `:default-value`, `:on-change-text`, `:placeholder`, `:input-ref`,
  `:auto-focus?`, `:disabled?`, `:blur?`, `:style`, and native TextInput props/events.
- `:clearable?` defaults to true. Clear appears for nonempty text or tags, emits
  `on-change-text ""`, then calls `on-clear`. The host removes its controlled tags there.
- `:tags`: ordered entries `{:tag {...}}` (`:type` defaults to `:standard`) or
  `{:type :context :context-tag {...}}`, with full existing tag props plus `:label`.
  Size defaults to 32; tags can be mixed. Content remains caller-supplied.
- Borderless, transparent 32 px row, regular 15 px text, 22 px line box and 5 px
  vertical padding. Tags use 8 px gaps; overflowing tags expose their trailing end.
  Clear has a 20 px slot, with 8 px separation normally and 32 px in multi-tag layouts.
- Parent owns backdrop blur. Native focus/selection determine state; no public state enum.
  Cursor uses the current customization color; selection uses that color at 20% opacity.
  Clear is hidden when both tags and text are empty, preserving the requested UX enhancement.

Figma source: Inputs / Search Input `8824:152912`, 120 variants. The native field stays
mounted when tags, focus, or clear visibility change. The showcase includes plain,
context, standard and mixed tags, text selection, disabled state, and theme-aware blur.

The standard Tag's emoji now occupies its existing 12/20 px media slot instead of
letting the native glyph's advance widen the tag. Empty tagged searches retain a
focusable native field without reserving visible text space.

Verified on the Fold development build: 32 px rows, clipped standard tags with
omitted `:type`, native typing/selection, clearing query and tags, and light/dark
blur with the customization color. Input lint and the running Shadow build pass.

## List item

The approved API combines ordinary list rows, values/metrics, media rows and
embedded cards. Quiz, Status and Preview List remain separate components.

- `:type`: `:default` (default), `:outline`, `:grey`, `:elevated`.
- `:compact?`: true selects the 48 px presentation; omission selects 56 px.
  These are minimum heights; multiline/custom content and a caption can grow.
  List item does not expose `:size`; Avatar/Button/Preview sizes stay numeric.
- `:title`, optional `:description`: strings or Hiccup. `:title-props` and
  `:description-props` preserve Text overrides.
- `:content`: tagged `:avatar`, `:image`, `:icon` or `:custom`, with the named
  props map or `:view` Hiccup. Default leading Avatar size is 32; media is 40.
- `:left` / `:right`: ordered slot entries. Supported types are `:icon`,
  `:button`, `:selector`, `:counter`, `:avatar`, `:image`, `:text`, `:values`,
  `:tag` (existing ContextTag), and `:custom` (`:view` Hiccup).
  Named maps retain their props; button adds `:label`, counter/text add `:value`.
- Values take `:primary` and one `:secondary`: a string or
  `{:type :change :direction :positive/:negative :percentage ... :value ...}`.
  Omit direction for neutral metrics. `:layout :balanced` uses equal columns.
- `:caption`: `{:text ... :icon {...} :text-props {...}}` below the main content.
- `:active?`, `:selected?`, `:disabled?`, `:blur?`, `:style` and pressable props.
  Omitted `:disabled?` remains nil. A selected row shows a check when no explicit
  right slot exists. Selection is caller-owned; pressed feedback is internal.
  Nested controls retain their own callbacks and are disabled with their row.
  `:blur?` adapts styling; the parent owns the backdrop blur.

`list.view` retains the old `:items`/section renderer. The active application's
13 old container calls were renamed to `list.view`; their behavior is preserved.
Legacy `simple-item` / `result-list` exports are retained for later migration.

Dependencies: neutral circular initials now use the Wallet Avatar's measured
Neutral/80 5% fill and 70% text (white equivalents in dark/blur). Positive/negative
metric glyphs use the exported Figma paths through the existing icon map, without
new bitmap assets or font files.

### Pending Preview List integration

**PREVIEW_LIST_SLOT_PENDING:** At the user's request, Preview List is deferred to
its own turn. When `preview-list` is implemented, return here: add the `:preview`
slot adapter, wire approval/contact examples that need it, and verify its spacing
inside `list.item`. Until then, previews can be supplied through `:custom` Hiccup.

The showcase exposes one example for each of the ten agreed layout categories,
plus content/slot controls, appearance, compact spacing, caption, active/selected,
disabled and theme-aware blur. It reuses existing demonstration images.

Press feedback runs on the entire animated row with the shared Button constants:
scale 1 → 0.985, translate-Y 0 → 2, press 120 ms/ease-out, release 220 ms/ease.
Background and outline colors transition with the same timing.

Corrected against the cached Figma measurements: 12 px gaps for Channel and
full Outline/Grey rows, 10 px for elevated rows, and 2 px between paired status
icons. Full Outline on dark blur uses white at 5%. The showcase also applies
Channel's muted title, the theme-aware information icon, and Edit's leading icon.

Verified on the A14 development build: all ten showcase categories render,
ordinary rows measure 56 px and compact rows 48 px, press opens the expected
alert, and light / dark blur presentations render. Scoped lint and compilation
passed. This does not constitute a pixel comparison of every Figma combination.

## Info components

Implemented; API recorded before this pass's implementation:

- `info-message [props content]`: existing status/icon/color/style props remain.
  Numeric `:size` is 13 (default) or 11. Legacy `:default`/`:tiny` sizes remain
  accepted. Use `:blur?`; legacy `:background :blur` remains compatible, with an
  explicitly supplied `:blur?` taking precedence. Icons remain caller-supplied.
- `information-box [props optional-content]`: retain status, title, description, icon,
  button, close-button, blur and native view props. `:size` selects 13/15 px body
  text; existing `:use-15-font?` remains supported. Description takes precedence
  over the optional child. Full button props are preserved, defaulting to size 24
  and Danger for error status / Primary otherwise. Close uses `:icon/cross`, with
  optional icon overrides inside `:close-button`. Leading icons stay caller-owned.
- New `list-info-box`: optional `:title`, `:items` containing existing List item
  maps, and native view props/`:style`. Reuses List; applies this container's compact
  row spacing without changing the shared List's defaults. Light/dark variants.

Measured sources: Info Message `2950:43382` (24 variants), Information Box
`2950:44187` (54 variants), List info box `39479:32479` (2 variants).
Message line boxes are 18/16 px with 16/12 px icons and a 4 px gap. Information
Box has a 12 px radius, inside border, 12 px horizontal content inset, 8 px gaps,
11 px compact vertical inset or 10/12 px rich top/bottom insets. Its 24 px button
sits 8 px below copy. List Info Box has radius 16, content insets 16/12 px,
an 8 px title gap, and 2 px between existing 20 px bullet rows.
Semantic/context colors are retained; sample text and icon choices are not fixed
content. Existing success/warning and 15 px box modes remain supported extensions.

## Empty state

Implemented. Retains `xquo/empty-state` and
its existing `:title`, `:description` (text/Hiccup), `:buttons`, `:blur?`,
`:image-spacing`, `:image-tint`, `:style`, and native root props.

- `:image` remains a caller-supplied React Native image source. The component
  displays it in an 80 px box with contain resizing. No image means no illustration.
- `:buttons` remains `[primary secondary]`. Each map accepts the full Button API
  plus `:label`; defaults are size 32, Primary first, Grey second in light/blur,
  and Dark Grey second in plain dark. Caller overrides are preserved. Blur styling
  is inherited by the secondary button using `:blur?`; the parent owns the backdrop.
- Two actions share their natural widest width, with a 12 px gap. One action keeps
  its natural width. No fixed width tied to the example labels.
- Reference geometry: 80 px illustration, 12 px top padding, 12 px illustration-to-copy
  spacing, 20 px horizontal copy/action padding, 2 px title/description gap,
  12 px copy bottom padding, 8 px before actions, 12 px action bottom padding.
  Text uses Semibold 15 and Regular 13 with the existing theme foreground tokens.
- The showcase covers 0/1/2 actions, editable copy/button labels, button overrides,
  an existing generic image, and blur. It uses the existing Button press animation and alerts.

Figma: Empty state `7874:239690`; all nine variants inspected (no actions, one
or two vertical actions; light, dark, dark blur). Reference heights with the
single-line example copy are 158/210/254 px. Light blur and omitted-image states
are supported extensions, not additional Figma references.

## Dropdowns

New exports; no existing dropdown API is replaced. The page-nav dropdown remains unchanged.

```clojure
[xquo/dropdown.button {:type     :outline
                      :size     40
                      :content  {:type :icon :icon {:name :icon/calendar}}
                      :active?  open?
                      :on-press open-menu!}
 "Dropdown"]

[xquo/dropdown.input {:label    "Account"
                     :active?  open?
                     :blur?    true
                     :on-press open-menu!}
 "Main account"]
```

- Button: `:type` is `:outline` (default), `:grey`, `:customisation`, or `:ghost`;
  numeric `:size` is 24, 32, or 40 (default). Customisation uses context color,
  with an optional `:color` override.
- Optional `:content` is `{:type :icon :icon {...}}` or
  `{:type :image :image {:source ...}}`. Standard maps preserve caller overrides;
  image maps also accept `:url`. Omitting the button's text makes a square
  content-only trigger, including the Figma emoji-image treatment.
- Both support `:active?`, `:disabled?`, `:blur?`, and normal Button press callbacks.
  `:active?` is controlled by the host. The host owns menu presentation and selection.
- Button `:photo?` uses the existing Button blur-40 implementation. `:photo?` and
  `:blur?` are alternatives. `:blur?` only changes styling; the parent supplies blur.
- Input has a fixed 40 px field, optional 18 px label, and an 8 px label gap.
  Its `:style` applies to the outer container; the button's `:style` applies to the trigger.
- Showcases are listed alphabetically and use `js/alert` for press actions.
  The Network dropdown is excluded at the user's request.

Figma sources: Dropdowns `56:5001` (378 variants) and Dropdown input
`26956:25449` (48 variants), inspected on 2026-10-11. Sizes, radii, padding,
duotone arrows, state opacity, and semantic tokens come from those variants.
Measured exceptions are preserved: the default 32 px Grey icon is 19 px wide
in plain light/photo contexts; content-only Grey active fills differ from text
variants; the light-blur input without an icon uses 12 px left padding while
enabled and 16 px while disabled. Generic existing artwork replaces demo-only
emoji imagery, as requested for showcase resources.

## Dividers

Status: **implemented**. The following API was documented before implementation.

Requested exports: `xquo/divider.label`, `xquo/divider.line`, `xquo/divider.new`,
`xquo/divider.strength`, implemented under `xquo.divider.*` and re-exported by `xquo.core`.
Existing `divider-label` / `divider-line` calls and implementation imports in active `src/fc`
and Xquo have been migrated. The old exports and namespace paths were removed.
List configuration keys such as `:divider-label` and `:type :divider` remain unchanged.

| Component | API documented before implementation | Change from existing |
| --- | --- | --- |
| `divider.label` | Existing `:title`, `:compact?`, `:chevron`, `:chevron-icon`, `:counter`, `:right`, `:divider-line?`, `:blur?`, `:collapsible?`, controlled `:open?` / `:initial-open?`, press callbacks, animation overrides and `:style` | Export/namespace rename only; preserve behavior and correct measured layout |
| `divider.line` | `{:blur? ... :dashed? ... :style ...}`, plus native view props | Add theme-aware blur and dashed styling; rename export |
| `divider.new` | `{:title "New messages" :style ...}`, plus native view props | New; title supplied by caller, color/theme inherited from context |
| `divider.strength` | `{:status :weak :title "Weak" :style ...}`, plus native view props | New; status is `:informative` (default), `:alert`, `:very-weak`, `:weak`, `:okay`, `:strong`, `:very-strong`; title supplied by caller |

Label keeps compact/collapsible/top-line defaults true. Counter continues to select the left
chevron. Label press and chevron animations remain. New/Strength are static indicators, not
buttons or password evaluators; Strength derives its icon, arc and colors from status.
No raw opacity, gradient or progress knobs. New/Strength have no separate Figma blur variants.
Strength has one fixed design treatment rather than light/dark variants; its showcase uses a
local dark backdrop while the surrounding screen follows the selected theme. Each divider has
a separate, alphabetically listed showcase.

Figma sources inspected on 2026-10-08:

- [Label, 771:26244](https://www.figma.com/design/boq5KmgWVdihEHoVzqHVv6/Design-System----Mobile--Copy-?node-id=771-26244): 32 variants; 34/42 px height; 20 px side padding (16 px left with a left chevron); left gap 2, right-chevron gap 12; 13/Medium type; inside top stroke 1.
- [Line, 12340:207067](https://www.figma.com/design/boq5KmgWVdihEHoVzqHVv6/Design-System----Mobile--Copy-?node-id=12340-207067): 8 variants; 20 px height; 1 px center stroke at y=12; dashed pattern 3 px on / 4 px off. Solid and dashed have different neutral/blur colors.
- [New messages, 5278:132813](https://www.figma.com/design/boq5KmgWVdihEHoVzqHVv6/Design-System----Mobile--Copy-?node-id=5278-132813): 24 variants; 42 px height, padding 12/20/12/60; vertical 5%-to-transparent gradient; text at palette 50 light / 60 dark.
- [Strength, 14826:196016](https://www.figma.com/design/boq5KmgWVdihEHoVzqHVv6/Design-System----Mobile--Copy-?node-id=14826-196016): 7 variants; 42 px height; 20 px side padding, 12 px vertical padding, 4 px icon/text gap; 16 px indicator, radius 6.5 with 1.2 px strokes and 20/40/60/80/100% arcs. Informative has no visible indicator; Alert uses the existing alert icon.

New derives its foreground and vertical gradient directly from the inherited context color:
intensity 50 in light, 60 in dark; the gradient fades from 5% opacity to transparent.
Figma's sample hex colors are not a lookup table or separate palettes to implement.
The showcase changes the context using Xquo color keywords.

Strength uses semantic color families and intensities: Danger 60 for Alert/Very weak,
Warning 60 for Weak, Warning 50 for Okay, and Success 60 for Strong/Very strong.
Informative uses white-40 text and a white gradient. Colored tracks use family intensity 50
at 20% opacity; all backgrounds fade from 5% to transparent. No hex-indexed mappings or
hardcoded sample colors remain in these components.

Rendering corrections: Label uses an independent inside top stroke, so it does not shift the
content; the right chevron gap is 12 px and counters can grow. Line occupies 20 px rather than
21 px, with its centered stroke at y=12 and caller padding overrides preserved; dashed/solid
colors differ as in Figma. Embedded list
lines now receive `:blur?`. Label keeps its existing press/chevron animation; Figma Motion
inspection found no animation data on any of these four sets.

Verification: targeted lint reports zero errors/warnings, and the running Shadow watcher
compiles all 510 files with zero warnings. No automated tests were run.
Native text/gradient rendering still needs device comparison before claiming pixel parity.
Palette overrides intentionally change token-based colors; they are not Figma's original RGBs.

## Skipped-page APIs and review status

These interfaces were proposed before integration; implementations and device review are now
in progress as indicated in the index. Questions are tracked in
[implementation-questions.md](implementation-questions.md). Grouped exports use the existing
JS-object pattern in `xquo.core` (as for `divider` and `shell`).

### Audio

- `audio.soundtrack`: controlled `:progress` (0–1), optional display `:time`, `:state`
  (`:default`, `:deleting`, `:listening`), `:warning?`, `:on-seek` and `:style`.
- `audio.player`: `:state` (`:default`, `:playing`, `:loading`), `:time`, `:progress`,
  `:on-press`, `:on-seek`, `:style`; reuses Soundtrack and Xquo Button.
- `audio.recorder`: controlled recording/drag/playback state, `:time`, `:progress`,
  `:warning?`, `:on-record`, `:on-stop`, `:on-lock`, `:on-delete`, `:on-send`,
  `:on-play`, `:on-pause`, `:on-seek`, and optional `:on-state-change`.
  The host owns actual audio. Transient drag feedback belongs to the control.
  Three separate demos.

### Browser

- `browser.dapp-item`: `:title`, standard `:image` map, `:on-press`, `:style`.
- `browser.favorites`: `:items` containing dApp item props; composes dApp Item.
- `browser.new-tab`: `:title`, `:on-press`, full `:button` props, `:style`.
- `browser.input`: `:value`, `:on-change-text`, `:on-submit-editing`, `:image`,
  `:placeholder`, `:secure?`, `:blur?`, `:style`. Native input owns focus/typing/selection.
- `browser.navigation`: full maps for `:input`, `:avatar`, `:back`, `:forward`, `:reload`,
  plus `:blur?` and `:style`. Parent owns system insets and drawer presentation; actual Button/Avatar reused.
- Dependency `favicon`: tagged `:content` image/initials, `:blur?`, `:style`, for its
  distinct 16 px geometry and 11 px typography. Reuse standard image maps.

### Calendar

- `calendar.day` / `calendar.year`: selected/today/disabled flags where applicable,
  press callback, style, and label child. Context color/theme inherited.
- `calendar.picker`: controlled ISO `:month` (YYYY-MM), `:mode :single|:range`, `:value`
  (ISO date or {:start ISO :end ISO}), `:on-change`, `:on-month-change`, `:today`,
  `:min-date`, `:max-date`, optional `:years`, preferred `:locale`, `:style`.
  Device Spanish/English locale by default, English fallback; Sunday first as in Figma.
  Optional `:first-day-of-week` overrides the default `0` (Sunday).
  Actual Gregorian dates replace inconsistent sample date positions.

### Camera

- `camera.shutter`: `:action :shutter|:confirm`, `:on-press`, `:disabled?`, `:style`.
- `camera.controls`: controlled `:zoom`, `:zoom-options`, `:on-zoom-change`, `:flash?`,
  full `:flash` / `:flip` Button maps, full `:shutter` props, `:orientation`, `:style`.
- `camera.crop`: `:confirm` shutter props, `:rotate` Button props, `:orientation`, `:style`.
  Host owns camera/media; controls reuse actual Xquo Buttons.
- Dependency `segmented-tab`: numeric size 20/28, selected/disabled/blur flags,
  grey/dark-grey type, optional tagged symbol (icon/emoji/image), label child.

### Cards

- `card.banner`: `:title`, `:description`, standard `:image`, `:on-press`, `:style`.
- `card.wallet`: same props plus `:on-dismiss`; actual Xquo dismiss button.
  Two separate showcases with real Figma illustration assets.

### Community and Tag dependencies

- `community.stat [props value]`: optional standard `:icon`, `:style`.
- `community.role`: supplied `:label` and full ContextTag `:tag` map with `:label`.
- `community.channel-action`: `:title`, `:icon`, optional `:counter`, `:disabled?`,
  `:on-press`, `:style`; `community.channel-actions` composes supplied `:items` / `:columns`.
- `community.main-card`: `:title`, `:description`, full `:avatar`, standard `:cover`,
  full `:permissions`, `:stats`, `:tags`, `:loading?`, `:on-press`, `:style`.
- `community.token-gating`: one `:variant` selects `:community`, `:channel-list`,
  `:channel-detail` or `:community-detail`. `:sections` contain supplied title/description
  and `:groups`; groups contain `:rows` of optional `:prefix` and Tag Asset `:items`.
  Optional complete `:header`, `:info`, `:button`, `:information` maps, `:footer-text`,
  `:separator-label` and `:style`. Eligibility/domain decisions remain host-owned.
- `tag.standard [props label]`: numeric 24/32 size, tagged image/icon/emoji content,
  selected/disabled/blur flags, press props/style. Covers the Community prerequisite;
  unrelated Network/List Preview variant is deferred.
- `tag.permissions`: `:groups` of token image maps and optional totals, 24/32 size,
  `:locked?`, `:blur?`, `:more-groups?`, `:separator-label`, optional semantic
  `:background-color` override, `:style`. No eligibility evaluation.
- `tag.asset`: one `:variant :token|:collectible`, standard `:image`, `:label`, 24/32 size,
  `:option` nil/hold/add/loading, `:blur?`, press props/style. Existing icons/loaders reused.

### dApps and Avatar dependency corrections

- `dapp.list-card`: `:variant :account|:dapp|:smart-contract|:approval`, full `:avatar`,
  `:title`, `:description`, `:description-icon`, `:action`, `:blur?`, `:on-press`, `:style`.
- `dapp.group`: supplied `:header` props and `:items` of list-card props, `:blur?`, `:style`.
  Group and approvals Figma compositions share this implementation, with separate demos.
- `dapp.activity`: `:title`, supplied `:timestamp`, standard `:icon`, optional semantic
  `:status`, `:counter`, `:unread?`, `:blur?`, `:on-press`, `:style`; description children
  reuse ContextTag. Host owns transactions, balances, sorting and approval decisions.
- Avatar status badges now additionally support pending/confirmed/finalized/error. At 32 px,
  the activity status is 12 px with zero offset/gap, with proportional sizing elsewhere.
  Existing online/offline behavior is preserved.
- Corrected shared neutral icon Avatar fill/border from its masters: white/N95 fill and
  N20/N80 solid border. Source refs: 24100:239221, 24100:239233, 25276:235268.
- Added unambiguous Avatar `:content {:type :emoji-image :image {...}}` using existing
  emoji sizing/surface rules. This renders Figma bitmap emoji while normal images keep
  their full-shape rendering. Browser Navigation uses its exact Peach asset.
- Activity showcase account tags use exact exported Figma media through ContextTag's
  existing `:image-source` / `:image-sources` props. The fixtures preserve the 20 px media
  slot and 12 px dice/popcorn/fire/pig/moon artwork; no new ContextTag API was introduced.

### Drawer composition boundary

Bottom-sheet frames, handles, shadows, safe areas and presentation belong to the existing
TrueSheet-backed Xquo drawer, which will be reworked separately. Browser Navigation is
88 px of input/actions content, excluding the source grabber/system bar. Token Gating's
channel/detail variants provide content only; their borrowed handles, sheet fills, rounded
top frame, shadow and handle spacing were removed. Its actual Community card variant
retains its own card border/radius. dApp group card borders are component content, not
sheet chrome. No new drawer implementation or sheet behavior was introduced.

### Code — postponed

User explicitly requested skipping Code in this pass (2026-10-09). No implementation.

### Banners

- `banner.standard`: `:title`, optional `:icon` map / `:counter`, `:blur?`, `:on-press`,
  native pressable props and `:style`. Context color at 20%/40% idle/pressed; real Counter.
- `banner.alert`: `:title`, optional standard `:icon` map, full `:button` map with
  `:label`, and `:style`; semantic danger treatment, actual Button defaults to
  danger/24. The icon defaults to alert/16 and preserves caller overrides.
- `banner.global`: `:items` of `{:type :error|:alert :title ...}` and `:style`.
  One row or stack, semantic danger/warning, host-owned system insets.

## Pending component implementations

- Code — explicitly postponed by the user on 2026-10-09.
- Authentication / Context drawers — pages contain no components to implement.
- Unrelated Network/List Preview Tag variant — not needed by this pass's compositions.

## A14 review evidence for this pass

Screenshots are captured from the physical A14 and shared in chat for confirmation.
Local review images are under `/tmp/xquo-review-2026-10-09/`.
No automated tests were added or run. Scoped lint and the running Shadow build are clean;
the debug APK was rebuilt and reinstalled for the new generated Connect icon/font.

Device review found and corrected the Recorder preview halo clipping, New Tab border
clipping, Calendar week keys/range seams, Global banner join seam, and bitmap Avatar emoji.
The neutral icon Avatar color fix was verified against its original masters. Shared icon
primitives that have not been reworked still retain their own geometry/animation behavior
(e.g. Hold check-circle geometry and the existing loading-circle fill); these are tracked
for later icon work rather than replaced with bespoke duplicate icons.

Public APIs and source implementations are available; screenshots are review evidence,
not a claim that the user has already accepted every component/state.

dApp List Card, Group, Approvals and Activity captures were shared on 2026-10-09.
The final Activity connection capture shows the exact dice media and Connect glyph loaded.
Group's five-row example scrolls to its header. Approvals uses the existing outlined alert.
The pass stopped at dApps as requested; exhaustive Activity status/transaction capture remains
outside this completed capture batch.

## Button

Status: **implemented in the library; active host application callers migrated**. Source: [current view](../src/xquo/components/button/view.cljs),
[current styles](../src/xquo/components/button/style.cljs).

### Figma scope

Inspected only the [Button component set, node 4:32](https://www.figma.com/design/boq5KmgWVdihEHoVzqHVv6/Design-System----Mobile--Copy-?node-id=4-32)
on the Buttons page, through Figwright on 2026-10-08. It contains 945 variants.
The selected example was node 168:22464: Grey, Default, 32 px, right icon, light theme, Photo.
Other button-like components on the page are outside this change.

Figma axes:

- Type: Primary, Positive, Grey, Dark Grey, Outline, Ghost, Danger, Black.
- State: Default, Pressed, Disabled.
- Size: 40, 32, 24 px.
- Icon: absent, right, left, both, icon-only.
- Theme: Light, Dark.
- Background: None, Photo, Blur.

There is no separate rounded-shape axis in this set. Shape is derived from size, type,
and whether the button is icon-only. Keep these defaults internal; existing style overrides
can still express additional shapes. Figma's available combinations are reference cases,
not a reason to hide otherwise usable component combinations.

### Current public API

```clojure
[xquo/button {:type     :grey
              :size     32
              :photo?   true
              :icon     {:name :icon/arrow-right :side :right}
              :on-press on-press!}
 "Continue"]
```

| Prop | Current contract |
| --- | --- |
| `:type` | Keep the seven existing values; add `:black`. Default `:primary`. |
| `:size` | Keep 40, 32, 24; default 40. |
| `:color` | Keep the primary color override and context-color fallback. |
| `:blur?` | Select Figma's Blur surface styling; the backdrop owns the blur. |
| `:photo?` | Figma Photo styling plus a clipped 40 px background blur inside the button. No glass effect. |
| `:icon` | Keep the standard icon map and `:side`; default side remains `:right`. No child means icon-only. |
| `:icons` | Keep `{:left {...} :right {...}}`; explicit side maps take precedence over the single-icon placement. |
| `:disabled?` | Keep native interaction disabling and Figma disabled styling. |
| `:on-press`, `:on-long-press` | Keep pressable callbacks. |
| `:on-press-in`, `:on-press-out` | Keep callbacks alongside the component's internal pressed-state updates. |
| `:style` | One final style override for layout and appearance. |
| Child | Keep label strings and custom Hiccup. |

Theme remains inherited from Xquo context. Ordinary pressable props remain forwarded.
No public pressed-state, opacity, radius, foreground-color, or animation-timing knobs are added.

**Surface flag contract:** omit both flags for None; use either `:photo?`
or `:blur?`. They represent alternative Figma contexts, so specifying both is a configuration
error rather than a silently prioritized mode. This does not limit which button types can
be used in either context. Combinations not supplied by Figma must be identified as extensions.

Removed `:glass?` and the button-owned native glass effect. `:photo?` now creates a real
background blur at Figma amount 40 and applies the measured tint on the same rounded surface.
The earlier implementation supplied only tint and incorrectly required every app caller to
provide the missing effect. `:blur?` still adds no blur: that context already sits over a
prepared blurred surface. Neither mode creates glass/refraction.

### Old → current API

| Previous usage | Current replacement | Migration note |
| --- | --- | --- |
| `:background :none` or omitted | Omit `:photo?` and `:blur?` | No synthetic `:none` sentinel needed. |
| `:background :photo` | `:photo? true` | The button now supplies its own 40 px backdrop blur. |
| `:background :blur` | `:blur? true` | Styling flag only. |
| `:glass? true` | Remove | Native glass/refraction is discarded; Photo uses ordinary background blur instead. |
| `:blur-amount`, `:blur-intensity` on button | Remove from button | Photo fixes its internal Figma amount at 40; independent effect views use `:blur-amount`. |
| `:container-style` | Move needed properties into `:style` | Review precedence: the old container style preceded component styles; `:style` is final. Do not blindly combine maps. |
| Existing type, size, icon, icon-side, color and callback props | Keep | No unrelated naming migration. |
| No `:black` type | `:type :black` | New type. The Figma reference currently supplies it only in dark theme. Any light-theme rendering is an explicit extension. |

```clojure
;; Previous
[xquo/button {:type :grey :background :blur :glass? true :on-press on-press!}
 "Continue"]

;; Current
[xquo/button {:type :grey :blur? true :on-press on-press!}
 "Continue"]
```

### Applied visual rules from Figma

| Size | Standard radius | Text | Icon frame | Label-only horizontal padding | Icon-side / other-side padding | Icon/text gap |
| --- | --- | --- | --- | --- | --- | --- |
| 40 | 12 | Inter Medium 15 | 20 | 16 | 12 / 16 | 4 |
| 32 | 10 | Inter Medium 15 | 20 | 12 | 8 / 12 | 4 |
| 24 | 8 | Inter Medium 13 | 12 | 8 | 8 / 8 | 4 |

Widths hug the content; the example's label widths are not fixed component widths.
At 24 px, label-only examples use 2.5 px top / 3.5 px bottom padding, while examples with
icons use 3 / 3. At 40 and 32 px, the corresponding vertical padding is 9 / 9 and 5 / 5.
Account for React Native border sizing when reproducing these dimensions.

- Icon-only Primary, Positive, and Danger are circular. Figma stores radii 24/20/16
  for heights 40/32/24, all of which render as circles. Grey, Dark Grey, Outline, Ghost,
  and Black use the standard rounded-square radius.
- Grey/Photo has no stroke: light fill is white at 40% default and 50% pressed;
  dark fill is `#1B273D` at 40% and 50%. It has a background blur radius of 40.
- Grey/Blur has no stroke or additional blur effect: light fill is `#1B273D` at
  5% default and 10% pressed; dark fill is white at 5% and 10%.
- Outline/Blur stays transparent: light stroke uses `#1B273D` at 10% → 20%; dark
  stroke uses white at 10% → 20%. The dark disabled stroke is white at 5% before
  the component's disabled opacity is applied.
- Primary swaps its default/pressed palette steps between themes: light `#2A4AF5`
  → `#223BC4`, dark `#223BC4` → `#2A4AF5`.
- Dark Outline remains transparent when pressed; the stroke changes from `#202C42`
  to `#303D55`. The old implementation incorrectly added a pressed fill; that fill has been removed.
- Black in dark theme uses `#0D1625` → `#1B273D`. It is not an alias for Dark Grey,
  whose pressed fill differs.
- Disabled variants apply 30% component opacity. Some foreground/stroke values also
  change before that opacity; do not implement every disabled variant as opacity alone.
- With a label on dark Blur, Grey/Outline icons change from white-70 to white-40
  when pressed/disabled. Their icon-only counterparts remain white-100 before disabled
  component opacity. Photo label-adjacent icons use white-40 in dark theme and
  neutral-80-40 in light theme; icon-only foreground rules differ.

The hex colors above are recorded Figma measurements, not a request to hardcode them into
components. Resolve the matching Xquo tokens and verify the configured palette. A semantic
token with a different configured value cannot establish literal color parity by itself.

### Animation retained

Keep the existing [press-feedback constants](../src/xquo/foundations/animations.cljs):
scale 1 → 0.985, translate-Y 0 → 2, press 120 ms/ease-out, release 220 ms/ease.
Pressed/disabled color tables come from Figma and are independent of these retained animations.

### Library and application migration

- Migrated library: `xquo.components.page-nav.view/icon-button` no longer enables glass;
  its existing public surface modes are translated to `:photo?` / `:blur?`.
- Migrated library: `xquo.shell.option-card.view` (big variant) supplies `:blur? true` and retains
  its fixed size 40 and default Grey styling. Photo context is excluded for this shell-only card.
- Migrated library: Input passes `:blur?` to its trailing button. Drawer body/bottom-action
  buttons use the new flags, and bottom actions put their slot layout into `:style`.
- Migrated host app: the two camera buttons in
  `src/fc/screens/profile_customization/view.cljs` now use `:photo? true`.
- Migrated host app: the connect-account buttons in `src/fc/screens/places/view.cljs`,
  `src/fc/screens/directory/view.cljs`, `src/fc/screens/schedules/majors/view.cljs`, and
  `src/fc/screens/my_schedule/schedule/view.cljs` now use `:blur? true`.
- Removed `:glass?` from all six migrated buttons. Reviewed the active `src/fc` imports,
  direct button calls, and nested button maps; no remaining removed button props were found.
  Input's former `:container-style` was subsequently migrated to root `:style` below;
  remaining `:background` props belong to other APIs. Legacy `src/horarios_fc` was not changed. This records API migration,
  not exhaustive visual verification. Photo-button backdrop blur is supplied by the corrected component.
- Page-nav's own `:background` prop is a separate API. Do not mechanically replace it
  everywhere merely because the button changes.

### Showcase and verification boundary

`xquo.demo.button/screen` is linked from the Xquo index. It exposes all eight button types,
three sizes, label/icon/both/icon-only layouts, color, photo/blur modes and disabled state.
The shared top-right toggle changes theme; pressing the example uses `js/alert`.
The Photo preview shows the unblurred image; only the Photo button blurs its own backdrop.
Blur uses the shared theme-tinted, blurred backdrop and does not add another blur in Button.
No automated tests or exhaustive device comparison of all 945 variants are claimed.

## Effect view

For `:effect :blur`, `:blur-amount` is the only blur control. It is the Figma uniform-blur
value in layout units: 40 means Figma blur 40, 0 disables blur, and omission defaults to 40.
Use `:style` background color for an explicit tint; the calibrated Android path adds no
implicit material tint. Theme and ordinary view props remain available.

```clojure
[:effect/view {:effect      :blur
               :blur-amount 40
               :style       {:flex 1}}]
```

- Removed named/numeric `:intensity` and percentage `:blur-intensity` from blur callers.
  They no longer control `:effect :blur`. Existing callers already specifying amount 40
  keep that value. The navbar now uses the same explicit amount 40 instead of percentage 15.
- Android uses the calibrated Gaussian radius mapping. iOS still uses its native-material
  fallback for positive amounts; it is not yet a calibrated Figma-radius implementation.
  Amount 0 disables the blur on both platforms.
- Native material/progress fields remain private bridge details for the iOS fallback and
  untouched legacy Glass users. They are not additional blur API controls.
- A native Android rebuild is needed for the removal of the implicit material tint.

## Page navigation

Implemented `navigation.page-nav` under `xquo.navigation.page-nav.*`.

```clojure
[xquo/navigation.page-nav
 {:left   {:icon {:name :icon/arrow-left} :on-press back!}
  :center {:title       "General"
           :description "Online"
           :content     {:type   :avatar
                         :avatar {:size    32
                                  :content {:type :image :image {:source profile}}}}
           :icons       [{:name :icon/lock}]
           :on-press    open-details!}
  :right  [{:icon {:name :icon/options} :on-press menu!}]
  :blur?  true}
 [xquo/banner.standard {:title "Community information" :blur? true}]]
```

- `:left`: complete Button props (optional `:label`) or custom Hiccup.
- `:right`: vector of the same. Buttons default to 32 px; complete props are
  forwarded and actions are not silently truncated.
- `:center`: omitted, a title configuration, or custom Hiccup. Title configuration
  accepts `:title`, `:description`, inline `:caption`, `:content`, `:icons`,
  `:text-align` (`:left` default / `:center`), `:on-press`, press-in/out callbacks,
  `:disabled?`, and `:style`. Custom Hiccup is centered.
- Content is one tagged Avatar/Image/Icon props map. Defaults: Avatar 32, Image 24,
  Icon 20; override numeric `:size` in the nested props. Images retain `:source` /
  `:url` / `:style`. Avatar retains its shape, content and badge APIs.
- `:blur?` / `:photo?` select control treatment. The row is transparent and the
  parent owns the overall backdrop. Photo buttons retain their own button blur.
  Button `:type` overrides can match alternative solid surfaces.
- Children render below the row; reuse Banner without separate Channel/Message
  topbar components. The caller handles system safe-area/status-bar placement.

The common row is 56 px high, with 20 px side padding, 12 px action/slot gaps and
32 px buttons. Identity media under 24 px uses gap 4; larger media uses gap 8.
Plain titles use Medium 15/22; identities use Semibold 15/22; descriptions and
inline captions use Medium 13/18. Trailing icons are 16 px with gap 2.
Centered content balances measured side widths, including custom content.

Pressed-state examples `32179:55191` dim the title while side buttons remain
Default: Neutral 80/70 in light, White/70 in dark. The title's own `:on-press`
enables that feedback, using shared timing and animated text color. There is no
public pressed-state prop or global bar press handler.

Migration applied to active `src/fc` and demo callers:
`page-nav` -> `navigation.page-nav`; `:background :blur/:photo` -> `:blur?/:photo?`;
`:left-content` -> `:left`; omit `:center {:type :no-title}`; remove `:type :title`;
replace `:center {:type :custom :custom-content ...}` with the Hiccup itself.
Old left-action/title helpers were replaced with Button/Text. Legacy exports remain
available for other consumers; they are not used by the migrated app.

Figma references: Page Nav `159:6028` (304 variants), Channel Topbar `4551:179489`,
Messages Topbar `10645:129952`, and pressed examples `32179:55191`. Wallet network
selectors and account switchers remain custom slot content; no placeholder or
network-specific dependency is introduced in this pass.

Validation: targeted lint and the running Shadow watcher pass without warnings.
USB A14 checks covered light/dark, blur/photo, title hold/release callback,
centered identity with asymmetric actions, custom centered Dropdown, three actions,
and an existing Banner below the row. No component runtime errors after the fresh
bundle loaded. These are representative device checks, not exhaustive pixel diffs
of all 304 source variants.

## Input

Implemented, based on all 896 variants of Figma Input `55:2720`.
The other input families are outside this pass.

- `:size` remains 32/40 (default 40). `:blur?` is styling only; the parent owns blur.
- Leading content is now `:content {:type :icon :icon {...}}` or
  `{:type :image :image {:source ...}}` (also `:url` and native image props).
  Replace `:icon` and `:image-source`/`:image-style`; they are not concurrent inputs.
- `:style` now applies to the root; migrate old field `:style` to `:field-style`,
  and old `:container-style` to `:style`. Root flex growth is caller-owned.
- `:button` accepts full Button props plus `:label`. Size 24, Outline, hit slop,
  and inherited blur are defaults. A disabled input disables its button, with
  field opacity applied once; labels/counter remain undimmed.
- `:clearable?` reserves the 20 px clear slot, avoiding text-width changes while
  typing. The icon hides and becomes non-interactive whenever the value is empty,
  across all variants (an intentional UX enhancement over Figma). Clearing calls
  `:on-change-text` with `""` in both value modes, then optional `:on-clear`, without a timer.
- `:value`/`:default-value`, `:label`, `:max-length`, `:error?`, `:disabled?`,
  `:input-ref`, `:auto-focus?`, `:font`, and native TextInput props/events remain.
  `:multiline?` (or native `:multiline`) and surface `:min-height`/`:max-height`
  are retained. Native `maxLength` performs limiting without JS truncation.
- Native TextInput receives `value`/`defaultValue` on both platforms; Android no
  longer embeds a Text child. Uncontrolled text is not written back on each key.
  Only empty/nonempty transitions or an enabled counter update surrounding UI;
  multiline height updates only when its measured line count changes.

Migration in this checkout: social-link and emoji-picker leading content, directory
field styling, Places search and room-search leading content/field styling, shared-profile root styling,
and explicit root flex for row layouts in room selection and profile name/bio/links.
The new Input showcase exercises
both value modes, slots, errors, disabled state, blur, multiline bounds, and password
input. Existing press feedback stays; the native field and clear slot stay mounted.

Verification on the A14 development build: all 896 reference variants matched the
computed field padding, gap, radius and minimum height. This is geometry verification,
not 896 device screenshots. Controlled typing, cursor insertion, Samsung keyboard
`ñ`, clearing and multiline growth/scrolling were checked without replacing the
native input. A bounded field grows to 124 px and returns to 80 px after clearing;
the empty-field measurement handles Android reporting its previous viewport height.

The same recorded 36-character uncontrolled typing/clear flow passed before and
after: captured React fiber renders fell from 449 to 118, and commits taking at
least 16 ms fell from 28 to 4. Total commits were 35 and 43 respectively; native
TextInput still performs its own lightweight updates. These are development-device
measurements with profiling overhead, not a release smoothness guarantee. iOS was
not device-validated.

## Drawer

### Composable pieces (2026-10-10)

Implemented; the API was recorded before implementation. New grouped exports live
under `xquo.drawer.*`. See [Drawer usage](drawer-usage.md) for registration,
opening, closing and composition.

- `drawer.content`: children inside a TrueSheet; `:theme`, `:blur?`, `:handle?`, `:style`.
  Owns the single backdrop and 20 px space under the native grabber. TrueSheet owns
  presentation, rounded frame and system insets. Dark blur uses the existing blur view.
- `drawer.screen-options`: builds TrueSheet navigation options with the matching theme,
  background, 20 px corner radius and 32×4 grabber. Extra TrueSheet options are preserved.
- `drawer.top`: `:title`, `:description`, tagged `:content` (icon/image/avatar),
  `:title-icon`, `:description-icon`, `:trailing-icon`, full `:button`, `:counter`,
  `:context-tags`, `:compact?`, `:blur?`, `:style`. No built-in handle or sheet frame.
- `drawer.action`: `:title`, `:description`, tagged `:content` (icon/image),
  `:action` (nil/arrow/toggle/input), full `:input`, `:selected?`, `:on-select`,
  `:on-press`, `:color`, `:danger?`, `:disabled?`, `:blur?`, `:style`.
- `drawer.actions`: `:items` of action maps and optional `:divider` entries.
  Actions own the 8 px horizontal inset; the group applies bottom padding once.
- `drawer.divider`: reuses Divider Line with the drawer's measured 8/8 vertical
  spacing, so multiple action sections do not accumulate extra margins.
- `drawer.label`: `:label`, optional `:counter`, `:blur?`, `:style`.
- `drawer.body`: children, with 20 px horizontal and 12 px bottom spacing;
  string children use the measured 13 px body typography.
- `drawer.bottom-actions`: existing full `:buttons` maps plus `:label`, optional
  `:description` map (`:position`, `:status`, `:text`), real `:context-tag` props,
  `:scroll?`, `:blur?`, `:style`. Defaults to 40 px buttons while preserving overrides.
- `drawer.bottom-context`: `:left` / `:right` vectors of complete button maps,
  defaulting to 32 px outline buttons, plus `:blur?` and `:style`.
- `drawer.permissions-context`: string/renderable children or a full `:button` map,
  `:blur?`, `:style`; owns its 20/12 px horizontal/vertical spacing.

The Figma "Drawer buttons" sign-in/new-user panels are excluded at the user's request.

The old hyphenated exports remain available for existing callers. The single bare
`xquo/drawer` application caller now uses the new content/top/actions composition;
`drawer` becomes the grouped export, consistently with `divider` and `shell`.
No OS navigation bar, fixed 21 px imitation safe area, or separate context/reaction/
emoji/documentation drawer product is introduced.

Showcase: one configuration screen and one TrueSheet screen. Serializable navigation
params select the pieces, content and light/dark/dark-blur mode; callbacks are wired
inside the demo. It uses the installed TrueSheet 3.11.12 API, not v4-only props.

Cross-file review: Onboarding `15075:44613`, `15644:51616`, `15075:44186` and
Context Drawers `167:112209` confirm 20 px grabber space, 8 px action-group insets,
16 px separators, and the final 8 px action-group bottom inset. Text-only headers
above body copy use the compact 8 px bottom spacing. Some older Context Drawers
instances locally resize 48 px actions to 47 px; the current Design System master
remains the canonical row height.

The presentation overlay is Neutral 100 / 70%, without full-screen blur. Dark blur
sheet content uses blur 40 and White / 5%. `:handle? false` hides only the native bar;
the content keeps its 20 px top space. Native `:dismissible` / `:draggable` options
remain available. The showcase demonstrates the handle-free, non-dismissible case
with an explicit Close action. Default showcase height is content-sized (`:auto`).

A14 verification: light, dark and dark-blur compositions fit their content. The title
kept identical bounds with and without the handle; outside taps did not dismiss the
handle-free example, and its Close action returned to the configuration screen.
Overlay pixels matched the library's Neutral 100 / 70% token. Runtime screenshots
were shared in chat. Scoped lint and the running Shadow watcher are clean; no
automated tests were run. Native iOS presentation was not device-verified.

Status: **internal button migration implemented**. Drawer body buttons translate their
existing surface mode to `:photo?` / `:blur?`. Bottom-action buttons derive `:blur?` unless
the caller explicitly supplies it, and combine the slot layout with the final `:style`.
Active application-provided button maps were reviewed and contain no legacy button
`:background` / `:container-style` props. The drawer's own unrelated style/surface props
are unchanged.

## Counter

Status: **implemented**, with its own `xquo.demo.counter` screen and index entry.
Source: [Counter, 139:6406](https://www.figma.com/design/boq5KmgWVdihEHoVzqHVv6/Design-System----Mobile--Copy-?node-id=139-6406),
284 variants inspected on 2026-10-08.

```clojure
[xquo/counter {:type :default :size :large :blur? true} "99+"]
```

| Prop/content | Contract |
| --- | --- |
| `:type` | `:default` (default), `:secondary`, `:grey`, `:outline`, `:outline-color`, `:warning`, `:error` |
| `:size` | `:default` (default) or `:large` |
| `:blur?` | Theme-aware styling for an already blurred backdrop |
| Child value | Number or string; no automatic capping to `"99+"` |
| `:style` and view props | Forwarded to the root view |

Color remains **context-only**; there is no `:color` prop. Context supplies Default and
Outline-color; Warning/Error use the existing semantic tokens. Geometry and opacity stay internal.

Both sizes occupy 20 px in height. Default paints 16 px with radius 6 and Inter Medium 11;
Large paints 20 px with radius 8 and Inter Medium 13. Typography retains its Figma line height
and letter spacing. Corrected transparent Outline/Outline-color fills, border and text colors,
label widths and optical offsets, including `"99+"`, `"-99"`, and four-character labels.
Labels longer than the supplied fixtures grow naturally instead of using a clipped fixed width.

Preserved source-specific cases:

- Default dark + blur uses a color-60 root with the inset color-50 fill; Large stays color-60.
- Dark + blur Outline-color uses white-10 borders and white-40 text.
- Figma's negative (`"-99"`) dark + blur Grey/Outline/Outline-color variants retain their
  normal-dark colors in both sizes (`36823:24778` through `36823:24802`).
- Default single-character Outline-color in normal dark uses color-50 (`32857:32060`),
  while the other normal-dark examples use color-60.

Existing app calls in schedule-overlap, directory, and semester views need no migration.
The showcase exposes value, type, size, context color and blur; its backdrop follows the same
blur flag and selected theme. Source measurements are implemented; device pixel parity has
not been certified by the static verification used for this change.

## Step

Status: **implemented as a separate component**, with its own `xquo.demo.step` screen and index entry.
Source: [Step, 2500:42375](https://www.figma.com/design/boq5KmgWVdihEHoVzqHVv6/Design-System----Mobile--Copy-?node-id=2500-42375),
36 variants inspected on 2026-10-08. Its Figma `Color` axis describes the existing `:type` states.

```clojure
[xquo/step {:type :active :blur? true} 2]
```

| Prop/content | Contract / migration |
| --- | --- |
| `:type` | `:neutral` (default), `:complete`, `:active`; unchanged |
| `:blur?` | Replaces `:background :blur`; omit for the normal surface |
| Child value | Number or string; longer values grow instead of clipping |
| `:style` and view props | Forwarded to the root view |

Color remains **context-only**. Step is not a Counter variant and has no size prop.
The layout height is 20 px, radius 6, with Inter Medium 11 text. Corrected the single-character
painted surface from 16×16 to **18×18**, inset 1 px, retaining the separately measured text offsets.
Two-character roots are 20 px wide; three-character roots are 28 px wide. Neutral borders,
Complete fills and Active tints follow the measured light/dark and blur variants.

Both Step and Counter retain the inherited theme under `:blur?`; neither creates a blur view.
Their showcases use the existing shared 50/50 preview and controls layout with theme switching.
No press state or interaction API was added to these static indicators.

Embedded Steps in `xquo/list` and Settings item now receive the owning component's `:blur?`.
Settings item supplies an explicit label color through a context provider, replacing its ignored
Step `:color` prop; an omitted label color inherits context. No direct `xquo/step` calls were found
in active `src/fc`. Replace legacy Step `:background` props in external consumers.
Verification: targeted clj-kondo reports zero errors/warnings, and the running Shadow watcher
compiled the new demos with zero warnings. No automated tests or device inspection were run.
Source measurements are implemented; this does not certify device pixel parity.

## Avatar

Status: **new component implemented; not a completed parity certification**.

- Export: `xquo/avatar`. One tagged `:content` slot prevents competing top-level image/icon props.
- `:blur?` is a styling flag; the parent supplies the blurred backdrop.
- `:variant` is `:solid`, `:soft`, or nil. The temporary `:fill-opacity` draft prop and demo
  control were removed; fill strengths are internal design rules.
- `:badge` accepts a status, icon map, or image map. Badge types are not restricted to
  Figma's listed size combinations; known measurements are retained and others extended.
- Rings are deliberately omitted; images fill the full avatar footprint and transparent
  pixels show the context-color underlay.
- Press feedback retains scale and adds a dark overlay confined to the avatar, excluding
  the badge. Demo press handlers use `js/alert`.
- Known unresolved distinctions: channel initials use a 10% Figma tint while wallet initials
  use 20%, but the current semantic props do not distinguish them. Native emoji glyphs are
  not the same artwork as Figma's emoji image assets.

There was no older exported `xquo/avatar` API before this integration. Callers using the
intermediate `:background`/`:fill-opacity` drafts must switch to the current documented API.

## Color picker

Status: **implemented** for `xquo/color` and `xquo/color-picker`.
The existing `:blur?` prop is retained and forwarded by the picker to each swatch.
When true, the swatch resolves within a dark Xquo context and selects the dark + blur
variant, even inside a light-themed screen. It uses color shade 50; normal dark uses
shade 60 and normal light uses shade 50. No light + blur variant is inferred.
The flag is styling only and adds no native blur view. Without it, the parent theme
continues to apply. The surrounding screen theme is unchanged.

## Option card

Status: **unified component implemented**. Export: `xquo/shell.option-card`.
Implementation: `xquo.shell.option-card.view`; one showcase with a `:variant` selector.

| Previous call | Replacement |
| --- | --- |
| `xquo/shell.small-option-card` | `xquo/shell.option-card` with `:variant :small` (default) |
| `xquo/shell.big-option-card` | `xquo/shell.option-card` with `:variant :big` |

`:variant` selects the layout; `:size` is reserved for numeric dimensions.
The separate exports, namespaces and showcase routes were removed. No active app calls
used those old exports; the library showcase was migrated.

Both variants retain their existing visuals, behavior and props. Small accepts `:title`,
optional `:description`, `:image` or `:icon`, `:right-icon`, `:disabled?`, `:on-press` and `:style`.
Big requires `:title` and `:description`, accepts the standard `:image` map and `:button`
props plus `:label`, and renders the actual Xquo button. It still supplies button size 40,
`:blur? true` and default type Grey. The parent owns the dark blurred shell context.

## Notifications

### Toast and in-app notifications (2026-10-10)

API recorded before component implementation:

- `notification.toast`: `:type` (`:positive`, `:negative`, `:neutral`), optional
  standard `:icon` map, full `:button` map with `:label`, native props and `:style`;
  message is the Hiccup child. Always blurs its backdrop.
- `notification.in-app`: `:title`, optional `:description` (text or Hiccup), tagged
  `:content` (`:avatar` with its full avatar map, or `:icon`), `:attachment` with
  `:type :photos` and `:images`, or `:sticker`/`:gif` and an `:image` map, `:context-tag`, full `:buttons`
  maps with labels, `:on-press`, `:style`. Reuses Avatar, Button and Context Tag.
- Both inherit the surrounding Xquo theme: dark/shell uses white-70; light uses
  neutral-80-70. Each has one actual blur-40 backdrop, not a glass effect.
- `notification.provider` wraps the navigation tree once and presents a native
  overlay above TrueSheet drawers, including their exit animations. A regular
  Clojure atom holds the host's callbacks; rendered state stays local to the host.
  There is no separate notification React context.
  See [Notification usage](notification-usage.md).
- `notification.use-notifications` returns `{:show! ... :dismiss! ...}`. `show!`
  takes component Hiccup and optional `{:duration 5000 :on-dismiss callback}`,
  preserves the caller's theme/color and returns an id. Explicit `:duration nil`
  persists until swipe/programmatic dismissal. `dismiss!` accepts that id.
- Notifications stack independently. Hold pauses expiry; release returns a
  downward drag to the stack and starts a fresh duration. Upward swipe dismisses.

Migration: the old bare `[xquo/notification props message]` becomes
`[xquo/notification.toast props message]`. Types lose the `notification/` prefix;
`:glass?` is removed because both new components use backdrop blur. Top placement and stack
spacing belong to the notification host. Toasts keep 12 px side margins; in-app
notifications keep 8 px. `:style` replaces the old `:container-style` override. There are
no existing bare notification call sites in this checkout.

## Link preview

`xquo/link-preview` implements Links / Link Preview (`4219:99740`).

```clojure
[xquo/link-preview
 {:title       "Example article"
  :description "A short description of the linked content."
  :url         "example.com"
  :favicon     {:content {:type :initials :text "E"}}
  :image       {:source image-source}
  :on-press    open-link!}]
```

`:type` defaults to `:default`: landscape media height 139. `:detail` makes the image
square; `:url` renders only a wrapping URL using the customization color. Video uses
the default appearance: Figma's play control has zero opacity, so no visible play
button is invented. Width follows the parent; the reference is 295 wide. The card
uses radius 16, horizontal/bottom padding 12, top padding 10 (12 for URL-only),
6px favicon/title gap, 2px title/description gap, 8px URL gap and 12px media gap.
The 1px border is overlaid to preserve content geometry. Images use radius 12.
Full favicon and image props are retained. The host supplies metadata and handles
navigation; the component performs no requests. Press feedback reuses Button.
Light and dark match the defined variants; no blur variant is defined here.

## URL preview

`xquo/url-preview` implements Links / URL Preview (`19128:198652`), not URL preview list.

```clojure
[xquo/url-preview
 {:title     "Example article"
  :url       "example.com"
  :favicon   {:content {:type :initials :text "E"}}
  :image     {:source image-source}
  :on-remove remove-preview!}]
```

`:channel {:community "Community" :name "# design"}` replaces the title with the
breadcrumb. `:image` is optional; `:on-remove` enables removal. `:loading?` renders
the loading appearance, with `:loading-label` defaulting to "Generating preview".
No type is required. Metadata fetching, loading and removal remain host-owned.
The reference is 335×56, radius 12, horizontal padding 12, vertical padding 10,
6px gaps, favicon 16, thumbnail 36/radius 8, clear control 20, and two 18px text
lines. The existing 20px cross-circle icon matches Figma’s 14px disc and 1.2px
cross; its disc uses neutral-50 in light and neutral-60 in dark, with a white cross.
Loading uses the existing 12px animated loading icon and the exact 3/4 dash
pattern with a 1px border; its gap is 8 in light and 6 in dark, as specified.
The separate Profile Link card migration is documented under [Link card](#link-card).

Preview showcases share two generated fictional publications through
`xquo.demo.preview-assets`: Wanderlight (golden coastal travel photography) and
Form & Field (cobalt ceramics on a sunlit studio table). Their `.example` domains
are demonstration content. The two PNGs live in `resources/xquo-demo/previews/`;
no real publication logos or additional product asset collection is bundled.

Preview validation (2026-10-11): targeted lint passed with zero warnings/errors;
running Shadow watcher compilation returned `:ok`. Fold checks covered the default,
square-media and URL-only Link appearances; URL thumbnail, channel, loading and
remove callback; light/dark themes; both generated images after restarting the app.
Screenshots are visual evidence with demonstration assets, not an exhaustive pixel-diff
of every Figma variant. Initial post-restart navigation was attempted before readiness;
retrying after the navigation ref was ready succeeded. No component render errors occurred.

Follow-up design audit (2026-10-11): corrected URL Preview’s dark close disc from
neutral-40 to the source’s neutral-60. The existing icon already matches the source
geometry. Rechecked all Profile Input, Markdown Text/List and Link/URL source variants;
the Video play layer is transparent in both themes, and Markdown’s alternate wrapper
structure yields the same marker alignment, so neither requires additional UI.
Fold captures confirm the corrected close in dark/light and Profile’s 42px layout.
This is a source audit plus representative device inspection, not an exhaustive
pixel-diff across all variants.
