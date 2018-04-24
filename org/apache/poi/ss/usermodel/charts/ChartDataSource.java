package org.apache.poi.ss.usermodel.charts;

public interface ChartDataSource<T> {
    String getFormulaString();

    T getPointAt(int i);

    int getPointCount();

    boolean isNumeric();

    boolean isReference();
}
