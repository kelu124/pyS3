package org.apache.poi.ss.usermodel.charts;

public interface ValueAxis extends ChartAxis {
    AxisCrossBetween getCrossBetween();

    void setCrossBetween(AxisCrossBetween axisCrossBetween);
}
