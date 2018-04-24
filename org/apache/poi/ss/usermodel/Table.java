package org.apache.poi.ss.usermodel;

import java.util.regex.Pattern;

public interface Table {
    public static final Pattern isStructuredReference = Pattern.compile("[a-zA-Z_\\\\][a-zA-Z0-9._]*\\[.*\\]");

    int findColumnIndex(String str);

    int getEndColIndex();

    int getEndRowIndex();

    String getName();

    String getSheetName();

    int getStartColIndex();

    int getStartRowIndex();

    boolean isHasTotalsRow();
}
