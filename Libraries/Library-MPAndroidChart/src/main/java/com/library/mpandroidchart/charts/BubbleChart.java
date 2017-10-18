
package com.library.mpandroidchart.charts;

import android.content.Context;
import android.util.AttributeSet;

import com.library.mpandroidchart.data.BubbleData;
import com.library.mpandroidchart.data.BubbleDataSet;
import com.library.mpandroidchart.interfaces.BubbleDataProvider;
import com.library.mpandroidchart.renderer.BubbleChartRenderer;

/**
 * The BubbleChart. Draws bubbles. Bubble chart implementation: Copyright 2015
 * Pierre-Marc Airoldi Licensed under Apache License 2.0. In the BubbleChart, it
 * is the area of the bubble, not the radius or diameter of the bubble that
 * conveys the data.
 *
 * @author Philipp Jahoda
 */
public class BubbleChart extends BarLineChartBase<BubbleData> implements BubbleDataProvider {

    public BubbleChart(Context context) {
        super(context);
    }

    public BubbleChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BubbleChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void init() {
        super.init();

        mRenderer = new BubbleChartRenderer(this, mAnimator, mViewPortHandler);
    }

    @Override
    protected void calcMinMax() {
        super.calcMinMax();

        if (mDeltaX == 0 && mData.getYValCount() > 0)
            mDeltaX = 1;

        mXChartMin = -0.5f;
        mXChartMax = (float) mData.getXValCount() - 0.5f;

        if (mRenderer != null) {
            for (BubbleDataSet set : mData.getDataSets()) {

                final float xmin = set.getXMin();
                final float xmax = set.getXMax();

                if (xmin < mXChartMin)
                    mXChartMin = xmin;

                if (xmax > mXChartMax)
                    mXChartMax = xmax;
            }
        }

        mDeltaX = Math.abs(mXChartMax - mXChartMin);
    }

    public BubbleData getBubbleData() {
        return mData;
    }
}
