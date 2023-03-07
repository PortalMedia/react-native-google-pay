package com.busfor;

import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

public class RNGooglePayButtonViewManager extends SimpleViewManager<FrameLayout> {

  @Override
  public String getName() {
    return "RNGooglePayButton";
  }

  @Override
  protected FrameLayout createViewInstance(final ThemedReactContext reactContext) {
    LayoutInflater inflater = (LayoutInflater) reactContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    FrameLayout frameLayout = (FrameLayout) inflater.inflate(R.layout.buy_with_googlepay_button, null);
    return frameLayout;
  }

}
