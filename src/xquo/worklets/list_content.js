import {useEffect} from 'react';
import {
  Easing,
  useAnimatedStyle,
  useSharedValue,
  withTiming,
} from 'react-native-reanimated';

const easing = Easing.inOut(Easing.ease);

export function useCollapsibleStyle(visible, height, duration) {
  const progress = useSharedValue(visible ? 1 : 0);

  useEffect(() => {
    progress.value = withTiming(visible ? 1 : 0, {
      duration,
      easing,
    });
  }, [duration, progress, visible]);

  return useAnimatedStyle(
    function () {
      'worklet';

      const value = progress.value;

      return {
        height: height == null ? (visible ? undefined : 0) : height * value,
        opacity: value,
      };
    },
  );
}
