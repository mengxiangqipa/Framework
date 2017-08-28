package com.library.mpandroidchart.interfaces;

import com.library.mpandroidchart.components.YAxis.AxisDependency;
import com.library.mpandroidchart.data.BarLineScatterCandleBubbleData;
import com.library.mpandroidchart.utils.Transformer;

public interface BarLineScatterCandleBubbleDataProvider extends ChartInterface {

    Transformer getTransformer(AxisDependency axis);
    int getMaxVisibleCount();
    boolean isInverted(AxisDependency axis);
    
    int getLowestVisibleXIndex();
    int getHighestVisibleXIndex();

    BarLineScatterCandleBubbleData getData();
}
