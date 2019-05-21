package com.library.mpandroidchart.interfaces;

import android.graphics.PointF;
import android.graphics.RectF;

import com.library.mpandroidchart.data.ChartData;
import com.library.mpandroidchart.formatter.ValueFormatter;

/**
 * Interface that provides everything there is to know about the dimensions,
 * bounds, and range of the chart.
 *
 * @author Philipp Jahoda
 */
public interface ChartInterface {

    float getXChartMin();

    float getXChartMax();

    float getYChartMin();

    float getYChartMax();

    int getXValCount();

    int getWidth();

    int getHeight();

    PointF getCenterOfView();

    PointF getCenterOffsets();

    RectF getContentRect();

    ValueFormatter getDefaultValueFormatter();

    ChartData getData();
}
