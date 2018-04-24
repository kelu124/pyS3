package org.apache.poi.hpsf;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.poi.util.LittleEndian;

class Filetime {
    static final int SIZE = 8;
    private int _dwHighDateTime;
    private int _dwLowDateTime;

    Filetime(byte[] data, int offset) {
        this._dwLowDateTime = LittleEndian.getInt(data, offset + 0);
        this._dwHighDateTime = LittleEndian.getInt(data, offset + 4);
    }

    Filetime(int low, int high) {
        this._dwLowDateTime = low;
        this._dwHighDateTime = high;
    }

    long getHigh() {
        return (long) this._dwHighDateTime;
    }

    long getLow() {
        return (long) this._dwLowDateTime;
    }

    byte[] toByteArray() {
        byte[] result = new byte[8];
        LittleEndian.putInt(result, 0, this._dwLowDateTime);
        LittleEndian.putInt(result, 4, this._dwHighDateTime);
        return result;
    }

    int write(OutputStream out) throws IOException {
        LittleEndian.putInt(this._dwLowDateTime, out);
        LittleEndian.putInt(this._dwHighDateTime, out);
        return 8;
    }
}
