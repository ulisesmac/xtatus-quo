# Color Generator Strategy (Figma -> Tokens)

This document defines the exact strategy used by the Figma color generator and
how to reproduce its output consistently in code.

## Scope

This strategy applies to the **Figma generator scale** shown in:

- File: `Foundations (Copy)` (`5XXxaYKRVExxny18fi8Aft`)
- Node: `🎨 Color generator` (`749:923`)

It explains how the generator derives lighter and darker steps from a base
color.

## Source of truth

The current token file in `xquo` states it is generated from the foundations
page:

- [xquo/foundations/colors.cljs](../src/xquo/foundations/colors.cljs)

The runtime token resolver is lookup-only (memoized parsing + `get-in`), so all
actual color generation must happen before values are written to token maps.

## Inputs and anchors

Given a base hue at level `50`:

- `base-50`: `#RRGGBB` (source hue)
- lighter anchor: `#FFFFFF` (white)
- darker anchor: `#000000` (black)

## Level mapping

The generator uses these exact mixes:

| Level | Mix target | Target weight |
|---|---|---|
| 10 | white | 96% |
| 20 | white | 80% |
| 30 | white | 65% |
| 40 | white | 32% |
| 50 | base | 0% |
| 60 | black | 16% |
| 70 | black | 32% |
| 80 | black | 48% |
| 90 | black | 65% |

Interpretation:

- Lighter levels are tints (`base -> white`).
- Darker levels are shades (`base -> black`).

## Blend formula

Blend is channel-by-channel in RGB, rounded to nearest integer:

```text
out = round(base * (1 - p) + target * p)
```

Where:

- `base` is one channel from `base-50` (0..255)
- `target` is one channel from white or black (0..255)
- `p` is the target weight (0..1), from the table above

Apply this independently to `R`, `G`, and `B`, then format as `#RRGGBB`.

## Worked example (from generator row)

Example base: `#4360DF` (`Purple` row in node `749:923`).

Applying the formula above:

| Level | Result |
|---|---|
| 10 | `#F7F9FE` |
| 20 | `#D9DFF9` |
| 30 | `#BDC7F4` |
| 40 | `#7F93E9` |
| 50 | `#4360DF` |
| 60 | `#3851BB` |
| 70 | `#2E4198` |
| 80 | `#233274` |
| 90 | `#17224E` |

## Transparent steps (`opa`)

Transparent variants are represented as alpha variants of a base color. In
tokens this is stored under `:opa` and resolved by `:color/<name>-<level>-<opa>`.

Alpha suffix mapping (percent -> hex):

| % | Hex |
|---|---|
| 1 | `03` |
| 5 | `0D` |
| 10 | `1A` |
| 20 | `33` |
| 30 | `4D` |
| 40 | `66` |
| 50 | `80` |
| 60 | `99` |
| 70 | `B3` |
| 80 | `CC` |
| 90 | `E6` |
| 95 | `F2` |

Example:

- `#2A4AF5` at `10%` alpha -> `#2A4AF51A`
- Token shape:
  - `{:base "#2A4AF5" :opa {10 "#2A4AF51A"}}`

## Reproduction checklist

1. Choose `base-50` (`#RRGGBB`) from Figma.
2. Generate levels `10/20/30/40` by mixing with `#FFFFFF` using `96/80/65/32`.
3. Keep level `50` unchanged.
4. Generate levels `60/70/80/90` by mixing with `#000000` using `16/32/48/65`.
5. Generate `:opa` variants as needed using the alpha table above.
6. Write precomputed values into token maps (do not compute at runtime in UI
   components).
7. Verify lookups through `xquo.foundations.colors/get-color`.

## Notes

- This document describes the **generator method** visible in Figma node
  `749:923`.
- Some existing semantic/foundation tokens may intentionally use explicit
  values from design and not a fully generated ladder. When that happens, treat
  the design token value as authoritative.
