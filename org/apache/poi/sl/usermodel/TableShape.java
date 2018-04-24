package org.apache.poi.sl.usermodel;

public interface TableShape<S extends Shape<S, P>, P extends TextParagraph<S, P, ?>> extends Shape<S, P>, PlaceableShape<S, P> {
    TableCell<S, P> getCell(int i, int i2);

    double getColumnWidth(int i);

    int getNumberOfColumns();

    int getNumberOfRows();

    double getRowHeight(int i);

    void setColumnWidth(int i, double d);

    void setRowHeight(int i, double d);
}
