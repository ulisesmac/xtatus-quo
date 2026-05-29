# Xquo

## Install

Consumers must register xquo in two places because the CLJS library and the
native package are resolved by different toolchains.

Add this repository to the CLJS runtime in `deps.edn`:

```clojure
xtatus-quo/xtatus-quo {:local/root "<path-to-xquo>"}
```

This provides `xquo.*`, `xquo.$init`, and xquo resources through `deps.edn`.

Add the native package to React Native in `package.json`:

```json
"xquo": "file:<path-to-xquo>"
```

The npm package is only for React Native autolinking/codegen of the Effect
component. It is not the xquo CLJS API surface. Without this package entry,
`xquo/effect` may compile from CLJS, but React Native will not autolink the
native `EffectView` package.

The app must provide the React Native JS libraries used by xquo CLJS:

```json
"react-native-gesture-handler": "...",
"react-native-nano-icons": "...",
"react-native-reanimated": "...",
"react-native-safe-area-context": "...",
"react-native-svg": "..."
```

Then install native deps and rebuild the app:

```sh
npm install
(cd ios && pod install)
```

The effect native component is installed by React Native autolinking/codegen.
Check that React Native can see it before rebuilding:

```sh
node -p "require.resolve('xquo/effect')"
npx react-native config
```

The config output must include `dependencies.xquo`, the iOS `xquo.podspec`,
and the Android `EffectViewPackage`.

## Init

`xquo.$init` only defines compiler props. The app creates and installs the
Reagent compiler:

```clojure
(ns app.$init
  (:require [react-native.reagent-compiler.core :as rec]
            [reagent.core :as r]
            [xquo.$init :as xquo-init]))

(defonce reagent-compiler
  (rec/create xquo-init/compiler-props))

(r/set-default-compiler! reagent-compiler)
```

If the app has extra JS component libs, merge them into xquo's props:

```clojure
(rec/create (update xquo-init/compiler-props
                    :js-component-libs
                    conj
                    {:edge-to-edge edge-to-edge}))
```

Do not register `xquo/effect` again in the app compiler. `xquo.$init` already
registers `:effect/view`, `:effect/pressable`, `:animated/effect-view`, and
`:animated/effect-pressable`.

Wrap the app with xquo context. It provides xquo theme/color, Gesture Handler
root view, and Safe Area provider:

```clojure
[xquo.context/provider {:theme theme :color color}
 [app-root]]
```

## Fonts

Fonts live in `resources/fonts`.

Register `resources/fonts` with the consuming React Native app.

After adding, removing, or renaming fonts, update the app's font registration:

```sh
npx react-native-asset
```

On iOS, keep the app's `Info.plist` `UIAppFonts` aligned with the actual font
filenames, then rebuild the native app.

## Icons

Icon SVG sources live in `resources/icons/12`, `resources/icons/16`, and
`resources/icons/20`.

If the consuming app exposes a package script for generation, point
`react-native-nano-icons` at this repository root:

```json
"icons:generate": "react-native-nano-icons --path <path-to-xquo>"
```

After changing SVGs, regenerate the icon fonts and glyph maps:

```sh
npm run icons:generate
```

This reads `.nanoicons.json` from this repository and writes the generated
`.ttf` and `.glyphmap.json` files into `resources/icons`.

Apps using Shadow CLJS must also resolve `react-native-nano-icons` to the
React Native module build in the app build config:

```clojure
:js-options {:resolve {"react-native-nano-icons"
                       {:target  :npm
                        :require "react-native-nano-icons/lib/module/index.js"}}}
```

This prevents Metro from loading the package's Node/CommonJS entry. That entry
references `./createNanoIconsSet`, which is not shipped in the package's
CommonJS build.
