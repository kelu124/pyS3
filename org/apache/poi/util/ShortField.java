package org.apache.poi.util;

import java.io.IOException;
import java.io.InputStream;

public class ShortField implements FixedField {
    private final int _offset;
    private short _value;

    public ShortField(int offset) throws ArrayIndexOutOfBoundsException {
        if (offset < 0) {
            throw new ArrayIndexOutOfBoundsException("Illegal offset: " + offset);
        }
        this._offset = offset;
    }

    public ShortField(int offset, short value) throws ArrayIndexOutOfBoundsException {
        this(offset);
        set(value);
    }

    public ShortField(int offset, byte[] data) throws ArrayIndexOutOfBoundsException {
        this(offset);
        readFromBytes(data);
    }

    public ShortField(int offset, short value, byte[] data) throws ArrayIndexOutOfBoundsException {
        this(offset);
        set(value, data);
    }

    public short get() {
        return this._value;
    }

    public void set(short value) {
        this._value = value;
    }

    public void set(short value, byte[] data) throws ArrayIndexOutOfBoundsException {
        this._value = value;
        writeToBytes(data);
    }

    public void readFromBytes(byte[] data) throws ArrayIndexOutOfBoundsException {
        this._value = LittleEndian.getShort(data, this._offset);
    }

    public void readFromStream(InputStream stream) throws IOException {
        this._value = LittleEndian.readShort(stream);
    }

    public void writeToBytes(byte[] data) throws ArrayIndexOutOfBoundsException {
        LittleEndian.putShort(data, this._offset, this._value);
    }

    public String toString() {
        return String.valueOf(this._value);
    }
}
