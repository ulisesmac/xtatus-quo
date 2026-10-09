package com.xquo.effect

internal object BlurMath {
  // Figma's uniform blur amount is twice Gaussian sigma in design units.
  fun sigmaPixels(amount: Float, density: Float): Float = amount * density / 2f

  // AOSP RenderEffect uses sigma = 0.57735 * radius + 0.5 for positive radii.
  // Invert it AFTER downsampling, including its additive half-pixel term.
  fun renderEffectRadius(sigmaPixels: Float): Float =
    ((sigmaPixels - 0.5f) / 0.57735f).coerceAtLeast(0.01f)
}
