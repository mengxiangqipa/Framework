package com.library.videoview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

/**
 *     @author Yangjie
 *     className CustomFullScreenVideoView
 *     created at  2016/9/30  16:58
 *     自定义可以全屏播放的VideoView
 */
public class CustomFullScreenVideoView extends VideoView
{

	public CustomFullScreenVideoView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	public CustomFullScreenVideoView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public CustomFullScreenVideoView(Context context)
	{
		super(context);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		int width = getDefaultSize(0, widthMeasureSpec);
		int height = getDefaultSize(0, heightMeasureSpec);
		this.getHolder().setFixedSize(width, height);
		setMeasuredDimension(width, height);
	}

}