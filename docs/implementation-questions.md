# Xquo implementation questions

Track unanswered design/API decisions while the skipped Figma pages are implemented.
Keep implementation moving on independent work. Record the answer and affected components
when a decision is made; do not silently delete unresolved questions.

## Open

| ID | Component | Question | Working assumption | Status |
| --- | --- | --- | --- | --- |
| Q2 | Soundtrack | What duration/threshold should activate the near-limit danger treatment? Figma only gives an example timestamp. | :warning? is supplied by the host; Xquo does not infer a recording limit. | Awaiting product preference |
| Q6 | Community channel actions | Is Figma's larger dark-theme multi-row gap/bottom spacing intentional? | Preserve the measured theme-specific spacing. | Awaiting design confirmation |
| Q7 | Permissions tag | Is an optional semantic :background-color override acceptable for different dark parent surfaces? | Default theme/blur rules plus a token-based override; no hex lookup tables. | Awaiting API preference |
| Q8 | Recorder | What drag thresholds/feel are intended? Figma provides states but no interaction timeline. | Component-owned transient feedback and thresholds between measured action positions. | Awaiting interaction review |

## Deferred selector state

Figma Selectors has Indeterminate-labelled variants for all four types, but their
root opacity is zero. They are excluded from this pass; confirm whether they are
intended product states before adding a public prop or making them visible.

## Resolved

- Figma notification visuals (2026-10-10): PNG exports timed out; SVG exports
  recovered the actual toast/in-app visuals and embedded reference avatar.

- Notifications (2026-10-10): both use `notification.*`. Downward dragging returns
  to the stack and restarts the timer. Upward swiping dismisses that notification;
  holding/dragging pauses timed dismissal.

- Q3: the overflowing standalone Soundtrack fixture is represented with responsive layout and measured track/thumb geometry, following the requirement to match visuals without copying broken Figma structure.

- Drawer boundary (2026-10-09): user confirmed sheet frames/handles/presentation are separate from component content. Existing TrueSheet-backed Xquo drawer owns them. Borrowed chrome removed from Browser Navigation and token-gating content variants.

- Q4 (2026-10-09): Calendar uses device locale, supports Spanish/English, and permits an explicit preference. Other locales fall back to English. Sunday is the first weekday, matching Figma.
- Q5 (2026-10-09): user explicitly postponed Code for this pass. No Code implementation or staging.
- Resource workflow: restart the app when new images/resources are added. Ordinary source changes use hot reload.

- Q1 (2026-10-09): user confirmed the host owns media operations; Xquo owns controls and callbacks.
- Browser is included: it is a skipped component page before Banners.
- Authentication and Context drawers return no component/frame/instance content from scoped Figma reads; no implementation is grounded there.
- Browser uses native Android text selection and host system insets, rather than copying Figma's iOS selection handles/home indicator.
- Global banners leave system safe-area insets with the host; the reusable content row is 40 px high.

## Implementation order

Follow the current Figma page order for skipped pages. Dependencies may be implemented first.
Audio → Browser → Banners → Calendar → Camera → Cards → Community → dApps.
Inventory found 33 component sets / 395 variants, plus reusable dependencies; Code
(2 sets / 14 variants) is now explicitly postponed.
Within a page, shared dependencies are implemented first.

A component is ready for user review only after its showcase runs on the connected A14
and its screenshot has been shown in chat. Compilation alone is not visual acceptance.
