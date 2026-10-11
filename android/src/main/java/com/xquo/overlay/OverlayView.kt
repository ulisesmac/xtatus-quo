package com.xquo.overlay

import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import com.facebook.react.uimanager.JSTouchDispatcher
import com.facebook.react.uimanager.PointerEvents
import com.facebook.react.uimanager.RootView
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.TouchTargetHelper
import com.facebook.react.uimanager.UIManagerHelper
import com.facebook.react.views.view.ReactViewGroup

internal class OverlayView(private val reactContext: ThemedReactContext) : ReactViewGroup(reactContext) {
  private val overlay = OverlayRoot(reactContext)
  private var scene: ViewGroup? = null

  init {
    // Content is drawn by the window-level root, not this mounting placeholder.
    visibility = GONE
  }

  private val keepOnTop = ViewTreeObserver.OnPreDrawListener {
    scene?.let { root ->
      if (root.indexOfChild(overlay) != root.childCount - 1) overlay.bringToFront()
      var z = 0f
      for (index in 0 until root.childCount) {
        val child = root.getChildAt(index)
        if (child !== overlay) z = maxOf(z, child.z)
      }
      overlay.translationZ = z + 1f
      overlay.layout(0, 0, root.width, root.height)
    }
    true
  }

  override fun setId(id: Int) {
    super.setId(id)
    overlay.id = id
  }

  override fun addView(child: View?, index: Int) {
    // GestureHandlerRootView's manager does not forward pointerEvents.
    // Only notification descendants should claim touches in this full-screen host.
    (child as? ReactViewGroup)?.pointerEvents = PointerEvents.BOX_NONE
    overlay.addView(child, index)
  }
  override fun getChildCount(): Int = overlay.childCount
  override fun getChildAt(index: Int): View = overlay.getChildAt(index)
  override fun removeViewAt(index: Int) = overlay.removeViewAt(index)
  override fun removeAllViews() = overlay.removeAllViews()

  override fun onAttachedToWindow() {
    super.onAttachedToWindow()
    val root = rootView.findViewById<ViewGroup>(android.R.id.content) ?: return
    scene = root
    root.addView(overlay, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
    root.viewTreeObserver.addOnPreDrawListener(keepOnTop)
  }

  override fun onDetachedFromWindow() {
    scene?.let { root ->
      if (root.viewTreeObserver.isAlive) root.viewTreeObserver.removeOnPreDrawListener(keepOnTop)
      root.removeView(overlay)
    }
    scene = null
    super.onDetachedFromWindow()
  }
}

private class OverlayRoot(private val reactContext: ThemedReactContext) : ReactViewGroup(reactContext), RootView {
  private val touches = JSTouchDispatcher(this)
  private val coordinates = FloatArray(2)
  private var ownsTouch = false
  private var lastEventTime = -1L
  private var lastEventAction = -1
  private val dispatcher get() = UIManagerHelper.getEventDispatcherForReactTag(reactContext, id)

  init {
    pointerEvents = PointerEvents.BOX_NONE
    clipChildren = false
  }

  override fun dispatchTouchEvent(event: MotionEvent): Boolean {
    if (event.actionMasked == MotionEvent.ACTION_DOWN) {
      val target = TouchTargetHelper.findTargetTagAndCoordinatesForTouch(event.x, event.y, this, coordinates, null)
      ownsTouch = target != id && target != View.NO_ID
    }
    if (!ownsTouch) return false
    val handled = super.dispatchTouchEvent(event)
    if (event.actionMasked == MotionEvent.ACTION_UP || event.actionMasked == MotionEvent.ACTION_CANCEL) ownsTouch = false
    return handled
  }

  private fun dispatchToReact(event: MotionEvent) {
    if (event.eventTime == lastEventTime && event.action == lastEventAction) return
    lastEventTime = event.eventTime
    lastEventAction = event.action
    dispatcher?.let { touches.handleTouchEvent(event, it, reactContext) }
  }

  override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
    dispatchToReact(event)
    return super.onInterceptTouchEvent(event)
  }

  override fun onTouchEvent(event: MotionEvent): Boolean {
    dispatchToReact(event)
    return ownsTouch
  }

  override fun requestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
    parent?.requestDisallowInterceptTouchEvent(disallowIntercept)
  }

  override fun onChildStartedNativeGesture(childView: View?, ev: MotionEvent) {
    dispatcher?.let { touches.onChildStartedNativeGesture(ev, it) }
  }

  override fun onChildEndedNativeGesture(childView: View, ev: MotionEvent) {
    dispatcher?.let { touches.onChildEndedNativeGesture(ev, it) }
  }

  override fun handleException(t: Throwable) {
    reactContext.handleException(RuntimeException(t))
  }
}
