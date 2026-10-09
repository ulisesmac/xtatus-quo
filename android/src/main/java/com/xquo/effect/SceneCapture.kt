package com.xquo.effect

import android.graphics.Canvas
import android.graphics.RenderNode
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.facebook.react.uimanager.BackgroundStyleApplicator
import com.facebook.react.views.view.ReactViewGroup

@RequiresApi(31)
internal class SceneCapture(private val frame: SceneFrame, plans: List<SceneFrame.Plan>,
  private val rasterCache: SourceRasterCache, retainSources: Boolean) {
  private val sources = HashMap<View, RenderNode>()
  private val layers = plans.associate { it.effect to it.layers }
  // A disappearing native screen still draws its retained backdrop. Do not
  // evict/change pixel layers referenced by that snapshot during its exit;
  // any live capture in the meantime uses the original fresh-node path.
  private val rasterSources = if (retainSources) emptyMap()
    else frame.rasterSources(plans).also { rasterCache.retain(it.keys) }

  fun recordBehind(root: View, target: EffectView, canvas: Canvas) {
    val path = ArrayList<View>()
    var view: View? = target
    while (view != null) {
      path.add(view)
      if (view === root) break
      view = view.parent as? View
    }
    if (path.lastOrNull() !== root) return
    path.reverse()
    val save = canvas.save()
    try {
      root.clipBounds?.let(canvas::clipRect)
      drawPath(path, 0, canvas, layers.getValue(target))
    } finally {
      canvas.restoreToCount(save)
    }
  }

  private fun drawPath(path: List<View>, index: Int, canvas: Canvas, paint: Set<View>) {
    val view = path[index]
    if (index == path.lastIndex || !isDrawable(view)) return
    val parent = view as? ViewGroup ?: return

    // Partial ancestors contribute their background, not custom onDraw/foreground content.
    if (view in paint) view.background?.let {
      // Pre-draw capture can run before View.draw has assigned the first background bounds.
      it.setBounds(0, 0, view.width, view.height)
      it.draw(canvas)
    }
    clipChildrenScene(parent, canvas)

    val next = path[index + 1]
    for (child in frame.orderedChildren(parent)) {
      if (child === next) {
        drawChild(parent, child, canvas) { drawPath(path, index + 1, canvas, paint) }
        break
      }
      if (child in paint) drawChild(parent, child, canvas) { canvas.drawRenderNode(sourceFor(child)) }
    }
  }

  private fun drawChild(
    parent: ViewGroup,
    child: View,
    canvas: Canvas,
    draw: () -> Unit,
  ) {
    if (!isDrawable(child)) return
    val save = canvas.save()
    try {
      canvas.translate(
        (child.left - parent.scrollX).toFloat(),
        (child.top - parent.scrollY).toFloat(),
      )
      // matrix already includes translationX/Y, pivot, scale and rotation.
      canvas.concat(child.matrix)
      if (parent.clipChildren) {
        canvas.clipRect(0, 0, child.width, child.height)
        if (canvas.quickReject(0f, 0f, child.width.toFloat(), child.height.toFloat())) return
      }
      child.clipBounds?.let(canvas::clipRect)
      if (child.alpha < 1f) {
        // Keep overlapping children in one opacity group; the capture clip bounds the layer.
        canvas.saveLayerAlpha(null, (child.alpha * 255f).toInt().coerceIn(0, 255))
      }
      draw()
    } finally {
      canvas.restoreToCount(save)
    }
  }

  private fun sourceFor(view: View): RenderNode = sources.getOrPut(view) {
    val signature = rasterSources[view]
    if (signature != null) rasterCache.get(view, signature) { recordSource(view) }
    else recordSource(view)
  }

  private fun recordSource(view: View): RenderNode {
    // EdgeEffect mutates the node behind its RecordingCanvas. Give each source its
    // own viewport, and a fresh node each capture pass so stretch cannot persist.
    return RenderNode("BlurView.source").apply {
      setPosition(0, 0, view.width, view.height)
      setClipToBounds(false)
      val recording = beginRecording(view.width, view.height)
      try {
        recording.translate(-view.scrollX.toFloat(), -view.scrollY.toFloat())
        view.draw(recording)
      } finally {
        endRecording()
      }
    }
  }

  private fun clipChildrenScene(parent: ViewGroup, canvas: Canvas) {
    if (parent is ReactViewGroup && parent.overflow?.let { it != "visible" } == true) {
      BackgroundStyleApplicator.clipToPaddingBox(parent, canvas)
    }
    if (parent.clipToPadding &&
      (parent.paddingLeft != 0 || parent.paddingTop != 0 ||
        parent.paddingRight != 0 || parent.paddingBottom != 0)
    ) {
      canvas.clipRect(
        parent.paddingLeft,
        parent.paddingTop,
        parent.width - parent.paddingRight,
        parent.height - parent.paddingBottom,
      )
    }
  }

  private fun isDrawable(view: View): Boolean =
    view.visibility == View.VISIBLE && view.alpha > 0f &&
      view.width > 0 && view.height > 0 && view.scaleX != 0f && view.scaleY != 0f
}
