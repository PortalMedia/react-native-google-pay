package com.busfor;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public final class GooglePayButtonView extends FrameLayout {
  private int zaa;
  private int zab;
  private View googlePayButton;

  public GooglePayButtonView(@NonNull Context context) {
    this(context, (AttributeSet)null);
  }

  public GooglePayButtonView(@NonNull Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public GooglePayButtonView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);

    View existing = this.googlePayButton;
    if (existing != null) {
      this.removeView(existing);
    }
    this.googlePayButton = inflate(getContext(), R.layout.buy_with_googlepay_button, this);
    this.addView(this.googlePayButton);
  }

}
