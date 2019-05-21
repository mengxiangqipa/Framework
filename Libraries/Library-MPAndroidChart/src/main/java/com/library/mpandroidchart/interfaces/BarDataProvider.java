package com.library.mpandroidchart.interfaces;

import com.library.mpandroidchart.data.BarData;

public interface BarDataProvider extends BarLineScatterCandleBubbleDataProvider {

    BarData getBarData();

    boolean isDrawBarShadowEnabled();

    boolean isDrawValueAboveBarEnabled();

    boolean isDrawHighlightArrowEnabled();
}
