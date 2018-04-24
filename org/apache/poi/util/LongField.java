package org.apache.poi.util;

import java.io.IOException;
import java.io.InputStream;

public class LongField implements FixedField {
    private final int _offset;
    private long _value;

    public LongField(int offset) throws ArrayIndexOutOfBoundsException {
        if (offset < 0) {
            throw new ArrayIndexOutOfBoundsException("Illegal offset: " + offset);
        }
        this._offset = offset;
    }

    public LongField(int offset, long value) throws ArrayIndexOutOfBoundsException {
        this(offset);
        set(value);
    }

    public LongField(int offset, byte[] data) throws ArrayIndexOutOfBoundsException {
        this(offset);
        readFromBytes(data);
    }

    public LongField(int offset, long value, byte[] data) throws ArrayIndexOutOfBoundsException {
        this(offset);
        set(value, data);
    }

    public long get() {
        return this._value;
    }

    public void set(long value) {
        this._value = value;
    }

    public void set(long value, byte[] data) throws ArrayIndexOutOfBoundsException {
        this._value = value;
        writeToBytes(data);
    }

    public void readFromBytes(byte[] data) throws ArrayIndexOutOfBoundsException {
        this._value = LittleEndian.getLong(data, this._offset);
    }

    public void readFromStream(InputStream stream) throws IOException {
        this._value = LittleEndian.readLong(stream);
    }

    public void writeToBytes(byte[] data) throws ArrayIndexOutOfBoundsException {
        LittleEndian.putLong(data, this._offset, this._value);
    }

    public String toString() {
        return String.valueOf(this._value);
    }
}
