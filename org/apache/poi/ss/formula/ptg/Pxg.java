package org.apache.poi.ss.formula.ptg;

public interface Pxg {
    int getExternalWorkbookNumber();

    String getSheetName();

    void setSheetName(String str);

    String toFormulaString();
}
