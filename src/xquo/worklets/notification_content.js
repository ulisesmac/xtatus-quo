import {useCallback, useEffect, useMemo} from 'react';
import {usePanGesture} from 'react-native-gesture-handler';
import {
  cancelAnimation,
  Easing,
  useAnimatedStyle,
  useSharedValue,
  withDelay,
  withSpring,
  withTiming,
} from 'react-native-reanimated';
import {scheduleOnRN, scheduleOnUI} from 'react-native-worklets';

const SPRING = {damping: 28, stiffness: 180, mass: 1};
const ENTER = {duration: 440, easing: Easing.out(Easing.cubic)};
const EXIT = {duration: 400, easing: Easing.inOut(Easing.cubic)};

export function useNotificationMotion(
  duration,
  inset,
  offset,
  dismissing,
  onRemove,
  onHeight,
) {
  const y = useSharedValue(0);
  const startY = useSharedValue(0);
  const targetY = useSharedValue(inset + offset);
  const height = useSharedValue(0);
  const held = useSharedValue(false);
  const closing = useSharedValue(false);
  const visible = useSharedValue(0);
  const timer = useSharedValue(0);

  const controls = useMemo(() => {
    const dismiss = reason => {
      'worklet';
      if (closing.value) {
        return;
      }
      closing.value = true;
      cancelAnimation(timer);
      y.value = withTiming(-height.value - 24, EXIT, finished => {
        if (finished) {
          scheduleOnRN(onRemove, reason);
        }
      });
    };
    const restartTimer = (delay = 0) => {
      'worklet';
      cancelAnimation(timer);
      timer.value = 0;
      if (duration > 0 && !held.value && !closing.value) {
        timer.value = withDelay(
          duration + delay,
          withTiming(1, {duration: 0}, finished => {
            if (finished && !held.value && !closing.value) {
              dismiss('timeout');
            }
          }),
        );
      }
    };
    const layout = layoutHeight => {
      'worklet';
      const entering = height.value === 0;
      height.value = layoutHeight;
      if (entering) {
        y.value = -layoutHeight - 24;
        visible.value = 1;
        y.value = withTiming(targetY.value, ENTER);
        restartTimer(ENTER.duration);
      }
    };
    const move = nextOffset => {
      'worklet';
      const nextY = inset + nextOffset;
      if (nextY === targetY.value) {
        return;
      }
      targetY.value = nextY;
      if (height.value > 0 && !held.value && !closing.value) {
        y.value = withSpring(nextY, SPRING);
      }
    };
    return {dismiss, restartTimer, layout, move};
  }, [
    closing,
    duration,
    height,
    held,
    inset,
    onRemove,
    targetY,
    timer,
    visible,
    y,
  ]);

  const gestureConfig = useMemo(
    () => ({
      minDistance: 4,
      maxPointers: 1,
      onTouchesDown: () => {
        'worklet';
        if (closing.value) {
          return;
        }
        held.value = true;
        cancelAnimation(timer);
        cancelAnimation(y);
        startY.value = y.value;
      },
      onUpdate: event => {
        'worklet';
        if (!closing.value) {
          y.value = startY.value + event.translationY;
        }
      },
      onFinalize: event => {
        'worklet';
        held.value = false;
        if (closing.value) {
          return;
        }
        if (
          !event.canceled &&
          (event.translationY < -40 || event.velocityY < -600)
        ) {
          controls.dismiss('swipe');
        } else {
          y.value = withSpring(targetY.value, SPRING);
          controls.restartTimer();
        }
      },
    }),
    [closing, controls, held, startY, targetY, timer, y],
  );
  const gesture = usePanGesture(gestureConfig);
  const animatedStyle = useAnimatedStyle(() => ({
    opacity: visible.value,
    zIndex: held.value ? 2 : closing.value ? 1 : 0,
    transform: [{translateY: y.value}],
  }));
  const onLayout = useCallback(
    event => {
      const layoutHeight = event.nativeEvent.layout.height;
      onHeight(layoutHeight);
      scheduleOnUI(controls.layout, layoutHeight);
    },
    [controls, onHeight],
  );

  useEffect(() => {
    scheduleOnUI(controls.move, offset);
  }, [controls, offset]);
  useEffect(() => {
    if (dismissing) {
      scheduleOnUI(controls.dismiss, 'programmatic');
    }
  }, [controls, dismissing]);
  useEffect(
    () => () => {
      cancelAnimation(timer);
      cancelAnimation(y);
    },
    [timer, y],
  );

  return [gesture, animatedStyle, onLayout];
}
