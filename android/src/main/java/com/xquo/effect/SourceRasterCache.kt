package com.xquo.effect

import android.graphics.RenderNode
import android.os.Trace
import android.view.View
import androidx.annotation.RequiresApi

/** Bounded pixel cache for clipped, non-scrolling source subtrees. */
@RequiresApi(31)
internal class SourceRasterCache {
  private data class Entry(val signature: Long, val node: RenderNode, val pixels: Long)
  private val entries = LinkedHashMap<View, Entry>(16, 0.75f, true)
  private var pixels = 0L

  fun retain(views: Set<View>) {
    val iterator = entries.iterator()
    while (iterator.hasNext()) {
      val entry = iterator.next()
      if (entry.key !in views || !entry.key.isAttachedToWindow) {
        release(entry.value)
        iterator.remove()
      }
    }
  }

  fun get(view: View, signature: Long, record: () -> RenderNode): RenderNode {
    val size = view.width.toLong() * view.height
    if (size <= 0 || size > MAX_SOURCE_PIXELS) {
      entries.remove(view)?.let(::release)
      return record()
    }
    val previous = entries[view]
    if (previous?.signature == signature && previous.node.hasDisplayList()) {
      previous.node.setUseCompositingLayer(true, null)
      Trace.beginSection("BlurView.sourceRasterHit")
      Trace.endSection()
      return previous.node
    }
    if (previous != null) { entries.remove(view); release(previous) }
    while (pixels + size > MAX_PIXELS && entries.isNotEmpty()) {
      val iterator = entries.iterator()
      release(iterator.next().value)
      iterator.remove()
    }
    // Fresh nodes on invalidation preserve the existing overscroll protection.
    val node = record()
    // Repeated content changes should not allocate a new pixel layer each time.
    node.setUseCompositingLayer(previous == null, null)
    entries[view] = Entry(signature, node, size)
    pixels += size
    return node
  }

  private fun release(entry: Entry) {
    pixels -= entry.pixels
    // Existing blur recordings may still reference these drawing commands.
    entry.node.setUseCompositingLayer(false, null)
  }

  companion object {
    private const val MAX_SOURCE_PIXELS = 2_000_000L
    private const val MAX_PIXELS = 4_000_000L
  }
}
