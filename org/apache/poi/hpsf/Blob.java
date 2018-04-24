package org.apache.poi.hpsf;

import org.apache.poi.util.Internal;
import org.apache.poi.util.LittleEndian;

@Internal
class Blob {
    private byte[] _value;

    Blob(byte[] data, int offset) {
        int size = LittleEndian.getInt(data, offset);
        if (size == 0) {
            this._value = new byte[0];
        } else {
            this._value = LittleEndian.getByteArray(data, offset + 4, size);
        }
    }

    int getSize() {
        return this._value.length + 4;
    }
}
