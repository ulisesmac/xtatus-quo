package com.xquo.effect

import android.graphics.Color
import com.facebook.react.module.annotations.ReactModule
import com.facebook.react.bridge.JavaOnlyMap
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.uimanager.ReactStylesDiffMap
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.ViewManagerDelegate
import com.facebook.react.uimanager.ViewManagerPropertyUpdater
import com.facebook.react.uimanager.annotations.ReactProp
import com.facebook.react.viewmanagers.MTREffectViewManagerDelegate
import com.facebook.react.viewmanagers.MTREffectViewManagerInterface
import com.facebook.react.views.view.ReactViewGroup
import com.facebook.react.views.view.ReactViewManager

@ReactModule(name = EffectViewManager.REACT_CLASS)
class EffectViewManager : ReactViewManager(), MTREffectViewManagerInterface<ReactViewGroup> {
  private val effectDelegate = MTREffectViewManagerDelegate(this)
  private val viewDelegate = object : ViewManagerDelegate<ReactViewGroup> {
    @Suppress("DEPRECATION", "ACCIDENTAL_OVERRIDE")
    override fun setProperty(view: ReactViewGroup, propName: String, value: Any?) {
      when (propName) {
        "effect", "intensity", "theme", "blurIntensity", "blurAmount", "interactive" ->
          effectDelegate.setProperty(view, propName, value)
        // The generated base delegate does not include ReactViewManager's border/overflow props.
        else -> ViewManagerPropertyUpdater.updateProps(
          this@EffectViewManager, view, ReactStylesDiffMap(JavaOnlyMap.of(propName, value)),
        )
      }
    }
    @Suppress("ACCIDENTAL_OVERRIDE")
    override fun receiveCommand(view: ReactViewGroup, commandName: String, args: ReadableArray) {
      this@EffectViewManager.receiveCommand(view, commandName, args)
    }
  }
  override fun getName() = REACT_CLASS
  override fun getDelegate(): ViewManagerDelegate<ReactViewGroup> = viewDelegate
  override fun createViewInstance(context: ThemedReactContext): ReactViewGroup = EffectView(context)

  @ReactProp(name = "blurIntensity", defaultInt = 100)
  override fun setBlurIntensity(view: ReactViewGroup, value: Int) { (view as EffectView).strength = value }

  @ReactProp(name = "blurAmount", defaultFloat = -1f)
  override fun setBlurAmount(view: ReactViewGroup, value: Float) { (view as EffectView).blurAmount = value }

  @ReactProp(name = "intensity")
  override fun setIntensity(view: ReactViewGroup, value: String?) { (view as EffectView).material = value ?: "regular" }

  @ReactProp(name = "theme")
  override fun setTheme(view: ReactViewGroup, value: String?) { (view as EffectView).theme = value ?: "system" }

  @ReactProp(name = "effect")
  override fun setEffect(view: ReactViewGroup, value: String?) { /* Glass uses the frosted material fallback. */ }

  @ReactProp(name = "interactive", defaultBoolean = false)
  override fun setInteractive(view: ReactViewGroup, value: Boolean) { /* Touch handling uses ordinary ViewProps. */ }

  override fun setBackgroundColor(view: ReactViewGroup, backgroundColor: Int) {
    (view as EffectView).tint = backgroundColor
    super.setBackgroundColor(view, Color.TRANSPARENT)
  }

  companion object { const val REACT_CLASS = "MTREffectView" }
}
