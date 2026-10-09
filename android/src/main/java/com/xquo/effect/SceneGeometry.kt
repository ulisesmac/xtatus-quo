package com.xquo.effect

import android.graphics.Matrix
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi

@RequiresApi(31)
internal object SceneGeometry {
  fun toRoot(view: View, root: View, result: Matrix, includeAnimation: Boolean = true): Boolean {
    if (view === root) {
      result.reset()
      return true
    }
    val parent = view.parent as? View ?: return false
    if (!toRoot(parent, root, result, includeAnimation)) return false
    result.preTranslate((view.left - parent.scrollX).toFloat(), (view.top - parent.scrollY).toFloat())
    // Legacy native-stack animations live on a separate RenderNode matrix.
    // Read it after framework drawing; never advance the Animation ourselves.
    if (includeAnimation && view.animation != null) view.animationMatrix?.let(result::preConcat)
    result.preConcat(view.matrix)
    return true
  }

  fun isDisappearing(view: View, root: View): Boolean {
    if (!view.isAttachedToWindow) return false
    var current = view
    var disappearing = false
    while (current !== root) {
      val parent = current.parent as? ViewGroup ?: return false
      // startViewTransition retains mParent/attachment after removing a child
      // from the ordinary child array, until the exit animation finishes.
      if (parent.indexOfChild(current) < 0) disappearing = true
      current = parent
    }
    return disappearing
  }

  fun hasAnimation(view: View, root: View): Boolean {
    var current: View? = view
    while (current != null) {
      if (current.animation?.hasEnded() == false) return true
      if (current === root) break
      current = current.parent as? View
    }
    return false
  }
}
