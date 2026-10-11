import React from 'react';
import {Platform, StyleSheet, useWindowDimensions} from 'react-native';
import {FullWindowOverlay} from 'react-native-screens';

const NativeOverlay = Platform.OS === 'android'
  ? require('../specs/OverlayViewNativeComponent').default
  : FullWindowOverlay;

export function Overlay({children}) {
  const {width, height} = useWindowDimensions();
  return React.createElement(NativeOverlay, {
    style: [StyleSheet.absoluteFill, {width, height}],
    pointerEvents: 'box-none',
  }, children);
}
