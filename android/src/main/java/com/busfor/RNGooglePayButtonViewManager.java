package com.busfor;

import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;

import android.view.View;

import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.uimanager.annotations.ReactProp;

public class RNGooglePayButtonViewManager extends SimpleViewManager<GooglePayButtonView> {

  @Override
  public String getName() {
    return "RNGooglePayButton";
  }

  @Override
  protected View createViewInstance(final ThemedReactContext reactContext) {
    return new GooglePayButtonView(reactContext);
  }

}
