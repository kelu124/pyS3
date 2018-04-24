package org.apache.poi.ss.usermodel.charts;

public interface ChartAxisFactory {
    ChartAxis createCategoryAxis(AxisPosition axisPosition);

    ValueAxis createValueAxis(AxisPosition axisPosition);
}
