package org.apache.poi.ss.usermodel;

public interface Hyperlink extends org.apache.poi.common.usermodel.Hyperlink {
    int getFirstColumn();

    int getFirstRow();

    int getLastColumn();

    int getLastRow();

    void setFirstColumn(int i);

    void setFirstRow(int i);

    void setLastColumn(int i);

    void setLastRow(int i);
}
