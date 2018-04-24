package org.apache.poi.ss.formula;

public interface ExternSheetReferenceToken {
    String format2DRefAsString();

    int getExternSheetIndex();
}
