package org.apache.poi.ss.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

final class SSCellRange$ArrayIterator<D> implements Iterator<D> {
    private final D[] _array;
    private int _index = 0;

    public SSCellRange$ArrayIterator(D[] array) {
        this._array = (Object[]) array.clone();
    }

    public boolean hasNext() {
        return this._index < this._array.length;
    }

    public D next() {
        if (this._index >= this._array.length) {
            throw new NoSuchElementException(String.valueOf(this._index));
        }
        Object[] objArr = this._array;
        int i = this._index;
        this._index = i + 1;
        return objArr[i];
    }

    public void remove() {
        throw new UnsupportedOperationException("Cannot remove cells from this CellRange.");
    }
}
