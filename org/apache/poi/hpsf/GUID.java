package org.apache.poi.hpsf;

import org.apache.poi.util.Internal;
import org.apache.poi.util.LittleEndian;

@Internal
class GUID {
    static final int SIZE = 16;
    private int _data1;
    private short _data2;
    private short _data3;
    private long _data4;

    GUID(byte[] data, int offset) {
        this._data1 = LittleEndian.getInt(data, offset + 0);
        this._data2 = LittleEndian.getShort(data, offset + 4);
        this._data3 = LittleEndian.getShort(data, offset + 6);
        this._data4 = LittleEndian.getLong(data, offset + 8);
    }
}
