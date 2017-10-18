package com.library.mpandroidchart.interfaces;

import com.library.mpandroidchart.components.YAxis;
import com.library.mpandroidchart.data.LineData;

public interface LineDataProvider extends BarLineScatterCandleBubbleDataProvider {

    LineData getLineData();

    YAxis getAxis(YAxis.AxisDependency dependency);
}
