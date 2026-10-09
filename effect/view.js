import React from 'react';
import { Platform, StyleSheet } from 'react-native';

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

/**
 * blurAmount is the Figma uniform-blur value, defaulting to 40; zero disables blur.
 * Android maps 40 to Gaussian sigma 20dp. iOS retains its native-material fallback
 * for positive values until that platform has a calibrated radius implementation.
 * Material intensity remains an internal/legacy glass setting, not a blur control.
 */
const EffectView = React.forwardRef(function EffectView(props, ref) {
  const {
    blurAmount,
    effect,
    intensity,
    theme,
    interactive,
    ['interactive?']: interactiveQuestion,
    ...viewProps
  } = props;

  const normalizedEffect = valueName(effect) || 'blur';
  const removeBorders = normalizedEffect === 'glass';
  const resolvedBlurAmount = isNumber(blurAmount) && blurAmount >= 0 ? blurAmount : 40;
  delete viewProps.blurIntensity;

  return React.createElement(NativeEffectView, {
    ...viewProps,
    ref,
    ...(Platform.OS === 'android' ? {
      blurAmount: removeBorders && blurAmount == null ? -1 : resolvedBlurAmount,
    } : {}),
    style: withoutEffectHostStyle(viewProps.style, { removeBorders }),
    blurIntensity: resolvedBlurAmount === 0 ? 0 : 100,
    effect: normalizedEffect,
    intensity: removeBorders && !isNumber(intensity) ? valueName(intensity) || 'regular' : 'regular',
    theme: effectTheme(theme),
    interactive: interactive ?? interactiveQuestion ?? false,
  });
});

export default EffectView;
