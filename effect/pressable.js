import React from 'react';
import { Pressable, StyleSheet } from 'react-native';

import EffectView, { withoutEffectHostStyle } from './view';

const ROOT_STYLE = { position: 'relative' };

function resolveStyle(style, state) {
  return typeof style === 'function' ? style(state) : style;
}

function valueName(value) {
  if (value == null) {
    return value;
  }

  return String(value).replace(/^:/, '');
}

function renderChildren(children, state) {
  return typeof children === 'function' ? children(state) : children;
}

const EffectPressable = React.forwardRef(function EffectPressable(props, ref) {
  const {
    blurAmount,
    children,
    effect,
    intensity,
    theme,
    interactive,
    ['interactive?']: interactiveQuestion,
    onPressIn,
    onPressOut,
    style,
    ...pressableProps
  } = props;
  delete pressableProps.blurIntensity;

  const [pressed, setPressed] = React.useState(false);
  const resolvedInteractive = interactive ?? interactiveQuestion ?? false;
  const normalizedEffect = valueName(effect) || 'blur';
  const removeBorders = normalizedEffect === 'glass';
  const pressableState = { pressed };
  const onPressInHandler = (event) => {
    setPressed(true);

    if (onPressIn) {
      onPressIn(event);
    }
  };
  const onPressOutHandler = (event) => {
    setPressed(false);

    if (onPressOut) {
      onPressOut(event);
    }
  };

  const resolvedStyle = withoutEffectHostStyle(resolveStyle(style, pressableState), { removeBorders });

  const effectNode = React.createElement(
    EffectView,
    {
      ref,
      blurAmount,
      effect: normalizedEffect,
      intensity: intensity ?? 'regular',
      theme,
      interactive: resolvedInteractive,
      style: [
        ROOT_STYLE,
        resolvedStyle,
      ],
    },
    renderChildren(children, pressableState),
    React.createElement(Pressable, {
      ...pressableProps,
      onPressIn: onPressInHandler,
      onPressOut: onPressOutHandler,
      style: StyleSheet.absoluteFill,
    }),
  );

  return effectNode;
});

export default EffectPressable;
