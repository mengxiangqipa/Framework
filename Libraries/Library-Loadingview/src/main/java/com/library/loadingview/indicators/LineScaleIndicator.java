package com.library.loadingview.indicators;

import java.util.ArrayList;

import com.library.loadingview.Indicator;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 *     @author Yangjie
 *     className LineScaleIndicator
 *     created at  2017/1/12  12:23
 */
public class LineScaleIndicator extends Indicator
{

	private final float SCALE = 1.0f;

	float[] scaleYFloats = new float[] { SCALE, SCALE, SCALE, SCALE, SCALE, };

	@Override
	public void draw(Canvas canvas, Paint paint)
	{
		float translateX = getWidth() / 11;
		float translateY = getHeight() / 2;
		for (int i = 0; i < 5; i++)
		{
			canvas.save();
			canvas.translate((2 + i * 2) * translateX - translateX / 2, translateY);
			canvas.scale(SCALE, scaleYFloats[i]);
			RectF rectF = new RectF(-translateX / 2, -getHeight() / 2.5f, translateX / 2, getHeight() / 2.5f);
			canvas.drawRoundRect(rectF, 5, 5, paint);
			canvas.restore();
		}
	}

	@Override
	public ArrayList<ValueAnimator> onCreateAnimators()
	{
		ArrayList<ValueAnimator> animators = new ArrayList<>();
		long[] delays = new long[] { 100, 200, 300, 400, 500 };
		for (int i = 0; i < 5; i++)
		{
			final int index = i;
			ValueAnimator scaleAnim = ValueAnimator.ofFloat(1, 0.4f, 1);
			scaleAnim.setDuration(1000);
			scaleAnim.setRepeatCount(-1);
			scaleAnim.setStartDelay(delays[i]);
			addUpdateListener(scaleAnim, new ValueAnimator.AnimatorUpdateListener()
			{
				@Override
				public void onAnimationUpdate(ValueAnimator animation)
				{
					scaleYFloats[index] = (float) animation.getAnimatedValue();
					postInvalidate();
				}
			});
			animators.add(scaleAnim);
		}
		return animators;
	}

}
