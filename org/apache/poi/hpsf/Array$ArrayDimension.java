package org.apache.poi.hpsf;

import org.apache.poi.util.LittleEndian;

class Array$ArrayDimension {
    static final int SIZE = 8;
    private int _indexOffset;
    private long _size;

    Array$ArrayDimension(byte[] data, int offset) {
        this._size = LittleEndian.getUInt(data, offset);
        this._indexOffset = LittleEndian.getInt(data, offset + 4);
    }
}
