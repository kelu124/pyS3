package org.apache.poi.ss.usermodel.charts;

public interface ChartLegend extends ManuallyPositionable {
    LegendPosition getPosition();

    boolean isOverlay();

    void setOverlay(boolean z);

    void setPosition(LegendPosition legendPosition);
}
