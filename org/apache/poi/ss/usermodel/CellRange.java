package org.apache.poi.ss.usermodel;

import java.util.Iterator;

public interface CellRange<C extends Cell> extends Iterable<C> {
    C getCell(int i, int i2);

    C[][] getCells();

    C[] getFlattenedCells();

    int getHeight();

    String getReferenceText();

    C getTopLeftCell();

    int getWidth();

    Iterator<C> iterator();

    int size();
}
