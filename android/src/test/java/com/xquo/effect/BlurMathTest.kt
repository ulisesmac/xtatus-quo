package com.xquo.effect

import org.junit.Assert.assertEquals
import org.junit.Test

class BlurMathTest {
  @Test fun figmaFortyHasTwentyUnitSigmaAcrossDensitiesAndCaptureRounding() {
    for (density in listOf(0.75f, 1f, 1.5f, 2f, 2.625f, 3f, 4f)) {
      for (scale in listOf(0.25f, 0.251f, 0.267f, 0.5f, 1f)) {
        val radius = BlurMath.renderEffectRadius(BlurMath.sigmaPixels(40f, density) * scale)
        val actualSigma = (0.57735f * radius + 0.5f) / scale / density
        assertEquals("density=$density scale=$scale", 20f, actualSigma, 0.0001f)
      }
    }
  }

  @Test fun fractionalAmountsAreNotRoundedToIntensityPercentages() {
    for (amount in listOf(8f, 15.5f, 25f, 39.75f, 64f, 100f)) {
      val density = 2.625f
      val scale = 0.253f
      val radius = BlurMath.renderEffectRadius(BlurMath.sigmaPixels(amount, density) * scale)
      assertEquals(amount / 2f, (0.57735f * radius + 0.5f) / scale / density, 0.0001f)
    }
  }

  @Test fun zeroAmountHasZeroTargetSigma() {
    assertEquals(0f, BlurMath.sigmaPixels(0f, 3f), 0f)
  }
}
