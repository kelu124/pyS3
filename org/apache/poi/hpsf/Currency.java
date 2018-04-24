package org.apache.poi.hpsf;

import org.apache.poi.util.Internal;
import org.apache.poi.util.LittleEndian;

@Internal
class Currency {
    static final int SIZE = 8;
    private byte[] _value;

    Currency(byte[] data, int offset) {
        this._value = LittleEndian.getByteArray(data, offset, 8);
    }
}
