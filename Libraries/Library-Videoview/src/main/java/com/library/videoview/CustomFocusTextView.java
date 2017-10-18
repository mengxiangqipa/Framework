package com.library.videoview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class CustomFocusTextView extends TextView
{
	public CustomFocusTextView(Context context)
	{
		super(context);
	}

	public CustomFocusTextView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public CustomFocusTextView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	@Override
	public boolean isFocused()
	{
		return true;
	}
}
