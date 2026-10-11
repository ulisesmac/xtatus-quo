# Drawers

Register the drawer as a **TrueSheet navigation screen**. `drawer.content` renders
the pieces inside that screen; mounting it alone does not present a sheet.

## Register and open

```clojure
(ns app.drawer-example
  (:require [applied-science.js-interop :as j]
            [react-native.react-navigation.navigation :as nav]
            [react-native.react-navigation.true-sheet :as sheets]
            [xquo.core :as xquo]))

(def ^:private appearance
  {:theme :theme/dark
   :blur? true})

(defn options-drawer [_]
  (let [{:keys [blur?]} appearance]
    [xquo/drawer.content appearance
     [xquo/drawer.top {:title "Options" :blur? blur?}]
     [xquo/drawer.actions {:blur? blur?
                           :items [{:title    "Share"
                                    :content  {:type :icon :icon {:name :icon/share}}
                                    :on-press nav/go-back!}
                                   :divider
                                   {:title "Remove" :danger? true :on-press nav/go-back!}]}]
     [xquo/drawer.bottom-actions {:blur?   blur?
                                  :buttons [{:label "Close" :on-press nav/go-back!}]}]]))

(defn- open-drawer! []
  (nav/navigate! :screen/example-drawer))

(defn example-screen [_]
  [xquo/button {:on-press open-drawer!} "Show drawer"])

(def ^:private navigator
  (j/call (sheets/create-true-sheet-navigator {:initial-route-name :screen/example
                                               :screens            {:screen/example        #'example-screen
                                                                    :screen/example-drawer {:screen  #'options-drawer
                                                                                            :options (xquo/drawer.screen-options appearance)}}})
          :getComponent))

(defn screen [_]
  [:> navigator])
```

Mount `screen` in the app's existing navigation tree, which uses `nav/navigation-ref`.
The navigator's first route is the underlying screen; navigating to another route
presents a drawer. `nav/navigate!` accepts a screen keyword and an ordinary Clojure
params map, and handles encoding internally. `nav/go-back!` closes the presented
drawer, including a non-dismissible one. Keep callbacks inside the drawer screen;
do not put functions or Hiccup in navigation params.

For example, `(nav/navigate! :screen/example-drawer {:item-id item-id})` passes
content data; the drawer receives it as `:route-params`.

## Appearance and behavior

Define fixed appearance with the drawer and its route options, as `appearance`
above. Opening the drawer should not require `:theme` or `:blur?` params.
Reuse that configuration for `drawer.screen-options` and `drawer.content`, and
pass `:blur?` to pieces that accept blur styling. Native route options do not
automatically supply these content props. Theme is inherited by children of content.

| Mode | Drawer configuration |
| --- | --- |
| Light | `{:theme :theme/light}` |
| Dark | `{:theme :theme/dark}` |
| Dark + blur | `{:theme :theme/dark :blur? true}` |
| No handle, non-dismissible | Add `:handle? false :dismissible false :draggable false` |

Default height is `:auto`. Extra TrueSheet options can be supplied to
`drawer.screen-options`; use explicit detents/scroll configuration only for content
that requires it. Do not wrap a normal content-sized drawer in a filling scroll view.

The native grabber defaults to 32×4, within 20 px of top space. Hiding it preserves
that space. Non-dismissible drawers need an explicit Close/Cancel action. TrueSheet
owns the 20 px corner radius, gestures and system insets; never draw an OS navigation
bar or add a fixed imitation bottom safe area.

The dimming overlay uses Neutral 100 at 70%. `:blur?` forces a dark sheet and applies
blur 40 plus white-5 once in `drawer.content`; its pieces only change their colors.

## Compose pieces

| Piece | Content/API |
| --- | --- |
| `drawer.top` | Title, optional description, tagged `:content` (icon/image/avatar), counter, button or context tags. Use `:compact? true` directly above body copy. |
| `drawer.label` | `:label`, optional `:counter`. |
| `drawer.body` | Text or Hiccup children. |
| `drawer.actions` | `:items` of action maps; place `:divider` between sections. It applies the final bottom inset once. |
| `drawer.action` | Individual row: `:title`, description, tagged content, `:action` (arrow/toggle/input), selection and callbacks. |
| `drawer.divider` | The existing divider line with drawer-specific vertical spacing. |
| `drawer.bottom-actions` | Full Button maps in `:buttons`, each with `:label`; optional description and real context tag. |
| `drawer.bottom-context` | Full Button maps in `:left` and `:right`. |
| `drawer.permissions-context` | Inline text/components as children, or a full `:button` map. |

These pieces own their spacing. Do not add horizontal padding or inter-piece gaps
around them. Keep action sections in one `drawer.actions` list to avoid duplicating
the group's bottom inset. Button maps preserve caller overrides.

The showcase uses one configuration screen and one TrueSheet screen; navigation
params carry its selected content and appearance because the controls change both.
That is a showcase requirement, not the normal app usage pattern. Its local
`use-navigation` hook exposes raw JS methods, unlike the app's `nav/navigate!`;
the showcase keeps the conversion internal and does not depend on the host's global ref.
See [the demo](../src/xquo/demo/drawer/core.cljs) and
[API changes](../docs/component-api-changes.md#drawer).
