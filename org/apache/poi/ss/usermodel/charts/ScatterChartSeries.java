package org.apache.poi.ss.usermodel.charts;

public interface ScatterChartSeries extends ChartSeries {
    ChartDataSource<?> getXValues();

    ChartDataSource<? extends Number> getYValues();
}
