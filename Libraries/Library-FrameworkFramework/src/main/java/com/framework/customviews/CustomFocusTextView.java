package com.framework.customviews;

import android.content.Context;
import android.util.AttributeSet;

public class CustomFocusTextView extends android.support.v7.widget.AppCompatTextView {
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
