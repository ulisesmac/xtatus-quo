import { Easing, useDerivedValue, withTiming } from 'react-native-reanimated';
import { scheduleOnRN } from 'react-native-worklets';

const tabTransition = {
  duration: 300,
  easing: Easing.bezier(0.25, 0.1, 0.25, 1),
};

const edgeResistance = 0.34;
const minSwipeDistance = 44;
const maxSwipeDistance = 94;
const swipeDistanceRatio = 0.18;
const velocityProjection = 0.12;
const minSwipeVelocity = 520;

function swipeThreshold(pageWidth) {
  'worklet';
  return Math.min(maxSwipeDistance, Math.max(minSwipeDistance, pageWidth * swipeDistanceRatio));
}

function resistedTranslation(translationX, activeIndex, lastIndex) {
  'worklet';
  if ((activeIndex === 0 && translationX > 0) || (activeIndex === lastIndex && translationX < 0)) {
    return translationX * edgeResistance;
  }
  return translationX;
}

function nextIndex(activeIndex, lastIndex, translationX, velocityX, pageWidth) {
  'worklet';
  const projectedTranslation = translationX + velocityX * velocityProjection;
  const threshold = swipeThreshold(pageWidth);

  if ((projectedTranslation < -threshold || velocityX < -minSwipeVelocity) && activeIndex < lastIndex) {
    return activeIndex + 1;
  }

  if ((projectedTranslation > threshold || velocityX > minSwipeVelocity) && activeIndex > 0) {
    return activeIndex - 1;
  }

  return activeIndex;
}

export function useContentTranslateX(tabProgress, pageWidth) {
  return useDerivedValue(function () {
    'worklet';
    return -(tabProgress.value || 0) * pageWidth;
  });
}

export function useIndicatorTranslateX(tabProgress) {
  return useDerivedValue(function () {
    'worklet';
    return `${(tabProgress.value || 0) * 100}%`;
  });
}

export function useIndicatorGapTranslateX(tabProgress) {
  return useDerivedValue(function () {
    'worklet';
    return (tabProgress.value || 0) * 2;
  });
}

export function selectTab(tabProgress, index) {
  tabProgress.value = withTiming(index, tabTransition);
}

export function panOnBegin(tabProgress, startIndex) {
  return function () {
    'worklet';
    startIndex.value = Math.round(tabProgress.value || 0);
  };
}

export function panOnUpdate(tabProgress, startIndex, lastIndex, pageWidth) {
  return function (event) {
    'worklet';
    if (!pageWidth) {
      return;
    }

    const currentIndex = startIndex.value || 0;
    const translationX = resistedTranslation(event.translationX, currentIndex, lastIndex);
    tabProgress.value = currentIndex - translationX / pageWidth;
  };
}

export function panOnEnd(tabProgress, startIndex, pageWidth, lastIndex, onSelectIndex) {
  return function (event) {
    'worklet';
    const currentIndex = startIndex.value || 0;
    const targetIndex = nextIndex(currentIndex, lastIndex, event.translationX, event.velocityX, pageWidth);

    tabProgress.value = withTiming(targetIndex, tabTransition);

    if (targetIndex !== currentIndex) {
      scheduleOnRN(onSelectIndex, targetIndex);
    }
  };
}

export function panOnFinalize(tabProgress, startIndex) {
  return function (_event, success) {
    'worklet';
    if (!success) {
      tabProgress.value = withTiming(startIndex.value || 0, tabTransition);
    }
  };
}

export function useTranslateX(activeIndex, dragTranslation, pageWidth) {
  return useDerivedValue(function () {
    'worklet';
    const baseOffset = -(activeIndex.value || 0) * pageWidth;
    const dragOffset = dragTranslation.value || 0;

    if (Math.abs(dragOffset) > 0.5) {
      return baseOffset + dragOffset;
    }

    return withTiming(baseOffset, tabTransition);
  });
}
