/**
 * @flow strict-local
 */

import type {HostComponent, ViewProps} from 'react-native';
import type {
  Int32,
  WithDefault,
} from 'react-native/Libraries/Types/CodegenTypes';

import {codegenNativeComponent} from 'react-native';

type EffectKind = 'blur' | 'glass';
type EffectIntensity =
  | 'clear'
  | 'ultra-thin'
  | 'thin'
  | 'regular'
  | 'thick'
  | 'chrome';
type EffectTheme =
  | 'system'
  | 'light'
  | 'dark';

type NativeProps = $ReadOnly<{|
  ...ViewProps,
  blurIntensity?: WithDefault<Int32, 100>,
  effect?: WithDefault<EffectKind, 'blur'>,
  intensity?: WithDefault<EffectIntensity, 'regular'>,
  theme?: WithDefault<EffectTheme, 'system'>,
  interactive?: WithDefault<boolean, false>,
|}>;

export default (codegenNativeComponent<NativeProps>(
  'MTREffectView',
): HostComponent<NativeProps>);
