package com.xquo.overlay

import com.facebook.react.module.annotations.ReactModule
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.views.view.ReactViewGroup
import com.facebook.react.views.view.ReactViewManager

@ReactModule(name = OverlayViewManager.REACT_CLASS)
class OverlayViewManager : ReactViewManager() {
  override fun getName() = REACT_CLASS
  override fun createViewInstance(context: ThemedReactContext): ReactViewGroup = OverlayView(context)

  companion object { const val REACT_CLASS = "MTRXquoOverlay" }
}
