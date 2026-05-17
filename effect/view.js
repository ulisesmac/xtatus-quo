import React from 'react';
import { Platform, StyleSheet, View } from 'react-native';

import NativeEffectView from '../specs/EffectViewNativeComponent';

const SHADOW_STYLE_KEYS = [
  'boxShadow',
  'elevation',
  'shadowColor',
  'shadowOffset',
  'shadowOpacity',
  'shadowRadius',
];

const BORDER_STYLE_KEYS = [
  'borderColor',
  'borderWidth',
  'borderStyle',
  'borderBlockColor',
  'borderBlockEndColor',
  'borderBlockStartColor',
  'borderBottomColor',
  'borderBottomWidth',
  'borderEndColor',
  'borderEndWidth',
  'borderLeftColor',
  'borderLeftWidth',
  'borderRightColor',
  'borderRightWidth',
  'borderStartColor',
  'borderStartWidth',
  'borderTopColor',
  'borderTopWidth',
];

const SHOULD_STRIP_EFFECT_HOST_STYLE = Platform.OS === 'ios';

function valueName(value) {
  if (value == null) {
    return value;
  }

  return String(value).replace(/^:/, '');
}

function isNumber(value) {
  return typeof value === 'number' && Number.isFinite(value);
}

function effectTheme(value) {
  const name = valueName(value);

  if (name == null) {
    return 'system';
  }

  const themeName = name.includes('/') ? name.split('/').pop() : name;

  return themeName === 'light' || themeName === 'dark' ? themeName : 'system';
}

export function withoutEffectHostStyle(style, options = {}) {
  if (!SHOULD_STRIP_EFFECT_HOST_STYLE || style == null || typeof style === 'function') {
    return style;
  }

  if (Array.isArray(style)) {
    let changed = false;
    const stylesWithoutHostStyle = style.map((item) => {
      const itemWithoutHostStyle = withoutEffectHostStyle(item, options);
      changed = changed || itemWithoutHostStyle !== item;

      return itemWithoutHostStyle;
    });

    return changed ? stylesWithoutHostStyle : style;
  }

  if (typeof style === 'number') {
    return withoutEffectHostStyle(StyleSheet.flatten(style), options);
  }

  if (typeof style !== 'object') {
    return style;
  }

  let styleWithoutHostStyle = style;

  const keysToRemove = SHADOW_STYLE_KEYS
    .concat(options.removeBorders ? BORDER_STYLE_KEYS : []);

  keysToRemove.forEach((key) => {
    if (Object.prototype.hasOwnProperty.call(style, key)) {
      if (styleWithoutHostStyle === style) {
        styleWithoutHostStyle = { ...style };
      }

      delete styleWithoutHostStyle[key];
    }
  });

  return styleWithoutHostStyle;
}

const EffectView = React.forwardRef(function EffectView(props, ref) {
  const {
    blurIntensity,
    effect,
    intensity,
    theme,
    interactive,
    ['interactive?']: interactiveQuestion,
    ...viewProps
  } = props;

  const normalizedEffect = valueName(effect) || 'blur';
  const removeBorders = normalizedEffect === 'glass';
  const numericIntensity = isNumber(intensity) ? Math.round(intensity) : undefined;
  const resolvedBlurIntensity = blurIntensity ?? numericIntensity ?? 100;

  if (Platform.OS === 'android') {
    return React.createElement(View, {
      ...viewProps,
      ref,
    });
  }

  return React.createElement(NativeEffectView, {
    ...viewProps,
    ref,
    style: withoutEffectHostStyle(viewProps.style, { removeBorders }),
    blurIntensity: Math.max(0, Math.min(100, Math.round(resolvedBlurIntensity))),
    effect: normalizedEffect,
    intensity: numericIntensity == null ? valueName(intensity) || 'regular' : 'regular',
    theme: effectTheme(theme),
    interactive: interactive ?? interactiveQuestion ?? false,
  });
});

export default EffectView;
