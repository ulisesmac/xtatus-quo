# Xquo

## Install

Add xquo to the CLJS runtime:

```clojure
xtatus-quo/xtatus-quo {:local/root "xtatus-quo"}
```

This provides `xquo.*`, `xquo.$init`, and xquo resources through `deps.edn`.

Add the native Effect component to React Native:

```json
"xquo": "file:./xtatus-quo"
```

The npm package is only for React Native autolinking/codegen of the Effect
component. It is not the xquo CLJS API surface.

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
cd ios && pod install
```

The effect native component is installed by React Native autolinking/codegen.

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
                    merge
                    {:edge-to-edge edge-to-edge}))
```

Wrap the app with xquo context. It provides xquo theme/color, Gesture Handler
root view, and Safe Area provider:

```clojure
[xquo.context/provider {:theme theme :color color}
 [app-root]]
```

## Fonts

Fonts live in `resources/fonts`.

After adding, removing, or renaming fonts, update the app font registration:

```sh
npx react-native-asset
```

On iOS, also keep `ios/mailtracker/Info.plist` `UIAppFonts` aligned with the
actual font filenames, then rebuild the native app.

## Icons

Icon SVG sources live in `resources/icons/12`, `resources/icons/16`, and
`resources/icons/20`.

After changing SVGs, regenerate the icon fonts and glyph maps:

```sh
npm run icons:generate
```

This reads `xtatus-quo/.nanoicons.json` and writes the generated `.ttf` and
`.glyphmap.json` files into `resources/icons`.
