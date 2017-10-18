package com.library.pulltorefresh.rentalssun;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

public class RentalsSunHeaderView extends View
{

	private RentalsSunHeaderDrawable mDrawable;

	public RentalsSunHeaderView(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);
		init();
	}

	public RentalsSunHeaderView(Context context)
	{
		super(context);
		init();
	}

	public RentalsSunHeaderView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init();
	}

	private void init()
	{
		mDrawable = new RentalsSunHeaderDrawable(getContext(), this);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		int height = mDrawable.getTotalDragDistance() * 5 / 4;
		heightMeasureSpec = MeasureSpec.makeMeasureSpec(height + getPaddingTop() + getPaddingBottom(), MeasureSpec.EXACTLY);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom)
	{
		int pl = getPaddingLeft();
		int pt = getPaddingTop();
		mDrawable.setBounds(pl, pt, pl + right - left, pt + bottom - top);
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		mDrawable.draw(canvas);
	}

	@Override
	public void invalidateDrawable(Drawable dr)
	{
		if (dr == mDrawable)
		{
			invalidate();
		} else
		{
			super.invalidateDrawable(dr);
		}
	}

	public void changeStateInit()
	{
		mDrawable.resetOriginals();
	}

	public void changeStateFinish()
	{
		mDrawable.clearAnimation();
	}

	public void changeStateMoving(int deltaY, float progress)
	{
		mDrawable.offsetTopAndBottom(deltaY);
		mDrawable.setPercent(progress);
		invalidate();
	}

	public void changeStateOnRefreshing(int deltaY, float progress)
	{
		mDrawable.start();
	}
}
