package com.framework.widget;

import android.content.Context;
import android.util.AttributeSet;

public class CustomFocusTextView extends androidx.appcompat.widget.AppCompatTextView {
    public CustomFocusTextView(Context context) {
        super(context);
    }

    public CustomFocusTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomFocusTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean isFocused() {
        return true;
    }
}
