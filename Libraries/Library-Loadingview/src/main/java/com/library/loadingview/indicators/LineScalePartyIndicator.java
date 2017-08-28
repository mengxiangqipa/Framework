package com.library.loadingview.indicators;

import java.util.ArrayList;

import com.library.loadingview.Indicator;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 *     @author Yangjie
 *     className LineScalePartyIndicator
 *     created at  2017/1/12  12:24
 */
public class LineScalePartyIndicator extends Indicator
{

	private final float SCALE = 1.0f;

	private float[] scaleFloats = new float[] { SCALE, SCALE, SCALE, SCALE, SCALE, };

	@Override
	public void draw(Canvas canvas, Paint paint)
	{
		float translateX = getWidth() / 9;
		float translateY = getHeight() / 2;
		for (int i = 0; i < 4; i++)
		{
			canvas.save();
			canvas.translate((2 + i * 2) * translateX - translateX / 2, translateY);
			canvas.scale(scaleFloats[i], scaleFloats[i]);
			RectF rectF = new RectF(-translateX / 2, -getHeight() / 2.5f, translateX / 2, getHeight() / 2.5f);
			canvas.drawRoundRect(rectF, 5, 5, paint);
			canvas.restore();
		}
	}

	@Override
	public ArrayList<ValueAnimator> onCreateAnimators()
	{
		ArrayList<ValueAnimator> animators = new ArrayList<>();
		long[] durations = new long[] { 1260, 430, 1010, 730 };
		long[] delays = new long[] { 770, 290, 280, 740 };
		for (int i = 0; i < 4; i++)
		{
			final int index = i;
			ValueAnimator scaleAnim = ValueAnimator.ofFloat(1, 0.4f, 1);
			scaleAnim.setDuration(durations[i]);
			scaleAnim.setRepeatCount(-1);
			scaleAnim.setStartDelay(delays[i]);
			addUpdateListener(scaleAnim, new ValueAnimator.AnimatorUpdateListener()
			{
				@Override
				public void onAnimationUpdate(ValueAnimator animation)
				{
					scaleFloats[index] = (float) animation.getAnimatedValue();
					postInvalidate();
				}
			});
			animators.add(scaleAnim);
		}
		return animators;
	}

}
