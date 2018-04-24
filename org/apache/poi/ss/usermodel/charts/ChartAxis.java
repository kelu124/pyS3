package org.apache.poi.ss.usermodel.charts;

public interface ChartAxis {
    void crossAxis(ChartAxis chartAxis);

    AxisCrosses getCrosses();

    long getId();

    double getLogBase();

    AxisTickMark getMajorTickMark();

    double getMaximum();

    double getMinimum();

    AxisTickMark getMinorTickMark();

    String getNumberFormat();

    AxisOrientation getOrientation();

    AxisPosition getPosition();

    boolean isSetLogBase();

    boolean isSetMaximum();

    boolean isSetMinimum();

    boolean isVisible();

    void setCrosses(AxisCrosses axisCrosses);

    void setLogBase(double d);

    void setMajorTickMark(AxisTickMark axisTickMark);

    void setMaximum(double d);

    void setMinimum(double d);

    void setMinorTickMark(AxisTickMark axisTickMark);

    void setNumberFormat(String str);

    void setOrientation(AxisOrientation axisOrientation);

    void setPosition(AxisPosition axisPosition);

    void setVisible(boolean z);
}
