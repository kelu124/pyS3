package org.apache.poi.ss.usermodel.charts;

import org.apache.poi.ss.usermodel.Chart;

public interface ChartData {
    void fillChart(Chart chart, ChartAxis... chartAxisArr);
}
