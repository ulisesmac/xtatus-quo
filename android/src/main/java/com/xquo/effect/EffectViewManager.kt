package com.xquo.effect

import com.facebook.react.module.annotations.ReactModule
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.ViewGroupManager
import com.facebook.react.uimanager.ViewManagerDelegate
import com.facebook.react.uimanager.annotations.ReactProp
import com.facebook.react.viewmanagers.MTREffectViewManagerDelegate
import com.facebook.react.viewmanagers.MTREffectViewManagerInterface

@ReactModule(name = EffectViewManager.REACT_CLASS)
class EffectViewManager :
  ViewGroupManager<EffectView>(),
  MTREffectViewManagerInterface<EffectView> {

  private val delegate: ViewManagerDelegate<EffectView> = MTREffectViewManagerDelegate(this)

  override fun getName(): String = REACT_CLASS

  override fun createViewInstance(reactContext: ThemedReactContext): EffectView =
    EffectView(reactContext)

  override fun getDelegate(): ViewManagerDelegate<EffectView> = delegate

  @ReactProp(name = "blurIntensity", defaultInt = 100)
  override fun setBlurIntensity(view: EffectView, value: Int) {
    // Android currently renders this as a plain view.
  }

  @ReactProp(name = "effect")
  override fun setEffect(view: EffectView, value: String?) {
    // Android currently renders this as a plain view.
  }

  @ReactProp(name = "intensity")
  override fun setIntensity(view: EffectView, value: String?) {
    // Android currently renders this as a plain view.
  }

  @ReactProp(name = "theme")
  override fun setTheme(view: EffectView, value: String?) {
    // Android currently renders this as a plain view.
  }

  @ReactProp(name = "interactive", defaultBoolean = false)
  override fun setInteractive(view: EffectView, value: Boolean) {
    // Android currently renders this as a plain view.
  }

  companion object {
    const val REACT_CLASS = "MTREffectView"
  }
}
