package org.apache.poi.ss.usermodel.charts;

import java.util.List;

public interface LineChartData extends ChartData {
    LineChartSeries addSeries(ChartDataSource<?> chartDataSource, ChartDataSource<? extends Number> chartDataSource2);

    List<? extends LineChartSeries> getSeries();
}
