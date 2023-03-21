package com.busfor;

import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

public class RNGooglePayButtonViewManager extends SimpleViewManager<RelativeLayout> {

  @Override
  public String getName() {
    return "RNGooglePayButton";
  }

  @Override
  protected RelativeLayout createViewInstance(final ThemedReactContext reactContext) {
    LayoutInflater inflater = (LayoutInflater) reactContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(R.layout.buy_with_googlepay_button, null);
    return relativeLayout;
  }

}
