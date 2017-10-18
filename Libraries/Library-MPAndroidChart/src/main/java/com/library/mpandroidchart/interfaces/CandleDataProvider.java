package com.library.mpandroidchart.interfaces;

import com.library.mpandroidchart.data.CandleData;

public interface CandleDataProvider extends BarLineScatterCandleBubbleDataProvider {

    CandleData getCandleData();
}
