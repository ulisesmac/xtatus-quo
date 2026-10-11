# Notifications

Mount one provider inside the app's SafeAreaProvider, around its navigation tree:

```clojure
[xquo/notification.provider
 [app-navigation]]
```

The provider owns stacking and a native overlay above TrueSheet drawers. It does
not add a navigation route or block touches outside the notifications.

```clojure
(defn save-button []
  (let [{:keys [show!]} (xquo/notification.use-notifications)
        saved!        (react/use-callback (fn []
                                           (show! [xquo/notification.toast {:type :positive}
                                                   "Custom name updated"]
                                                  {:duration 5000}))
                                         [show!])]
    [xquo/button {:on-press saved!} "Show toast"]))
```

`show!` returns an id; pass it to the hook's `dismiss!` to dismiss that item with
its exit animation. Options are `:duration` in milliseconds (default 5000;
explicit `nil` is persistent) and `:on-dismiss`, called with `:timeout`, `:swipe`
or `:programmatic`. Each notification has its own timer and gesture. Touch pauses
expiry; release starts a fresh duration. Dragging down springs back; swiping up
dismisses. New notifications enter at the top of the stack.

The caller's Xquo theme/color is preserved. On dark/shell surfaces notifications
use white-70; on light surfaces they use neutral-80-70. Both blur the content behind
them with blur 40. No `:glass?` or opt-in `:blur?` is needed.

## Components

`notification.toast` takes a message child and props:

- `:type`: `:positive`, `:negative`, or `:neutral` (default).
- `:icon`: standard icon map overriding the semantic default.
- `:button`: full Xquo Button map plus `:label`; defaults to size 24, Grey, blur styling.
- `:style` and other native view props.

`notification.in-app` takes:

- `:title`, optional `:description` string or Hiccup.
- `:content`: `{:type :avatar :avatar {...}}` or `{:type :icon :icon {...}}`.
  Avatar receives its full props map, defaulting to size 32.
- `:attachment`: `{:type :photos :images [{:source ...}]}`, or
  `{:type :sticker :image {...}}` / `{:type :gif :image {...}}`.
- `:context-tag`: full Context Tag map plus `:label`.
- `:buttons`: full Button maps plus `:label`, default size 24.
- `:on-press`, `:style`, and other native pressable/view props.

They can also render directly as ordinary Hiccup. The provider/hook adds the
presentation lifecycle. The showcase includes the selected Profile and Onboarding
toast messages, configurable appearance/content/duration, and a real drawer.
