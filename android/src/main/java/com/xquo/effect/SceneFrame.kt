package com.xquo.effect

import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Region
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.facebook.react.uimanager.BackgroundStyleApplicator
import com.facebook.react.uimanager.style.BorderRadiusProp
import com.facebook.react.uimanager.style.LogicalEdge
import com.facebook.react.views.view.ReactViewGroup
import java.util.WeakHashMap

/** A frame-local description of visible effects and the content each one samples. */
@RequiresApi(31)
internal class SceneFrame(
  private val root: View,
  private val revisions: WeakHashMap<View, Long>,
  private val epoch: Long,
) {
  class Plan(
    val effect: EffectView,
    val layers: Set<View>,
    private val content: Long,
    private val dependencies: Set<EffectView>,
  ) {
    fun signature(): Long {
      var result = content
      for (effect in dependencies) result = result * 31 + effect.backdropVersion
      return result
    }
  }

  private data class Layer(val view: View, val backgroundOnly: Boolean)
  private data class Content(val signature: Long, val recording: Long, val effects: Set<EffectView>, val requiresFreshSource: Boolean)
  private val children = HashMap<ViewGroup, List<View>>()
  private val contents = HashMap<View, Content>()
  private val backgrounds = HashMap<View, Long>()
  private val geometries = HashMap<View, Long>()
  private val recordingGeometries = HashMap<View, Long>()
  private val intersection = Region()
  private val clipBounds = Rect()
  private val opaqueRects = HashMap<View, Rect?>()
  private val opaquePaths = HashMap<View, Boolean>()
  private val visibleRects = HashMap<View, Rect>()
  private val sampleRects = HashMap<EffectView, Rect>()
  private val values = FloatArray(9)
  private val window = Rect().also { root.getGlobalVisibleRect(it) }

  fun orderedChildren(parent: ViewGroup): List<View> = children.getOrPut(parent) {
    ArrayList<View>(parent.childCount).apply {
      for (index in 0 until parent.childCount) {
        parent.getChildAt(parent.getChildDrawingOrder(index))?.let(::add)
      }
      sortBy { it.z }
    }
  }

  fun plans(attached: Collection<EffectView>): List<Plan> {
    val needed = Region()
    val remaining = HashSet<EffectView>()
    for (effect in attached) {
      if (effect.needsCapture && drawable(effect) && !visibleRect(effect).isEmpty) {
        needed.op(visibleRect(effect), Region.Op.UNION)
        remaining.add(effect)
      }
    }
    val effects = ArrayList<EffectView>()
    fun visit(view: View) {
      if (needed.isEmpty || remaining.isEmpty() || !drawable(view)) return
      // A leaf cannot contain a blur. Its only visibility contribution is a
      // proven opaque background; ordinary text/images need no global-rect walk.
      if (view !is ViewGroup) {
        opaqueRect(view)?.let { needed.op(it, Region.Op.DIFFERENCE) }
        return
      }
      val visible = visibleRect(view)
      val onScreen = !visible.isEmpty
      // Overflow can put a child outside its parent's bounds, so only prune clipped groups.
      if ((!onScreen || !intersects(needed, visible)) &&
        (view !is ViewGroup || view.clipChildren ||
          (view is ReactViewGroup && view.overflow?.let { it != "visible" } == true))) return
      if (view is ViewGroup) orderedChildren(view).asReversed().forEach(::visit)
      if (view is EffectView) remaining.remove(view)
      if (view is EffectView && view.needsCapture && onScreen &&
        intersects(needed, visible)) {
        effects.add(view)
        // A visible upper blur can need a lower effect that is otherwise covered.
        needed.op(sampleBounds(view), Region.Op.UNION)
      }
      opaqueRect(view)?.let { needed.op(it, Region.Op.DIFFERENCE) }
    }
    visit(root)
    // Build every content signature before recording: View.draw can clear dirty flags.
    return effects.asReversed().map(::plan)
  }

  private fun plan(effect: EffectView): Plan {
    val path = ArrayList<View>()
    var current: View? = effect
    while (current != null) {
      path.add(current)
      if (current === root) break
      current = current.parent as? View
    }
    path.reverse()
    // Retain a complete scene display list so a departing view can sample new
    // positions without revisiting children that native navigation has removed.
    // Fully opaque screens still eliminate everything behind them.
    val region = window
    val layers = ArrayList<Layer>()
    for (index in 0 until path.lastIndex) {
      val parent = path[index] as ViewGroup
      layers.add(Layer(parent, true))
      for (child in orderedChildren(parent)) {
        if (child === path[index + 1]) break
        if (drawable(child)) layers.add(Layer(child, false))
      }
    }
    // Anything behind a proven opaque cover cannot contribute to this backdrop.
    val start = layers.indexOfLast { layer ->
      if (layer.backgroundOnly) opaqueRect(layer.view)?.contains(region) == true
      else covers(layer.view, region)
    }.coerceAtLeast(0)
    // The target changes sampling/output, not the scene drawn before it.
    var signature = 0L
    for (index in 0 until path.lastIndex) signature = signature * 31 + geometry(path[index])
    val paint = LinkedHashSet<View>()
    val dependencies = LinkedHashSet<EffectView>()
    for (layer in layers.subList(start, layers.size)) {
      paint.add(layer.view)
      signature = signature * 31 + System.identityHashCode(layer.view)
      if (layer.backgroundOnly) {
        signature = signature * 31 + background(layer.view)
        if (layer.view is EffectView) dependencies.add(layer.view)
      } else {
        val source = content(layer.view)
        signature = signature * 31 + source.signature
        dependencies.addAll(source.effects)
      }
    }
    return Plan(effect, paint, signature, dependencies)
  }

  private fun covers(view: View, area: Rect): Boolean {
    if (!drawable(view)) return false
    if (opaqueRect(view)?.contains(area) == true) return true
    return view is ViewGroup && orderedChildren(view).asReversed().any { covers(it, area) }
  }

  private fun content(view: View): Content = contents.getOrPut(view) {
    var hash = geometry(view, placement = false)
    val effects = LinkedHashSet<EffectView>()
    var requiresFreshSource = view is android.widget.ScrollView ||
      view is android.widget.HorizontalScrollView || view.hasTransientState() ||
      view.animation?.hasEnded() == false
    if (drawable(view)) {
      if (view.isDirty) revisions[view] = epoch
      hash = hash * 31 + (revisions[view] ?: 0L)
      // Whole subtrees report drawable changes through isDirty; only partial
      // ancestors need individual background-property fingerprints.
      if (view is EffectView) {
        hash = hash * 31 + view.configurationVersion
        effects.add(view)
      }
      if (view is ViewGroup) for (child in orderedChildren(view)) {
        val source = content(child)
        hash = hash * 31 + source.signature
        effects.addAll(source.effects)
        requiresFreshSource = requiresFreshSource || source.requiresFreshSource
      }
    }
    Content(geometry(view) * 31 + hash, hash, effects, requiresFreshSource)
  }

  fun rasterSources(plans: List<Plan>): Map<View, Long> {
    val result = HashMap<View, Long>()
    for (plan in plans) for (view in plan.layers) {
      // Bound the raster without cropping overflow; native transitions and
      // scroll viewports retain fresh source nodes.
      if (view !is ReactViewGroup || view.overflow != "hidden" || view is EffectView ||
        SceneGeometry.hasAnimation(view, root)) continue
      val source = content(view)
      if (source.effects.isEmpty() && !source.requiresFreshSource) result[view] = source.recording
    }
    return result
  }

  private fun background(view: View): Long = backgrounds.getOrPut(view) {
    val drawable = view.background
    var hash = System.identityHashCode(drawable).toLong()
    if (drawable != null) {
      hash = hash * 31 + drawable.alpha
      hash = hash * 31 + drawable.level
      hash = hash * 31 + drawable.state.contentHashCode()
      if (drawable is ColorDrawable) hash = hash * 31 + drawable.color
      // Unknown/stateful native backgrounds remain conservative. Ancestor dirtiness
      // caused solely by children must not invalidate ordinary RN container backgrounds.
      if (view.isDirty && (drawable.isStateful ||
          (view !is ReactViewGroup && drawable !is ColorDrawable))) hash = hash * 31 + epoch
    }
    if (drawable != null && view is ReactViewGroup) {
      hash = hash * 31 + (BackgroundStyleApplicator.getBackgroundColor(view) ?: 0)
      for (corner in BorderRadiusProp.entries) {
        hash = hash * 31 + (BackgroundStyleApplicator.getBorderRadius(view, corner)?.hashCode() ?: 0)
      }
      for (edge in LogicalEdge.entries) {
        hash = hash * 31 + (BackgroundStyleApplicator.getBorderWidth(view, edge)?.toBits() ?: 0)
        hash = hash * 31 + (BackgroundStyleApplicator.getBorderColor(view, edge) ?: 0)
      }
      hash = hash * 31 + (BackgroundStyleApplicator.getBorderStyle(view)?.hashCode() ?: 0)
    }
    if (view is EffectView) hash = hash * 31 + view.configurationVersion
    hash
  }

  private fun geometry(view: View, placement: Boolean = true): Long =
    (if (placement) geometries else recordingGeometries).getOrPut(view) {
      var hash = System.identityHashCode(view).toLong()
      if (placement) { hash = hash * 31 + view.left; hash = hash * 31 + view.top }
      hash = hash * 31 + view.width; hash = hash * 31 + view.height
      hash = hash * 31 + view.scrollX; hash = hash * 31 + view.scrollY
      hash = hash * 31 + view.visibility
      if (placement) { hash = hash * 31 + view.alpha.toBits(); hash = hash * 31 + view.z.toBits() }
      hash = hash * 31 + view.layoutDirection
      hash = hash * 31 + if (view.getClipBounds(clipBounds)) clipBounds.hashCode() else 0
      hash = hash * 31 + view.paddingLeft; hash = hash * 31 + view.paddingTop
      hash = hash * 31 + view.paddingRight; hash = hash * 31 + view.paddingBottom
      if (view is ViewGroup) {
        hash = hash * 31 + if (view.clipChildren) 1 else 0
        hash = hash * 31 + if (view.clipToPadding) 1 else 0
      }
      if (view is ReactViewGroup) hash = hash * 31 + (view.overflow?.hashCode() ?: 0)
      if (placement) {
        view.matrix.getValues(values)
        for (value in values) hash = hash * 31 + value.toBits()
      }
      hash
    }

  private fun sampleBounds(effect: EffectView): Rect = sampleRects.getOrPut(effect) {
    val padding = effect.capturePadding
    val rect = RectF(-padding, -padding, effect.width + padding, effect.height + padding)
    val matrix = Matrix()
    effect.transformMatrixToGlobal(matrix)
    matrix.mapRect(rect)
    Rect().also { rect.roundOut(it); if (!it.intersect(window)) it.setEmpty() }
  }

  private fun visibleRect(view: View): Rect = visibleRects.getOrPut(view) {
    Rect().also { if (!view.getGlobalVisibleRect(it)) it.setEmpty() }
  }

  private fun opaqueRect(view: View): Rect? {
    if (opaqueRects.containsKey(view)) return opaqueRects[view]
    val color = if (view is ReactViewGroup) BackgroundStyleApplicator.getBackgroundColor(view)
      else (view.background as? ColorDrawable)?.color
    val rect = if (color != null && Color.alpha(color) == 255 &&
      view.background?.alpha == 255 && opaquePath(view)) visibleRect(view).takeUnless { it.isEmpty }
      else null
    opaqueRects[view] = rect
    return rect
  }

  private fun opaquePath(view: View): Boolean = opaquePaths.getOrPut(view) {
    view.alpha == 1f && view.matrix.rectStaysRect() && !view.clipToOutline &&
      view.animation?.hasEnded() != false &&
      (view !is ReactViewGroup || BorderRadiusProp.entries.none {
        (BackgroundStyleApplicator.getBorderRadius(view, it)?.resolve(1f) ?: 0f) != 0f
      }) && (view === root || (view.parent as? View)?.let(::opaquePath) != false)
  }

  private fun intersects(region: Region, bounds: Rect): Boolean {
    if (region.quickReject(bounds)) return false
    if (region.quickContains(bounds)) return true
    intersection.set(region)
    return intersection.op(bounds, Region.Op.INTERSECT)
  }

  private fun drawable(view: View): Boolean =
    view.visibility == View.VISIBLE && view.alpha > 0f && view.width > 0 &&
      view.height > 0 && view.scaleX != 0f && view.scaleY != 0f
}
