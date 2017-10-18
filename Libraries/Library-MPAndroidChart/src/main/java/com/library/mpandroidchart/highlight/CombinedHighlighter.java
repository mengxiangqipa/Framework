package com.library.mpandroidchart.highlight;

import com.library.mpandroidchart.data.ChartData;
import com.library.mpandroidchart.data.CombinedData;
import com.library.mpandroidchart.data.DataSet;
import com.library.mpandroidchart.interfaces.BarLineScatterCandleBubbleDataProvider;
import com.library.mpandroidchart.utils.SelectionDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Philipp Jahoda on 12/09/15.
 */
public class CombinedHighlighter extends ChartHighlighter<BarLineScatterCandleBubbleDataProvider> {

    public CombinedHighlighter(BarLineScatterCandleBubbleDataProvider chart) {
        super(chart);
    }

    /**
     * Returns a list of SelectionDetail object corresponding to the given xIndex.
     *
     * @param xIndex
     * @return
     */
    @Override
    protected List<SelectionDetail> getSelectionDetailsAtIndex(int xIndex) {

        CombinedData data = (CombinedData) mChart.getData();

        // get all chartdata objects
        List<ChartData> dataObjects = data.getAllData();

        List<SelectionDetail> vals = new ArrayList<SelectionDetail>();

        float[] pts = new float[2];

        for (int i = 0; i < dataObjects.size(); i++) {

            for(int j = 0; j < dataObjects.get(i).getDataSetCount(); j++) {

                DataSet<?> dataSet = dataObjects.get(i).getDataSetByIndex(j);

                // dont include datasets that cannot be highlighted
                if (!dataSet.isHighlightEnabled())
                    continue;

                // extract all y-values from all DataSets at the given x-index
                final float yVal = dataSet.getYValForXIndex(xIndex);
                if (yVal == Float.NaN)
                    continue;

                pts[1] = yVal;

                mChart.getTransformer(dataSet.getAxisDependency()).pointValuesToPixel(pts);

                if (!Float.isNaN(pts[1])) {
                    vals.add(new SelectionDetail(pts[1], j, dataSet));
                }
            }
        }

        return vals;
    }
}
