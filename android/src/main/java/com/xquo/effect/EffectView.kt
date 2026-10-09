package com.xquo.effect

import android.content.res.Configuration
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Paint
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import android.graphics.Matrix
import android.graphics.RenderEffect
import android.graphics.RenderNode
import android.graphics.RectF
import android.graphics.Shader
import android.os.Build
import android.os.Trace
import android.view.View
import androidx.annotation.RequiresApi
import com.facebook.react.uimanager.BackgroundStyleApplicator
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.views.view.ReactViewGroup
import kotlin.math.ceil

internal class EffectView(context: ThemedReactContext) : ReactViewGroup(context) {
  var strength = 100
    set(value) { field = value.coerceIn(0, 100); changed() }
  var blurAmount = -1f
    set(value) { field = value; changed() }
  var material = "regular"
    set(value) { field = value; changed() }
  var theme = "system"
    set(value) { field = value; changed() }
  var effectEnabled = true
    set(value) { field = value; changed() }
  var tint = Color.TRANSPARENT
    set(value) { field = value; changed() }
  var fallback: Int? = null
    set(value) { field = value; changed() }

  private var coordinator: BlurCoordinator? = null
  private val gpu = if (Build.VERSION.SDK_INT >= 31) GpuBackdrop() else null
  var configurationVersion = 0L
    private set
  var backdropVersion = 0L
    private set
  private var backdropNeeded = true
  private val backdropDrawable = BackdropDrawable()

  fun setBackdropNeeded(needed: Boolean) {
    if (backdropNeeded == needed) return
    backdropNeeded = needed
    // Update the native display list too, so covered views do not keep drawing
    // a RenderEffect whose source may animate independently on RenderThread.
    backdropDrawable.invalidateSelf()
    invalidate()
  }

  private val radiusPx: Float get() {
    val dp = when (material) {
      "clear" -> 10f
      "ultra-thin" -> 12f
      "thin" -> 18f
      "thick" -> 36f
      "chrome" -> 44f
      else -> 28f
    }
    return dp * resources.displayMetrics.density * strength / 100f
  }
  private val sigmaPx: Float get() = if (blurAmount >= 0f)
    BlurMath.sigmaPixels(blurAmount, resources.displayMetrics.density) else -1f
  private val zeroBlur: Boolean get() = if (blurAmount >= 0f) blurAmount == 0f else strength == 0
  val capturePadding: Float get() = ceil(if (sigmaPx >= 0f) sigmaPx * 3f + 8f else radiusPx * 2f)

  val needsCapture: Boolean get() = effectEnabled && !zeroBlur && Color.alpha(tint) < 255

  init {
    // The visible effect is a real native background, including for hit testing
    // and React Native's background styling/recycling lifecycle.
    background = backdropDrawable
    setWillNotDraw(false)
  }

  override fun onAttachedToWindow() {
    super.onAttachedToWindow()
    if (Build.VERSION.SDK_INT >= 31) {
      val scene = rootView.findViewById<View>(android.R.id.content) ?: rootView
      coordinator = BlurCoordinator.attach(scene, this)
    }
  }

  override fun onDetachedFromWindow() {
    coordinator?.detach(this)
    coordinator = null
    if (Build.VERSION.SDK_INT >= 31) gpu?.clear()
    super.onDetachedFromWindow()
  }

  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    super.onSizeChanged(w, h, oldw, oldh)
    changed()
  }

  override fun onConfigurationChanged(newConfig: Configuration) {
    super.onConfigurationChanged(newConfig)
    changed()
  }

  private fun drawBackdrop(canvas: Canvas) {
    val saved = canvas.save()
    BackgroundStyleApplicator.clipToPaddingBox(this, canvas)
    val dark = theme == "dark" || (theme == "system" &&
      resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES)
    if (effectEnabled && zeroBlur) {
      // Zero bypasses the effect on every Android version; explicit tint still applies.
      canvas.drawColor(tint)
    } else if (!effectEnabled || gpu == null || !canvas.isHardwareAccelerated()) {
      canvas.drawColor(fallback ?: if (dark) 0xFF232838.toInt() else 0xFFF1F2F5.toInt())
    } else {
      val materialTint = when (material) {
        "clear" -> 16
        "ultra-thin" -> 24
        "thin" -> 36
        "thick" -> 92
        "chrome" -> 120
        else -> 56
      }
      val color = when {
        blurAmount >= 0f -> tint
        tint != Color.TRANSPARENT -> tint
        zeroBlur -> Color.TRANSPARENT
        dark -> Color.argb(materialTint, 18, 22, 32)
        else -> Color.argb(materialTint, 245, 246, 250)
      }
      if (Build.VERSION.SDK_INT >= 31 && backdropNeeded && Color.alpha(color) < 255) {
        gpu.draw(canvas, width, height, color)
      } else {
        canvas.drawColor(color)
      }
    }
    canvas.restoreToCount(saved)
  }

  @RequiresApi(31)
  fun beginBackdropFrame() { gpu?.beginFrame() }

  @RequiresApi(31)
  fun recordBackdrop(root: View, capture: SceneCapture) {
    if (needsCapture) {
      gpu?.recordSource(root, this, capture)
    }
  }

  @RequiresApi(31)
  fun prepareBackdrop(root: View) {
    if (needsCapture && gpu?.prepare(root, this, radiusPx, sigmaPx) == true) backdropVersion++
  }

  @get:RequiresApi(31)
  val hasBackdropSource: Boolean get() = gpu?.hasContent == true

  @RequiresApi(31)
  fun retainBackdrop(root: View): Boolean =
    hasBackdropSource && SceneGeometry.isDisappearing(this, root)

  @RequiresApi(31)
  fun positionBackdrop(root: View) {
    if (backdropNeeded) gpu?.position(root, this)
  }

  private fun changed() {
    configurationVersion++
    backdropDrawable.invalidateSelf()
    invalidate()
  }

  private inner class BackdropDrawable : Drawable() {
    private val paint = Paint()

    override fun draw(canvas: Canvas) {
      if (paint.alpha == 255 && paint.colorFilter == null) {
        drawBackdrop(canvas)
      } else {
        val save = canvas.saveLayer(null, paint)
        try { drawBackdrop(canvas) } finally { canvas.restoreToCount(save) }
      }
    }

    override fun setAlpha(alpha: Int) {
      if (paint.alpha == alpha) return
      paint.alpha = alpha
      changed()
    }

    override fun getAlpha(): Int = paint.alpha

    override fun setColorFilter(colorFilter: ColorFilter?) {
      if (paint.colorFilter == colorFilter) return
      paint.colorFilter = colorFilter
      changed()
    }

    override fun getColorFilter(): ColorFilter? = paint.colorFilter

    @Deprecated("Drawable opacity is unused")
    override fun getOpacity(): Int = PixelFormat.TRANSLUCENT
  }

  @RequiresApi(31)
  private class GpuBackdrop {
    // Retain drawing commands for the scene, not a full-resolution bitmap.
    // Only the visible sampling rectangle is rasterized for the blur.
    private val source = RenderNode("BlurView.scene")
    private val blurred = RenderNode("BlurView.backdrop")
    // Cache the filtered pixels as well as the sampling commands. A moving
    // parent's clip must not rerun the filter on an otherwise unchanged scene.
    private val filtered = RenderNode("BlurView.filtered").apply {
      setUseCompositingLayer(true, null)
    }
    private val placement = RenderNode("BlurView.placement").apply { setClipToBounds(false) }
    private val tintPaint = Paint()
    private var filterTint: Int? = null
    private val viewToRoot = Matrix()
    private val rootToView = Matrix()
    private val previousTransform = Matrix()
    private val bounds = RectF()
    private val previousBounds = RectF()
    private val sampledBounds = RectF()
    private val restingBounds = RectF()
    private val restingTransform = Matrix()
    private var radius = 0f
    private var sigma = -1f
    private var padding = 0f
    private var filterX = -1f
    private var filterY = -1f
    private var sourceChanged = true
    private var recordedThisFrame = false
    private var sampleScaleX = 1f
    private var sampleScaleY = 1f

    val hasContent: Boolean get() = source.hasDisplayList()

    fun beginFrame() { recordedThisFrame = false }

    fun recordSource(root: View, view: EffectView, capture: SceneCapture) {
      if (root.width <= 0 || root.height <= 0 || view.width <= 0 || view.height <= 0) return
      if (source.width != root.width || source.height != root.height) sampledBounds.setEmpty()
      source.setPosition(0, 0, root.width, root.height)
      val canvas = source.beginRecording(root.width, root.height)
      try {
        capture.recordBehind(root, view, canvas)
      } finally {
        source.endRecording()
      }
      sourceChanged = true
      recordedThisFrame = true
    }

    fun prepare(root: View, view: EffectView, nextRadius: Float, nextSigma: Float): Boolean {
      val nextPadding = view.capturePadding
      if (radius != nextRadius || sigma != nextSigma || padding != nextPadding) {
        radius = nextRadius
        sigma = nextSigma
        padding = nextPadding
        sourceChanged = true
      }
      val changed = sourceChanged
      return position(root, view) || changed
    }

    fun position(root: View, view: EffectView): Boolean {
      if (!hasContent || !SceneGeometry.toRoot(view, root, viewToRoot) ||
        !viewToRoot.invert(rootToView)) return false

      bounds.set(0f, 0f, view.width.toFloat(), view.height.toFloat())
      viewToRoot.mapRect(bounds)
      bounds.inset(-padding, -padding)
      // Clamp at the real scene edge. Transparent space outside the window must
      // never become input pixels, or sharp content leaks through the blurred alpha.
      if (!bounds.intersect(0f, 0f, source.width.toFloat(), source.height.toFloat())) {
        val hadPlacement = placement.hasDisplayList()
        placement.discardDisplayList()
        return hadPlacement
      }
      if (!sourceChanged && bounds == previousBounds && rootToView == previousTransform &&
        blurred.hasDisplayList() && filtered.hasDisplayList() && placement.hasDisplayList()) return false

      // While pixels are changing, filter only the current visible input. Once
      // they settle, reserving the resting region makes movement cacheable.
      val followBounds = recordedThisFrame && sampledBounds != bounds
      if (sourceChanged || !blurred.hasDisplayList() || !filtered.hasDisplayList() ||
        followBounds || !sampledBounds.contains(bounds) || !placement.hasDisplayList()) {
        if (followBounds || !sampledBounds.contains(bounds)) {
          sampledBounds.set(bounds)
          // Reserve the resting region during a native slide. The source is in
          // window coordinates: movement should change placement, not its pixels.
          if (!recordedThisFrame && SceneGeometry.hasAnimation(view, root) &&
            SceneGeometry.toRoot(view, root, restingTransform, includeAnimation = false)) {
            restingBounds.set(0f, 0f, view.width.toFloat(), view.height.toFloat())
            restingTransform.mapRect(restingBounds)
            restingBounds.inset(-padding, -padding)
            sampledBounds.union(restingBounds)
            sampledBounds.intersect(0f, 0f, source.width.toFloat(), source.height.toFloat())
          }
        }
        val width = ceil(sampledBounds.width() / SCALE).toInt().coerceAtLeast(1)
        val height = ceil(sampledBounds.height() / SCALE).toInt().coerceAtLeast(1)
        val scaleX = width / sampledBounds.width()
        val scaleY = height / sampledBounds.height()
        sampleScaleX = scaleX
        sampleScaleY = scaleY
        val blurX = if (sigma >= 0f) BlurMath.renderEffectRadius(sigma * scaleX)
          else (radius * scaleX).coerceAtLeast(0.01f)
        val blurY = if (sigma >= 0f) BlurMath.renderEffectRadius(sigma * scaleY)
          else (radius * scaleY).coerceAtLeast(0.01f)
        if (blurX != filterX || blurY != filterY) {
          blurred.setRenderEffect(RenderEffect.createBlurEffect(blurX, blurY, Shader.TileMode.CLAMP))
          filterX = blurX
          filterY = blurY
        }
        Trace.beginSection("BlurView.filter")
        try {
          blurred.setPosition(0, 0, width, height)
          val sampling = blurred.beginRecording(width, height)
          try {
            sampling.scale(scaleX, scaleY)
            sampling.translate(-sampledBounds.left, -sampledBounds.top)
            sampling.drawRenderNode(source)
          } finally {
            blurred.endRecording()
          }
          filtered.setPosition(0, 0, width, height)
          val result = filtered.beginRecording(width, height)
          try {
            result.drawRenderNode(blurred)
          } finally {
            filtered.endRecording()
          }
        } finally { Trace.endSection() }
      }
      // Placement is cheap to record and uses public APIs. Keep the filtered
      // display list unchanged when only the native animation matrix moves.
      placement.setPosition(0, 0, view.width, view.height)
      val output = placement.beginRecording(view.width, view.height)
      try {
        output.concat(rootToView)
        output.translate(sampledBounds.left, sampledBounds.top)
        output.scale(1f / sampleScaleX, 1f / sampleScaleY)
        output.drawRenderNode(filtered)
      } finally {
        placement.endRecording()
      }
      previousBounds.set(bounds)
      previousTransform.set(rootToView)
      sourceChanged = false
      return true
    }

    fun draw(canvas: Canvas, width: Int, height: Int, tint: Int) {
      // Source-over tint is associative with compositing the blur. Apply it in
      // the existing layer paint instead of another full-resolution color pass.
      if (filterTint != tint) {
        tintPaint.colorFilter = BlendModeColorFilter(tint, BlendMode.SRC_OVER)
        filtered.setUseCompositingLayer(true, tintPaint)
        filterTint = tint
      }
      // Keep the node reference in the View's display list even while its content
      // is empty. A hidden rerender can precede recapture after reattachment;
      // skipping the reference would require another press/invalidation to show it.
      placement.setPosition(0, 0, width, height)
      canvas.drawRenderNode(placement)
    }

    fun clear() {
      source.discardDisplayList()
      blurred.discardDisplayList()
      filtered.discardDisplayList()
      placement.discardDisplayList()
      sampledBounds.setEmpty()
      sourceChanged = true
    }

    companion object { private const val SCALE = 4f }
  }
}
