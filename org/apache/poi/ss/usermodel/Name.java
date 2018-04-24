package org.apache.poi.ss.usermodel;

public interface Name {
    String getComment();

    String getNameName();

    String getRefersToFormula();

    int getSheetIndex();

    String getSheetName();

    boolean isDeleted();

    boolean isFunctionName();

    void setComment(String str);

    void setFunction(boolean z);

    void setNameName(String str);

    void setRefersToFormula(String str);

    void setSheetIndex(int i);
}
