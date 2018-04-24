package org.apache.poi.hssf.usermodel;

import java.util.Iterator;
import java.util.NoSuchElementException;
import org.apache.poi.ss.usermodel.Sheet;

final class HSSFWorkbook$SheetIterator<T extends Sheet> implements Iterator<T> {
    private T cursor = null;
    private final Iterator<T> it;
    final /* synthetic */ HSSFWorkbook this$0;

    public HSSFWorkbook$SheetIterator(HSSFWorkbook hSSFWorkbook) {
        this.this$0 = hSSFWorkbook;
        this.it = hSSFWorkbook._sheets.iterator();
    }

    public boolean hasNext() {
        return this.it.hasNext();
    }

    public T next() throws NoSuchElementException {
        this.cursor = (Sheet) this.it.next();
        return this.cursor;
    }

    public void remove() throws IllegalStateException {
        throw new UnsupportedOperationException("remove method not supported on HSSFWorkbook.iterator(). Use Sheet.removeSheetAt(int) instead.");
    }
}
