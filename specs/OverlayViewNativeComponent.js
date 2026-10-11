/** @flow strict-local */

import type {HostComponent, ViewProps} from 'react-native';
import {codegenNativeComponent} from 'react-native';

type NativeProps = $ReadOnly<{|
  ...ViewProps,
|}>;

export default (codegenNativeComponent<NativeProps>('MTRXquoOverlay', {
  excludedPlatforms: ['iOS'],
}): HostComponent<NativeProps>);
