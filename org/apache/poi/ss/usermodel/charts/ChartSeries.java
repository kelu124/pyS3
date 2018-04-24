package org.apache.poi.ss.usermodel.charts;

import org.apache.poi.ss.util.CellReference;

public interface ChartSeries {
    CellReference getTitleCellReference();

    String getTitleString();

    TitleType getTitleType();

    void setTitle(String str);

    void setTitle(CellReference cellReference);
}
