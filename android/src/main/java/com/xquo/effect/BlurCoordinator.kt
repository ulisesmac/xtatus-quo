package com.xquo.effect

import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Trace
import android.view.View
import android.view.ViewTreeObserver
import androidx.annotation.RequiresApi
import java.util.WeakHashMap

@RequiresApi(31)
internal class BlurCoordinator private constructor(private val root: View) : ViewTreeObserver.OnPreDrawListener {
  private val attached = HashSet<EffectView>()
  private val signatures = HashMap<EffectView, Long>()
  private val revisions = WeakHashMap<View, Long>()
  private val rasterCache = SourceRasterCache()
  private var neededLastFrame = emptySet<EffectView>()
  private var epoch = 0L

  // The framework assigns legacy animation matrices while drawing children.
  // This pixel-free overlay runs afterwards, before RenderThread consumes the
  // frame, so the backdrop stays aligned with this frame's actual transforms.
  private val placementUpdater = object : Drawable() {
    override fun draw(canvas: Canvas) {
      for (effect in attached) effect.positionBackdrop(root)
    }
    override fun setAlpha(alpha: Int) {}
    override fun setColorFilter(colorFilter: ColorFilter?) {}
    @Deprecated("Drawable opacity is unused")
    override fun getOpacity(): Int = PixelFormat.TRANSPARENT
  }

  init {
    placementUpdater.setBounds(0, 0, 1, 1)
    root.overlay.add(placementUpdater)
  }

  fun requestUpdate(view: EffectView) { signatures.remove(view) }

  fun detach(view: EffectView) {
    attached.remove(view)
    signatures.remove(view)
    neededLastFrame = neededLastFrame - view
    if (attached.isEmpty()) {
      rasterCache.retain(emptySet())
      if (root.viewTreeObserver.isAlive) root.viewTreeObserver.removeOnPreDrawListener(this)
      root.overlay.remove(placementUpdater)
      coordinators.remove(root)
    }
  }

  override fun onPreDraw(): Boolean {
    if (!root.isHardwareAccelerated || attached.none { it.needsCapture }) {
      rasterCache.retain(emptySet())
      neededLastFrame = emptySet()
      return true
    }
    Trace.beginSection("BlurView.compose")
    try {
      for (effect in attached) effect.beginBackdropFrame()
      val frame = SceneFrame(root, revisions, ++epoch)
      val plans = frame.plans(attached)
      val needed = plans.mapTo(HashSet()) { it.effect }
      var retainSources = false
      for (effect in attached) {
        val retaining = effect.retainBackdrop(root)
        retainSources = retainSources || retaining
        effect.setBackdropNeeded(effect in needed || retaining)
      }
      val capture = SceneCapture(frame, plans, rasterCache, retainSources)
      for (plan in plans) {
        val effect = plan.effect
        // Lower stages refresh first; their versions propagate only to consumers.
        val signature = plan.signature()
        val refresh = effect !in neededLastFrame || signatures[effect] != signature ||
          !effect.hasBackdropSource
        if (refresh) {
          Trace.beginSection(if (Trace.isEnabled()) "BlurView.capture/${effect.getTag(com.facebook.react.R.id.react_test_id) ?: effect.id}" else "BlurView.capture")
          try {
            effect.recordBackdrop(root, capture)
            signatures[effect] = signature
          } finally { Trace.endSection() }
        }
        // Update filter/placement before upper effects consume this stage, even
        // when its source commands can be reused.
        effect.prepareBackdrop(root)
      }
      for (effect in attached) effect.positionBackdrop(root)
      if (attached.any { SceneGeometry.hasAnimation(it, root) }) placementUpdater.invalidateSelf()
      neededLastFrame = needed
    } finally { Trace.endSection() }
    return true
  }

  companion object {
    private val coordinators = WeakHashMap<View, BlurCoordinator>()

    fun attach(root: View, view: EffectView): BlurCoordinator {
      check(Build.VERSION.SDK_INT >= 31)
      val coordinator = coordinators.getOrPut(root) {
        BlurCoordinator(root).also { root.viewTreeObserver.addOnPreDrawListener(it) }
      }
      coordinator.attached.add(view)
      coordinator.requestUpdate(view)
      return coordinator
    }
  }
}
