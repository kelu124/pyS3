package org.apache.poi.ss.usermodel.charts;

public interface LineChartSeries extends ChartSeries {
    ChartDataSource<?> getCategoryAxisData();

    ChartDataSource<? extends Number> getValues();
}
