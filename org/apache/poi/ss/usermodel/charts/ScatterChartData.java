package org.apache.poi.ss.usermodel.charts;

import java.util.List;

public interface ScatterChartData extends ChartData {
    ScatterChartSeries addSerie(ChartDataSource<?> chartDataSource, ChartDataSource<? extends Number> chartDataSource2);

    List<? extends ScatterChartSeries> getSeries();
}
